package com.example.ateachingapplication.slice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Reserve;
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

public class DeleteTeacherSlice extends AbilitySlice implements Component.ClickedListener {
    private Text teacherSub;
    private Text teacherTel;
    private Text teacherName;
    private Text startTime;
    private Text endTime;
    private Reserve reserve;
    private Image back;
    private Button deleteReserve;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_delect_reserve_teacher);
        reserve = intent.getSerializableParam("reserve");
        teacherSub = (Text)findComponentById(ResourceTable.Id_subject);
        teacherTel = (Text)findComponentById(ResourceTable.Id_telephone);
        teacherName = (Text)findComponentById(ResourceTable.Id_name);
        startTime = (Text)findComponentById(ResourceTable.Id_start_time);
        endTime  = (Text)findComponentById(ResourceTable.Id_end_time);
        deleteReserve = (Button)findComponentById(ResourceTable.Id_delete_teacher);
        deleteReserve.setClickedListener(this);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyy-MM-dd");
        teacherSub.setText(reserve.getSubject());
        teacherTel.setText(reserve.getTeacherPhone());
        teacherName.setText(reserve.getTeacherName());
        startTime.setText(simpleDateFormat.format(reserve.getStartDate()));
        endTime.setText(simpleDateFormat.format(reserve.getEndDate()));
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
        if (component.getId() == ResourceTable.Id_delete_teacher){
            String url = "http://101.132.74.147:8081/reserve/deleteReserve";
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(reserve);
            ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(DeleteTeacherSlice.this)
                            .setText("网络不稳定")
                            .show();
                }
                @Override
                public void onResponse(String s) {
                    Result result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        new ToastDialog(DeleteTeacherSlice.this)
                                .setText("取消预约成功！详情到信息中查看")
                                .show();
                        terminate();
                    }else if (result.getCode() == 400){
                        new ToastDialog(DeleteTeacherSlice.this)
                                .setText("取消预约失败")
                                .setDuration(5000)
                                .show();
                    }
                }
            });

        }
    }
}
