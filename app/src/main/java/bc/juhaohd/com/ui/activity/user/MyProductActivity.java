package bc.juhaohd.com.ui.activity.user;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.MineProductController;
import bc.juhaohd.com.ui.activity.HomeShowNewActivity;
import bc.juhaohd.com.ui.activity.MainNewForJuHaoActivity;

/**
 * Created by bocang on 18-7-16.
 */

public class MyProductActivity extends BaseActivity {

    private TextView tv_title;
    public boolean isAttrloadData;
    public boolean isTypeloadData;
    private int isTypeId = 0;
    public String filter_attr_name;
    public String category="212";
    public String filter_attr;
    private TextView new_tv;
    private TextView competitive_tv;
    private TextView hot_tv;
    private EditText et_search;
    private TextView tv_none_sort;
    public TextView tv_current_select;
    public  String filter_type;
    public ImageView top_iv;
    public String keyword="";
    private MineProductController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new MineProductController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_product);
        tv_title = (TextView) findViewById(R.id.tv_title);
        new_tv = (TextView) findViewById(R.id.new_tv);
        competitive_tv = (TextView) findViewById(R.id.competitive_tv);
        hot_tv = (TextView) findViewById(R.id.hot_tv);
        tv_current_select = (TextView) findViewById(R.id.tv_current_select);
        tv_none_sort = (TextView) findViewById(R.id.tv_none_sort);
        top_iv = (ImageView) findViewById(R.id.top_iv);
        et_search = (EditText) findViewById(R.id.et_search);

        new_tv.setOnClickListener(this);
        competitive_tv.setOnClickListener(this);
        hot_tv.setOnClickListener(this);
        tv_none_sort.setOnClickListener(this);
//
//        switch (HomeShowNewActivity.mFragmentState){
//            case 0://默认
//                filter_attr_name=" ";
//                break;
//            case 1://类型
//                filter_attr_name="类型";
//                break;
//            case 2://空间
//                filter_attr_name="空间";
//                break;
//            case 3://风格
//                filter_attr_name="风格";
//                break;
//        }
//        if(getIntent().hasExtra(Constance.filter_attr_name)){
//            filter_type = getIntent().getStringExtra(Constance.filter_attr_name);
//        }
//        if(getIntent().hasExtra(Constance.keyword)){
//            keyword = getIntent().getStringExtra(Constance.keyword);
//        }
//        tv_title.setText(filter_attr_name);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
