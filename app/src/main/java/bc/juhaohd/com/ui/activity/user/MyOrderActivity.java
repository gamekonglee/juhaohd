package bc.juhaohd.com.ui.activity.user;

import android.content.Intent;
import android.view.View;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.user.MyOrderController;

/**
 * @author: Jun
 * @date : 2017/2/6 11:08
 * @description :我的订单
 */
public class MyOrderActivity extends BaseActivity {
    private MyOrderController mController;
    public  int mOrderType=0;

    public static String PARTNER;
    public static String SELLER;
    public static String RSA_PRIVATE;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new MyOrderController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_myorder);
        //沉浸式状态栏
        setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mOrderType=intent.getIntExtra(Constance.order_type,0);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
