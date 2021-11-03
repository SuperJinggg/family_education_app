package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.domain.CAward;
import com.example.ateachingapplication.provider.ChargeAwardProvider;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class ChargeAward extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private CAward award = null;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_charge_award);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        ListContainer listContainer_ = (ListContainer)findComponentById(ResourceTable.Id_listcontainer_charge_award);
        List<CAward> list = getData();
        ChargeAwardProvider provider = new ChargeAwardProvider(list,ChargeAward.this);
        listContainer_.setItemProvider(provider);
        listContainer_.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {

                award = (CAward) listContainer_.getItemProvider().getItem(i);
                //创建弹框对象
                CommonDialog commonDialog = new CommonDialog(ChargeAward.this);
                //设置弹框标题
                commonDialog.setTitleText("提示");
                //设置弹框提示内容

                commonDialog.setContentText("确认花费" + award.getPrice() + "来兑换价值￥" + award.getCost() + "的奖学券吗？");

                //点击弹框外部可关闭弹框
                commonDialog.setAutoClosable(false);
                //设置弹框选择按钮
                commonDialog.setButton(0, "确定", new IDialog.ClickedListener() {
                    @Override
                    public void onClick(IDialog iDialog, int i) {
                        commonDialog.destroy();
                        Intent intent1 = new Intent();
                        intent1.setParam("award",award);
                        setResult(intent1);
                        terminate();
                    }
                });
                commonDialog.setButton(1, "再逛逛", new IDialog.ClickedListener() {
                    @Override
                    public void onClick(IDialog iDialog, int i) {
                        commonDialog.destroy();
                    }
                });
                //将弹框展示出来
                commonDialog.show();
            }
        });
    }

    private List<CAward> getData() {
        List<CAward> list1 = new ArrayList<>();
        java.util.Date date1= new java.util.Date();
        Date date = new Date(date1.getTime());
        double j = 1.0;
        for (int i = 1; i<=10; i++){
            list1.add(new CAward( (j++)*50.0,date,i*10));
        }
        return list1;
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
            Intent intent1 = new Intent();
            award = null;
            intent1.setParam("award",award);
            setResult(intent1);
            terminate();
        }
    }
}
