package com.example.ateachingapplication.slice.game;

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
import ohos.agp.window.dialog.ToastDialog;

public class GameSelect extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Button game1;

    private ParMoney parMoney;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_game_select);

        parMoney = intent.getSerializableParam("ParMoney");
        game1 = (Button)findComponentById(ResourceTable.Id_select_game1);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        game1.setClickedListener(this);
    }

    @Override
    protected void onResult(int requestCode, Intent resultIntent) {
        super.onResult(requestCode, resultIntent);
        switch (requestCode){
            case 100:
                int addPoints = resultIntent.getIntParam("points",0);
                String url = "http://101.132.74.147:8081/parMoney/recharge";
                Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
                parMoney.setPoint(parMoney.getPoint()+addPoints);
                String json = gson.toJson(parMoney);
                ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(GameSelect.this)
                                .setText("网络不稳定")
                                .show();
                    }
                    @Override
                    public void onResponse(String s) {
                        Result result = gson.fromJson(s,Result.class);
                        if (result.getCode() == 200){
                            new ToastDialog(GameSelect.this)
                                    .setText(result.getMsg())
                                    .show();
                        }else if (result.getCode() == 400){
                            new ToastDialog(GameSelect.this)
                                    .setText(result.getMsg())
                                    .setDuration(5000)
                                    .show();
                        }
                    }
                });
        }
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
        if (component.getId() == ResourceTable.Id_select_game1){
            Intent intent = new Intent();
            presentForResult(new Game1(),intent,100);
        }
    }
}
