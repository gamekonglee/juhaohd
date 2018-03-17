package bc.juhaohd.com.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.MainController;
import bc.juhaohd.com.ui.fragment.CartFragment;
import bc.juhaohd.com.ui.fragment.HomeHDFragment;
import bc.juhaohd.com.ui.fragment.HomePageFragment;
import bc.juhaohd.com.ui.fragment.MineFragment;
import bc.juhaohd.com.ui.fragment.ProgrammeFragment;
import bc.juhaohd.com.ui.fragment.SceneHDFragment;
import bc.juhaohd.com.ui.view.BottomBar;
import bc.juhaohd.com.ui.view.WarnDialog;
import bocang.json.JSONArray;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity {
    public HomeHDFragment mHomeFragment;
    private HomePageFragment mHomePageFragment;
    private SceneHDFragment mSceneFragment;
    private CartFragment mCartFragment;
    private ProgrammeFragment mMatchFragment;
    private MineFragment mMineFragment;
    public BottomBar mBottomBar;
    private Fragment currentFragmen;
    private int pager = 2;
    private long exitTime;
    public static JSONArray mCategories;
    private MainController mController;
    public String download = DOWNLOAD_SERVICE;
    private EditText et_search;
    public static boolean isForeground = false;
    private int mFragmentState = 0;
    private int selectType = 0;
    private LinearLayout to_back_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        EventBus.getDefault().register(this);
    }


    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        //        JPushInterface.init(getApplicationContext());
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JPushInterface.init(this);
    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController = new MainController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main_hd);
        mBottomBar = (BottomBar) findViewById(R.id.title_bar);
        mBottomBar.setOnClickListener(mBottomBarClickListener);
        MyShare.get(this).putInt(Constance.barheight, mBottomBar.getHeight());
        initTab();
        et_search = (EditText) findViewById(R.id.et_search);

        Drawable drawable = getResources().getDrawable(R.drawable.search);
        drawable.setBounds(5, 0, 35, 35);
        et_search.setCompoundDrawables(drawable, null, null, null);

        //        et_search.setImeOptions(EditorInfo.);
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(MainActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    if (mFragmentState == 0) {
                        //接下来在这里做你自己想要做的事，实现自己的业务。
                        if(AppUtils.isEmpty(mHomeFragment))return false;
                        mHomeFragment.searchData(et_search.getText().toString());
                    }
                }

                return false;
            }
        });

        if (selectType == R.id.frag_cart_ll) {
            this.mBottomBar.selectItem(R.id.frag_cart_ll);
            this.clickTab4Layout();
        } else if (selectType == R.id.frag_mine_ll) {
            this.mBottomBar.selectItem(R.id.frag_mine_ll);
            this.clickTab5Layout();
        }

        to_back_ll = (LinearLayout)findViewById(R.id.to_back_ll);
        to_back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(MainActivity.this, HomeShowActivity.class, true);
            }
        });


    }



    @Override
    protected void initData() {
        Intent intent = getIntent();
        selectType = intent.getIntExtra(Constance.selectType, 0);
    }

    private BottomBar.IBottomBarItemClickListener mBottomBarClickListener = new BottomBar.IBottomBarItemClickListener() {
        @Override
        public void OnItemClickListener(int resId) {
            switch (resId) {

                case R.id.frag_product_ll:
                    mFragmentState = 0;
                    clickTab1Layout();
                    break;
                case R.id.frag_diy_ll:
                    mFragmentState = 1;
                    clickTab2Layout();
                    break;
                case R.id.frag_match_ll:
                    mFragmentState = 2;
                    clickTab3Layout();
                    break;
                case R.id.frag_cart_ll:
                    mFragmentState = 3;
                    clickTab4Layout();
                    break;
                case R.id.frag_mine_ll:
                    mFragmentState = 4;
                    clickTab5Layout();
                    break;
                case R.id.frag_home_ll:
                    mFragmentState = 5;
                    clickTab6Layout();
                    break;
            }
        }
    };

    /**
     * 初始化底部标签
     */
    private void initTab() {
        if (mHomePageFragment == null) {
            mHomePageFragment = new HomePageFragment();
        }
        if (!mHomePageFragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_bar, mHomePageFragment).commit();

            // 记录当前Fragment
            currentFragmen = mHomePageFragment;
        }


    }

    /**
     * 点击第1个tab
     */
    public void clickTab1Layout() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeHDFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mHomeFragment);

    }

    /**
     * 点击第2个tab
     */
    private void clickTab2Layout() {
        if (mSceneFragment == null) {
            mSceneFragment = new SceneHDFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mSceneFragment);

    }

    /**
     * 点击第3个tab
     */
    private void clickTab3Layout() {
        if (mMatchFragment == null) {
            mMatchFragment = new ProgrammeFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mMatchFragment);

    }

    /**
     * 点击第4个tab
     */
    private void clickTab4Layout() {
        if (mCartFragment == null) {
            mCartFragment = new CartFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mCartFragment);

    }

    /**
     * 点击第5个tab
     */
    private void clickTab5Layout() {
        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mMineFragment);

    }

    /**
     * 点击第6个tab
     */
    private void clickTab6Layout() {
        if (mHomePageFragment == null) {
            mHomePageFragment = new HomePageFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mHomePageFragment);

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
                    .add(R.id.top_bar, fragment).commit();
        } else {
            transaction.hide(currentFragmen).show(fragment).commit();
        }

        currentFragmen = fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (pager == 2) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    exitTime = System.currentTimeMillis();
                    MyToast.show(this, R.string.back_desktop);
                } else {
                    finish();
                    //                    MyToast.cancelToast();
                    //                    getApp().toDesktop();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * show 激活 dialog
     */
    public void showActivateDialog() {
        WarnDialog activateDialog = new WarnDialog(this, "请激活该设备", "确定", true, false, false);
        activateDialog.setListener(new bc.juhaohd.com.ui.view.BaseDialog.IConfirmListener() {
            @Override
            public void onDlgConfirm(bc.juhaohd.com.ui.view.BaseDialog dlg, int flag) {
                if (flag == 0) {
                    MyToast.show(MainActivity.this, "激活成功!!");
                }
            }
        });
        activateDialog.show();
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //在主线程执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(Integer action) {
        if (action == Constance.CARTCOUNT) {
            mController.setIsShowCartCount();
        }
    }

}
