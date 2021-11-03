package com.example.ateachingapplication.slice;

import com.example.ateachingapplication.ResourceTable;
import com.example.ateachingapplication.domain.Parent;
import com.example.ateachingapplication.domain.Teacher;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;

public class TeacherMessageAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Button bookTeacher;
    private Button checkTeacherTime;
    private Text teacherName;
    private Text teacherSex;
    private Text teacherTel;
    private Text teacherAddress;
    private Text teacherSub;
    private Text teacherExperience;
    private Image back;
    private Teacher teacher;
    private Parent parent;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_teacher_message);
        parent = intent.getSerializableParam("my");
        teacher = intent.getSerializableParam("teacher");
        teacherName = (Text)findComponentById(ResourceTable.Id_teacher_name);
        teacherSex = (Text)findComponentById(ResourceTable.Id_teacher_sex);
        teacherTel = (Text)findComponentById(ResourceTable.Id_teacher_telephone);
        teacherAddress = (Text)findComponentById(ResourceTable.Id_teacher_address);
        teacherSub = (Text)findComponentById(ResourceTable.Id_teacher_subject);
        teacherExperience = (Text)findComponentById(ResourceTable.Id_teacher_experience);
        bookTeacher = (Button)findComponentById(ResourceTable.Id_book_teacher);
        checkTeacherTime = (Button)findComponentById(ResourceTable.Id_check_teacher_time);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        bookTeacher.setClickedListener(this);
        checkTeacherTime.setClickedListener(this);
        teacherName.setText(teacher.getTeacherName());
        teacherSex.setText(teacher.getTeacherSex());
        teacherTel.setText(teacher.getTeacherPhone());
        teacherAddress.setText(teacher.getTeacherAddress());
        teacherSub.setText(teacher.getSubject());
        teacherExperience.setText(teacher.getTeacherExper());

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
        if (component.getId() == ResourceTable.Id_book_teacher){
            Intent intent1 = new Intent();
            intent1.setParam("my",parent);
            intent1.setParam("teacher",teacher);
            present(new ReserveTeacherSlice(),intent1);
        }
        if (component.getId() == ResourceTable.Id_check_teacher_time){
            Intent intent1 = new Intent();
            intent1.setParam("teacher",teacher);
            present(new TeacherTimeSlice(),intent1);
        }
        if (component.getId() == ResourceTable.Id_back){
            terminate();
        }
    }
}
