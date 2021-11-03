package com.example.ateachingapplication.slice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Reserve;
import com.example.ateachingapplication.domain.Teacher;
import com.example.ateachingapplication.provider.TeacherTimeProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;

import java.util.Date;
import java.util.List;

public class TeacherTimeSlice extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Teacher teacher;
    private Text teacherName;
    Date date = new Date();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_reserve_teacher_info);
        teacher = intent.getSerializableParam("teacher");
        teacherName = (Text)findComponentById(ResourceTable.Id_time_teacher_name);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        teacherName.setText(teacher.getTeacherName() + "的时间安排");
        String url = "http://101.132.74.147:8081/reserve/reserveList";
        ListContainer listContainer = (ListContainer)findComponentById(ResourceTable.Id_listcontainer_teacher_time);
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(teacher);
        ZZRHttp.postJson(url,json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(TeacherTimeSlice.this)
                        .setText("网络不稳定")
                        .show();
            }
            @Override
            public void onResponse(String s) {
                Result result = gson.fromJson(s,Result.class);
                if (result.getCode() == 200){
                    new ToastDialog(TeacherTimeSlice.this)
                            .setText(result.getMsg())
                            .show();

                    List<Reserve> list = gson.fromJson(result.getResult(),new TypeToken<List<Reserve>>(){}.getType());
                    if (list.size()>0){
                        TeacherTimeProvider teacherTimeProvider = new TeacherTimeProvider(list,TeacherTimeSlice.this);
                        listContainer.setItemProvider(teacherTimeProvider);
                    }else {
                        new ToastDialog(TeacherTimeSlice.this)
                                .setText("老师很闲！")
                                .show();
                    }
                }else if (result.getCode() == 400){
                    new ToastDialog(TeacherTimeSlice.this)
                            .setText("老师很闲！")
                            .show();
                }
            }
        });


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
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_back){
            terminate();
        }
    }
}