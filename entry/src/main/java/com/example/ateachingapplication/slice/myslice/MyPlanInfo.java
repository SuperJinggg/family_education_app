package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Plan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;

import java.text.SimpleDateFormat;

public class MyPlanInfo extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Plan plan;
    private Text startTime;
    private Text endTime;
    private Text planInfo;
    private Button deletePlan;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_plan_info);
        plan = intent.getSerializableParam("plan");
        startTime =(Text)findComponentById(ResourceTable.Id_plan_start_time);
        endTime =(Text)findComponentById(ResourceTable.Id_plan_end_time);
        planInfo =(Text)findComponentById(ResourceTable.Id_plan_info);
        deletePlan = (Button)findComponentById(ResourceTable.Id_delete_plan);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        deletePlan.setClickedListener(this);
        planInfo.setText(plan.getPlanContent());
        startTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(plan.getStartDate()));
        endTime.setText(new SimpleDateFormat("yyyy-MM-dd").format(plan.getEndDate()));
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
        if (component.getId() == ResourceTable.Id_delete_plan){
            String url = "http://101.132.74.147:8081/plan/deletePlan";
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(plan);
            ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(MyPlanInfo.this)
                            .setText("网络不稳定")
                            .show();
                }

                @Override
                public void onResponse(String s) {
                    Result result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        new ToastDialog(MyPlanInfo.this)
                                .setText(result.getMsg())
                                .show();
                        terminate();
                    }else if (result.getCode() == 400){
                        new ToastDialog(MyPlanInfo.this)
                                .setText(result.getMsg())
                                .show();
                    }
                }
            });
        }
    }
}
