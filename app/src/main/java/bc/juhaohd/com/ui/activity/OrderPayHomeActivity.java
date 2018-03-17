package bc.juhaohd.com.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.OrderPayController;
import bc.juhaohd.com.ui.view.MyWebView;
import bc.juhaohd.com.ui.view.ShowDialog;
import bocang.utils.LogUtils;

public class OrderPayHomeActivity extends BaseActivity {

    public TextView tv_money;
    public TextView tv_name;
    public TextView tv_mobie;
    public TextView tv_osn;
    public TextView tv_address;
    public MyWebView web_wv;
    public String consignee;
    public String money;
    public String mobie;
    public String shipping;
    public String address;
    public String idArray;
    public MyWebView web_wv_wx;
    private OrderPayController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new OrderPayController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_pay_home);
        tv_money = (TextView) findViewById(R.id.totla_money_tv);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_mobie = (TextView) findViewById(R.id.tv_mobie);
        tv_osn = (TextView) findViewById(R.id.tv_osn);
        tv_address = (TextView) findViewById(R.id.tv_address);
        web_wv = (MyWebView) findViewById(R.id.web_wv);
        web_wv_wx=findViewById(R.id.web_wv_wx);
        if(getIntent().hasExtra(Constance.consignee)){
            consignee = getIntent().getStringExtra(Constance.consignee);
            money = getIntent().getFloatExtra(Constance.money,0)+"";
            mobie = getIntent().getStringExtra(Constance.tel);
            shipping = getIntent().getStringExtra(Constance.shipping);
            address = getIntent().getStringExtra(Constance.address);
            idArray = getIntent().getStringExtra(Constance.id);
            String name=getIntent().getStringExtra(Constance.name);
            tv_money.setText("¥"+money);
            tv_name.setText(""+name);
            tv_mobie.setText(""+mobie);
            tv_address.setText(address);

        }
        if(Constance.TEST_MODE){
            startActivity(new Intent(this,OrderPaySuccessActivity.class));
            finish();
        }


    }

    @Override
    protected void initData() {

    }
    public void goBack(View v){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "是否退出当前的确定订单?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                finish();
            }

            @Override
            public void negtive() {

            }
        });
    }

    @Override
    protected void onViewClick(View v) {

    }
}
