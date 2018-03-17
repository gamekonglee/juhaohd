package bc.juhaohd.com.controller;

import android.os.Message;

import bc.juhaohd.com.bean.AppVersion;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.HomeShowActivity;
import bc.juhaohd.com.ui.activity.HomeShowNewActivity;
import bc.juhaohd.com.ui.view.ShowDialog;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.NetWorkUtils;
import bc.juhaohd.com.utils.upload.UpAppUtils;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.MyToast;

import static bc.juhaohd.com.ui.activity.IssueApplication.mUserObject;

/**
 * @author: Jun
 * @date : 2017/10/10 11:04
 * @description :
 */
public class HomeShowController extends BaseController implements INetworkCallBack {
    private HomeShowActivity mView;
    private String mAppVersion;

    public HomeShowController(HomeShowActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    public HomeShowController(HomeShowNewActivity homeShowNewActivity) {
//        mView = homeShowNewActivity;
        initView();
        initViewData();
    }


    private void initViewData() {
        sendUser();
        sendVersion();
    }

    /**
     * 获取版本号
     */
    private void sendVersion() {
        mNetWork.sendVersion(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppVersion = NetWorkUtils.doGet(NetWorkConst.VERSION_URL);
                if (AppUtils.isEmpty(mAppVersion))
                    return;
                String localVersion = CommonUtil.localVersionName(mView);
                if ("-1".equals(mAppVersion)) {

                } else {
                    boolean isNeedUpdate = CommonUtil.isNeedUpdate(localVersion, mAppVersion);
                    if (isNeedUpdate) {
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowDialog mDialog = new ShowDialog();
                                mDialog.show(mView, "升级提示", "有最新的升级包，是否升级?", new ShowDialog.OnBottomClickListener() {
                                    @Override
                                    public void positive() {
                                        //                                        broadcastReceiver = new UpdateApkBroadcastReceiver();
                                        //                                        mView.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                        //                                        Intent intent = new Intent(mView, UpdateApkService.class);
                                        //                                        mView.startService(intent);
                                        AppVersion appVersion = new AppVersion();
                                        appVersion.setVersion(mAppVersion);
                                        appVersion.setName(NetWorkConst.APK_NAME);
                                        appVersion.setDes("");
                                        appVersion.setForcedUpdate("0");
                                        appVersion.setUrl(NetWorkConst.DOWN_APK_URL);
                                        if (appVersion != null) {
                                            new UpAppUtils(mView, appVersion);
                                        }
                                    }

                                    @Override
                                    public void negtive() {
                                        mView.finish();
                                    }
                                });
                            }
                        });


                    }
                }

            }
        }).start();

    }

    private void initView() {

    }

    /**
     * 获取用户信息
     */
    public void sendUser() {
        mNetWork.sendUser(this);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 获取邀请码用户信息
     *
     * @param id
     */
    private void sendShopAddress(int id) {
        mNetWork.sendShopAddress(id, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                JSONObject jsonObject = ans.getJSONObject(Constance.shop);
                if (AppUtils.isEmpty(jsonObject))
                    return;

                //                String parent_name =jsonObject.getString(Constance.parent_name);
                //                mView.operator_tv.setText(parent_name);
                mView.tel_tv.setText("联系方式:" + jsonObject.getString(Constance.phone));
                mView.address_tv.setText("商家地址:" + jsonObject.getString(Constance.address));
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                MyToast.show(mView, "数据异常，请重试!");
            }
        });
    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.PROFILE:
                mUserObject = ans.getJSONObject(Constance.user);
                int userId = 0;
                if (!AppUtils.isEmpty(mUserObject.getString("parent_id"))) {
                    if (mUserObject.getInt("parent_id") == 0) {
                        MyShare.get(mView).putInt(Constance.USERCODEID, mUserObject.getInt("id"));
                        userId = mUserObject.getInt("id");
                    } else {
                        MyShare.get(mView).putInt(Constance.USERCODEID, mUserObject.getInt("parent_id"));
                        userId = mUserObject.getInt("parent_id");
                    }

                }

                if (!AppUtils.isEmpty(mUserObject.getString("parent_name"))) {
                    mView.operator_tv.setText(mUserObject.getString("parent_name"));
                } else {
                    mView.operator_tv.setText(mUserObject.getString("nickname"));
                }

                String yaoqing = mUserObject.getString(Constance.invite_code);
                if (!AppUtils.isEmpty(yaoqing))
                    mView.two_code_tv.setText("注册邀请码:" + yaoqing);
                sendShopAddress(userId);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        if (null == mView || mView.isFinishing())
            return;

    }
}
