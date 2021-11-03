package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.domain.ParMoney;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;

public class ChargeMoney extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Text currentMoney;
    private Button chargeMoney;
    private ParMoney parMoney;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_charge_money);
        parMoney = intent.getSerializableParam("wallet");
        currentMoney = (Text)findComponentById(ResourceTable.Id_current_money);
        chargeMoney = (Button)findComponentById(ResourceTable.Id_charge_money);
        back = (Image)findComponentById(ResourceTable.Id_back);
        currentMoney.setText(parMoney.getBalance()+"");
        back.setClickedListener(this);
        chargeMoney.setClickedListener(this);
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
        if (component.getId() == ResourceTable.Id_charge_money){
            Intent intent = new Intent();
            intent.setParam("ParMoney",parMoney);
            present(new Charge(),intent);
        }
    }
}