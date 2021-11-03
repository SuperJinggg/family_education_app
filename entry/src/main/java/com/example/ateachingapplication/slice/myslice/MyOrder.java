package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.BookOrder;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.provider.BookOrderProvider;
import com.example.ateachingapplication.slice.DeleteBookOrderSlice;
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


public class MyOrder extends AbilitySlice implements Component.ClickedListener {
    String url = "http://101.132.74.147:8081/bookOrder";
    private Image back ;
    private Parent parent;
    private Result result = new Result();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_my_order);
        parent = intent.getSerializableParam("my");
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        ListContainer listContainer_ = (ListContainer)findComponentById(ResourceTable.Id_listcontainer_order);
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(parent);
        ZZRHttp.postJson(url + "/myOrderList", json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(MyOrder.this)
                        .setText("网络不稳定")
                        .show();
            }

            @Override
            public void onResponse(String s) {
                result = gson.fromJson(s,Result.class);
                if (result.getCode() == 200){
                    List<BookOrder> list = gson.fromJson(result.getResult(),new TypeToken<List<BookOrder>>(){}.getType());
                    if (list.size()>0){
                        BookOrderProvider bookOrderProvider = new BookOrderProvider(list,MyOrder.this);
                        listContainer_.setItemProvider(bookOrderProvider);
                        listContainer_.setItemClickedListener(new ListContainer.ItemClickedListener() {
                            @Override
                            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                                BookOrder bookOrder = (BookOrder) listContainer_.getItemProvider().getItem(i);
                                Intent intent = new Intent();
                                intent.setParam("book",bookOrder);
                                present(new DeleteBookOrderSlice(), intent);
                            }
                        });
                    }
                }else if (result.getCode() == 400){
                    new ToastDialog(MyOrder.this)
                            .setText("没有订单")
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