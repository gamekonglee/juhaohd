package bc.juhaohd.com.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.HomePageController;
import bc.juhaohd.com.ui.activity.MainActivity;
import bc.juhaohd.com.ui.activity.user.WebActivity;

/**
 * @author: Jun
 * @date : 2017/9/30 10:58
 * @description :
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {
    private HomePageController mController;
    private LinearLayout bt01_ll, bt02_ll, bt03_ll, bt04_ll, bt05_ll, bt06_ll,bt07_ll,bt08_ll;
    private ImageView zone01_iv, zone02_iv, zone03_iv, zone04_iv, zone05_iv, zone06_iv,
            tiyan_iv,pinpaitehui_iv,shangxin_iv,maishoutuijian_iv,pinpaixingxian_iv,rexiaotop_iv;
    private Intent mIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_home_page, null);
    }

    @Override
    protected void initController() {
        mController = new HomePageController(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mController.setResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mController.setPause();
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
        bt01_ll = (LinearLayout) getActivity().findViewById(R.id.bt01_ll);
        bt02_ll = (LinearLayout) getActivity().findViewById(R.id.bt02_ll);
        bt03_ll = (LinearLayout) getActivity().findViewById(R.id.bt03_ll);
        bt04_ll = (LinearLayout) getActivity().findViewById(R.id.bt04_ll);
        bt05_ll = (LinearLayout) getActivity().findViewById(R.id.bt05_ll);
        bt06_ll = (LinearLayout) getActivity().findViewById(R.id.bt06_ll);
        bt07_ll = (LinearLayout) getActivity().findViewById(R.id.bt07_ll);
        bt08_ll = (LinearLayout) getActivity().findViewById(R.id.bt08_ll);
        zone01_iv = (ImageView) getActivity().findViewById(R.id.zone01_iv);
        zone02_iv = (ImageView) getActivity().findViewById(R.id.zone02_iv);
        zone03_iv = (ImageView) getActivity().findViewById(R.id.zone03_iv);
        zone04_iv = (ImageView) getActivity().findViewById(R.id.zone04_iv);
        zone05_iv = (ImageView) getActivity().findViewById(R.id.zone05_iv);
        zone06_iv = (ImageView) getActivity().findViewById(R.id.zone06_iv);
        tiyan_iv = (ImageView) getActivity().findViewById(R.id.tiyan_iv);
        pinpaitehui_iv = (ImageView) getActivity().findViewById(R.id.pinpaitehui_iv);
        shangxin_iv = (ImageView) getActivity().findViewById(R.id.shangxin_iv);
        maishoutuijian_iv = (ImageView) getActivity().findViewById(R.id.maishoutuijian_iv);
        pinpaixingxian_iv = (ImageView) getActivity().findViewById(R.id.pinpaixingxian_iv);
        rexiaotop_iv = (ImageView) getActivity().findViewById(R.id.rexiaotop_iv);
        bt01_ll.setOnClickListener(this);
        bt02_ll.setOnClickListener(this);
        bt03_ll.setOnClickListener(this);
        bt04_ll.setOnClickListener(this);
        bt05_ll.setOnClickListener(this);
        bt06_ll.setOnClickListener(this);
        bt07_ll.setOnClickListener(this);
        bt08_ll.setOnClickListener(this);
        zone01_iv.setOnClickListener(this);
        zone02_iv.setOnClickListener(this);
        zone03_iv.setOnClickListener(this);
        zone04_iv.setOnClickListener(this);
        zone05_iv.setOnClickListener(this);
        zone06_iv.setOnClickListener(this);
        tiyan_iv.setOnClickListener(this);
        pinpaitehui_iv.setOnClickListener(this);
        shangxin_iv.setOnClickListener(this);
        maishoutuijian_iv.setOnClickListener(this);
        pinpaixingxian_iv.setOnClickListener(this);
        rexiaotop_iv.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt01_ll://现代简约
                mController.selectTypeProduct("现代简约",0);
                break;
            case R.id.bt02_ll://温馨宜家
                mController.selectTypeProduct("宜家",0);
                break;
            case R.id.bt03_ll://浪漫田园
                mController.selectTypeProduct("田园",0);
                break;
            case R.id.bt04_ll://典雅中式
                mController.selectTypeProduct("中式",0);
                break;
            case R.id.bt05_ll://小资美式
                mController.selectTypeProduct("美式",0);
                break;
            case R.id.bt06_ll://奢华欧式
                mController.selectTypeProduct("后现代奢华",0);
                break;
            case R.id.bt07_ll://后现代
                mController.selectTypeProduct("现代",0);
                break;
            case R.id.bt08_ll://新中式
                mController.selectTypeProduct("新中式",0);
                break;
            case R.id.zone01_iv://客厅
                mController.selectTypeProduct("客厅",1);
                break;
            case R.id.zone02_iv://卧室
                mController.selectTypeProduct("卧室",1);
                break;
            case R.id.zone03_iv://餐厅
                mController.selectTypeProduct("餐厅",1);
                break;
            case R.id.zone04_iv://儿童
                mController.selectTypeProduct("儿童房",1);
                break;
            case R.id.zone05_iv://光源
                mController.selectTypeProduct("光源",1);
                break;
            case R.id.zone06_iv://更多
                mController.selectTypeProduct("全部",1);
                break;
            case R.id.tiyan_iv://体验馆
                mIntent = new Intent(this.getActivity(), WebActivity.class);
                mIntent.putExtra(Constance.url, "http://www.juhao.com/select_shop.php");
                mIntent.putExtra(Constance.PC, true);
                this.startActivity(mIntent);
                break;
            case R.id.pinpaitehui_iv://品牌特惠
                break;
            case R.id.shangxin_iv://每周上新
                ((MainActivity)getActivity()).mBottomBar.selectItem(R.id.frag_product_ll);
                ((MainActivity)getActivity()).clickTab1Layout();
                ((MainActivity)getActivity()).mHomeFragment.isTypeloadData=true;
                ((MainActivity)getActivity()).mHomeFragment.isTypeId=R.id.new_tv;
                ((MainActivity)getActivity()).mHomeFragment.selectTypeProduct(R.id.new_tv);
                break;
            case R.id.maishoutuijian_iv://买手推荐
                ((MainActivity)getActivity()).mBottomBar.selectItem(R.id.frag_product_ll);
                ((MainActivity)getActivity()).clickTab1Layout();
                ((MainActivity)getActivity()).mHomeFragment.isTypeloadData=true;
                ((MainActivity)getActivity()).mHomeFragment.isTypeId=R.id.competitive_tv;
                ((MainActivity)getActivity()).mHomeFragment.selectTypeProduct(R.id.competitive_tv);
                break;
            case R.id.pinpaixingxian_iv://品牌形象

                break;
            case R.id.rexiaotop_iv://热销top
                ((MainActivity)getActivity()).mBottomBar.selectItem(R.id.frag_product_ll);
                ((MainActivity)getActivity()).clickTab1Layout();
                ((MainActivity)getActivity()).mHomeFragment.isTypeloadData=true;
                ((MainActivity)getActivity()).mHomeFragment.isTypeId=R.id.hot_tv;
                ((MainActivity)getActivity()).mHomeFragment.selectTypeProduct(R.id.hot_tv);
                break;
        }
    }
}
