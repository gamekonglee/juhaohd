package bc.juhaohd.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.common.hxp.view.PullableGridView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.controller.product.HomeHDController;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/3/30 13:50
 * @description :
 */
public class HomeHDFragment extends BaseFragment implements View.OnClickListener {
    private HomeHDController mController;
    private TextView competitive_tv, new_tv, hot_tv;
    private ImageView top_iv;
    private PullableGridView mGoodsSv;
    private int mHeigh = 0;
    public String filter_attr;
    public  String category="";
    public String filter_attr_name;
    public Boolean isAttrloadData = false;
    public Boolean isTypeloadData = false;
    public int isTypeId = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_home_hd, null);
    }


    @Override
    protected void initController() {
        mController = new HomeHDController(this);
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        competitive_tv = (TextView) getView().findViewById(R.id.competitive_tv);
        new_tv = (TextView) getView().findViewById(R.id.new_tv);
        hot_tv = (TextView) getView().findViewById(R.id.hot_tv);
        competitive_tv.setOnClickListener(this);
        new_tv.setOnClickListener(this);
        hot_tv.setOnClickListener(this);
        mGoodsSv = (PullableGridView) getView().findViewById(R.id.gridView);
        mHeigh = this.getResources().getDisplayMetrics().heightPixels;
        top_iv = (ImageView) getView().findViewById(R.id.top_iv);
        top_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoodsSv.setSelection(0);
                top_iv.setVisibility(View.VISIBLE);
            }
        });

        mGoodsSv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("520it", position + "");
                if (position == 8) {
                    top_iv.setVisibility(View.VISIBLE);
                } else {
                    top_iv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (isAttrloadData) {
            isAttrloadData = false;
            getAttrData();

        }
        if (isTypeloadData) {
            isTypeloadData = false;
            selectTypeProduct(isTypeId);
        }

    }

    public void getAttrData() {
        if (AppUtils.isEmpty(mController))
            return;

        if (!AppUtils.isEmpty(filter_attr_name)) {
            mController.getDropDownMenuText();
        }
        selectTypeProduct(0);

        mController.page = 1;
        setShowDialog(true);
        setShowDialog("正在获取中..");
        showLoading();
        mController.selectProduct(mController.page, "20", null, null, null);
    }


    public void searchData(String keyword) {
        mController.keyword = keyword;
        mController.page = 1;

        setShowDialog(true);
        setShowDialog("正在获取中..");
        showLoading();
        mController.selectProduct(mController.page, "20", null, null, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        selectTypeProduct(v.getId());

    }

    public void selectTypeProduct(int type) {
        if (AppUtils.isEmpty(mController))
            return;

        competitive_tv.setTextColor(getActivity().getResources().getColor(R.color.black));
        new_tv.setTextColor(getActivity().getResources().getColor(R.color.txt_black));
        hot_tv.setTextColor(getActivity().getResources().getColor(R.color.txt_black));
        if (type == 0) {
            mController.mSortKey = "";
            mController.mSortValue = "";
            return;
        }

        filter_attr="";
        filter_attr_name="类型,空间,风格";
        if (!AppUtils.isEmpty(filter_attr_name)) {
            mController.getDropDownMenuText();
        }

        switch (type) {
            case R.id.competitive_tv:
                competitive_tv.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryRed));
                break;
            case R.id.new_tv:
                new_tv.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryRed));
                break;
            case R.id.hot_tv:
                hot_tv.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryRed));
                break;
        }
        mController.selectSortType(type);
    }


}
