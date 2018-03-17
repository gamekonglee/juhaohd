package bc.juhaohd.com.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuListView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.buy.CartController;
import bc.juhaohd.com.ui.activity.buy.ConfirmOrderActivity;

public class CartActivity extends BaseActivity implements View.OnClickListener {
    private TextView edit_tv, settle_tv,export_tv;
    private SwipeMenuListView listView;
    private CheckBox checkAll;
    private Boolean mIsBack=false;
    private LinearLayout back_ll;
    private CartController mController;
    private ScrollView contentView;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Constance.TEST_MODE){
            startActivity(new Intent(this, ConfirmOrderActivity.class));
        }
    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new CartController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_cart);
        edit_tv = (TextView)  findViewById(R.id.edit_tv);
        settle_tv = (TextView) findViewById(R.id.settle_tv);
        export_tv = (TextView) findViewById(R.id.export_tv);
        checkAll = (CheckBox) findViewById(R.id.checkAll);
        contentView =(ScrollView) findViewById(R.id.contentView);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        settle_tv.setOnClickListener(this);
        export_tv.setOnClickListener(this);
        edit_tv.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mController.setCkeckAll(isChecked);
            }
        });
        back_ll = (LinearLayout) findViewById(R.id.back_ll);
        if(mIsBack==false){
            back_ll.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_tv:
                mController.setEdit();
                break;
            case R.id.settle_tv:
                mController.sendSettle();
                break;
            case R.id.export_tv://导出清单
                mController.exportData();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==200&&resultCode==200){
            startActivity(data);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mController.sendShoppingCart();
        checkAll.setChecked(false);
    }
    public void showContentView() {
        contentView.setVisibility(View.VISIBLE);
    }
}
