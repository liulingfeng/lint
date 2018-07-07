package com.mistong.lint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        IntentFilter intentFilter = new IntentFilter();
        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver, intentFilter);
    }

    private void init() {
        Log.e("德玛西亚", "你好啊");
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).run();

        Intent intent = getIntent();
        User user = (User)intent.getSerializableExtra("user");
        Color.parseColor("#fffff");
    }

    @Override
    protected void onDestroy() {
        init();
        super.onDestroy();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
