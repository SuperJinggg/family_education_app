package com.example.ateachingapplication.slice.game;

import com.example.ateachingapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

//切西瓜游戏
//？？？还剩余定时10秒自动结束游戏功能
public class Game1 extends AbilitySlice implements Component.ClickedListener, Component.TouchEventListener, TickTimer.TickListener {
    Text text1,text2;
    Image image;
    ProgressBar progressBar;
    Button button;
    TickTimer tickTimer;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_game1);
        progressBar = (ProgressBar)findComponentById(ResourceTable.Id_pb);
        text1 = (Text)findComponentById(ResourceTable.Id_text1);
        text2 = (Text)findComponentById(ResourceTable.Id_text2);
        tickTimer = (TickTimer) findComponentById(ResourceTable.Id_tt);
        button = (Button)findComponentById(ResourceTable.Id_bt);
        button.setClickedListener(this);
        image = (Image)findComponentById(ResourceTable.Id_img);

        //TickTimer基本设置
        tickTimer.setCountDown(false);
        tickTimer.setFormat("mm:ss");
        //给定时器绑定定时事件
        tickTimer.setTickListener(this);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    //标志游戏开始
    Boolean flag = false;
    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_bt){
            Button button = (Button)component;
            button.setText("切！！！");
            tickTimer.start();
            startTime = String2Long(tickTimer.getText());
            image.setTouchEventListener(this);
            button.setClickable(false);
            flag = true;
        }


    }
    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if (flag){
            float startX=0,startY=0,endX=0,endY=0;
            int action = touchEvent.getAction();
            MmiPoint mmiPoint = touchEvent.getPointerPosition(0);
            if (action == TouchEvent.PRIMARY_POINT_DOWN){
                startX = mmiPoint.getX();
                startY = mmiPoint.getY();
            }else if (action == TouchEvent.PRIMARY_POINT_DOWN){
                endX = mmiPoint.getX();
                endY = mmiPoint.getY();
            }
            if (startX != endX && startY != endY){
                int process = progressBar.getProgress();
                if (process>=50&&process<100){
                    image.setImageAndDecodeBounds(ResourceTable.Media_w2);
                    image.setScaleMode(Image.ScaleMode.INSIDE);
                }else if (process == 100){
                    image.setImageAndDecodeBounds(ResourceTable.Media_w3);
                    image.setScaleMode(Image.ScaleMode.STRETCH);
                }
                Random random = new Random();
                int i = random.nextInt(6);
                if ((process+=i)>100)process=100;
                if (process==100){
                    tickTimer.stop();
                    text2.setText("恭喜挑战成功！");
                    button.setText("游戏结束");
                    button.setTextColor(Color.RED);
                    //创建弹框对象
                    CommonDialog commonDialog = new CommonDialog(Game1.this);
                    //设置弹框标题
                    commonDialog.setTitleText("游戏结束");
                    //设置弹框提示内容

                    commonDialog.setContentText("挑战成功！获得10积分奖励！");

                    //点击弹框外部可关闭弹框
                    commonDialog.setAutoClosable(false);
                    //设置弹框选择按钮
                    commonDialog.setButton(0, "确定", new IDialog.ClickedListener() {
                        @Override
                        public void onClick(IDialog iDialog, int i) {
                            commonDialog.destroy();
                            Intent intent = new Intent();
                            intent.setParam("points",10);
                            setResult(intent);
                            terminate();
                        }
                    });
                    //将弹框展示出来
                    commonDialog.show();


                }

                progressBar.setProgressValue(process);
                progressBar.setProgressHintText(process+"%");
            }
        }
        return true;
    }
    long startTime=0;
    long nowTime;
    @Override
    public void onTickTimerUpdate(TickTimer tickTimer) {

        nowTime = String2Long(tickTimer.getText());
        if (nowTime-startTime>=10000){
            tickTimer.stop();
            text2.setText("太可惜了！");
            button.setText("游戏结束");
            button.setTextColor(Color.RED);
            //创建弹框对象
            CommonDialog commonDialog = new CommonDialog(Game1.this);
            //设置弹框标题
            commonDialog.setTitleText("游戏结束");
            //设置弹框提示内容
            commonDialog.setContentText("挑战失败！下次再来！");
            //点击弹框外部可关闭弹框
            commonDialog.setAutoClosable(false);
            //设置弹框选择按钮
            commonDialog.setButton(0, "再试一次", new IDialog.ClickedListener() {
                @Override
                public void onClick(IDialog iDialog, int i) {
                    commonDialog.destroy();
                    button.setClickable(true);
                    button.setText("点击开始");
                    button.setTextColor(Color.WHITE);
                    text2.setText("请在10秒内将西瓜切成块！");
                }
            });
            commonDialog.setButton(1, "退出", new IDialog.ClickedListener() {
                @Override
                public void onClick(IDialog iDialog, int i) {
                    commonDialog.destroy();
                    Intent intent = new Intent();
                    intent.setParam("points",0);
                    setResult(intent);
                    terminate();
                }
            });
            //将弹框展示出来
            commonDialog.show();
        }
    }
    //把字符串类型的时间，变成毫秒值
    public long String2Long(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        Date date = null;
        //抓异常，不要抛
        //因为定时器事件会不停调用onTickTimerUpdate方法
        //所以也不停的调用此方法，使得每次调用都抛异常
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long result = date.getTime();
        return result;
    }
}

