package com.example.ateachingapplication.slice;

import com.example.ateachingapplication.slice.bookslice.SelectBook;
import com.example.ateachingapplication.slice.myslice.*;
import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.ImageItem;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.domain.Teacher;
import com.example.ateachingapplication.provider.PageProvider;
import com.example.ateachingapplication.provider.TeacherProvider;
import com.example.ateachingapplication.slice.messageslice.BlogSlice;
import com.example.ateachingapplication.slice.messageslice.DiscountSlice;
import com.example.ateachingapplication.slice.messageslice.OrderSuggestionSlice;
import com.example.ateachingapplication.slice.messageslice.OrderTeacherMessageSlice;
import com.example.ateachingapplication.slice.searchslice.GradeSearchSlice;
import com.example.ateachingapplication.slice.searchslice.SubjectSearchSlice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.location.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TeachingAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    Boolean flag ;
    //地理定位“总管家”
    private Locator myLocator;
    //定位回调
    private LocatorCallback myLocatorCallback;
    private int tabLastPosition;
    private TabList tabList;
    private String[] str = {"首页", "找老师","书屋", "消息","我的"};
    private TabListSelect listSelect = new TabListSelect();
    private AbilitySlice slice = this;
    private Component componentLayout;
    private static int SUBJECT_NAME_REQUEST_CODE=100;
    private static int GRADE_REQUEST_CODE=200;
    private static int SUGGESTION_REQUEST_CODE=300;
    private Parent parent;
    String url = "http://101.132.74.147:8081";
    Result result = new Result();
    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_navigation_bar);


        parent = intent.getSerializableParam("my");

        tabList = (TabList) findComponentById(ResourceTable.Id_tab_list_bottom);

        for (int i = 0; i < str.length; i++) {
            TabList.Tab tab = tabList.new Tab(getContext());
            tab.setText(str[i]);
            tabList.addTab(tab);
        }
        tabList.setFixedMode(true);

        tabList.addTabSelectedListener(listSelect);
        //默认第一个被选中
        tabList.getTabAt(0).select();


    }
    class TabListSelect implements TabList.TabSelectedListener{
        @Override
        public void onSelected(TabList.Tab tab) {
            // 当某个Tab从未选中状态变为选中状态时的回调
            ComponentContainer container = (ComponentContainer) findComponentById(ResourceTable.Id_tab_container);

            //根据tab接收的数据，匹配字符串，选择相应功能
            if(tab.getText().equals("首页")){
                container.removeAllComponents();
                componentLayout = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_ability_firstpage,null,false);
                implementFirstPage(componentLayout);
                container.addComponent(componentLayout);
            }else if(tab.getText().equals("找老师")){
                container.removeAllComponents();
                componentLayout = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_ability_search,null,false);
                implementSearchTeacher(componentLayout);
                container.addComponent(componentLayout);
            }else if(tab.getText().equals("书屋")){
                container.removeAllComponents();
                componentLayout = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_ability_book_home,null,false);
                implementBookHome(componentLayout);
                container.addComponent(componentLayout);
            }else if(tab.getText().equals("消息")){
                container.removeAllComponents();
                componentLayout = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_ability_mes,null,false);
                implementMessage(componentLayout);
                container.addComponent(componentLayout);
            }else if(tab.getText().equals("我的")){
                container.removeAllComponents();
                componentLayout = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_ability_person_message,null,false);
                implementMy(componentLayout);
                container.addComponent(componentLayout);
            }
        }


        @Override
        public void onUnselected(TabList.Tab tab) {
            // 当某个Tab从选中状态变为未选中状态时的回调
            tabLastPosition = tab.getPosition();
        }

        @Override
        public void onReselected(TabList.Tab tab) {
            // 当某个Tab已处于选中状态，再次被点击时的状态回调
        }
    }



    private DirectionalLayout math;
    private DirectionalLayout chinese;
    private DirectionalLayout english;
    private DirectionalLayout physical;
    private DirectionalLayout political;
    private DirectionalLayout chemical;
    private DirectionalLayout biological;
    private DirectionalLayout geographic;

    private List<ImageItem> imageItems=new ArrayList<>();
    private int[] images = {
            ResourceTable.Media_teacher1,
            ResourceTable.Media_teacher2,
            ResourceTable.Media_teacher3,
            ResourceTable.Media_teacher4,
    };
    private PageProvider pageProvider;
    private PageSlider pageSlider;
    private RadioContainer radioContainer;
    private Text position;

    public void implementFirstPage(Component componentLayout){
        math = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_math);
        chinese = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_chinese);
        english = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_english);
        physical = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_physical);
        political = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_political);
        chemical = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_chemical);
        biological = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_biological);
        geographic = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_geographic);
        position = (Text)componentLayout.findComponentById(ResourceTable.Id_position);
        math.setClickedListener(this);
        chinese.setClickedListener(this);
        english.setClickedListener(this);
        physical.setClickedListener(this);
        political.setClickedListener(this);
        chemical.setClickedListener(this);
        biological.setClickedListener(this);
        geographic.setClickedListener(this);
        flag = false;
        pageSlider = (PageSlider) componentLayout.findComponentById(ResourceTable.Id_page_slider);
        radioContainer = (RadioContainer)componentLayout.findComponentById(ResourceTable.Id_radio_container);
        //初始化图片和文字数据封装在集合中
        initPage();
        ((RadioButton)radioContainer.getComponentAt(0)).setChecked(true);
        //创建适配器对象，将当前界面对象和封装好的集合发送过去
        pageProvider = new PageProvider(imageItems,this);
        //将适配器加载至滑动组件上，完成同步组装
        pageSlider.setProvider(pageProvider);
        pageSlider.setPageSwitchTime(2000);
        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) {

            }

            @Override
            public void onPageSlideStateChanged(int i) {

            }

            @Override
            public void onPageChosen(int i) {
                ((RadioButton)radioContainer.getComponentAt(i)).setChecked(true);
            }
        });

        radioContainer.setMarkChangedListener(new RadioContainer.CheckedStateChangedListener() {
            @Override
            public void onCheckedChanged(RadioContainer radioContainer, int i) {
                pageSlider.setCurrentPage(i);
            }
        });

        GeoConvert geoConvert = new GeoConvert(Locale.getDefault());

        myLocator = new Locator(this);
        myLocatorCallback = new LocatorCallback() {
            @Override
            public void onLocationReport(Location location) {
                getUITaskDispatcher().asyncDispatch(()->{
                    //访问互联网，异步处理
                    getGlobalTaskDispatcher(TaskPriority.DEFAULT).asyncDispatch(()->{
                        //逆地理编码
                        try {
                            List<GeoAddress> geoAddresses = geoConvert.getAddressFromLocation(
                                    location.getLatitude(),
                                    location.getLongitude(),
                                    1);
                            //打印地址
                            if (geoAddresses != null && geoAddresses.size() > 0){
                                getUITaskDispatcher().asyncDispatch(()->{
                                    position.setText(geoAddresses.get(0).getLocality());
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
            //状态改变时回调
            @Override
            public void onStatusChanged(int i) {

            }
            //出错时回调
            @Override
            public void onErrorReport(int i) {

            }
        };
        //导航场景RequestParam
        RequestParam requestParam = new RequestParam(RequestParam.SCENE_NAVIGATION);
        //开始定位
        myLocator.startLocating(requestParam,myLocatorCallback);


    }
    private void initPage() {
        for (int i = 0; i < images.length; i++) {
            ImageItem imageItem=new ImageItem(images[i]);
            imageItems.add(imageItem);
        }
    }
    private Button subjectName;
    private Button grade;
    private ListContainer listContainer_TeacherSearchResult;
    private ArrayList<Teacher> datalist=null;
    private Image searchBack;
    public void implementSearchTeacher(Component componentLayout){
        subjectName = (Button)componentLayout.findComponentById(ResourceTable.Id_subject_name);
        grade = (Button)componentLayout.findComponentById(ResourceTable.Id_grade);
        searchBack = (Image)componentLayout.findComponentById(ResourceTable.Id_search_back_arrow);
        subjectName.setClickedListener(this);
        grade.setClickedListener(this);
        searchBack.setClickedListener(this);
        //找到ListContainer
        listContainer_TeacherSearchResult = (ListContainer)componentLayout.findComponentById(ResourceTable.Id_select_teacher);
        //判断是否从主页直接到找老师
        if (!flag) {
            ZZRHttp.get(url + "/teacher/teacherList", new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(slice)
                            .setText("网络不稳定")
                            .show();
                }

                @Override
                public void onResponse(String s) {
                    result = gson.fromJson(s, Result.class);
                    if (result.getCode() == 200) {
                        List<Teacher> list = gson.fromJson(result.getResult(), new TypeToken<List<Teacher>>() {}.getType());
                        TeacherProvider teacherProvider = new TeacherProvider(list, slice);
                        listContainer_TeacherSearchResult.setItemProvider(teacherProvider);
                        listContainer_TeacherSearchResult.setItemClickedListener(new ListContainer.ItemClickedListener() {
                            @Override
                            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                                Teacher teacher = (Teacher) listContainer_TeacherSearchResult.getItemProvider().getItem(i);

                                Intent intent = new Intent();
                                intent.setParam("my",parent);
                                intent.setParam("teacher", teacher);
                                present(new TeacherMessageAbilitySlice(), intent);

                            }
                        });
                    } else if (result.getCode() == 400) {
                        new ToastDialog(slice)
                                .setText("查询不到结果...")
                                .show();
                    }
                }
            });
        }
    }




    private Text bookName1;
    private Text bookName2;
    private Text bookName3;
    private Text bookName4;
    private Text bookName5;
    private Text bookName6;
    private Text bookName7;
    private Text bookName8;
    private Text bookName9;
    private DirectionalLayout primaryChinese;
    private DirectionalLayout primaryMath;
    private DirectionalLayout primaryEnglish;
    private DirectionalLayout middleMath;
    private DirectionalLayout middleEnglish;
    private DirectionalLayout highMath;
    private DirectionalLayout highEnglish;
    private DirectionalLayout highPhysical;
    private DirectionalLayout highPolitical;
    private Image bookHomeBack;
    public void implementBookHome(Component componentLayout) {
        bookName1 = (Text)componentLayout.findComponentById(ResourceTable.Id_book_name1);
        bookName2 = (Text)componentLayout.findComponentById(ResourceTable.Id_book_name2);
        bookName3 = (Text)componentLayout.findComponentById(ResourceTable.Id_book_name3);
        bookName4 = (Text)componentLayout.findComponentById(ResourceTable.Id_book_name4);
        bookName5 = (Text)componentLayout.findComponentById(ResourceTable.Id_book_name5);
        bookName6 = (Text)componentLayout.findComponentById(ResourceTable.Id_book_name6);
        bookName7 = (Text)componentLayout.findComponentById(ResourceTable.Id_book_name7);
        bookName8 = (Text)componentLayout.findComponentById(ResourceTable.Id_book_name8);
        bookName9 = (Text)componentLayout.findComponentById(ResourceTable.Id_book_name9);
        primaryChinese = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_primary_chinese);
        primaryMath = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_primary_math);
        primaryEnglish = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_primary_english);
        middleMath = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_middle_math);
        middleEnglish = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_middle_english);
        highMath = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_high_math);
        highEnglish = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_high_english);
        highPhysical = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_high_physical);
        highPolitical = (DirectionalLayout)componentLayout.findComponentById(ResourceTable.Id_high_political);
        bookHomeBack = (Image)componentLayout.findComponentById(ResourceTable.Id_book_home_back_arrow);
        primaryChinese.setClickedListener(this);
        primaryMath.setClickedListener(this);
        primaryEnglish.setClickedListener(this);
        middleMath.setClickedListener(this);
        middleEnglish.setClickedListener(this);
        highMath.setClickedListener(this);
        highEnglish.setClickedListener(this);
        highPhysical.setClickedListener(this);
        highPolitical.setClickedListener(this);
        bookHomeBack.setClickedListener(this);
    }


    private DirectionalLayout orderTeach;
    private DirectionalLayout orderSuggest;
    private DirectionalLayout blog;
    private DirectionalLayout discount;
    private Image mesBack;
    public void implementMessage(Component componentLayout){
        orderTeach = (DirectionalLayout) componentLayout.findComponentById(ResourceTable.Id_order_teach);
        orderSuggest = (DirectionalLayout) componentLayout.findComponentById(ResourceTable.Id_suggest);
        blog = (DirectionalLayout) componentLayout.findComponentById(ResourceTable.Id_blog);
        discount = (DirectionalLayout) componentLayout.findComponentById(ResourceTable.Id_discount);
        mesBack =(Image)componentLayout.findComponentById(ResourceTable.Id_mes_back_arrow);
        orderTeach.setClickedListener(this);
        orderSuggest.setClickedListener(this);
        blog.setClickedListener(this);
        discount.setClickedListener(this);
        mesBack.setClickedListener(this);
    }
    private Text telephone;
    private Image order;
    private Image wallet;
    private Image award;
    private DirectionalLayout myTeacher;
    private DirectionalLayout myValuation;
    private DirectionalLayout myAnswer;
    private DirectionalLayout myCourse;
    private DirectionalLayout myPlan;
    private DirectionalLayout settings;
    private Image myBack;
    public void implementMy(Component componentLayout){
        telephone = (Text) componentLayout.findComponentById(ResourceTable.Id_phone_numbers);
        myTeacher = (DirectionalLayout) componentLayout.findComponentById(ResourceTable.Id_my_teacher);
        myValuation = (DirectionalLayout) componentLayout.findComponentById(ResourceTable.Id_my_valuation);
        myAnswer = (DirectionalLayout) componentLayout.findComponentById(ResourceTable.Id_my_answer);
        myCourse = (DirectionalLayout) componentLayout.findComponentById(ResourceTable.Id_my_course);
        myPlan = (DirectionalLayout) componentLayout.findComponentById(ResourceTable.Id_my_plan);
        settings = (DirectionalLayout) componentLayout.findComponentById(ResourceTable.Id_settings);
        award = (Image)componentLayout.findComponentById(ResourceTable.Id_award);
        wallet = (Image)componentLayout.findComponentById(ResourceTable.Id_mywallet);
        order = (Image)componentLayout.findComponentById(ResourceTable.Id_myorder);
        myBack = (Image)componentLayout.findComponentById(ResourceTable.Id_my_back_arrow);
        myTeacher.setClickedListener(this);
        myValuation.setClickedListener(this);
        myAnswer.setClickedListener(this);
        myCourse.setClickedListener(this);
        myPlan.setClickedListener(this);
        settings.setClickedListener(this);
        award.setClickedListener(this);
        wallet.setClickedListener(this);
        order.setClickedListener(this);
        myBack.setClickedListener(this);
        telephone.setText(parent.getParPhone());
    }


    @Override
    public void onClick(Component component) {
        //首页
        if (component.getId()==ResourceTable.Id_math){
            flag = true;
            tabList.getTabAt(1).select();
            subjectName.setText("数学");
            getTeacherData(subjectName.getText(),grade.getText());
        }
        if (component.getId()==ResourceTable.Id_chinese){
            flag = true;
            tabList.getTabAt(1).select();
            subjectName.setText("语文");
            getTeacherData(subjectName.getText(),grade.getText());
        }
        if (component.getId()==ResourceTable.Id_english){
            flag = true;
            tabList.getTabAt(1).select();
            subjectName.setText("英语");
            getTeacherData(subjectName.getText(),grade.getText());
        }
        if (component.getId()==ResourceTable.Id_physical){
            flag = true;
            tabList.getTabAt(1).select();
            subjectName.setText("物理");
            getTeacherData(subjectName.getText(),grade.getText());
        }
        if (component.getId()==ResourceTable.Id_political){
            flag = true;
            tabList.getTabAt(1).select();
            subjectName.setText("政治");
            getTeacherData(subjectName.getText(),grade.getText());
        }
        if (component.getId()==ResourceTable.Id_chemical){
            flag = true;
            tabList.getTabAt(1).select();
            subjectName.setText("化学");
            getTeacherData(subjectName.getText(),grade.getText());
        }
        if (component.getId()==ResourceTable.Id_biological){
            flag = true;
            tabList.getTabAt(1).select();
            subjectName.setText("生物");
            getTeacherData(subjectName.getText(),grade.getText());
        }
        if (component.getId()==ResourceTable.Id_geographic){
            flag = true;
            tabList.getTabAt(1).select();
            subjectName.setText("地理");
            getTeacherData(subjectName.getText(),grade.getText());
        }
        //找老师页面
        if (component.getId()==ResourceTable.Id_search_back_arrow){
            tabList.getTabAt(tabLastPosition).select();
        }
        if (component.getId()==ResourceTable.Id_subject_name){
            Intent intent = new Intent();
            intent.setParam("presentsubject",subjectName.getText());
            presentForResult(new SubjectSearchSlice(),intent,SUBJECT_NAME_REQUEST_CODE);
        }
        if (component.getId()==ResourceTable.Id_grade){
            Intent intent = new Intent();
            presentForResult(new GradeSearchSlice(),intent,GRADE_REQUEST_CODE);
        }
        //书屋页面
        if (component.getId()==ResourceTable.Id_book_home_back_arrow){
            tabList.getTabAt(tabLastPosition).select();
        }
        if (component.getId()==ResourceTable.Id_primary_chinese){
            Intent intent = new Intent();
            intent.setParam("book",ResourceTable.Media_primarychinese);
            intent.setParam("bookName",bookName1.getText());
            intent.setParam("my",parent);
            present(new SelectBook(),intent);
        }
        if (component.getId()==ResourceTable.Id_primary_math){
            Intent intent = new Intent();
            intent.setParam("my",parent);
            intent.setParam("book",ResourceTable.Media_primarymath);
            intent.setParam("bookName",bookName2.getText());
            present(new SelectBook(),intent);
        }
        if (component.getId()==ResourceTable.Id_primary_english){
            Intent intent = new Intent();
            intent.setParam("book",ResourceTable.Media_primaryenglish);
            intent.setParam("bookName",bookName3.getText());
            intent.setParam("my",parent);
            present(new SelectBook(),intent);
        }
        if (component.getId()==ResourceTable.Id_middle_math){
            Intent intent = new Intent();
            intent.setParam("book",ResourceTable.Media_middlemath);
            intent.setParam("bookName",bookName4.getText());
            intent.setParam("my",parent);
            present(new SelectBook(),intent);
        }
        if (component.getId()==ResourceTable.Id_middle_english){
            Intent intent = new Intent();
            intent.setParam("book",ResourceTable.Media_middleenglish);
            intent.setParam("bookName",bookName5.getText());
            intent.setParam("my",parent);
            present(new SelectBook(),intent);
        }
        if (component.getId()==ResourceTable.Id_high_math){
            Intent intent = new Intent();
            intent.setParam("book",ResourceTable.Media_highmath);
            intent.setParam("bookName",bookName6.getText());
            intent.setParam("my",parent);
            present(new SelectBook(),intent);
        }
        if (component.getId()==ResourceTable.Id_high_english){
            Intent intent = new Intent();
            intent.setParam("book",ResourceTable.Media_highenglish);
            intent.setParam("bookName",bookName7.getText());
            intent.setParam("my",parent);
            present(new SelectBook(),intent);
        }
        if (component.getId()==ResourceTable.Id_high_physical){
            Intent intent = new Intent();
            intent.setParam("book",ResourceTable.Media_highphysical);
            intent.setParam("bookName",bookName8.getText());
            intent.setParam("my",parent);
            present(new SelectBook(),intent);
        }
        if (component.getId()==ResourceTable.Id_high_political){
            Intent intent = new Intent();
            intent.setParam("book",ResourceTable.Media_highpolitical);
            intent.setParam("bookName",bookName9.getText());
            intent.setParam("my",parent);
            present(new SelectBook(),intent);
        }
        //消息页面
        if (component.getId()==ResourceTable.Id_mes_back_arrow){
            tabList.getTabAt(tabLastPosition).select();
        }
        if (component.getId()==ResourceTable.Id_order_teach){
            Intent intent = new Intent();
            present(new OrderTeacherMessageSlice(),intent);
        }
        if (component.getId()==ResourceTable.Id_suggest){
            Intent intent = new Intent();
            intent.setParam("my",parent);
            present(new OrderSuggestionSlice(),intent);
        }
        if (component.getId()==ResourceTable.Id_blog){
            Intent intent = new Intent();
            present(new BlogSlice(),intent);
        }
        if (component.getId()==ResourceTable.Id_discount){
            Intent intent = new Intent();
            present(new DiscountSlice(),intent);
        }
        //我的页面
        if (component.getId()==ResourceTable.Id_my_back_arrow){
            tabList.getTabAt(tabLastPosition).select();
        }
        if (component.getId() == ResourceTable.Id_myorder){
            Intent intent = new Intent();
            intent.setParam("my",parent);
            present(new MyOrder(),intent);
        }
        if (component.getId() == ResourceTable.Id_mywallet){
            Intent intent = new Intent();
            intent.setParam("my",parent);
            present(new MyWallet(),intent);
        }
        if (component.getId() == ResourceTable.Id_award){
            Intent intent = new Intent();
            intent.setParam("my",parent);
            present(new Award(),intent);
        }
        if (component.getId() == ResourceTable.Id_my_teacher){
            Intent intent = new Intent();
            intent.setParam("my",parent);
            present(new MyTeacher(),intent);
        }
        if (component.getId() == ResourceTable.Id_my_valuation){
            Intent intent = new Intent();
            intent.setParam("my",parent);
            present(new MyValuation(),intent);
        }
        if (component.getId() == ResourceTable.Id_my_answer){
            Intent intent = new Intent();
            present(new MyAnswer(),intent);
        }
        if (component.getId() == ResourceTable.Id_my_course){
            Intent intent = new Intent();
            present(new MyCourse(),intent);
        }
        if (component.getId() == ResourceTable.Id_my_plan){
            Intent intent = new Intent();
            intent.setParam("my",parent);
            present(new MyPlan(),intent);
        }
        if (component.getId() == ResourceTable.Id_settings){
            Intent intent = new Intent();
            present(new Settings(),intent);
        }

    }
    public ArrayList<Teacher> getSubjectTeacher(String subject){
        ArrayList<Teacher> list = new ArrayList<>();
        return list;
    }
    @Override
    protected void onResult(int requestCode, Intent resultIntent) {
        super.onResult(requestCode, resultIntent);
        switch (requestCode){
            case 100:
                subjectName.setText(resultIntent.getStringParam("subjectname"));
                getTeacherData(subjectName.getText(),grade.getText());
                break;
            case 200:
                grade.setText(resultIntent.getStringParam("grade"));
                getTeacherData(subjectName.getText(),grade.getText());
                break;
            case 300:
                new ToastDialog(this)
                        .setText(resultIntent.getStringParam("suggestionsubject") + resultIntent.getStringParam("suggestiontopic") + resultIntent.getStringParam("suggestion"))
                        .show();
                break;
            default:break;
        }
    }

    public void getTeacherData(String subjectSelect,String gradeSelect){
        if (subjectSelect.equals("科目") && gradeSelect.equals("年级")){
            //没选科目和年级
            return;
        }else if (!subjectSelect.equals("科目") && gradeSelect.equals("年级")){
            //选了科目


            ZZRHttp.get(url + "/teacher/findTeacherBySubject?subject=" + subjectSelect,  new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(slice)
                            .setText("网络不稳定")
                            .show();
                }
                @Override
                public void onResponse(String s) {
                    result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        List<Teacher> list = gson.fromJson(result.getResult(),new TypeToken<List<Teacher>>(){}.getType());
                        if (list.size()>0){
                            TeacherProvider teacherProvider = new TeacherProvider(list,slice);
                            listContainer_TeacherSearchResult.setItemProvider(teacherProvider);
                            listContainer_TeacherSearchResult.setItemClickedListener(new ListContainer.ItemClickedListener() {
                                @Override
                                public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                                    Teacher teacher = (Teacher) listContainer_TeacherSearchResult.getItemProvider().getItem(i);

                                    Intent intent = new Intent();
                                    intent.setParam("my",parent);
                                    intent.setParam("teacher", teacher);
                                    present(new TeacherMessageAbilitySlice(), intent);
                                }
                            });
                        }
                    }else if (result.getCode() == 400){
                        new ToastDialog(slice)
                                .setText("查询不到结果...")
                                .show();
                    }
                }
            });
        }else if (subjectSelect.equals("科目") && !gradeSelect.equals("年级")){
            //选了年级
            ZZRHttp.get(url + "/teacher/findTeacherByGrade?grade=" + gradeSelect,  new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(slice)
                            .setText("网络不稳定")
                            .show();
                }
                @Override
                public void onResponse(String s) {
                    result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        List<Teacher> list = gson.fromJson(result.getResult(),new TypeToken<List<Teacher>>(){}.getType());
                        if (list.size()>0){
                            TeacherProvider teacherProvider = new TeacherProvider(list,slice);
                            listContainer_TeacherSearchResult.setItemProvider(teacherProvider);
                            listContainer_TeacherSearchResult.setItemClickedListener(new ListContainer.ItemClickedListener() {
                                @Override
                                public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                                    Teacher teacher = (Teacher) listContainer_TeacherSearchResult.getItemProvider().getItem(i);

                                    Intent intent = new Intent();
                                    intent.setParam("my",parent);
                                    intent.setParam("teacher", teacher);
                                    present(new TeacherMessageAbilitySlice(), intent);
                                }
                            });
                        }
                    }else if(result.getCode() == 400){
                        new ToastDialog(slice)
                                .setText("查询不到结果...")
                                .show();
                    }
                }
            });
        }else if (!subjectSelect.equals("科目") && !gradeSelect.equals("年级")){
            //选择了科目和年级

            ZZRHttp.get(url + "/teacher/findTeacherBySubjectAndGrade?grade=" + gradeSelect + "&subject=" + subjectSelect,  new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(slice)
                            .setText("网络不稳定")
                            .show();
                }
                @Override
                public void onResponse(String s) {
                    result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        List<Teacher> list = gson.fromJson(result.getResult(),new TypeToken<List<Teacher>>(){}.getType());
                        if (list.size()>0){
                            TeacherProvider teacherProvider = new TeacherProvider(list,slice);
                            listContainer_TeacherSearchResult.setItemProvider(teacherProvider);
                            listContainer_TeacherSearchResult.setItemClickedListener(new ListContainer.ItemClickedListener() {
                                @Override
                                public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                                    Teacher teacher = (Teacher) listContainer_TeacherSearchResult.getItemProvider().getItem(i);

                                    Intent intent = new Intent();
                                    intent.setParam("my",parent);
                                    intent.setParam("teacher", teacher);
                                    present(new TeacherMessageAbilitySlice(), intent);
                                }
                            });
                        }
                    }else if (result.getCode() == 400){
                        new ToastDialog(slice)
                                .setText("查询不到结果...")
                                .show();
                    }
                }
            });
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        myLocator.stopLocating(myLocatorCallback);
    }
}
