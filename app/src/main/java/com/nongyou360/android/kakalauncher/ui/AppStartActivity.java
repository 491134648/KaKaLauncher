package com.nongyou360.android.kakalauncher.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.nongyou360.android.kakalauncher.AppManager;
import com.nongyou360.android.kakalauncher.R;
import com.nongyou360.android.kakalauncher.util.PreferenceHelper;

/**
 * APP启动页
 * Created by kanghua on 2016/7/7.
 */
public class AppStartActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 防止第三方跳转时出现双实例
        Activity aty = AppManager.getActivity(MainActivity.class);
        if (aty != null && !aty.isFinishing()) {
            finish();
        }
        //SystemTool.gc(this); //针对性能好的手机使用，加快应用相应速度
        int cacheVersion = PreferenceHelper.readInt(this, "first_install",
                "first_install", -1);
        int currentVersion = getVersionCode();
        if (cacheVersion < currentVersion) {
            //final View view = View.inflate(this, R.layout.activity_appstart_guide, null);
            PreferenceHelper.write(this, "first_install", "first_install",
                    currentVersion);
            redirectToGuide();
        }else{
            final View view = View.inflate(this, R.layout.app_start, null);
            setContentView(view);
            // 渐变展示启动屏
            AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
            aa.setDuration(800);//此动画应该持续多长时间。持续时间不能为负
            view.startAnimation(aa);
            aa.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation arg0) {
                    redirectTo();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}

                @Override
                public void onAnimationStart(Animation animation) {}
            });
        }
    }
    private  void redirectToGuide(){
        Intent intent = new Intent(this, AppGuideActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * 跳转到...
     */
    private void redirectTo() {
        /*Intent uploadLog = new Intent(this, LogUploadService.class);
        startService(uploadLog);*/
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public  int getVersionCode() {
        int versionCode = 0;
        try {
            versionCode =getBaseContext().getPackageManager()
                    .getPackageInfo(getBaseContext().getPackageName(),
                            0).versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
            versionCode = 0;
        }
        return versionCode;
    }
}
