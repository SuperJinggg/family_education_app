package com.example.ateachingapplication.slice.searchslice;

import com.example.ateachingapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.utils.Color;

public class GradeSearchSlice extends AbilitySlice implements Component.ClickedListener {
    private Button grade1;
    private Button grade2;
    private Button grade3;
    private Button grade4;
    private Button grade5;
    private Button grade6;
    private Button grade7;
    private Button grade8;
    private Button grade9;
    private Button grade10;
    private Button grade11;
    private Button grade12;
    private Button sureGradeSelect;
    private String gradeSelect = "年级";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_grade_search);
        grade1 = (Button)findComponentById(ResourceTable.Id_grade1);
        grade2 = (Button)findComponentById(ResourceTable.Id_grade2);
        grade3 = (Button)findComponentById(ResourceTable.Id_grade3);

        sureGradeSelect = (Button)findComponentById(ResourceTable.Id_sure_grade_select);
        grade1.setClickedListener(this);
        grade2.setClickedListener(this);
        grade3.setClickedListener(this);

        sureGradeSelect.setClickedListener(this);
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
        if (component.getId() == ResourceTable.Id_grade1){
            gradeSelect=grade1.getText();
            grade1.setTextColor(Color.RED);
            grade2.setTextColor(Color.BLACK);
            grade3.setTextColor(Color.BLACK);
        }
        if (component.getId() == ResourceTable.Id_grade2){
            gradeSelect=grade2.getText();
            grade2.setTextColor(Color.RED);
            grade1.setTextColor(Color.BLACK);
            grade3.setTextColor(Color.BLACK);
        }
        if (component.getId() == ResourceTable.Id_grade3){
            gradeSelect=grade3.getText();
            grade3.setTextColor(Color.RED);
            grade2.setTextColor(Color.BLACK);
            grade1.setTextColor(Color.BLACK);
        }

        if (component.getId() == ResourceTable.Id_sure_grade_select){
            Intent i = new Intent();
            i.setParam("grade",gradeSelect);
            setResult(i);
            terminate();
        }

    }
}
