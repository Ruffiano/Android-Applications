package com.example.user.demo;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ServiceActivity extends Service {

    private Handler mHandler;
    private NumberThread mNumberThread;
    Context mContext;
    private boolean check;
    Bundle bun;

    @Override
    public void onCreate() {
        super.onCreate();

        //쓰레드------------------------------------
        mHandler = new Handler();

        mNumberThread = new NumberThread(true);
        mNumberThread.start();

        mContext = getApplicationContext();
        //------------------------------------------

        bun = new Bundle();
        bun.putString("notiMessage","아이가 울고 있어요");

//        Intent popupIntent = new Intent(getApplicationContext(), AlertDialogActivity.class);
//
//        popupIntent.putExtras(bun);
//        PendingIntent pie= PendingIntent.getActivity(getApplicationContext(), 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
//        try {
//            pie.send();
//        } catch (PendingIntent.CanceledException e) {
////            LogUtil.degug(e.getMessage());
//        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //쓰레드를 이용한 알림 띄우기
    class NumberThread extends Thread {

        public NumberThread(boolean isPlay) {
            check = isPlay;
        }

        public void stopThread() {
            check = !check;
        }

        @Override
        public void run() {
            super.run();
            while (check) {
                //check = false;
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Intent popupIntent = new Intent(getApplicationContext(), AlertDialogActivity.class);

                        popupIntent.putExtras(bun);
                        PendingIntent pie= PendingIntent.getActivity(getApplicationContext(), 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
                        try {
                            pie.send();
                        } catch (PendingIntent.CanceledException e) {
//            LogUtil.degug(e.getMessage());
                        }
                    }
                });  
            }
        }
    }

}
