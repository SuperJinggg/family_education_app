package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.domain.Plan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MakePlan extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private TextField startTY;
    private TextField startTM;
    private TextField startTD;
    private TextField endTY;
    private TextField endTM;
    private TextField endTD;
    private TextField planInfo;
    private Button submit;
    private Parent parent;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_make_plan);
        parent = intent.getSerializableParam("my");
        startTY = (TextField)findComponentById(ResourceTable.Id_plan_start_time_year);
        startTM = (TextField)findComponentById(ResourceTable.Id_plan_start_time_moth);
        startTD = (TextField)findComponentById(ResourceTable.Id_plan_start_time_day);
        endTY = (TextField)findComponentById(ResourceTable.Id_plan_end_time_year);
        endTM = (TextField)findComponentById(ResourceTable.Id_plan_end_time_moth);
        endTD = (TextField)findComponentById(ResourceTable.Id_plan_end_time_day);
        planInfo = (TextField) findComponentById(ResourceTable.Id_make_plan_info);
        submit = (Button)findComponentById(ResourceTable.Id_sure_plan);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        submit.setClickedListener(this);
        startTY.setText("");
        startTM.setText("");
        startTD.setText("");
        endTD.setText("");
        endTM.setText("");
        endTY.setText("");
        planInfo.setText("");
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
        if (component.getId() == ResourceTable.Id_sure_plan){
            String startT = startTY.getText() + "-" + startTM.getText() + "-" + startTD.getText();
            String endT = endTY.getText() + "-" + endTM.getText() + "-" + endTD.getText();
            Date startDate=null;
            Date endDate=null;
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startT);
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endT);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            java.sql.Date startTime = new java.sql.Date(startDate.getTime());
            java.sql.Date endTime = new java.sql.Date(endDate.getTime());
            if (startTY.getText().equals("")||startTM.getText().equals("")||startTD.getText().equals("")||endTY.getText().equals("")||endTM.getText().equals("")||endTD.getText().equals("")||planInfo.getText().equals("")){
                new ToastDialog(this)
                        .setText("计划信息不能为空！")
                        .show();
                return;
            }
            Plan plan = new Plan();
            plan.setParPhone(parent.getParPhone());
            plan.setPlanContent(planInfo.getText());
            plan.setStartDate(startTime);
            plan.setEndDate(endTime);
            String url = "http://101.132.74.147:8081/plan/makePlan";
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(plan);
            ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(MakePlan.this)
                            .setText("网络不稳定")
                            .show();
                }

                @Override
                public void onResponse(String s) {
                    Result result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        new ToastDialog(MakePlan.this)
                                .setText("制定计划成功！")
                                .show();
                        terminate();
                    }else if (result.getCode() == 400){
                        new ToastDialog(MakePlan.this)
                                .setText(result.getMsg())
                                .setDuration(5000)
                                .show();
                    }
                }
            });
        }
    }
}