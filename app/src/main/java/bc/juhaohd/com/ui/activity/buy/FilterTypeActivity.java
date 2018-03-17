package bc.juhaohd.com.ui.activity.buy;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.classify.FilterTypeController;
import bocang.json.JSONArray;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/1/20 15:29
 * @description :
 */
public class FilterTypeActivity extends BaseActivity {
    public Intent mIntent;
    public JSONArray mAttrArray;
    private FilterTypeController mController;
    private String mTitle;
    private TextView title_tv;
    public int mType=0;


    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new FilterTypeController(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_filter_type);
        title_tv = (TextView)findViewById(R.id.title_tv);
        if(AppUtils.isEmpty(mTitle)) return;
        title_tv.setText(mTitle);
        mType=mIntent.getIntExtra(Constance.type,0);
    }

    @Override
    protected void initData() {
        mIntent=getIntent();
        mAttrArray= (JSONArray) mIntent.getSerializableExtra(Constance.attr_list);
        mTitle=mIntent.getStringExtra(Constance.filter_attr_name);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
