package bc.juhaohd.com.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.util.SparseArray;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.pgyersdk.crash.PgyCrashManager;
import com.sevenonechat.sdk.sdkCustomUi.UiCustomOptions;
import com.sevenonechat.sdk.sdkinfo.SdkRunningClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.ArticlesBean;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.utils.ImageLoadProxy;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.view.BaseApplication;
import cn.jpush.android.api.JPushInterface;

/**
 * @author Jun
 * @time 2017/1/6  16:06
 * @desc 全局
 */
public class IssueApplication extends BaseApplication {
    public static SparseArray<JSONObject> mSelectedLightSA = new SparseArray<>();
    protected static Context mContext = null;
    public static List<BaseActivity> baseActivities;
    public static List<ArticlesBean> articlesBeans;
    private static DisplayImageOptions defaultOptions;
    private static ImageLoaderConfiguration config;
    private static DisplayImageOptions options;
    public static boolean isStartedAd=false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext= getApplicationContext();
        super.mInstance = this;
//        Thread.currentThread().setUncaughtExceptionHandler(new MyExceptionHander());
        initImageLoader();
        ImageLoadProxy.initImageLoader(mContext);
        PgyCrashManager.register(this);
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        SdkRunningClient.getInstance().initAndLoginSdk(getApplicationContext(),"196417",
                "168e5ed4-d961-4230-a72c-696343615e17",initUiCustomOptions());
        baseActivities = new ArrayList<>();
        articlesBeans=new ArrayList<>();
        noAd=false;
        UMConfigure.init(this, "5cee536a3fc19587b9000383", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        // 支持在子进程中统计自定义事件
        UMConfigure.setProcessEvent(true);

    }
    private UiCustomOptions initUiCustomOptions() {
        UiCustomOptions options = new UiCustomOptions();
        options.msgTitle_backgroudColor = R.color.green;
        return options;
    }
    public static boolean noAd=false;
    public static void addActivity(BaseActivity activity){
        baseActivities.add(activity);
    }
    public  static BaseActivity getBaseActivity(int p){
          if(baseActivities!=null&&baseActivities.size()>p)
              return  baseActivities.get(p);
        else return null;
    }
    public static List<BaseActivity> getBaseActivities(){
        return baseActivities;
    }
    /**
     * 获得全局上下文
     * @return
     */
    public static Context getcontext() {
        return mContext;
    }

//    //初始化网络图片缓存库
//    private void initImageLoader(){
//        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .cacheInMemory(true).cacheOnDisk(true).build();
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
//        ImageLoader.getInstance().init(config);
//    }
    private class MyExceptionHander implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            // Logger.i("MobileSafeApplication", "发生了异常,但是被哥捕获了..");
            //            LogUtils.d("MobileSafeApplication","发生了异常,但是被哥捕获了..");
            //并不能把异常消灭掉,只是在应用程序关掉前,来一个留遗嘱的事件
            //获取手机硬件信息
            try {
                Field[] fields = Build.class.getDeclaredFields();
                StringBuffer sb = new StringBuffer();
                for(Field field:fields){
                    String value = field.get(null).toString();
                    String name  = field.getName();
                    sb.append(name);
                    sb.append(":");
                    sb.append(value);
                    sb.append("\n");
                }
                File file=new File(getFilesDir(),"error.log");
                FileOutputStream out = new FileOutputStream(file);
                StringWriter wr = new StringWriter();
                PrintWriter err = new PrintWriter(wr);
                //获取错误信息
                ex.printStackTrace(err);
                String errorlog = wr.toString();
                sb.append(errorlog);
                out.write(sb.toString().getBytes());
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //杀死页面进程
            restartApp();
        }
    }

    public void restartApp(){
        Intent intent = new Intent(mContext,HomeShowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }


    //初始化网络图片缓存库
    private void initImageLoader(){
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    public  static DisplayImageOptions getImageLoaderOption() {
        if(defaultOptions==null) {
            defaultOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
                    .cacheOnDisk(true).build();
        }
        // default
// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
// Remove
        if(config==null){
            // .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
// Remove
            config = new ImageLoaderConfiguration.Builder(
                    getInstance())
                    .threadPoolSize(4)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .denyCacheImageMultipleSizesInMemory()
// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
                    .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                    .memoryCacheSize((int) (2 * 1024 * 1024))
                    .memoryCacheSizePercentage(25)
                    .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(200)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                    .defaultDisplayImageOptions(defaultOptions).writeDebugLogs() // Remove
                    .build();
        }
// Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        if(options==null){
            // resource or
// drawable
// resource or
// drawable
// resource or
// drawable
// default
// default
// default
// default
// default
// default
// default
// default
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.bg_default) // resource or
                    // drawable
                    .showImageForEmptyUri(R.drawable.bg_default) // resource or
                    // drawable
                    .showImageOnFail(R.drawable.bg_default) // resource or
                    .cacheInMemory(true)// drawable
                    .resetViewBeforeLoading(false) // default
                    .delayBeforeLoading(1000).cacheInMemory(true) // default
                    .cacheOnDisk(true) // default
                    .considerExifParams(false) // default
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                    .bitmapConfig(Bitmap.Config.RGB_565) // default
                    .displayer(new SimpleBitmapDisplayer()) // default
                    .handler(new Handler()) // default
                    .build();
        }
        return options;
    }

    /**
     * 返回桌面
     */
    public void toDesktop(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    public  static String mCId="";

    public static JSONObject mUserObject;

    public  static String imagePath="";

    public  static File cameraPath;

    public static  boolean isClassify=false;

    public static int mCartCount=0;

    public static int mLightIndex = 0;//点出来的灯的序号

    public static JSONArray mSelectProducts=new JSONArray();

    public static JSONArray mSelectScreens=new JSONArray();

    public static String sharePath = "";

    public static String shareRemark = "";

}
