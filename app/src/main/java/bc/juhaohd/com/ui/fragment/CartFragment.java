package bc.juhaohd.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuListView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.buy.CartController;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 购物车
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {
    private CartController mController;
    private TextView edit_tv, settle_tv,export_tv;
    private SwipeMenuListView listView;
    private CheckBox checkAll;
    private Boolean mIsBack=false;
    private LinearLayout back_ll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_cart, null);
    }

    @Override
    protected void initController() {
        mController = new CartController(this);
    }

    @Override
    protected void initViewData() {
        isToken();
    }

    @Override
    protected void initView() {
        edit_tv = (TextView)  getActivity().findViewById(R.id.edit_tv);
        settle_tv = (TextView) getActivity().findViewById(R.id.settle_tv);
        export_tv = (TextView) getActivity().findViewById(R.id.export_tv);
        checkAll = (CheckBox) getActivity().findViewById(R.id.checkAll);
        settle_tv.setOnClickListener(this);
        export_tv.setOnClickListener(this);
        edit_tv.setOnClickListener(this);
        checkAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mController.setCkeckAll(isChecked);
            }
        });
        back_ll = (LinearLayout) getActivity().findViewById(R.id.back_ll);
        if(mIsBack==false){
            back_ll.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        if(getArguments()==null) return;
        if(getArguments().get(Constance.product)==null) return;
        mIsBack= (Boolean) getArguments().get(Constance.product);
    }

    @Override
    public void onStart() {
        super.onStart();
        mController.sendShoppingCart();
        checkAll.setChecked(false);
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

        }
    }
}
