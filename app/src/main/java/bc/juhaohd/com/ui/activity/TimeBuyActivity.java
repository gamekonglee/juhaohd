package bc.juhaohd.com.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.controller.BuyTimeController;

public class TimeBuyActivity extends BaseActivity {

    private BuyTimeController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new BuyTimeController(this);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_time_buy);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
