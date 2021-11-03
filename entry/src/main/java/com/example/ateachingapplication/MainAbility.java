package com.example.ateachingapplication;

import com.example.ateachingapplication.slice.MainAbilitySlice;
import com.example.ateachingapplication.slice.RegisterAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        super.addActionRoute("register", RegisterAbilitySlice.class.getName());
        requestPermissionsFromUser(new String[]{
                "ohos.permission.INTERNET",
        },0);

    }
}
