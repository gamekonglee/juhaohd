package bc.juhaohd.com.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.ui.activity.user.OrderDetailActivity;

public class OrderPaySuccessActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_order_pay_success);
        TextView tv_turn_home= (TextView) findViewById(R.id.tv_turn_back_toHome);
        TextView tv_order_detail= (TextView) findViewById(R.id.tv_order_detail);
        tv_order_detail.setOnClickListener(this);
        tv_turn_home.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_turn_back_toHome:
                HomeShowNewActivity.mFragmentState=0;
                startActivity(new Intent(this,HomeShowNewActivity.class));
                finish();
                break;
            case R.id.tv_order_detail:
                Intent intent=getIntent();
                intent.setClass(this,OrderDetailActivity.class);
                HomeShowNewActivity.mFragmentState=6;
//                intent=new Intent(this,HomeShowNewActivity.class);
//                startActivity(intent);
                String mOrder=intent.getStringExtra(Constance.order);
                int state=intent.getIntExtra(Constance.state,-1);
                intent.putExtra(Constance.order,mOrder);
                intent.putExtra(Constance.state,state);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onViewClick(View v) {

    }
}
