package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.ParMoney;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.ToastDialog;

public class Charge extends AbilitySlice implements Component.ClickedListener {
        private Image back;
        private Button sureCharge;
        private TextField moneyNum;
        private ParMoney parMoney;
        @Override
        public void onStart(Intent intent) {
            super.onStart(intent);
            super.setUIContent(ResourceTable.Layout_charge);
            parMoney = intent.getSerializableParam("ParMoney");
            sureCharge = (Button) findComponentById(ResourceTable.Id_sure_charge_money);
            moneyNum = (TextField)findComponentById(ResourceTable.Id_money_num);
            back = (Image)findComponentById(ResourceTable.Id_back);
            moneyNum.setText("");
            back.setClickedListener(this);
            sureCharge.setClickedListener(this);
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
            if (component.getId() == ResourceTable.Id_sure_charge_money){
                if (moneyNum.getText().equals("")){
                    new ToastDialog(Charge.this)
                            .setText("充值金额不能为空！")
                            .show();
                    return;
                } else if (Double.parseDouble(moneyNum.getText())>50000){
                    new ToastDialog(Charge.this)
                            .setText("充值金额超过单笔上限！")
                            .show();
                    return;
                }
                String url = "http://101.132.74.147:8081/parMoney/recharge";
                Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
                parMoney.setBalance(Double.parseDouble(moneyNum.getText())+parMoney.getBalance());
                String json = gson.toJson(parMoney);
                ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(Charge.this)
                                .setText("网络不稳定")
                                .show();
                    }
                    @Override
                    public void onResponse(String s) {
                        Result result = gson.fromJson(s,Result.class);
                        if (result.getCode() == 200){
                            new ToastDialog(Charge.this)
                                    .setText(result.getMsg())
                                    .show();
                        }else if (result.getCode() == 400){
                            new ToastDialog(Charge.this)
                                    .setText(result.getMsg())
                                    .setDuration(5000)
                                    .show();
                        }
                    }
                });
            }
        }
}
