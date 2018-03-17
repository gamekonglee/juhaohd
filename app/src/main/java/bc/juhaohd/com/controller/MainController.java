package bc.juhaohd.com.controller;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.AppVersion;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.MainActivity;
import bc.juhaohd.com.ui.view.ShowDialog;
import bc.juhaohd.com.utils.NetWorkUtils;
import bc.juhaohd.com.utils.upload.UpAppUtils;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;

/**
 * @author: Jun
 * @date : 2017/3/17 10:24
 * @description :
 */
public class MainController extends BaseController implements INetworkCallBack {
    private TextView unMessageReadTv;
    private MainActivity mView;
    private String mAppVersion;

    public MainController(MainActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        sendShoppingCart();
//        sendVersion();
    }

    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    private void initView() {
        unMessageReadTv = (TextView) mView.findViewById(R.id.unMessageReadTv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 获取版本号
     */
    private void sendVersion(){
        mNetWork.sendVersion(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppVersion= NetWorkUtils.doGet(NetWorkConst.VERSION_URL);
                if(AppUtils.isEmpty(mAppVersion)) return;
                String localVersion = CommonUtil.localVersionName(mView);
                if ("-1".equals(mAppVersion)) {

                } else {
                    boolean isNeedUpdate = CommonUtil.isNeedUpdate(localVersion, mAppVersion);
                    if (isNeedUpdate){
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

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.GETCART:
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    IssueApplication.mCartCount = ans.getJSONArray(Constance.goods_groups).getJSONObject(0).getJSONArray(Constance.goods).length();
                    setIsShowCartCount();
                }
                break;
            case NetWorkConst.VERSION_URL:
               mAppVersion= ans.getString(Constance.JSON);

                break;
        }
    }

    private UpdateApkBroadcastReceiver broadcastReceiver;

    private class UpdateApkBroadcastReceiver extends BroadcastReceiver {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onReceive(Context context, final Intent intent) {
            // 判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                // 注销广播
                mView.unregisterReceiver(broadcastReceiver);
                broadcastReceiver = null;

                // 获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                DownloadManager down = (DownloadManager) mView.getSystemService(mView.download);
                final Uri uri = down.getUriForDownloadedFile(downId);
            }
        }
    }



    public void setIsShowCartCount() {
        unMessageReadTv.setVisibility(IssueApplication.mCartCount==0?View.GONE:View.VISIBLE);
        unMessageReadTv.setText(IssueApplication.mCartCount+"");
    }


    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

    }
}
