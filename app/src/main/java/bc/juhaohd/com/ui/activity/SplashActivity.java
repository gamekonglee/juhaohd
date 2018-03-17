package bc.juhaohd.com.ui.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.pgyersdk.crash.PgyCrashManager;

import java.util.Timer;
import java.util.TimerTask;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bocang.utils.IntentUtil;

/**
 * @author Jun
 * @time 2017/1/5  10:29
 * @desc 启动页
 */
public class SplashActivity extends Activity {
    private ImageView mLogoIv;
    private AlphaAnimation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                new Timer().schedule(new TimerSchedule(), 2600);
        // 强制切换为横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_splash);
        mAnimation = new AlphaAnimation(0.2f, 1.0f);

        mLogoIv = (ImageView) findViewById(R.id.logo_iv);
        mLogoIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //布置透明度动画
        mAnimation.setDuration(2500);
        mAnimation.setFillAfter(true);
        mLogoIv.startAnimation(mAnimation);
    }


    private class TimerSchedule extends TimerTask {
        @Override
        public void run() {
//            IntentUtil.startActivity(SplashActivity.this, HomeShowActivity.class, true);
            try  {
                // code
                IntentUtil.startActivity(SplashActivity.this, HomeShowNewActivity.class, true);
            } catch (Exception e) {
                PgyCrashManager.reportCaughtException(SplashActivity.this, e);
            }

        }
    }
}
