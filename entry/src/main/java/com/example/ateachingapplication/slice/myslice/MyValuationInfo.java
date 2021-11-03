package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Evaluate;
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

public class MyValuationInfo extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Evaluate evaluate;
    private Text teacherName;
    private Text teacherSub;
    private Text teacherInfo;
    private Button deleteValuation;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_valuation_info);
        evaluate = intent.getSerializableParam("valuation");
        teacherName = (Text)findComponentById(ResourceTable.Id_valuation_name);
        teacherSub = (Text)findComponentById(ResourceTable.Id_valuation_subject);
        teacherInfo = (Text)findComponentById(ResourceTable.Id_valuation_info);
        teacherName.setText(evaluate.getTeacherName());
        teacherSub.setText(evaluate.getSubject());
        teacherInfo.setText(evaluate.getContent());
        deleteValuation = (Button)findComponentById(ResourceTable.Id_delete_valuation);
        deleteValuation.setClickedListener(this);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
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
        if (component.getId()==ResourceTable.Id_delete_valuation){
            String url = "http://101.132.74.147:8081/evaluate/deleteEvaluate";
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(evaluate);
            ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(MyValuationInfo.this)
                            .setText("网络不稳定")
                            .show();
                }

                @Override
                public void onResponse(String s) {
                    Result result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        new ToastDialog(MyValuationInfo.this)
                                .setText(result.getMsg())
                                .show();
                        terminate();
                    }else if (result.getCode() == 400){
                        new ToastDialog(MyValuationInfo.this)
                                .setText(result.getMsg())
                                .show();
                    }
                }
            });
        }
    }
}

