package com.example.topikhelper;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class MainBackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public MainBackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            toast.cancel();

            //Intent t = new Intent(activity, Main_Activity.class);
            //activity.startActivity(t);

            activity.moveTaskToBack(true);
            activity.finish();
            Intent intent = new Intent(activity, Main_Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            android.os.Process.killProcess(android.os.Process.myPid());

        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
