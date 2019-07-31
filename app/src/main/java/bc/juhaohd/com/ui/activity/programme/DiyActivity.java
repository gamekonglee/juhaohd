package bc.juhaohd.com.ui.activity.programme;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.programme.DiyController;
import bc.juhaohd.com.ui.activity.CartActivity;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.buy.ShoppingCartActivity;
import bc.juhaohd.com.ui.view.ShowDialog;
import bc.juhaohd.com.utils.FileUtil;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.PermissionUtils;

/**
 * @author: Jun
 * @date : 2017/2/18 10:31
 * @description :场景配灯
 */
public class DiyActivity extends BaseActivity {
    private DiyController mController;
    private ImageView goproductIv,goscreenctIv,goPhotoIv,goImageIv,goBrightnessIv,cartIv,goTowCodeIv,goShareIv,goSaveIv,goBackBtn;
    private LinearLayout image_ll,goCart_ll,detail_ll,delete_ll,add_data_ll;
    public  LinearLayout select_ll;
    private FrameLayout sceneFrameLayout;
    public String mPath;
//    public SparseArray<JSONObject> mSelectedLightSA = new SparseArray<>();
    public JSONObject mGoodsObject;
    public String mProperty="";
    public String mProductId="";
    private LinearLayout seekbar_ll;
    private RelativeLayout data_rl,detail_rl;
    private EditText et_search;
    private Button toDetailBtn,toCollectBtn;
    public String mSceenPath;
    private TextView select_product_tv,select_diy_tv,add_data_tv;
    private ListView product_lv,diy_lv;
    public int mSelectType=0;

    @Override
    protected void InitDataView() {
//        mSelectType=0;
//        switchProOrDiy();
    }

    @Override
    protected void initController() {
        mController=new DiyController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_diy);
        //沉浸式状态栏
        setColor(this, getResources().getColor(R.color.colorPrimary));
        goproductIv = getViewAndClick(R.id.goproductIv);
        goscreenctIv = getViewAndClick(R.id.goscreenctIv);
        goPhotoIv = getViewAndClick(R.id.goPhotoIv);
        goImageIv = getViewAndClick(R.id.goImageIv);
        goBrightnessIv = getViewAndClick(R.id.goBrightnessIv);
        cartIv = getViewAndClick(R.id.cartIv);
        goTowCodeIv = getViewAndClick(R.id.goTowCodeIv);
        goSaveIv = getViewAndClick(R.id.goSaveIv);
        image_ll = getViewAndClick(R.id.image_ll);
        goCart_ll = getViewAndClick(R.id.goCart_ll);
        detail_ll = getViewAndClick(R.id.detail_ll);
        delete_ll = getViewAndClick(R.id.delete_ll);
        goBackBtn = getViewAndClick(R.id.goBackBtn);
        et_search = getViewAndClick(R.id.et_search);
//        goShareIv = getViewAndClick(R.id.goShareIv);
        toDetailBtn = getViewAndClick(R.id.toDetailBtn);
        toCollectBtn = getViewAndClick(R.id.toCollectBtn);
        add_data_ll = getViewAndClick(R.id.add_data_ll);
        sceneFrameLayout = getViewAndClick(R.id.sceneFrameLayout);
        select_ll = (LinearLayout)findViewById(R.id.select_ll);
        seekbar_ll = (LinearLayout)findViewById(R.id.seekbar_ll);
        data_rl = (RelativeLayout)findViewById(R.id.data_rl);
        detail_rl = (RelativeLayout)findViewById(R.id.detail_rl);
        Drawable drawable = getResources().getDrawable(R.drawable.search);
        drawable.setBounds(5, 0, 35, 35);
        et_search.setCompoundDrawables(drawable, null, null, null);
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(DiyActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    mController.searchData(et_search.getText().toString());
                    data_rl.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
        select_product_tv = getViewAndClick(R.id.select_product_tv);
        select_diy_tv = getViewAndClick(R.id.select_diy_tv);
        product_lv = (ListView)findViewById(R.id.product_lv);
        diy_lv = (ListView)findViewById(R.id.diy_lv);
        add_data_tv = (TextView)findViewById(R.id.add_data_tv);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mPath=intent.getStringExtra(Constance.photo);
        mProperty=intent.getStringExtra(Constance.property);
        mSceenPath=intent.getStringExtra(Constance.path);
        mGoodsObject= (JSONObject) intent.getSerializableExtra(Constance.product);
        if(AppUtils.isEmpty(mGoodsObject)) return;
        mProductId=mGoodsObject.getString(Constance.id);
        IssueApplication.mSelectedLightSA=new SparseArray<>();
    }

    @Override
    protected void onViewClick(View v) {
        seekbar_ll.setVisibility(View.GONE);
//        data_rl.setVisibility(View.GONE);
        detail_rl.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.goproductIv:
                mController.selectProduct();
                break;
            case R.id.goscreenctIv:
                mController.selectScreen();
                break;
            case R.id.goPhotoIv:
                mController.goPhotoImage();
                break;
            case R.id.goImageIv:
                mController.sendBackgroudImage();
                break;
            case R.id.goBrightnessIv:
                mController.goBrightness();
                break;
            case R.id.cartIv:
                IntentUtil.startActivity(this, CartActivity.class, false);
                break;
            case R.id.sceneFrameLayout:
                mController.selectIsFullScreen();
                break;
            case R.id.goTowCodeIv:
                    mController.goPhoto();
                break;
            case R.id.goSaveIv:
                mController.saveDiy();
                break;
            case R.id.goBackBtn:
                getFinish();
                break;
            case R.id.image_ll:
                mController.sendProductImage();
                break;
            case R.id.goCart_ll:
                mController.goShoppingCart();
                break;
            case R.id.detail_ll:
                mController.senDetail();
                break;
            case R.id.delete_ll:
                mController.goDetele();
                break;
            case R.id.toDetailBtn:
                mController.goDetail();
                break;
            case R.id.toCollectBtn:
                mController.sendCollect();
                break;
            case R.id.select_product_tv://切换产品
                mSelectType=0;
                switchProOrDiy();
                mController.selectShowData();
                break;
            case R.id.select_diy_tv://切换场景
                mSelectType=1;
                switchProOrDiy();
                mController.selectShowData();
                break;
            case R.id.add_data_ll://添加数据
                if(mSelectType==0){
                    mController.selectProduct();
                }else{
                    mController.selectSceneDatas();
                }
                break;
//            case R.id.goShareIv:
//                mController.getShareDiy(0,false);
//                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        getFinish();
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出
     */
    private void getFinish(){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "是否退出配灯界面?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                finish();
                IssueApplication.mSelectProducts = new JSONArray();
                IssueApplication.mSelectScreens = new JSONArray();
            }

            @Override
            public void negtive() {

            }
        });
    }

    /**
     * 切换产品或场景
     */
    public void switchProOrDiy(){
        select_product_tv.setBackgroundResource(R.color.deep_transparent);
        select_diy_tv.setBackgroundResource(R.color.deep_transparent);
        product_lv.setVisibility(View.GONE);
        diy_lv.setVisibility(View.GONE);
        if(mSelectType==0){
            select_product_tv.setBackgroundResource(R.color.transparent);
            product_lv.setVisibility(View.VISIBLE);
            add_data_tv.setText("选择产品");

        }else{
            select_diy_tv.setBackgroundResource(R.color.transparent);
            product_lv.setVisibility(View.VISIBLE);
            add_data_tv.setText("选择场景");

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
                FileUtil.openImage(DiyActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.onBackPressed();
    }
}
