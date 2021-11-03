package com.example.ateachingapplication.slice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.domain.Reserve;
import com.example.ateachingapplication.domain.Teacher;
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

public class ReserveTeacherSlice extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private TextField startTY;
    private TextField startTM;
    private TextField startTD;
    private TextField endTY;
    private TextField endTM;
    private TextField endTD;
    private Button submit;
    private Text teacherName;
    private Text teacherSub;
    private String myTEL;
    private Teacher teacher;
    private Parent parent;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_reserve_teacher);
        teacher = intent.getSerializableParam("teacher");
        parent = intent.getSerializableParam("my");
        teacherName = (Text)findComponentById(ResourceTable.Id_reserve_teacher_name);
        teacherSub = (Text)findComponentById(ResourceTable.Id_reserve_teacher_subject);
        startTY = (TextField)findComponentById(ResourceTable.Id_reserve_start_time_year);
        startTM = (TextField)findComponentById(ResourceTable.Id_reserve_start_time_moth);
        startTD = (TextField)findComponentById(ResourceTable.Id_reserve_start_time_day);
        endTY = (TextField)findComponentById(ResourceTable.Id_reserve_end_time_year);
        endTM = (TextField)findComponentById(ResourceTable.Id_reserve_end_time_moth);
        endTD = (TextField)findComponentById(ResourceTable.Id_reserve_end_time_day);
        submit = (Button)findComponentById(ResourceTable.Id_submit_book_teacher);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        submit.setClickedListener(this);
        teacherName.setText(teacher.getTeacherName());
        teacherSub.setText(teacher.getSubject());

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
        if (component.getId() == ResourceTable.Id_submit_book_teacher){
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
            if (startTY.getText().equals("")||startTM.getText().equals("")||startTD.getText().equals("")||endTY.getText().equals("")||endTM.getText().equals("")||endTD.getText().equals("")){
                new ToastDialog(this)
                        .setText("预约信息不能为空！")
                        .show();
                return;
            }
            Reserve reserve = new Reserve();
            reserve.setParPhone(parent.getParPhone());
            reserve.setTeacherPhone(teacher.getTeacherPhone());
            reserve.setTeacherName(teacher.getTeacherName());
            reserve.setSubject(teacher.getSubject());
            reserve.setStartDate(startTime);
            reserve.setEndDate(endTime);
            String url = "http://101.132.74.147:8081/reserve/makeReserve";
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(reserve);
            ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(ReserveTeacherSlice.this)
                            .setText("网络不稳定")
                            .show();
                }

                @Override
                public void onResponse(String s) {
                    Result result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        new ToastDialog(ReserveTeacherSlice.this)
                                .setText("预约教师成功！详情到信息中查看")
                                .show();
                        terminate();
                    }else if (result.getCode() == 400){
                        new ToastDialog(ReserveTeacherSlice.this)
                                .setText("预约的时间段与老师安排时间冲突")
                                .setDuration(5000)
                                .show();
                    }
                }
            });
        }
    }
}
