package bc.juhaohd.com.ui.activity.buy;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.buy.ConfirmOrderController;
import bc.juhaohd.com.ui.activity.EditValueActivity;
import bc.juhaohd.com.ui.activity.OrderPayHomeActivity;
import bc.juhaohd.com.ui.view.ShowDialog;
import bocang.json.JSONArray;
import bocang.json.JSONObject;

/**
 * @author: Jun
 * @date : 2017/2/24 16:51
 * @description :
 */
public class ConfirmOrderActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private ConfirmOrderController mController;
    public JSONArray goodsList;
    public RelativeLayout address_rl;
    private TextView money_tv, settle_tv;
    private RadioGroup group;
    private RelativeLayout logistic_type_rl;
    public   JSONObject mAddressObject;
    private Intent mIntent;
    private LinearLayout remark_ll;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new ConfirmOrderController(this);
    }

    @Override
    protected void initView() {

        setContentView(R.layout.activity_confirm_order);
        address_rl = getViewAndClick(R.id.address_rl);
        settle_tv = getViewAndClick(R.id.settle_tv);
        money_tv = (TextView) findViewById(R.id.summoney_tv);
        money_tv.setText("￥" + money + "");
        group = (RadioGroup)this.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(this);
        logistic_type_rl = (RelativeLayout)findViewById(R.id.logistic_type_rl);
        logistic_type_rl = getViewAndClick(R.id.logistic_type_rl);
        remark_ll = getViewAndClick(R.id.remark_ll);
        if(Constance.TEST_MODE){
            startActivity(new Intent(this, OrderPayHomeActivity.class));
        }
    }

    public float money = 0;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        goodsList = (JSONArray) intent.getSerializableExtra(Constance.goods);
        mAddressObject = (JSONObject) intent.getSerializableExtra(Constance.address);
        money = intent.getFloatExtra(Constance.money, 0);

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.address_rl:
                mController.selectAddress();
                break;
            case R.id.settle_tv:
                mController.settleOrder();
                break;
            case R.id.logistic_type_rl:
                mController.selectLogistic();
                break;
            case R.id.remark_ll:
                mIntent = new Intent(this, EditValueActivity.class);
                mIntent.putExtra(Constance.TITLE, "订单备注");
                mIntent.putExtra(Constance.CODE, 007);
                startActivityForResult(mIntent, 007);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mController.selectCheckType(group.getCheckedRadioButtonId());
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
}
