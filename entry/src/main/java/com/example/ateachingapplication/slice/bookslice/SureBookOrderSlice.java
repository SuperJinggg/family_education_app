package com.example.ateachingapplication.slice.bookslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.data.Result;
import com.example.ateachingapplication.domain.BookOrder;
import com.example.ateachingapplication.domain.ParMoney;
import com.example.ateachingapplication.domain.ParReward;
import com.example.ateachingapplication.domain.Parent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.ToastDialog;

import java.util.List;

public class SureBookOrderSlice extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private BookOrder bookOrder;
    private Image bookImage;
    private Text bookName;
    private Text bookNum;
    private Text bookPrice;
    private Text discount;
    private Text finallyPrice;
    private Button submit;
    private Image in;
    private Parent parent;
    private String discountPrice;
    private double price;
    private ParMoney parMoney = null;
    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
    Result result = new Result();
    private ParReward award;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_sure_book_order);
        parent = intent.getSerializableParam("my");
        bookOrder = intent.getSerializableParam("bookOrder");
        int book = intent.getIntParam("bookImage",0);
        bookImage = (Image)findComponentById(ResourceTable.Id_book_image);
        bookName = (Text)findComponentById(ResourceTable.Id_sure_book_order_name);
        bookNum = (Text)findComponentById(ResourceTable.Id_sure_book_order_num);
        bookPrice = (Text)findComponentById(ResourceTable.Id_sure_book_order_price);
        discount = (Text)findComponentById(ResourceTable.Id_discount_num);
        finallyPrice = (Text)findComponentById(ResourceTable.Id_all_price);
        submit = (Button)findComponentById(ResourceTable.Id_submit_order) ;
        in = (Image)findComponentById(ResourceTable.Id_in);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        submit.setClickedListener(this);
        discount.setClickedListener(this);
        in.setClickedListener(this);
        bookImage.setImageAndDecodeBounds(book);
        bookName.setText(bookOrder.getBookName());
        bookNum.setText("×" + bookOrder.getCounts()+"");
        bookPrice.setText("￥"+(bookOrder.getPrice()+""));
        price = bookOrder.getPrice();
        finallyPrice.setText(price+"");
        String json = gson.toJson(parent);
        //查找奖学券
        ZZRHttp.postJson("http://101.132.74.147:8081/parReward/myReward", json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(SureBookOrderSlice.this)
                        .setText("网络不稳定")
                        .show();
            }

            @Override
            public void onResponse(String s) {
                result = gson.fromJson(s, Result.class);
                if (result.getCode() == 200) {
                    List<ParReward> list = gson.fromJson(result.getResult(), new TypeToken<List<ParReward>>() {}.getType());
                    if (list.size()>0){
                        discount.setText(list.size() + "张可用");
                    }else {
                        discount.setText("暂无可用");
                        discount.setTextColor(Color.GRAY);
                    }
                }else if (result.getCode() == 400){
                    new ToastDialog(SureBookOrderSlice.this)
                            .setText("没有优惠券！")
                            .show();
                }
            }
        });
        //查找钱包
        String url = "http://101.132.74.147:8081/parMoney/myMoney";
        Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
        json = gson.toJson(parent);
        ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(SureBookOrderSlice.this)
                        .setText("网络不稳定")
                        .show();
            }
            @Override
            public void onResponse(String s) {
                Result result = gson.fromJson(s,Result.class);
                if (result.getCode() == 200){
                    parMoney = gson.fromJson(result.getResult(), ParMoney.class);
                }else if (result.getCode() == 400){
                    new ToastDialog(SureBookOrderSlice.this)
                            .setText("获取账户金额失败")
                            .setDuration(5000)
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
    protected void onResult(int requestCode, Intent resultIntent) {
        super.onResult(requestCode, resultIntent);
        if (requestCode == 100){
            if (!resultIntent.getStringParam("discountPrice").equals("")){
                discountPrice = resultIntent.getStringParam("discountPrice");
                discount.setText("-￥" + discountPrice);
                discount.setTextColor(Color.RED);
                price = price - Double.parseDouble(discountPrice);
                if (price>0){
                    bookOrder.setPrice(price);
                    finallyPrice.setText(price + "");
                }else {
                    bookOrder.setPrice(0.0);
                    finallyPrice.setText(0+"");
                }
                award = resultIntent.getSerializableParam("discount");
            }
        }
    }
    int flag = 0;
    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_back){
            terminate();
        }
        if (component.getId() == ResourceTable.Id_in || component.getId() == ResourceTable.Id_discount_num){
            if (discount.getText().equals("暂无可用")){
                return;
            }
            Intent intent1 = new Intent();
            intent1.setParam("my",parent);
            presentForResult(new SelectAwardSlice(),intent1,100);
        }
        if (component.getId() == ResourceTable.Id_submit_order){
            if (Double.parseDouble(finallyPrice.getText())>parMoney.getBalance()){
                new ToastDialog(SureBookOrderSlice.this)
                        .setText("余额不足！无法购买！")
                        .show();
                return;
            }

            parMoney.setBalance(parMoney.getBalance() - Double.parseDouble(finallyPrice.getText()));

            String json = gson.toJson(bookOrder);
            String url = "http://101.132.74.147:8081/bookOrder/orderBook";
            ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(SureBookOrderSlice.this)
                            .setText("网络不稳定")
                            .show();
                }
                @Override
                public void onResponse(String s) {
                    Result result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        new ToastDialog(SureBookOrderSlice.this)
                                .setText("添加订单成功！")
                                .setDuration(3500)
                                .show();

                    }else if (result.getCode() == 400){
                        new ToastDialog(SureBookOrderSlice.this)
                                .setText("添加订单失败！")
                                .show();
                    }
                }
            });

                json = gson.toJson(parMoney);
                url = "http://101.132.74.147:8081/parMoney/recharge";
                ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(SureBookOrderSlice.this)
                                .setText("网络不稳定")
                                .show();
                    }
                    @Override
                    public void onResponse(String s) {
                        Result result = gson.fromJson(s,Result.class);
                        if (result.getCode() == 200){

                        }else if (result.getCode() == 400){
                            new ToastDialog(SureBookOrderSlice.this)
                                    .setText(result.getMsg())
                                    .show();
                        }
                    }
                });

                if (award != null){
                    json = gson.toJson(award);
                    url = "http://101.132.74.147:8081/parReward/useReward";
                    ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                        @Override
                        public void onFailure(int i, String s) {
                            new ToastDialog(SureBookOrderSlice.this)
                                    .setText("网络不稳定")
                                    .show();
                        }
                        @Override
                        public void onResponse(String s) {
                            Result result = gson.fromJson(s,Result.class);
                            if (result.getCode() == 200){
                                terminate();
                            }else if (result.getCode() == 400){
                                new ToastDialog(SureBookOrderSlice.this)
                                        .setText(result.getMsg())
                                        .show();
                            }
                        }
                    });
                }

            }
        }
    }