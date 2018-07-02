package com.mistong.lint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("德玛西亚","你好啊");
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).run();
    }
}
