package com.mistong.lint;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        Log.e("德玛西亚","你好啊");
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).run();
        Toast.makeText(this,"你好啊",Toast.LENGTH_SHORT);
    }
}
