package bc.juhaohd.com.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import bc.juhaohd.com.listener.CusWebViewDownLoadListener;
import bocang.utils.AppUtils;

/**
 * Created by XY on 2017/6/8.
 */

@SuppressLint("SetJavaScriptEnabled")
public class MyWebView extends WebView {
    public LoadingDialog mDialog;
    private Activity mActivity;
    private WebSettings wSettings;

    public MyWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {


        wSettings = getSettings();
        //String ua = wSettings.getUserAgentString();
        //LogUtil.e("ua::"+ua);
        wSettings.setJavaScriptEnabled(true);
        wSettings.setAppCacheEnabled(true);
        wSettings.setDatabaseEnabled(true);
        wSettings.setDomStorageEnabled(true);
        wSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        wSettings.setAllowFileAccess(true);

        wSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        wSettings.setUseWideViewPort(true);//设置webview推荐使用的窗口
        wSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式
        wSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
        wSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        wSettings.setAllowFileAccess(true); // 允许访问文件
        wSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        wSettings.setSupportZoom(true); // 支持缩放


        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                setVisibility(View.INVISIBLE);
                if (mActivity!=null) {
                    if(AppUtils.isEmpty(mDialog)){
                        mDialog=new LoadingDialog(mActivity);
                    }
                    try{
                        mDialog.show();
                    }catch (Exception e){

                    }
//                    MyProgressDialog.show(mActivity, true);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setVisibility(View.VISIBLE);
//                MyProgressDialog.close();
                if(mDialog!=null)mDialog.dismiss();
                super.onPageFinished(view, url);
            }
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        final String description, final String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
//                Log.e("ErrorCode", "errorCode::" + errorCode + "--description::"
//                        + description + "--failingUrl::" + failingUrl);
//                loadUrl("file:///android_asset/error.html");
//                postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        loadUrl("javascript:setcontent('" + "网络不给力啊" + "')");
//                        loadUrl("javascript:seturl('" + failingUrl + "')");
//                    }
//                }, 1000);
////                MyProgressDialog.close();
                mDialog.dismiss();
            }
        });

        setWebChromeClient(new WebChromeClient());
    }

    public void setActivity(Activity myActivity) {
        // TODO Auto-generated method stub
        mActivity = myActivity;
        init();
        //主要用于平板，针对特定屏幕代码调整分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        if(mActivity!=null)mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            wSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            wSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            wSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            wSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            wSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            wSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        this.setDrawingCacheEnabled(true);
        wSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        wSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        setDownloadListener(new CusWebViewDownLoadListener(mActivity));
//        addJavascriptInterface(new JSInterface(mActivity, this), "JSInterface");

    }

}
