package bc.juhaohd.com.ui.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.HomeShowNewController;
import bc.juhaohd.com.ui.activity.user.LoginActivity;
import bc.juhaohd.com.ui.fragment.Home.HomeIndexFragment;
import bc.juhaohd.com.ui.fragment.Home.MineNewFragment;
import bc.juhaohd.com.ui.fragment.Home.SpaceFragment;
import bc.juhaohd.com.ui.fragment.Home.StyleFragment;
import bc.juhaohd.com.ui.fragment.Home.TypeFragment;
import bc.juhaohd.com.ui.fragment.SceneHDFragment;
import bc.juhaohd.com.ui.view.BottomBarOfHome;
import bc.juhaohd.com.utils.MyShare;
import bocang.utils.AppUtils;

public class HomeShowNewActivity extends BaseActivity {

//    public TextView tel_tv,address_tv,operator_tv,two_code_tv;
    private HomeShowNewController mHomeShowController;
    public static int mFragmentState=0;
    public  HomeIndexFragment mHomeFragment;
    Fragment currentFragmen;
    public BottomBarOfHome bottom_bar;
    public  TypeFragment mTypeFragment;
    public  SpaceFragment mSpaceFragment;
    public  StyleFragment mStyleFragment;
    public LinearLayout main_rl;
    public  MineNewFragment mMineFragment;
    private ImageView iv_left;
    private ImageView iv_right;
    private SceneHDFragment mSceenSelect;
    private boolean isAppInFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toast.makeText(HomeShowNewActivity.this, "oncreate", Toast.LENGTH_SHORT).show();
        String packageName = this.getPackageName();
        ActivityManager activityManager= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appTask = activityManager.getRunningTasks(1);

        if (appTask != null)
            if(appTask.size()>0)
                if(appTask.get(0).topActivity.toString().contains(packageName))
                    isAppInFront = true;
        if(Constance.TEST_MODE){
            startActivity(new Intent(this,CartActivity.class));
        }
    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mHomeShowController = new HomeShowNewController(this);

    }

    @Override
    protected void initView() {
//        tel_tv = (TextView) findViewById(R.id.tel_tv);
//        address_tv = (TextView) findViewById(R.id.address_tv);
//        operator_tv = (TextView) findViewById(R.id.operator_tv);
//        two_code_tv = (TextView) findViewById(R.id.two_code_tv);
        setContentView(R.layout.activity_home_show_new);
        bottom_bar = (BottomBarOfHome) findViewById(R.id.title_bar01);
        bottom_bar.setOnClickListener(mBottomBarClickListener);
        main_rl = (LinearLayout) findViewById(R.id.main_rl);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFragmentState>0){
                    mFragmentState--;
                    if(mFragmentState==4){
                        mFragmentState--;
                    }
                    refresUI();
                }
            }
        });
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFragmentState<6){
                    mFragmentState++;
                    if(mFragmentState==4){
                        mFragmentState++;
                    }
                    refresUI();
                }

            }
        });
        initTab();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        if (!isFinishing()) {
            super.onResume();
            refresUI();
        }
        mHomeShowController.sendUser();
        if(!IssueApplication.noAd){
            countDownTimer.start();
        }
//        Toast.makeText(HomeShowNewActivity.this, "oncreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onViewClick(View v) {

    }


    public void refresUI() {

        int tabid=R.id.frag_home_tv;
        switch (mFragmentState){
            case 0:
                tabid=R.id.frag_home_tv;
                break;
            case 1:
                tabid=R.id.frag_type_tv;
                break;
            case 2:
                tabid=R.id.frag_space_tv;
                break;
            case 3:
                tabid=R.id.frag_style_tv;
                break;
            case 4:
                tabid=R.id.frag_search_tv;
                break;
            case 5:
                tabid=R.id.frag_sceen_tv;
//                tabid=R.id.frag_type_tv;
                break;
            case 6:
                if(isToken()){
                    return;
                }
                tabid=R.id.frag_mine_tv;
                break;
        }
        bottom_bar.selectItem(tabid);

    }

    /**
     * 初始化底部标签
     */
    public void initTab() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeIndexFragment();
        }
        if (!mHomeFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_content, mHomeFragment).commit();

            // 记录当前Fragment
            currentFragmen = mHomeFragment;
        }
//        if (mMineFragment == null) {
//            mMineFragment = new MineNewFragment();
//        }
//        if (!mMineFragment.isAdded()) {
//            // 提交事务
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fl_content, mMineFragment).commit();
//
//            // 记录当前Fragment
//            currentFragmen = mMineFragment;
//        }


    }
    public BottomBarOfHome.IBottomBarItemClickListener mBottomBarClickListener = new BottomBarOfHome.IBottomBarItemClickListener() {
        @Override
        public void OnItemClickListener(int resId) {
            switch (resId) {

                case R.id.frag_home_tv:
                    mFragmentState = 0;
                    clickTab1Layout();
                    break;
                case R.id.frag_type_tv:
                    mFragmentState=1;
                    clickTab2Layout();
                    break;
                case R.id.frag_space_tv:
                    mFragmentState=2;
                    clickTab3Layout();
                    break;
                case R.id.frag_style_tv:
                    mFragmentState=3;
                    clickTab4Layout();
                    break;
                case R.id.frag_search_tv:
                    clickTab5Layout();
                    break;
                case R.id.frag_sceen_tv:
                    mFragmentState=5;
                    clickTab6Layout();
                    break;
                case R.id.frag_mine_tv:
                    if(isToken()){
                        return;
                    }
                    mFragmentState=6;
                    clickTab7Layout();
                    break;
//                case R.id.frag_diy_ll:
//                    mFragmentState = 1;
//                    clickTab2Layout();
//                    break;
//                case R.id.frag_match_ll:
//                    mFragmentState = 2;
//                    clickTab3Layout();
//                    break;
//                case R.id.frag_cart_ll:
//                    mFragmentState = 3;
//                    clickTab4Layout();
//                    break;
//                case R.id.frag_mine_ll:
//                    mFragmentState = 4;
//                    clickTab5Layout();
//                    break;
//                case R.id.frag_home_ll:
//                    mFragmentState = 5;
//                    clickTab6Layout();
//                    break;
            }
        }




    };



    private void clickTab7Layout() {
        if (mMineFragment == null) {
            mMineFragment = new MineNewFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mMineFragment);
    }

    private void clickTab6Layout() {
        if (mSceenSelect == null) {
            mSceenSelect = new SceneHDFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mSceenSelect);
    }

    private void clickTab4Layout() {
        if (mStyleFragment == null) {
            mStyleFragment = new StyleFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mStyleFragment);
    }

    private void clickTab3Layout() {
        if (mSpaceFragment == null) {
            mSpaceFragment = new SpaceFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mSpaceFragment);
    }

    private void clickTab5Layout() {
        startActivity(new Intent(this,SearchActivity.class));
    }
    private void clickTab2Layout() {
        if (mTypeFragment == null) {
            mTypeFragment = new TypeFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mTypeFragment);
    }

    /**
     * 点击第1个tab
     */
    public void clickTab1Layout() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeIndexFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mHomeFragment);

    }
    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragmen == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragmen)
                    .add(R.id.fl_content, fragment).commit();
        }else {
            transaction.hide(currentFragmen).show(fragment).commit();
        }

        currentFragmen = fragment;
    }
    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ( secondTime - firstTime < 2000) {
                android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
                System.exit(0);
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
