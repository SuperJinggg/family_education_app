package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.CAward;
import com.example.ateachingapplication.domain.ParMoney;
import com.example.ateachingapplication.domain.ParReward;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.slice.game.GameSelect;
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

import java.sql.Date;

public class ChargePoints extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Text currentPoints;
    private Button chargeAward;
    private Button chargePoints;
    private ParMoney parMoney;
    private Parent parent;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_charge_points);
        parMoney = intent.getSerializableParam("wallet");
        parent = intent.getSerializableParam("my");
        currentPoints = (Text)findComponentById(ResourceTable.Id_current_points);
        chargeAward = (Button)findComponentById(ResourceTable.Id_charge_award);
        chargePoints = (Button)findComponentById(ResourceTable.Id_charge_points);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        chargeAward.setClickedListener(this);
        chargePoints.setClickedListener(this);
        currentPoints.setText(parMoney.getPoint()+"");

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
        if (requestCode == 100){
            if (resultIntent.getSerializableParam("award")!=null){
                CAward award = resultIntent.getSerializableParam("award");
                ParReward parReward = new ParReward();
                parReward.setParPhone(parent.getParPhone());
                parReward.setCost(award.getCost());
                java.util.Date date1= new java.util.Date();
                Date date = new Date(date1.getTime());
                parReward.setEndDate(date);
                String url = "http://101.132.74.147:8081/parReward/giveReward";
                Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
                String json = gson.toJson(parReward);
                ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(ChargePoints.this)
                                .setText("网络不稳定")
                                .show();
                    }
                    @Override
                    public void onResponse(String s) {
                        Result result = gson.fromJson(s,Result.class);
                        if (result.getCode() == 200){
                            new ToastDialog(ChargePoints.this)
                                    .setText(result.getMsg())
                                    .show();
                        }else if (result.getCode() == 400){
                            new ToastDialog(ChargePoints.this)
                                    .setText(result.getMsg())
                                    .show();
                        }
                    }
                });

                parMoney.setPoint(parMoney.getPoint()-award.getPrice());
                url = "http://101.132.74.147:8081/parMoney/recharge";
                json = gson.toJson(parMoney);
                ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(ChargePoints.this)
                                .setText("网络不稳定")
                                .show();
                    }

                    @Override
                    public void onResponse(String s) {
                        Result result = gson.fromJson(s,Result.class);
                        if (result.getCode() == 200){
                            new ToastDialog(ChargePoints.this)
                                    .setText(result.getMsg())
                                    .show();
                        }else if (result.getCode() == 400){
                            new ToastDialog(ChargePoints.this)
                                    .setText(result.getMsg())
                                    .show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_back){
            terminate();
        }
        if (component.getId() == ResourceTable.Id_charge_points){
            Intent intent = new Intent();

            intent.setParam("ParMoney",parMoney);
            present(new GameSelect(),intent);
        }
        if (component.getId() == ResourceTable.Id_charge_award){
            Intent intent = new Intent();
            presentForResult(new ChargeAward(),intent,100);
        }
    }
}