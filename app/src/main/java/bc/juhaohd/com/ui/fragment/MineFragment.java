package bc.juhaohd.com.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.user.MineController;
import bc.juhaohd.com.ui.activity.user.LoginActivity;
import bc.juhaohd.com.ui.view.ShowDialog;
import bc.juhaohd.com.utils.MyShare;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 我的页面
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private CircleImageView head_cv;
    private View collect_rl, version_rl, clear_cache_rl, cotact_cutomer_rl, out_login_rl, order_rl,
            payment_rl, delivery_rl, Receiving_rl,address_rl,stream_rl;
    private MineController mController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_mine, null);
    }

    @Override
    protected void initController() {
        mController = new MineController(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mController.sendUser();
    }

    @Override
    protected void initViewData() {
        isToken();
    }

    @Override
    protected void initView() {
        head_cv = (CircleImageView) getActivity().findViewById(R.id.head_cv);
        collect_rl = (View) getActivity().findViewById(R.id.collect_rl);
        version_rl = (RelativeLayout) getActivity().findViewById(R.id.version_rl);
        clear_cache_rl = (RelativeLayout) getActivity().findViewById(R.id.clear_cache_rl);
        address_rl = (RelativeLayout) getActivity().findViewById(R.id.address_rl);
        cotact_cutomer_rl = (RelativeLayout) getActivity().findViewById(R.id.cotact_cutomer_rl);
        out_login_rl = (RelativeLayout) getActivity().findViewById(R.id.out_login_rl);
        order_rl = (RelativeLayout) getActivity().findViewById(R.id.order_rl);
        payment_rl = (RelativeLayout) getActivity().findViewById(R.id.payment_rl);
        delivery_rl = (RelativeLayout) getActivity().findViewById(R.id.delivery_rl);
        Receiving_rl = (RelativeLayout) getActivity().findViewById(R.id.Receiving_rl);
        stream_rl = (RelativeLayout) getActivity().findViewById(R.id.stream_rl);
        head_cv.setOnClickListener(this);
        collect_rl.setOnClickListener(this);
        version_rl.setOnClickListener(this);
        clear_cache_rl.setOnClickListener(this);
        cotact_cutomer_rl.setOnClickListener(this);
        out_login_rl.setOnClickListener(this);
        order_rl.setOnClickListener(this);
        payment_rl.setOnClickListener(this);
        delivery_rl.setOnClickListener(this);
        Receiving_rl.setOnClickListener(this);
        address_rl.setOnClickListener(this);
        stream_rl.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_cv://头像
                mController.setHead();
                break;
            case R.id.collect_rl://我的收藏
                mController.setCollect();
                break;
            case R.id.version_rl://当前版本
                //                mController.setOrder();
                break;
            case R.id.clear_cache_rl://清楚缓存
                mController.clearCache();
                break;
            case R.id.cotact_cutomer_rl://联系客服
                mController.sendCall();
                break;
            case R.id.out_login_rl://退出登录
                ShowDialog mDialog = new ShowDialog();
                mDialog.show(this.getActivity(), "提示", "确定退出登录?", new ShowDialog.OnBottomClickListener() {
                    @Override
                    public void positive() {
                        MyShare.get(MineFragment.this.getActivity()).putString(Constance.TOKEN, "");
                        Intent logoutIntent = new Intent(MineFragment.this.getActivity(), LoginActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logoutIntent);
                    }

                    @Override
                    public void negtive() {

                    }
                });
                break;
            case R.id.order_rl:
                mController.setOrder();
                break;
            case R.id.payment_rl:
                mController.setPayMen();
                break;
            case R.id.delivery_rl:
                mController.setDelivery();
                break;
            case R.id.Receiving_rl://待收货
                mController.setReceiving();
                break;
            case R.id.address_rl://管理收货地址
                mController.setAddress();
                break;
            case R.id.stream_rl://管理物流地址
                mController.setStream();
                break;
        }
    }
}
