package com.example.ateachingapplication;

import com.example.ateachingapplication.slice.TeacherMessageAbilitySlice;
import com.example.ateachingapplication.slice.TeachingAbilitySlice;
import com.example.ateachingapplication.slice.searchslice.SubjectSearchSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class TeachingAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(TeachingAbilitySlice.class.getName());
        super.addActionRoute("subjectsearch", SubjectSearchSlice.class.getName());
        super.addActionRoute("teachermessage", TeacherMessageAbilitySlice.class.getName());
        //动态申请敏感权限
        requestPermissions();
    }
    public void requestPermissions(){
        //需要处理的权限
        String[] permissions = {
                "ohos.permission.LOCATION",
                "ohos.permission.LOCATION_IN_BACKGROUND"
        };
        //可动态授权的权限
        List<String> permissionsToProcess = new ArrayList<>();
        //遍历需要处理的权限
        for (String permission : permissions){
            //判断需要处理的权限是否可动态授权
            if (verifySelfPermission(permission) != 0 && canRequestPermission(permission)){
                permissionsToProcess.add(permission);
            }
        }
        //弹窗申请权限
        requestPermissionsFromUser(permissionsToProcess.toArray(new String[0]),0);
    }
}
