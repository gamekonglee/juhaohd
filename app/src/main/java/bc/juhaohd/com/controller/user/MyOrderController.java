package bc.juhaohd.com.controller.user;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.user.MyOrderActivity;
import bc.juhaohd.com.ui.activity.user.MyOrderNewActivity;
import bc.juhaohd.com.ui.adapter.FragmentVPAdapter;
import bc.juhaohd.com.ui.fragment.OrderFragment;
import bc.juhaohd.com.ui.view.MyPagerSlidingTabStrip;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONObject;

/**
 * @author: Jun
 * @date : 2017/2/6 11:10
 * @description :我的订单
 */
public class MyOrderController extends BaseController implements INetworkCallBack {
    private MyOrderNewActivity mView;
    private MyPagerSlidingTabStrip mtabs;
    private ViewPager main_viewpager;
    private String[] titleArrs;
    private MyPageChangeListener mListener;
    private OrderFragment mOrderFragment;
    private ArrayList<OrderFragment> listFragment;

    public MyOrderController(MyOrderNewActivity myOrderNewActivity) {
        mView=myOrderNewActivity;
        initView();
        initViewData();
    }

    /**
     * 支付订单
     */
    private void sendPaymentInfo(){
        mNetWork.sendPaymentInfo(this);
    }


    private List<OrderFragment> fragmentList = new ArrayList<OrderFragment>(); //碎片链表
    private List<String> contentList = new ArrayList<String>(); //内容链表

    public MyOrderController(MyOrderActivity v){
//        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
//        sendPaymentInfo();
    }

    private void initView() {
        titleArrs = UIUtils.getStringArr(R.array.order_titles);
        main_viewpager = (ViewPager)mView.findViewById(R.id.main_viewpager);
        mtabs = (MyPagerSlidingTabStrip) mView.findViewById(R.id.tabs);
        listFragment = new ArrayList<>();
        contentList.add("-1");
        contentList.add("0");
        contentList.add("1");
        contentList.add("2");
        contentList.add("4");
        contentList.add("5");

        //有多少个标题就有多少个碎片，动态添加
        for(int i=0;i<titleArrs.length;i++){
            OrderFragment testFm = new OrderFragment().newInstance(contentList, i);
            fragmentList.add(testFm);
        }
        main_viewpager.setAdapter(new FragmentVPAdapter(mView.getSupportFragmentManager(), (ArrayList<OrderFragment>) fragmentList, titleArrs));
        mtabs.setViewPager(main_viewpager);
        mListener=new MyPageChangeListener();
        mtabs.setOnPageChangeListener(mListener);
        main_viewpager.setCurrentItem(mView.mOrderType);

    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.PAYMENTINFO:
                 int errorCode=ans.getInt(Constance.error_code);
                if(errorCode==0){
                    mView.PARTNER=ans.getString(Constance.partner);
                    mView.SELLER=ans.getString(Constance.seller_id);
                    mView.RSA_PRIVATE=ans.getString(Constance.private_key);
                }
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

    }

    class MyPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //当选择后才进行获取数据，而不是预加载
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * FragmentPagerAdapter能够预加载，
     * FragmentPagerAdapter不能够预加载，一切换就会销毁掉以前的Fragment
     */
    class TestAdapter extends FragmentPagerAdapter {

        public TestAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }


        @Override
        public int getCount() {
            if(titleArrs!=null){
                return titleArrs.length;
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position>titleArrs.length){
                return "暂未加载";
            }
            return titleArrs[position];
        }
    }

    @Override
    protected void handleMessage(int action, Object[] values) {
        
    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
