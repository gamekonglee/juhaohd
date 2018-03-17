package bc.juhaohd.com.ui.activity.product;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.product.ProductDetailHDController;
import bc.juhaohd.com.ui.view.MyScrollView;
import bocang.json.JSONObject;

/**
 * @author: Jun
 * @date : 2017/4/1 14:05
 * @description :
 */
public class ProductDetailHDActivity extends BaseActivity {
    private ProductDetailHDController mController;
    public static JSONObject goodses;
    public String mProperty = "";
    public String mPropertyValue = "";
    public com.alibaba.fastjson.JSONObject mProductObject;
    public int productId;
    private ImageView share_iv;
    private LinearLayout callLl, collect_Ll;
    private Button toDiyBtn, toCartBtn;
    private ImageView top_iv;
    private MyScrollView msv;
    private int mHeigh=0;
    public LinearLayout main_ll;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ProductDetailHDController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_detail_hd);
        share_iv = getViewAndClick(R.id.share_iv);
        callLl = getViewAndClick(R.id.callLl);
        collect_Ll = getViewAndClick(R.id.collect_Ll);
        toDiyBtn = getViewAndClick(R.id.toDiyBtn);
        toCartBtn = getViewAndClick(R.id.toCartBtn);
        top_iv = getViewAndClick(R.id.top_iv);
        main_ll = (LinearLayout)findViewById(R.id.main_ll);
        mHeigh = this.getResources().getDisplayMetrics().heightPixels;

        msv = (MyScrollView) findViewById(R.id.msv);
        msv.setScrollListener(new MyScrollView.ScrollListener() {
            @Override
            public void onScrollToBottom() {

            }

            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScroll(int scrollY) {
                if(mHeigh<scrollY){
                    top_iv.setVisibility(View.VISIBLE);
                }else{
                    top_iv.setVisibility(View.GONE);
                }
            }

            @Override
            public void notBottom() {

            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        productId = intent.getIntExtra(Constance.product, 0);
    }



    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.share_iv://分享
                mController.sendShareProduct();
                break;
            case R.id.callLl://联系客服
                mController.sendCall();
                break;
            case R.id.collect_Ll://收藏
                mController.sendcollectProduct();
                break;
            case R.id.toDiyBtn://配配看
                mController.sendDiy();
                break;
            case R.id.toCartBtn://加入购物车
                mController.sendGoCart();
                break;
            case R.id.top_iv://加入购物车
                msv.scrollTo(10, 10);
                top_iv.setVisibility(View.GONE);
                break;
        }
    }
}
