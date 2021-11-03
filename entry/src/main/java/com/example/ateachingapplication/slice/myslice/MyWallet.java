package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.ParMoney;
import com.example.ateachingapplication.domain.Parent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;

public class MyWallet extends AbilitySlice implements Component.ClickedListener {
    private Text myMoney;
    private Text myPoints;
    private DirectionalLayout money;
    private DirectionalLayout points;
    private Image back;
    private Parent parent;
    private ParMoney parMoney = null;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_my_wallet);
        parent = intent.getSerializableParam("my");
        myMoney = (Text)findComponentById(ResourceTable.Id_my_money);
        myPoints = (Text)findComponentById(ResourceTable.Id_my_points);
        money = (DirectionalLayout)findComponentById(ResourceTable.Id_money);
        points = (DirectionalLayout)findComponentById(ResourceTable.Id_points);
        back = (Image)findComponentById(ResourceTable.Id_back);
        money.setClickedListener(this);
        points.setClickedListener(this);
        back.setClickedListener(this);
        String url = "http://101.132.74.147:8081/parMoney/myMoney";
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(parent);
        ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(MyWallet.this)
                        .setText("网络不稳定")
                        .show();
            }
            @Override
            public void onResponse(String s) {
                Result result = gson.fromJson(s,Result.class);
                if (result.getCode() == 200){

                    parMoney = gson.fromJson(result.getResult(),ParMoney.class);
                    if (parMoney != null){
                        myMoney.setText(parMoney.getBalance()+"");
                        myPoints.setText(parMoney.getPoint()+"");
                    }
                }else if (result.getCode() == 400){
                    new ToastDialog(MyWallet.this)
                            .setText("获取账户金额失败")
                            .setDuration(5000)
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
        if (component.getId() == ResourceTable.Id_money){
            Intent intent = new Intent();
            intent.setParam("wallet",parMoney);
            present(new ChargeMoney(),intent);
        }
        if (component.getId() == ResourceTable.Id_points){
            Intent intent = new Intent();
            intent.setParam("wallet",parMoney);
            intent.setParam("my",parent);
            present(new ChargePoints(),intent);
        }
    }
}