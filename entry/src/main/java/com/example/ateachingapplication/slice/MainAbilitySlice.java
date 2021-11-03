package com.example.ateachingapplication.slice;

import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.domain.Parent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.ToastDialog;

public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Button register;
    private Button login;
    private TextField loginName;
    private TextField loginPwd;
    private Text forgetPassword;
    Result result = new Result();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_login);
        login = (Button)findComponentById(ResourceTable.Id_login);
        login.setClickedListener(this);
        register = (Button)findComponentById(ResourceTable.Id_register_user);
        register.setClickedListener(this);
        loginName =(TextField)findComponentById(ResourceTable.Id_login_phonenum);
        loginPwd = (TextField)findComponentById(ResourceTable.Id_login_password);
        forgetPassword = (Text)findComponentById(ResourceTable.Id_forget_password);
        forgetPassword.setClickedListener(this);
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
        String url = "http://101.132.74.147:8081/parent/login";
        if (component == register){
            Intent intent = new Intent();
            present(new RegisterAbilitySlice(),intent);
        }
        if(component == forgetPassword){
            Intent intent = new Intent();
            present(new ForgetPasswordSlice(),intent);
        }
        if (component == login){
            if (loginName.getText().equals("")||loginPwd.getText().equals("")){
                new ToastDialog(this)
                        .setText("用户名或密码不能为空！")
                        .show();
            }else {
                Parent parent = new Parent();
                parent.setParPhone(loginName.getText());
                parent.setPassword(loginPwd.getText());
                Gson gson = new GsonBuilder().serializeNulls().create();
                String json = gson.toJson(parent);
                ZZRHttp.postJson(url,json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(MainAbilitySlice.this)
                                .setText("网络不稳定")
                                .show();
                    }

                    @Override
                    public void onResponse(String s) {
                        //访问成功
                        //s为result的json格式
                        result = gson.fromJson(s,Result.class);
                        if (result.getCode()==200){
                            Parent parent1 = null;
                            parent1 = gson.fromJson(result.getResult(),Parent.class);
                            if (parent1 != null){
                                //用户名和密码都正确
                                //进应用
                                //登录主页面
                                Intent intent = new Intent();
                                intent.setParam("my",parent1);
                                Operation operation = new Intent.OperationBuilder()
                                        .withDeviceId("")
                                        .withBundleName("com.example.ateachingapplication")
                                        .withAbilityName("com.example.ateachingapplication.TeachingAbility")
                                        .build();
                                intent.setOperation(operation);
                                startAbility(intent);
                            }
                        }else if (result.getCode() == 400){
                            new ToastDialog(MainAbilitySlice.this)
                                    .setText(result.getMsg())
                                    .show();
                        }
                    }
                });
            }

        }
    }
}
