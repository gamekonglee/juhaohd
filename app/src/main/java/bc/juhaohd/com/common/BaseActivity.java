package bc.juhaohd.com.common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pgyersdk.crash.PgyCrashManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.AdBean;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.ui.activity.AdWaitActivity;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.user.LoginActivity;
import bc.juhaohd.com.ui.view.LoadingDialog;
import bc.juhaohd.com.utils.FileUtil;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.NetworkStateManager;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Jun
 * @time 2017/1/5  10:31
 * @desc ${TODD}
 */
public abstract class BaseActivity  extends FragmentActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 222;
    private IssueApplication app;
    public CountDownTimer countDownTimer;
    private long advertisingTime = 60*1000;//定时跳转广告时间
    public LoadingDialog mDialog;
    private int mAdCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PgyCrashManager.register(this);
        //设置屏幕长亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initData();
        initView();
        initController();
        InitDataView();
        app = (IssueApplication) getApplication();

    }
    /**
     * 判断是否有toKen
     */
    public Boolean isToken() {
        String token = MyShare.get(this).getString(Constance.TOKEN);
        if(AppUtils.isEmpty(token)){
            Intent logoutIntent = new Intent(this, LoginActivity.class);
//            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logoutIntent);
            return true;
        }
        return  false;
    }
    /**
     * 检查网络是否联网
     */
    public static boolean hashkNewwork() {

        boolean hasNetwork = NetworkStateManager.instance().isNetworkConnected();
        if (!hasNetwork) {
//            Toast.makeText(IssueApplication.getInstance(), "您的网络连接已中断", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * 跳轉廣告
     */
    public void startAD() {
        if(!hashkNewwork()){
            return;
        }
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(advertisingTime, 1000l) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    //定时完成后的操作
                    //跳转到广告页面
                    if(IssueApplication.isStartedAd){
                        return;
                    }
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        PgyCrashManager.reportCaughtException(BaseActivity.this,new Exception("onFinish_requestPermission"));
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
                    }else {
                        PgyCrashManager.reportCaughtException(BaseActivity.this,new Exception("onFinish_getpic"));
                        getPic();
                    }

                }
            };
            countDownTimer.start();
        } else {
            countDownTimer.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200&&grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
//            getPic();
            PgyCrashManager.reportCaughtException(BaseActivity.this,new Exception("onRequestPermissionsResult_getpic"));
            getPic();
        }else {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                } else {
                    // No explanation needed, we can request the permission.
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
//            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
        }
    }

    private void getPic() {
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("id","266")
                .build();
        Request request=new Request.Builder().post(body).url(NetWorkConst.BANNER_INDEX).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PgyCrashManager.reportCaughtException(BaseActivity.this,new Exception("getpic_onFailure"));
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                PgyCrashManager.reportCaughtException(BaseActivity.this,new Exception("pic_onResponse"));
                String result= null;
                try {
                    result = response.body().string();
                    LogUtils.logE("ad",result);
                    JSONObject jsonObject=new JSONObject(result);
                    result=jsonObject.getJSONArray(Constance.banners).toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final List<AdBean> adBeans = new Gson().fromJson(result,new TypeToken<List<AdBean>>(){}.getType());
                if(adBeans!=null&&adBeans.size()>0){
                    try {
                        boolean isNeedRefresh=false;
                        for(int i=0;i<adBeans.size();i++){
                            String url=adBeans.get(i).getAd_code();
                            String path = FileUtil.getAdExternDir(url);
                            File imageFile = new File(path );
                            if (!imageFile.exists()) {
                                isNeedRefresh=true;
//                                uri=new ArrayList<>();
                                break;
//                                        imageURL = "file://" + imageFile.toString();
//                                        ImageLoader.getInstance().displayImage(imageURL, holder.imageView);
                            }else {
                                LogUtils.logE("file","isExists");
//                                uri.add(imageFile.toString());
                            }
                        }
                        if(isNeedRefresh){
                            mAdCount = 0;
                            for (int i = 0; i < adBeans.size(); i++) {
//                                    ImageView imageView = new ImageView(AdWaitActivity.this);
//                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            bitmap = ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_1);
                                final int finalI = i;
                                ImageLoader.getInstance().loadImage(NetWorkConst.SCENE_HOST+"/data/afficheimg/"+ adBeans.get(i).getAd_code(), new ImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String s, View view) {

                                    }

                                    @Override
                                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                                        PgyCrashManager.reportCaughtException(BaseActivity.this,new Exception("loadPicFailed"+s));
                                    }

                                    @Override
                                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                                                    bitmaps.add(bitmap);
//                                        LogUtils.logE("imguri",s);
                                        mAdCount++;
                                        try {
                                            File filePic = new File(FileUtil.getAdExternDir(adBeans.get(finalI).getAd_code()));
                                            if (!filePic.exists()) {
                                                filePic.getParentFile().mkdirs();
                                                filePic.createNewFile();
                                            }
                                            FileOutputStream fos = new FileOutputStream(filePic);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                                            uri.add(filePic.toString());
                                            fos.flush();
                                            fos.close();
                                            if(bitmap!=null&&!bitmap.isRecycled()){
                                                bitmap.recycle();
                                            }
                                            if(mAdCount ==adBeans.size()){
                                                PgyCrashManager.reportCaughtException(BaseActivity.this,new Exception("loadComplete+startAdActivity"));
                                                if(IssueApplication.isStartedAd){
                                                    return;
                                                }
                                                startActivity(new Intent(BaseActivity.this,AdWaitActivity.class));
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            PgyCrashManager.reportCaughtException(BaseActivity.this,new Exception("getPic_load_completeExpetion"+e.getMessage()));
                                        }

                                    }

                                    @Override
                                    public void onLoadingCancelled(String s, View view) {

                                    }
                                });

                            }

                        }else {
                            //不需要更新
                            startActivity(new Intent(BaseActivity.this,AdWaitActivity.class));
                        }

                    }catch (Exception e){
                        PgyCrashManager.reportCaughtException(BaseActivity.this,e);
                    }


                }
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //有按下动作时取消定时
//                LogUtils.logE("dispatch:","ActionDown");
                if (countDownTimer != null){
                    countDownTimer.cancel();
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起时启动定时
//                LogUtils.logE("dispatch:","ActionUp");
                startAD();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public Handler handler = new Handler();


    protected abstract void InitDataView();

    protected abstract void initController();

    protected abstract void initView();

    protected abstract void initData();

    public void tip(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }

    public IssueApplication getApp() {
        return app;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
//        Log.v("520it","销毁");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!IssueApplication.noAd){
        startAD();
        }else {
            if (countDownTimer != null){
                countDownTimer.cancel();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LogUtils.logE("BaseActivity:","onPause:cancel");
        //当activity不在前台是停止定时
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    /** * 设置状态栏颜色 * * @param activity 需要设置的activity * @param color 状态栏颜色值 */
    public static void setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /** * 生成一个和状态栏大小相同的矩形条 * * @param activity 需要设置的activity * @param color 状态栏颜色值 * @return 状态栏矩形条 */
    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);

        return statusView;
    }

    public void goBack(View v) {
        finish();
    }


    /**
     * 获取并绑定点击
     *
     * @param id id
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewAndClick(@IdRes int id) {
        T v = getView(id);
        v.setOnClickListener(this);
        return v;
    }

    /**
     * 获取 控件
     *
     * @param id 行布局中某个组件的id
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onClick(View v) {
        onViewClick(v);
    }

    protected abstract void onViewClick(View v);

    private boolean showDialog;// 显示加载对话框
    private boolean isDestroy;
    /**
     * 显示加载对话框
     *
     * @param showDialog 是否显示加载对话框
     */
    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    /**
     * 加载框文本
     */
    public void setShowDialog(String msg) {
        if (isDestroy) return;
        if (msg == null) {
            showDialog = false;
            return;
        }
        showDialog = true;
        if (mDialog == null) {
            setLoadingDialog(new LoadingDialog(this, msg));
        } else {
            mDialog.setLoadMsg(msg);
        }
    }

    public void setLoadingDialog(LoadingDialog loadingDialog) {
        this.mDialog = loadingDialog;
    }

    /**
     * 隐藏加载框
     */
    public  void hideLoading() {
        if (isDestroy) return;
        if (mDialog != null)
            mDialog.dismiss();
    }

    /**
     * 显示加载框
     */
    public void showLoading() {
        if (isDestroy) return;
        if (showDialog) {
            if (mDialog == null) {
                setLoadingDialog(new LoadingDialog(this));
            }
            try{
                mDialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    private View errorView ,contentView;
    private TextView error_tv ;
    private ImageView error_iv ;
    private RotateAnimation animation ;


    /**
     * 加载页面
     * @param resId
     */
    public void showLoadingPage(String tip,int resId){
        errorinit();
        if (errorView==null){
            return;
        }
        if (error_iv==null){
            return;
        }
        if (error_tv==null){
            return;
        }
        if (contentView==null){
            return;
        }

        errorView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
//        if (loginView!=null){
//            loginView.setVisibility(View.GONE);
//        }
        if (!TextUtils.isEmpty(tip)){
            error_tv.setText(tip);
        }else {
            error_tv.setText("数据正在加载...");
        }
        error_iv.setImageResource(resId);
        /** 设置旋转动画 */
        if (animation==null){
            animation =new RotateAnimation(0f,359f, Animation.RELATIVE_TO_SELF,
                    0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(1000);//设置动画持续时间
            /** 常用方法 */
            animation.setRepeatCount(Integer.MAX_VALUE);//设置重复次数
            animation.startNow();
        }
        error_iv.setAnimation(animation);
    }


    /**
     * 显示错误页面
     * @param message
     * @param resId
     */
    public void showErrorView(String message,int resId){
        errorinit();
        if (errorView==null){
            return;
        }
        if (error_iv==null){
            return;
        }
        if (error_tv==null){
            return;
        }
        if (contentView==null){
            return;
        }
//        if (loginView!=null){
//            loginView.setVisibility(View.GONE);
//        }
        error_iv.setImageResource(resId);
        if (!TextUtils.isEmpty(message)){
            error_tv.setText(message);
        }else {
            error_tv.setText("数据加载失败！");
        }
        error_iv.setAnimation(null);
        errorView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
    }

    private void errorinit() {
        errorView = findViewById(R.id.errorView);
//        errorView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onReloadDataListener!=null){
//                    onReloadDataListener.request(true);
//                }
//            }
//        });
        error_iv = (ImageView)findViewById(R.id.error_iv);
        error_tv = (TextView) findViewById(R.id.error_tv);
        contentView = findViewById(R.id.contentView);
    }


    /**
     * 显示内容区域
     */
    public void showContentView(){
        errorinit();
        if (errorView==null){
            return;
        }
        if (error_iv==null){
            return;
        }
        if (error_tv==null){
            return;
        }
        if (contentView==null){
            return;
        }
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }


}
