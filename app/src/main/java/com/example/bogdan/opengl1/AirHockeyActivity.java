package com.example.bogdan.opengl1;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by bogdan on 01.03.2016.
 */
public class AirHockeyActivity extends AppCompatActivity {
    private GLSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2()) {
            Toast.makeText(this, "OpenGL ES 2.0 in this device not suppported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        surfaceView = new GLSurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(new AirHockeyRenderer(this));
        setContentView(surfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }

    private boolean supportES2() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }
}
