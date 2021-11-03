package com.example.ateachingapplication.slice.myslice;

import com.example.ateachingapplication.MainAbility;
import com.example.ateachingapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;

public class Settings extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Button goOut;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_settings);
        back = (Image)findComponentById(ResourceTable.Id_back);
        goOut = (Button)findComponentById(ResourceTable.Id_go_out);
        back.setClickedListener(this);
        goOut.setClickedListener(this);
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
        if (component.getId()==ResourceTable.Id_back){
            terminate();
        }
        if (component.getId()==ResourceTable.Id_go_out){
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.ateachingapplication")
                    .withAbilityName(MainAbility.class.getName())
                    .build();
            intent.setOperation(operation);
            startAbility(intent);
        }
    }
}