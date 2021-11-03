package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.slice.DeleteTeacherSlice;
import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.domain.Reserve;
import com.example.ateachingapplication.provider.ReserveProvider;
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

public class MyTeacher extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Parent parent;
    private List<Reserve> list = null;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_my_teacher);
        parent = intent.getSerializableParam("my");
        back = (Image) findComponentById(ResourceTable.Id_back);
        ListContainer listContainer_ = (ListContainer) findComponentById(ResourceTable.Id_listcontainer_my_teacher);
        back.setClickedListener(this);
        String url = "http://101.132.74.147:8081/reserve/myReserveList";
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(parent);
        ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(MyTeacher.this)
                        .setText("网络不稳定")
                        .show();
            }
            @Override
            public void onResponse(String s) {
                Result result = gson.fromJson(s, Result.class);
                if (result.getCode() == 200) {
                    list = gson.fromJson(result.getResult(), new TypeToken<List<Reserve>>() {}.getType());
                    ReserveProvider reserveProvider = new ReserveProvider(list, MyTeacher.this);
                    listContainer_.setItemProvider(reserveProvider);
                    listContainer_.setItemClickedListener(new ListContainer.ItemClickedListener() {
                        @Override
                        public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                            Reserve reserve = (Reserve) listContainer_.getItemProvider().getItem(i);
                            Intent intent1 = new Intent();
                            intent1.setParam("reserve", reserve);
                            present(new DeleteTeacherSlice(), intent1);
                        }
                    });
                } else if (result.getCode() == 400) {
                    new ToastDialog(MyTeacher.this)
                            .setText("无预约信息")
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
