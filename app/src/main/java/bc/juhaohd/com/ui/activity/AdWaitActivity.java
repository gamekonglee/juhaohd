package bc.juhaohd.com.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
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
import com.pgyersdk.crash.PgyCrashManager;

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.ui.view.AutoScrollViewPager;
import bc.juhaohd.com.utils.ImageUtil;
import bc.juhaohd.com.utils.UIUtils;

public class AdWaitActivity extends Activity {

    Handler handler=new Handler();
    private AutoScrollViewPager vp;
    private List<ImageView> imageViews;
    private static final int TIME = 10*1000;
    private int itemPosition;
    private List<Bitmap> bitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_ad_wait);
        vp = (AutoScrollViewPager) findViewById(R.id.vp);

        MyPagerAdapter myPagerAdapter=new MyPagerAdapter();
        imageViews = new ArrayList<>();
        bitmaps = new ArrayList<>();
        try {


            for (int i = 0; i < 4; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            bitmap = ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_1);
                ;

                switch (i) {
                    case 0:
                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_1)));
                        break;
                    case 1:
                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_2)));
                        break;
                    case 2:
                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_3)));
                        break;
                     case 3:
                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_4)));
                        break;
//                    case 4:
//                        bitmaps.add(ImageUtil.compressBgImage(ImageUtil.getBitmapById(AdWaitActivity.this, R.mipmap.ad_5)));
//                        break;
                }

//            imageView.setImageBitmap(bitmaps.get(i));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdWaitActivity.this.finish();
                    }
                });
                imageViews.add(imageView);

            }
        }catch (Exception e){
            PgyCrashManager.reportCaughtException(this,e);
        }
        vp.setAdapter(myPagerAdapter);



    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(runnableForViewPager, TIME);
    }

    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView v=imageViews.get(position);

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
            v.setImageBitmap(bitmaps.get(position));
            ViewGroup parent = (ViewGroup) v.getParent();
            //Log.i("ViewPaperAdapter", parent.toString());
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            bitmaps.set(position,null);
            container.removeView((View) object);
        }
    }

    private int mCount=4;
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
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
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
