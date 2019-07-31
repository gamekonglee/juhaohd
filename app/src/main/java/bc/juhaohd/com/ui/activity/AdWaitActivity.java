package bc.juhaohd.com.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.misc.Utils;
import com.baiiu.filter.util.UIUtil;
import com.bigkoo.convenientbanner.CBLoopViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pgyersdk.crash.PgyCrashManager;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.AdBean;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.ui.view.AutoScrollViewPager;
import bc.juhaohd.com.utils.FileUtil;
import bocang.json.JSONObject;
import bocang.utils.LogUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdWaitActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 200;
//
//    Handler handler=new Handler();
//    private AutoScrollViewPager vp;
//    private List<ImageView> imageViews;
//    private static final int TIME = 10*1000;
//    private int itemPosition;
//    private List<Bitmap> bitmaps;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
//        setContentView(R.layout.activity_ad_wait);
//        vp = (AutoScrollViewPager) findViewById(R.id.vp);
//
//        MyPagerAdapter myPagerAdapter=new MyPagerAdapter();
//        imageViews = new ArrayList<>();
//        bitmaps = new ArrayList<>();
//        try {
//
//
//            for (int i = 0; i < 4; i++) {
//                ImageView imageView = new ImageView(this);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
////            bitmap = ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_1);
//                switch (i) {
//                    case 0:
//                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_2)));
//                        break;
//                    case 1:
//                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_1)));
//                        break;
//                    case 2:
//                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_4)));
//                        break;
//                     case 3:
//                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_5)));
//                        break;
////                    case 4:
////                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_6)));
////                        break;
////                    case 5:
////                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_6)));
////                        break;
////                    case 6 :
////                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_7)));
////                        break;
//                }
//
////            imageView.setImageBitmap(bitmaps.get(i));
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AdWaitActivity.this.finish();
//                    }
//                });
//                imageViews.add(imageView);
//
//            }
//        }catch (Exception e){
//            PgyCrashManager.reportCaughtException(this,e);
//        }
//        vp.setAdapter(myPagerAdapter);
//
//
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        handler.removeCallbacksAndMessages(null);
//        handler.postDelayed(runnableForViewPager, TIME);
//    }
//
//    class MyPagerAdapter extends PagerAdapter{
//
//        @Override
//        public int getCount() {
//            return 4;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view==object;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            ImageView v=imageViews.get(position);
//
////            switch (position){
////                case 0:
////                    bitmaps.set(0,ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_1)));
////                    break;
////                case 1:
////                    bitmaps.set(1,ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_2)));
////                    break;
////                case 2:
////                    bitmaps.set(2,ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_3)));
////                    break;
//////                case 3:
//////                    bitmap =ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_4);
//////                    break;
////            }
//            v.setImageBitmap(bitmaps.get(position));
//            ViewGroup parent = (ViewGroup) v.getParent();
//            //Log.i("ViewPaperAdapter", parent.toString());
//            if (parent != null) {
//                parent.removeAllViews();
//            }
//            container.addView(v);
//            return v;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
////            bitmaps.set(position,null);
//            container.removeView((View) object);
//        }
//    }
//
//    private int mCount=4;
//    /**
//     * ViewPager的定时器
//     */
//    Runnable runnableForViewPager = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                itemPosition++;
//                handler.postDelayed(this, TIME);
//                vp.setCurrentItem(itemPosition % mCount);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        for(int i=0;i<bitmaps.size();i++){
//            Bitmap bitmap=bitmaps.get(i);
//            if(bitmap != null ){
//                bitmap.recycle();
//                bitmap = null;
//            }
//            System.gc();
//        }
//        handler.removeCallbacksAndMessages(null);
//        super.onDestroy();
//    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==3){
                PgyCrashManager.reportCaughtException(AdWaitActivity.this,new Exception("Ad_handler_post_addView"));
                for (int i = 0; i < bitmaps.size(); i++) {
                    ImageView imageView = new ImageView(AdWaitActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageBitmap(bitmaps.get(i));
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AdWaitActivity.this.finish();
                        }
                    });
                    imageViews.add(imageView);
                }
                mCount=imageViews.size();
                myPagerAdapter = new MyPagerAdapter();
                vp.setAdapter(myPagerAdapter);
                isComplete = true;
                handler.removeCallbacksAndMessages(null);
                if(isComplete){
                    handler.postDelayed(runnableForViewPager, TIME);
                }

            }else if(msg.what==2){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        count=0;
                        for(int i=0;i<uri.size();i++){
                            ImageLoader.getInstance().loadImage("file://" + uri.get(i), new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String s, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String s, View view, FailReason failReason) {
                                    PgyCrashManager.reportCaughtException(AdWaitActivity.this,new Exception("Ad_handler_load_failed"+s));
                                }

                                @Override
                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                    PgyCrashManager.reportCaughtException(AdWaitActivity.this,new Exception("Ad_handler_load_complete"+s));
                                    count++;
                                    bitmaps.add(bitmap);
                                    if(count==uri.size()){
                                        handler.sendEmptyMessage(3);
                                    }
                                }

                                @Override
                                public void onLoadingCancelled(String s, View view) {

                                }
                            });
                        }
                    }
                });
            }
        }
    };
    private AutoScrollViewPager vp;
    private List<ImageView> imageViews;
    private static final int TIME = 10*1000;
    private int itemPosition;
    private List<Bitmap> bitmaps;
    private List<AdBean> adBeans;
    private MyPagerAdapter myPagerAdapter;
    private int count;
    private List<String> uri;
    private boolean isComplete=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_ad_wait);
        IssueApplication.isStartedAd=true;
        vp = (AutoScrollViewPager) findViewById(R.id.vp);

        imageViews = new ArrayList<>();
        bitmaps = new ArrayList<>();
        uri = new ArrayList<>();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
        }else {
        getPic();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200&&grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
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

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
        }
    }

    private void getPic() {
        PgyCrashManager.reportCaughtException(this,new Exception("Ad_getpic"));
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("id","266")
                .build();
        Request request=new Request.Builder().post(body).url(NetWorkConst.BANNER_INDEX).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PgyCrashManager.reportCaughtException(AdWaitActivity.this,new Exception("Ad_getpic_onFailure"));
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String result= null;
                try {
                    result = response.body().string();
                    LogUtils.logE("ad",result);
                    JSONObject jsonObject=new JSONObject(result);
                    result=jsonObject.getJSONArray(Constance.banners).toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                adBeans = new Gson().fromJson(result,new TypeToken<List<AdBean>>(){}.getType());
                if(adBeans!=null&&adBeans.size()>0){
                    try {
                        boolean isNeedRefresh=false;
                        for(int i=0;i<adBeans.size();i++){
                            String url=adBeans.get(i).getAd_code();
                            String path = FileUtil.getAdExternDir(url);
                            File imageFile = new File(path );
                            if (!imageFile.exists()) {
                                isNeedRefresh=true;
                                uri=new ArrayList<>();
                                break;
//                                        imageURL = "file://" + imageFile.toString();
//                                        ImageLoader.getInstance().displayImage(imageURL, holder.imageView);
                            }else {
                                LogUtils.logE("file","isExists");
                                uri.add(imageFile.toString());
                            }
                        }
                        if(isNeedRefresh){
                            count = 0;
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

                                    }

                                    @Override
                                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                                                    bitmaps.add(bitmap);
                                        LogUtils.logE("imguri",s);
                                        count++;
                                        try {
                                            File filePic = new File(FileUtil.getAdExternDir(adBeans.get(finalI).getAd_code()));
                                            if (!filePic.exists()) {
                                                filePic.getParentFile().mkdirs();
                                                filePic.createNewFile();
                                            }
                                            FileOutputStream fos = new FileOutputStream(filePic);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                            uri.add(filePic.toString());
                                            fos.flush();
                                            fos.close();
                                            if(bitmap!=null&&!bitmap.isRecycled()){
                                                bitmap.recycle();
                                            }
                                            if(count==adBeans.size()){
                                                handler.sendEmptyMessage(2);
                                            }
                                        } catch (IOException e) {
                                            PgyCrashManager.reportCaughtException(AdWaitActivity.this,new Exception("Ad_getpic_load_complete"+e.getMessage()));
                                        }

                                    }

                                    @Override
                                    public void onLoadingCancelled(String s, View view) {

                                    }
                                });

//            imageView.setImageBitmap(bitmaps.get(i));
//                                    imageView.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            AdWaitActivity.this.finish();
//                                        }
//                                    });
//                                    imageViews.add(imageView);

                            }

                        }else {
                            //不需要更新
                            handler.sendEmptyMessage(2);
                        }

                    }catch (Exception e){
                        PgyCrashManager.reportCaughtException(AdWaitActivity.this,e);
                    }


                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView v=imageViews.get(position);
            try {
//            switch (position){
//                case 0:
//                    bitmaps.set(0,ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_1)));
//                    break;
//                case 1:
//                    bitmaps.set(1,ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_2)));
//                    break;
//                case 2:
//                    bitmaps.set(2,ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_3)));
//                    break;
////                case 3:
////                    bitmap =ImageUtil.getBitmapById(AdWaitActivity.this,R.mipmap.ad_4);
////                    break;
//            }
                if (bitmaps.get(position)!=null&&!bitmaps.get(position).isRecycled()){
                    v.setImageBitmap(bitmaps.get(position));
                }
                ViewGroup parent = (ViewGroup) v.getParent();
                //Log.i("ViewPaperAdapter", parent.toString());
                if (parent != null) {
                    parent.removeAllViews();
                }
                container.addView(v);
                return v;
            }catch (Exception e){
                PgyCrashManager.reportCaughtException(AdWaitActivity.this,new Exception("instantiateItem"));
            }
            return v;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            bitmaps.set(position,null);
            container.removeView((View) object);
        }
    }

    private int mCount=(imageViews==null?0:imageViews.size());
    /**
     * ViewPager的定时器
     */
    Runnable runnableForViewPager = new Runnable() {
        @Override
        public void run() {
            try {
                itemPosition++;
                handler.postDelayed(this, TIME);

                vp.setCurrentItem(itemPosition % mCount);
            } catch (Exception e) {
                PgyCrashManager.reportCaughtException(AdWaitActivity.this,new Exception("runnableForViewPager"+ e.getMessage()));
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        IssueApplication.isStartedAd=false;
        for(int i=0;i<bitmaps.size();i++){
            Bitmap bitmap=bitmaps.get(i);
            if(bitmap != null ){
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
