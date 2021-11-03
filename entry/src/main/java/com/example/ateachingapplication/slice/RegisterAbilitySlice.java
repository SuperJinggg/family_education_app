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
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

public class RegisterAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Button register;
    private Button getCode;
    private TextField registerPhone;
    private TextField registerPwd;
    private TextField surePwd;
    private TextField inputCode;
    private String code;
    private Button backLogin;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_register);
        register = (Button) findComponentById(ResourceTable.Id_register);
        registerPhone = (TextField) findComponentById(ResourceTable.Id_register_phonenum);
        registerPwd = (TextField) findComponentById(ResourceTable.Id_register_password);
        surePwd = (TextField) findComponentById(ResourceTable.Id_sure_password);
        inputCode = (TextField)findComponentById(ResourceTable.Id_code);
        getCode = (Button)findComponentById(ResourceTable.Id_get_code);
        backLogin = (Button)findComponentById(ResourceTable.Id_back_login);
        register.setClickedListener(this);
        getCode.setClickedListener(this);
        backLogin.setClickedListener(this);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    Result result = new Result();

    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_register) {

            String url = "http://101.132.74.147:8081/parent/register";
            if (registerPwd.getText().equals("") || surePwd.getText().equals("") || registerPhone.getText().equals("")) {
                new ToastDialog(this)
                        .setText("手机号和密码不能为空")
                        .show();
            } else if (!registerPwd.getText().equals(surePwd.getText())) {
                //判断两次输入的密码是否相同
                ToastDialog toastDialog = new ToastDialog(this);
                toastDialog.setText("两次输入的密码不一样！");
                toastDialog.show();
            } else if (!code.equals(inputCode.getText())){
                ToastDialog toastDialog = new ToastDialog(this);
                toastDialog.setText("验证码错误！");
                toastDialog.show();
                code = VerifyCodeUtils.generateVerifyCode(4);
                getCode.setText(code);
            }
            else {
                Parent parent = new Parent();
                parent.setParPhone(registerPhone.getText());
                parent.setPassword(registerPwd.getText());
                Gson gson = new GsonBuilder().serializeNulls().create();
                String json = gson.toJson(parent);
                ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(RegisterAbilitySlice.this)
                                .setText("网络不稳定")
                                .show();
                    }

                    @Override
                    public void onResponse(String s) {
                        result = gson.fromJson(s, Result.class);
                        if (result.getCode() == 200) {
                            Parent parent1 = null;
                            parent1 = gson.fromJson(result.getResult(), Parent.class);
                            Intent intent = new Intent();
                            intent.setParam("register_my", parent1);
                            present(new SureRegister(), intent);
                        } else if (result.getCode() == 400) {
                            new ToastDialog(RegisterAbilitySlice.this)
                                    .setText(result.getMsg())
                                    .show();
                        }
                    }
                });
            }
        }
        if (component.getId() == ResourceTable.Id_get_code){
            code = VerifyCodeUtils.generateVerifyCode(6);
            getCode.setText(code);
        }
        if (component.getId() == ResourceTable.Id_back_login){
            terminate();
        }
    }
}