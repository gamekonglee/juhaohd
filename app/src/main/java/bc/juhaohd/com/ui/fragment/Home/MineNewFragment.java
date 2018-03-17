package bc.juhaohd.com.ui.fragment.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.user.MineController;
import bc.juhaohd.com.ui.activity.user.LoginActivity;
import bc.juhaohd.com.ui.view.ShowDialog;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.UIUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 我的页面
 */
public class MineNewFragment extends BaseFragment implements View.OnClickListener {
    private CircleImageView head_cv;
    private View collect_rl, out_login_rl, order_rl,
            payment_rl, delivery_rl, Receiving_rl,address_rl,stream_rl;
    private MineController mController;
    private TextView tv_setting;
    private Bitmap icon_my_order;
    private Bitmap icon_receiving_address;
    private Bitmap icon_my_logistics;
    private Bitmap icon_my_collection;
    private Bitmap icon_setting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_mine_new, null);
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

    }

    @Override
    protected void initView() {
        head_cv = (CircleImageView) getActivity().findViewById(R.id.head_cv);
        collect_rl = (View) getActivity().findViewById(R.id.collect_rl);
//        version_rl = (View) getActivity().findViewById(R.id.version_rl);
//        clear_cache_rl = (View) getActivity().findViewById(R.id.clear_cache_rl);
        address_rl = (View) getActivity().findViewById(R.id.address_rl);
//        cotact_cutomer_rl = (View) getActivity().findViewById(R.id.cotact_cutomer_rl);
//        out_login_rl = (View) getActivity().findViewById(R.id.out_login_rl);
        order_rl = (View) getActivity().findViewById(R.id.order_rl);
        payment_rl = (View) getActivity().findViewById(R.id.payment_rl);
        delivery_rl = (View) getActivity().findViewById(R.id.delivery_rl);
        Receiving_rl = (View) getActivity().findViewById(R.id.Receiving_rl);
        stream_rl = (View) getActivity().findViewById(R.id.stream_rl);
        tv_setting = (TextView) getActivity().findViewById(R.id.tv_setting);
        TextView tv_change= (TextView) getActivity().findViewById(R.id.tv_change);
        ImageView iv_01=getView().findViewById(R.id.iv_01);
        ImageView iv_02=getView().findViewById(R.id.iv_02);
        ImageView iv_03=getView().findViewById(R.id.iv_03);
        ImageView iv_04=getView().findViewById(R.id.iv_04);
        ImageView iv_05=getView().findViewById(R.id.iv_05);

        icon_my_order = UIUtils.readBitMap(getActivity(), R.mipmap.icon_my_order);
        iv_01.setImageBitmap(icon_my_order);

        icon_receiving_address = UIUtils.readBitMap(getActivity(), R.mipmap.icon_receiving_address);
        iv_02.setImageBitmap(icon_receiving_address);

        icon_my_logistics = UIUtils.readBitMap(getActivity(), R.mipmap.icon_my_logistics);
        iv_03.setImageBitmap(icon_my_logistics);

        icon_my_collection = UIUtils.readBitMap(getActivity(), R.mipmap.icon_my_collection);
        iv_04.setImageBitmap(icon_my_collection);

        icon_setting = UIUtils.readBitMap(getActivity(), R.mipmap.icon_setting);
        iv_05.setImageBitmap(icon_setting);


        tv_change.setOnClickListener(this);
        head_cv.setOnClickListener(this);
        collect_rl.setOnClickListener(this);
//        version_rl.setOnClickListener(this);
//        clear_cache_rl.setOnClickListener(this);
//        cotact_cutomer_rl.setOnClickListener(this);
//        out_login_rl.setOnClickListener(this);
        order_rl.setOnClickListener(this);
        payment_rl.setOnClickListener(this);
        delivery_rl.setOnClickListener(this);
        Receiving_rl.setOnClickListener(this);
        address_rl.setOnClickListener(this);
        stream_rl.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        if(icon_my_order!=null)icon_my_order.recycle();
        icon_my_order=null;
        if(icon_receiving_address!=null)icon_receiving_address.recycle();
        icon_receiving_address=null;
        if(icon_my_logistics!=null)icon_my_logistics.recycle();
        icon_my_logistics=null;
        if(icon_my_collection!=null)icon_my_collection.recycle();
        icon_my_collection=null;
        if(icon_setting!=null)icon_setting.recycle();
        icon_setting=null;

        super.onDestroy();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change:
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
                        MyShare.get(MineNewFragment.this.getActivity()).putString(Constance.TOKEN, "");
                        Intent logoutIntent = new Intent(MineNewFragment.this.getActivity(), LoginActivity.class);
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
            case R.id.tv_setting:
                mController.setSetting();
                break;
        }
    }
    public void clear() {
        mController.clear();
    }
}
