package com.dsunny.subway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.dsunny.subway.activity.SearchActivity;
import com.dsunny.subway.util.Utils;

/**
 * @author m 欢迎页面
 * 
 */
public class MainSplashActivity extends Activity {
    public static final String TAG = "MainActivity";

    private static final long DELAY_TIME = 2000L;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClass(MainSplashActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainsplash);
        Utils.copyDBFile(this);
        handler.postDelayed(runnable, DELAY_TIME);
    }

}
