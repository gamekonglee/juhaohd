package bc.juhaohd.com.ui.activity.user;

import android.content.Intent;
import android.view.View;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.ui.view.MyWebView;

/**
 * @author: Jun
 * @date : 2017/9/28 16:28
 * @description :
 */
public class WebActivity extends BaseActivity {
    MyWebView mWebView;
    String mUrl = "";
    private boolean isPC = false;

    @Override
    protected void InitDataView() {
        mWebView.loadUrl(mUrl);
    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_web);
        mWebView = (MyWebView) findViewById(R.id.webview);
        mWebView.setActivity(this);
        if (isPC) {
            mWebView.getSettings().setUserAgentString("PC");
            mWebView.getSettings().setDefaultTextEncodingName("GBK");//设置编码格式
        }
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(Constance.url);
        isPC = intent.getBooleanExtra(Constance.PC, false);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
