package com.example.ateachingapplication.slice.messageslice;

import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Evaluate;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.domain.Teacher;
import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.slice.searchslice.SubjectSearchSlice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;

import java.sql.Date;

public class OrderSuggestionSlice extends AbilitySlice implements Component.ClickedListener {
    private Image backArrow;
    private Button suggestionReport;
    private Text suggestionSubject;
    private Text suggestionTopic;
    private TextField suggestion;
    private Parent parent;
    private Teacher teacher;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_mes_order_suggest);
        parent = intent.getSerializableParam("my");
        backArrow = (Image)findComponentById(ResourceTable.Id_order_suggest_back_arrow);
        suggestionReport = (Button)findComponentById(ResourceTable.Id_suggest_report);
        suggestionSubject = (Text)findComponentById(ResourceTable.Id_suggest_subject);
        suggestionTopic = (Text)findComponentById(ResourceTable.Id_suggest_topic);
        suggestion = (TextField)findComponentById(ResourceTable.Id_suggestion);
        suggestionSubject.setClickedListener(this);
        suggestionTopic.setClickedListener(this);
        backArrow.setClickedListener(this);
        suggestionReport.setClickedListener(this);
        suggestionTopic.setText("");
        suggestionSubject.setText("");
        suggestion.setText("");
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
    protected void onResult(int requestCode, Intent resultIntent) {
        super.onResult(requestCode, resultIntent);
        if (requestCode == 400){
            String subject = resultIntent.getStringParam("subjectname");
            if (subject.equals("科目")){return;}
            suggestionSubject.setText(subject);
        }else if (requestCode == 500){
            teacher = resultIntent.getSerializableParam("teacher");
            suggestionTopic.setText(teacher.getTeacherName());
        }
    }

    @Override
    public void onClick(Component component) {
        if (component.getId()==ResourceTable.Id_order_suggest_back_arrow){
            terminate();
        }
        if (component.getId()==ResourceTable.Id_suggest_subject){
            Intent intent = new Intent();
            presentForResult(new SubjectSearchSlice(),intent,400);
        }
        if (component.getId()==ResourceTable.Id_suggest_topic){
            if (suggestionSubject.getText().equals("")){
                suggestionTopic.setText("请先选择科目！");
                suggestionTopic.setTextColor(Color.RED);
                return;
            }
            Intent intent = new Intent();
           intent.setParam("subject",suggestionSubject.getText());
            presentForResult(new SelectTeacherSlice(),intent,500);
        }
        if (component.getId()==ResourceTable.Id_suggest_report){
            if (suggestionTopic.getText().equals("")||suggestion.getText().equals("")||suggestionSubject.getText().equals("")){
                return;
            }
            String url = "http://101.132.74.147:8081/evaluate/makeEvaluate";
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
            Evaluate evaluate = new Evaluate();
            java.util.Date date1= new java.util.Date();
            Date date = new Date(date1.getTime());
            evaluate.setParPhone(parent.getParPhone());
            evaluate.setTeacherName(teacher.getTeacherName());
            evaluate.setTeacherPhone(teacher.getTeacherPhone());
            evaluate.setSubject(suggestionSubject.getText());
            evaluate.setContent(suggestion.getText());
            evaluate.setDates(date);

            String json = gson.toJson(evaluate);
            ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(OrderSuggestionSlice.this)
                            .setText("网络不稳定")
                            .show();
                }
                @Override
                public void onResponse(String s) {
                    Result result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        new ToastDialog(OrderSuggestionSlice.this)
                                .setText(result.getMsg())
                                .show();
                    }else if (result.getCode() == 400){
                        new ToastDialog(OrderSuggestionSlice.this)
                                .setText(result.getMsg())
                                .show();
                    }
                }
            });

        }
    }
}