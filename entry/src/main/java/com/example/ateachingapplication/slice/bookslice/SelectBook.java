package com.example.ateachingapplication.slice.bookslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.domain.BookOrder;
import com.example.ateachingapplication.domain.Parent;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

import java.util.Random;

public class SelectBook extends AbilitySlice implements Component.ClickedListener {
    private String bookName;
    private int book;
    private Text selectBookName;
    private Text selectBookPrice;
    private Image selectBook;
    private Image back;
    private Button sureSelectBook;
    private Parent parent;
    private TextField bookNum;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_book_infomation);
        parent = intent.getSerializableParam("my");
        book = intent.getIntParam("book",0);
        bookName = intent.getStringParam("bookName");
        bookNum = (TextField)findComponentById(ResourceTable.Id_select_book_num);
        back = (Image)findComponentById(ResourceTable.Id_back);
        selectBook = (Image)findComponentById(ResourceTable.Id_select_book);
        selectBookName = (Text)findComponentById(ResourceTable.Id_select_book_name);
        selectBookPrice = (Text)findComponentById(ResourceTable.Id_select_book_price);
        sureSelectBook = (Button)findComponentById(ResourceTable.Id_sure_select_book);
        selectBook.setImageAndDecodeBounds(book);
        selectBookName.setText(bookName);
        Random random = new Random();
        int i = 50 + random.nextInt(100);
        selectBookPrice.setText(i+"");
        back.setClickedListener(this);
        sureSelectBook.setClickedListener(this);
        bookNum.setText("");
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
        if (component.getId() == ResourceTable.Id_sure_select_book){
            if (bookNum.getText().equals("")){
                new ToastDialog(this)
                        .setText("请填写购买数量！")
                        .show();
                return;
            }
            BookOrder bookOrder = new BookOrder();
            bookOrder.setParPhone(parent.getParPhone());
            bookOrder.setBookName(selectBookName.getText());
            bookOrder.setCounts(Integer.parseInt(bookNum.getText()));
            bookOrder.setStates(0);
            bookOrder.setPrice(Double.parseDouble(selectBookPrice.getText())*Integer.parseInt(bookNum.getText()));
            Intent intent = new Intent();
            intent.setParam("bookOrder",bookOrder);
            intent.setParam("bookImage",book);
            intent.setParam("my",parent);
            present(new SureBookOrderSlice(),intent);
        }
    }
}
