package com.example.ateachingapplication.slice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.domain.Parent;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class SureRegister extends AbilitySlice implements Component.ClickedListener {
    private Button button;
    private Parent parent;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_sure_register);
        button = (Button) findComponentById(ResourceTable.Id_sure_register);
        button.setClickedListener(this);
        parent =  intent.getSerializableParam("register_my");
        System.out.println(parent);

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
        if (component.getId() == ResourceTable.Id_sure_register){
            Intent intent1 = new Intent();
            intent1.setParam("my",parent);
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.ateachingapplication")
                    .withAbilityName("com.example.ateachingapplication.TeachingAbility")
                    .build();
            intent1.setOperation(operation);
            startAbility(intent1);
        }
    }
}

