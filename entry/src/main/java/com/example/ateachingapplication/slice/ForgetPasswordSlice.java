package com.example.ateachingapplication.slice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.utils.VerifyCodeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.ToastDialog;

public class ForgetPasswordSlice extends AbilitySlice implements Component.ClickedListener {
    String code;
    Button generateCode;
    Result result = new Result();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_forget_password);

        //单击注册按钮跳转到注册页面
        Button update = (Button) findComponentById(ResourceTable.Id_update);
        update.setClickedListener(this);

        generateCode = (Button) findComponentById(ResourceTable.Id_generate_code);
        generateCode.setClickedListener(this);

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
        switch(component.getId()){
            case ResourceTable.Id_generate_code:
                code = VerifyCodeUtils.generateVerifyCode(6);
                generateCode.setText(code);
                break;
            case ResourceTable.Id_update:
                String url = "http://101.132.74.147:8081/parent/forgetPassword";
                TextField phone = (TextField) findComponentById(ResourceTable.Id_update_phone);
                TextField password = (TextField) findComponentById(ResourceTable.Id_update_password);
                TextField passwords = (TextField) findComponentById(ResourceTable.Id_password_again);
                TextField codes = (TextField) findComponentById(ResourceTable.Id_update_code);
                if(phone.getText().equals("")||password.getText().equals("")||passwords.getText().equals("")||codes.getText().equals("")){
                    new ToastDialog(this)
                            .setText("请输入完整信息")
                            .show();
                }else if(!password.getText().equals(passwords.getText())){
                    new ToastDialog(this)
                            .setText("密码不一致")
                            .show();
                }else if(!codes.getText().equals(code)){
                    new ToastDialog(this)
                            .setText("验证码错误")
                            .show();
                }else{
                    Parent parent = new Parent();
                    parent.setParPhone(phone.getText());
                    parent.setPassword(password.getText());
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String json = gson.toJson(parent);
                    ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                        @Override
                        public void onFailure(int i, String s) {
                            new ToastDialog(ForgetPasswordSlice.this)
                                    .setText("网络不稳定")
                                    .show();
                        }

                        @Override
                        public void onResponse(String s) {
                            result = gson.fromJson(s, Result.class);
                            if (result.getCode() == 200) {
                                Parent parent1 =null;
                                parent1 = gson.fromJson(result.getResult(),Parent.class);
                                if(parent1!=null){
                                    new ToastDialog(ForgetPasswordSlice.this)
                                            .setText(result.getMsg())
                                            .show();
                                    Intent intent = new Intent();
                                    present(new MainAbilitySlice(), intent);
                                }
                            } else if (result.getCode() == 400) {
                                new ToastDialog(ForgetPasswordSlice.this)
                                        .setText(result.getMsg())
                                        .show();
                            }
                        }
                    });
                }
                break;
        }
    }
}
