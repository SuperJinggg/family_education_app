package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.ParReward;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.provider.ParRewardProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.agp.window.dialog.ToastDialog;

import java.util.List;

public class Award extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Parent parent;
    String url = "http://101.132.74.147:8081/parReward";
    private Result result =new Result();

    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_award);
        parent = intent.getSerializableParam("my");
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        ListContainer listContainer = (ListContainer) findComponentById(ResourceTable.Id_listcontainer_award);
        String json = gson.toJson(parent);
        ZZRHttp.postJson(url+"/myReward", json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(Award.this)
                        .setText("网络不稳定")
                        .show();
            }

            @Override
            public void onResponse(String s) {
                result = gson.fromJson(s,Result.class);
                if (result.getCode() == 200){
                    List<ParReward> list = gson.fromJson(result.getResult(),new TypeToken<List<ParReward>>(){}.getType());
                    if (list.size()>0){
                        ParRewardProvider parRewardProvider = new ParRewardProvider(list,Award.this);
                        listContainer.setItemProvider(parRewardProvider);
                    }else {
                        //创建弹框对象
                        CommonDialog commonDialog = new CommonDialog(Award.this);
                        //设置弹框标题
                        commonDialog.setTitleText("提示");
                        //设置弹框提示内容
                        commonDialog.setContentText("无可用的优惠卷");
                        //点击弹框外部可关闭弹框
                        commonDialog.setAutoClosable(true);
                        //设置弹框选择按钮
                        commonDialog.setButton(0, "确定", new IDialog.ClickedListener() {
                            @Override
                            public void onClick(IDialog iDialog, int i) {
                                commonDialog.destroy();
                            }
                        });
                        //将弹框展示出来
                        commonDialog.show();
                    }
                }else if(result.getCode() == 400){
                    new ToastDialog(Award.this)
                            .setText("没有优惠券！")
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
    }
}
