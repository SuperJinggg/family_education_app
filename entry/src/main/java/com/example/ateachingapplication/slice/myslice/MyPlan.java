package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.domain.Plan;
import com.example.ateachingapplication.provider.PlanProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

import java.util.List;

public class MyPlan extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private DirectionalLayout makePlan;
    private Parent parent;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_my_plan);
        parent = intent.getSerializableParam("my");
        back = (Image)findComponentById(ResourceTable.Id_back);
        makePlan = (DirectionalLayout) findComponentById(ResourceTable.Id_make_plan);
        makePlan.setClickedListener(this);
        back.setClickedListener(this);
        ListContainer listContainer_ = (ListContainer)findComponentById(ResourceTable.Id_listcontainer_plan);
        String url = "http://101.132.74.147:8081/plan/myPlan";
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(parent);
        ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(MyPlan.this)
                        .setText("网络不稳定")
                        .show();
            }
            @Override
            public void onResponse(String s) {
                Result result = gson.fromJson(s,Result.class);
                if (result.getCode() == 200){
                    List<Plan> list = gson.fromJson(result.getResult(),new TypeToken<List<Plan>>(){}.getType());
                    PlanProvider provider = new PlanProvider(list,MyPlan.this);
                    listContainer_.setItemProvider(provider);
                    listContainer_.setItemClickedListener(new ListContainer.ItemClickedListener() {
                        @Override
                        public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                            Plan plan = (Plan) listContainer_.getItemProvider().getItem(i);
                            Intent intent1 = new Intent();
                            intent1.setParam("plan",plan);
                            present(new MyPlanInfo(),intent1);
                        }
                    });
                }else if (result.getCode() == 400){
                    new ToastDialog(MyPlan.this)
                            .setText("没有计划")
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
        if (component.getId() == ResourceTable.Id_make_plan){
            Intent intent = new Intent();
            intent.setParam("my",parent);
            present(new MakePlan(),intent);
        }
    }
}
