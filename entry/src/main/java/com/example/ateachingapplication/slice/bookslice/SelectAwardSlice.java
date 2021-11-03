package com.example.ateachingapplication.slice.bookslice;

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
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

import java.util.List;

public class SelectAwardSlice extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Button sure;
    private Text discountPrice;
    private Parent parent;
    Result result = new Result();
    private ParReward discount;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_select_award);
        parent = intent.getSerializableParam("my");
        sure = (Button)findComponentById(ResourceTable.Id_sure_discount);
        discountPrice = (Text)findComponentById(ResourceTable.Id_sure_discount_price);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        sure.setClickedListener(this);
        ListContainer listContainer_ = (ListContainer) findComponentById(ResourceTable.Id_listcontainer_discount);
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(parent);
        ZZRHttp.postJson( "http://101.132.74.147:8081/parReward/myReward", json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(SelectAwardSlice.this)
                        .setText("网络不稳定")
                        .show();
            }

            @Override
            public void onResponse(String s) {
                result = gson.fromJson(s, Result.class);
                if (result.getCode() == 200){
                    List<ParReward> list = gson.fromJson(result.getResult(),new TypeToken<List<ParReward>>(){}.getType());
                    if (list.size()>0){
                        ParRewardProvider parRewardProvider = new ParRewardProvider(list,SelectAwardSlice.this);
                        listContainer_.setItemProvider(parRewardProvider);
                        listContainer_.setItemClickedListener(new ListContainer.ItemClickedListener() {
                            @Override
                            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {

                                discount = (ParReward)listContainer_.getItemProvider().getItem(i);
                                discountPrice.setText(discount.getCost()+"");
                            }
                        });
                    }
                }else if(result.getCode() == 400){
                    new ToastDialog(SelectAwardSlice.this)
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
            Intent intent = new Intent();
            intent.setParam("discount",discount);
            intent.setParam("discountPrice","");
            setResult(intent);
            terminate();
        }
        if (component.getId() == ResourceTable.Id_sure_discount){
            Intent intent = new Intent();
            intent.setParam("discount",discount);
            intent.setParam("discountPrice",discountPrice.getText());
            setResult(intent);
            terminate();
        }
    }
}
