package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.Evaluate;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.provider.ValuationProvider;
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
import ohos.agp.window.dialog.ToastDialog;

import java.util.List;

public class MyValuation extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Parent parent;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_my_valuation);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        parent = intent.getSerializableParam("my");
        String url = "http://101.132.74.147:8081/evaluate/myEvaluateList";
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(parent);
        ListContainer listContainer_ = (ListContainer) findComponentById(ResourceTable.Id_listcontainer_valuation);
        ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(MyValuation.this)
                        .setText("网络不稳定")
                        .show();
            }

            @Override
            public void onResponse(String s) {
                Result result = gson.fromJson(s,Result.class);
                if (result.getCode() == 200){
                    List<Evaluate> list = gson.fromJson(result.getResult(),new TypeToken<List<Evaluate>>(){}.getType());
                    ValuationProvider provider = new ValuationProvider(list,MyValuation.this);
                    listContainer_.setItemProvider(provider);
                    listContainer_.setItemClickedListener(new ListContainer.ItemClickedListener() {
                        @Override
                        public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                            Evaluate evaluate = (Evaluate)listContainer_.getItemProvider().getItem(i);
                            Intent intent1 = new Intent();
                            intent1.setParam("valuation",evaluate);
                            present(new MyValuationInfo(),intent1);
                        }
                    });
                }else if (result.getCode() == 400){
                    new ToastDialog(MyValuation.this)
                            .setText("没有评价信息")
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