package com.lesgrosspoof.bemydiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class SplashScreenActivity extends Activity {
    private static final int STOPSPLASH = 0;
    private static final long SPLASHTIME = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        Message msg = new Message(); 
        msg.what = STOPSPLASH; 
        splashHandler.sendMessageDelayed(msg, SPLASHTIME); 
    }
    
    private Handler splashHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPSPLASH:
                    //remove SplashScreen from view
                    Intent intent = new Intent(SplashScreenActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
