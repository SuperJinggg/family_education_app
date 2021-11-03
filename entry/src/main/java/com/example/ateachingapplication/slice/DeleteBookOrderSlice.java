package com.example.ateachingapplication.slice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.BookOrder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;

public class DeleteBookOrderSlice extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private BookOrder bookOrder;
    private Text bookName;
    private Button deleteBook;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_delete_book_order);
        bookName = (Text)findComponentById(ResourceTable.Id_delete_book_name);
        deleteBook = (Button)findComponentById(ResourceTable.Id_delete_book);
        bookOrder = intent.getSerializableParam("book");
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        deleteBook.setClickedListener(this);
        bookName.setText(bookOrder.getBookName());
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
        if (component.getId() == ResourceTable.Id_delete_book){
            String url = "http://101.132.74.147:8081/bookOrder/cancelOrder";
            Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(bookOrder);
            ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(DeleteBookOrderSlice.this)
                            .setText("网络不稳定")
                            .show();
                }
                @Override
                public void onResponse(String s) {
                    Result result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        new ToastDialog(DeleteBookOrderSlice.this)
                                .setText("取消订单成功！")
                                .show();
                        terminate();
                    }else if (result.getCode() == 400){
                        new ToastDialog(DeleteBookOrderSlice.this)
                                .setText(result.getMsg())
                                .show();
                    }
                }
            });
        }
    }
}

