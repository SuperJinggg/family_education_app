package com.example.ateachingapplication.slice.searchslice;

import com.example.ateachingapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.utils.Color;

public class SubjectSearchSlice extends AbilitySlice implements Component.ClickedListener {
    private Button sub1;
    private Button sub2;
    private Button sub3;
    private Button sub4;
    private Button sub5;
    private Button sub6;
    private Button sub7;
    private Button sub8;
    private Button sureSubSelect;
    private String subjectname = "科目";
    private String presentsubject;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_subject_search);
        sub1 = (Button)findComponentById(ResourceTable.Id_sub1);
        sub2 = (Button)findComponentById(ResourceTable.Id_sub2);
        sub3 = (Button)findComponentById(ResourceTable.Id_sub3);
        sub4 = (Button)findComponentById(ResourceTable.Id_sub4);
        sub5 = (Button)findComponentById(ResourceTable.Id_sub5);
        sub6 = (Button)findComponentById(ResourceTable.Id_sub6);
        sub7 = (Button)findComponentById(ResourceTable.Id_sub7);
        sub8 = (Button)findComponentById(ResourceTable.Id_sub8);
        sureSubSelect = (Button)findComponentById(ResourceTable.Id_sure_sub_select);
        sub1.setClickedListener(this);
        sub2.setClickedListener(this);
        sub3.setClickedListener(this);
        sub4.setClickedListener(this);
        sub5.setClickedListener(this);
        sub6.setClickedListener(this);
        sub7.setClickedListener(this);
        sub8.setClickedListener(this);
        sureSubSelect.setClickedListener(this);
        presentsubject = intent.getStringParam("presentsubject");
        if (sub1.getText().equals(presentsubject)){
            sub1.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
        }
        if (sub2.getText().equals(presentsubject)){
            sub2.setTextColor(Color.RED);
            sub1.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
        }
        if (sub3.getText().equals(presentsubject)){
            sub3.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
        }
        if (sub4.getText().equals(presentsubject)){
            sub4.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
        }
        if (sub5.getText().equals(presentsubject)){
            sub5.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
        }
        if (sub6.getText().equals(presentsubject)){
            sub6.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
        }
        if (sub7.getText().equals(presentsubject)){
            sub7.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
        }
        if (sub8.getText().equals(presentsubject)){
            sub8.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
        }
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
        if (component.getId()==ResourceTable.Id_sub1){
            sub1.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
            subjectname=sub1.getText();
        }
        if (component.getId()==ResourceTable.Id_sub2){
            sub2.setTextColor(Color.RED);
            sub1.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
            subjectname=sub2.getText();
        }
        if (component.getId()==ResourceTable.Id_sub3){
            sub3.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
            subjectname=sub3.getText();
        }
        if (component.getId()==ResourceTable.Id_sub4){
            sub4.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
            subjectname=sub4.getText();
        }
        if (component.getId()==ResourceTable.Id_sub5){
            sub5.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
            subjectname=sub5.getText();
        }
        if (component.getId()==ResourceTable.Id_sub6){
            sub6.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
            subjectname=sub6.getText();
        }
        if (component.getId()==ResourceTable.Id_sub7){
            sub7.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            sub8.setTextColor(Color.BLACK);
            subjectname=sub7.getText();
        }
        if (component.getId()==ResourceTable.Id_sub8){
            sub8.setTextColor(Color.RED);
            sub2.setTextColor(Color.BLACK);
            sub3.setTextColor(Color.BLACK);
            sub4.setTextColor(Color.BLACK);
            sub5.setTextColor(Color.BLACK);
            sub6.setTextColor(Color.BLACK);
            sub7.setTextColor(Color.BLACK);
            sub1.setTextColor(Color.BLACK);
            subjectname=sub8.getText();
        }
        if (component.getId()==ResourceTable.Id_sure_sub_select){
            Intent i = new Intent();
            i.setParam("subjectname",subjectname);
            setResult(i);
            terminate();
        }
    }
}
