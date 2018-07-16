package bc.juhaohd.com.ui.activity;

import android.view.View;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.controller.TaoCanHomeController;

/**
 * Created by bocang on 18-6-15.
 */

public class TaoCanHomeActivity extends BaseActivity {

    private TaoCanHomeController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new TaoCanHomeController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_taocan);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
