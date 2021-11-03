package com.example.ateachingapplication.slice.messageslice;

import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Teacher;
import com.example.ateachingapplication.provider.TeacherProvider;
import com.example.ateachingapplication.ResourceTable;
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
import ohos.agp.window.dialog.ToastDialog;

import java.util.List;

public class SelectTeacherSlice extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_suggest_select_teacher);
        back = (Image)findComponentById(ResourceTable.Id_suggest_back);
        back.setClickedListener(this);
        String subjectSelect = intent.getStringParam("subject");
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
        ListContainer listContainer_ = (ListContainer)findComponentById(ResourceTable.Id_listcontainer_suggest_select_teacher);
        ZZRHttp.get("http://101.132.74.147:8081/teacher/findTeacherBySubject?subject=" + subjectSelect,  new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(SelectTeacherSlice.this)
                        .setText("网络不稳定")
                        .show();
            }
            @Override
            public void onResponse(String s) {
                Result result = gson.fromJson(s, Result.class);
                if (result.getCode() == 200){
                    List<Teacher> list = gson.fromJson(result.getResult(),new TypeToken<List<Teacher>>(){}.getType());
                    if (list.size()>0){
                        TeacherProvider teacherProvider = new TeacherProvider(list,SelectTeacherSlice.this);
                        listContainer_.setItemProvider(teacherProvider);
                        listContainer_.setItemClickedListener(new ListContainer.ItemClickedListener() {
                            @Override
                            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                                Teacher teacher = (Teacher) listContainer_.getItemProvider().getItem(i);

                                Intent intent = new Intent();
                                intent.setParam("teacher", teacher);
                                setResult(intent);
                                terminate();
                            }
                        });
                    }
                }else if (result.getCode() == 400){
                    new ToastDialog(SelectTeacherSlice.this)
                            .setText("查询不到结果...")
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

