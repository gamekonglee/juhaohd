package bc.juhaohd.com.controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.AppVersion;
import bc.juhaohd.com.bean.ArticlesBean;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.HomeShowActivity;
import bc.juhaohd.com.ui.activity.HomeShowNewActivity;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.fragment.Home.HomeIndexFragment;
import bc.juhaohd.com.ui.view.ShowDialog;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.NetWorkUtils;
import bc.juhaohd.com.utils.UIUtils;
import bc.juhaohd.com.utils.upload.UpAppUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

import static bc.juhaohd.com.ui.activity.IssueApplication.mUserObject;

/**
 * Created by DEMON on 2018/1/18.
 */
public class HomeShowNewController extends BaseController implements INetworkCallBack{
    private HomeShowNewActivity mView;
    private String mAppVersion;
    private JSONArray mArticlesArray;
    private List<ArticlesBean> mArticlesBeans = new ArrayList<>();
    private ProgressDialog pd;

    public HomeShowNewController(HomeShowNewActivity homeShowNewActivity) {
        mView = homeShowNewActivity;
        initView();
        initViewData();
    }


    private void initViewData() {
        sendUser();
        sendVersion();
//        pd = ProgressDialog.show(mView,"","加载中");
        sendArticle();
        checkSystem();
    }

    private void checkSystem() {
        mNetWork.checkSystem(new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                    JSONObject data=ans.getJSONObject(Constance.data);
                    if(data!=null&&data.length()>0){
                        UIUtils.showSystemStopDialog(mView,data.getString(Constance.text));
                    }
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {

            }
        });
    }

    /**
     * 获取文章列表
     */
    private void sendArticle() {
        int page = 1;
        int per_page = 20;
        mNetWork.sendArticle(page, per_page, this);
    }


    /**
     * 获取版本号
     */
    private void sendVersion() {
        mNetWork.sendVersion(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ans = NetWorkUtils.doGet(NetWorkConst.VERSION_URL_CONTENT);
                final JSONObject jsonObject=new JSONObject(ans);

                if (AppUtils.isEmpty(jsonObject))
                    return;
                mAppVersion=jsonObject.getString(Constance.version);
                String localVersion = CommonUtil.localVersionName(mView);
                if ("-1".equals(mAppVersion)) {

                } else {
                    boolean isNeedUpdate = CommonUtil.isNeedUpdate(localVersion, mAppVersion);
                    if (isNeedUpdate) {
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Dialog dialog = new Dialog(mView, R.style.customDialog);
                                dialog.setContentView(R.layout.dialog_update);
                                TextView tv_info= (TextView) dialog.findViewById(R.id.tv_update_info);
                                Button btn_upgrate= (Button) dialog.findViewById(R.id.btn_upgrate);
                                ImageView iv_close= (ImageView) dialog.findViewById(R.id.iv_close);
                                String updateInfo=jsonObject.getString(Constance.text);
                                tv_info.setText(""+ Html.fromHtml(updateInfo).toString());
                                dialog.show();
                                btn_upgrate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AppVersion appVersion = new AppVersion();
                                        appVersion.setVersion(mAppVersion);
                                        appVersion.setName(NetWorkConst.APK_NAME);
                                        appVersion.setDes("");
                                        appVersion.setForcedUpdate("0");
                                        appVersion.setUrl(NetWorkConst.DOWN_APK_URL);
                                        if (appVersion != null) {
                                            dialog.dismiss();
                                            new UpAppUtils(mView, appVersion);
                                        }
                                    }
                                });
                                iv_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
//                                ShowDialog mDialog = new ShowDialog();
//                                mDialog.show(mView, "升级提示", "有最新的升级包，是否升级?", new ShowDialog.OnBottomClickListener() {
//                                    @Override
//                                    public void positive() {
//                                        //                                        broadcastReceiver = new UpdateApkBroadcastReceiver();
//                                        //                                        mView.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//                                        //                                        Intent intent = new Intent(mView, UpdateApkService.class);
//                                        //                                        mView.startService(intent);
//                                        AppVersion appVersion = new AppVersion();
//                                        appVersion.setVersion(mAppVersion);
//                                        appVersion.setName(NetWorkConst.APK_NAME);
//                                        appVersion.setDes("");
//                                        appVersion.setForcedUpdate("0");
//                                        appVersion.setUrl(NetWorkConst.DOWN_APK_URL);
//                                        if (appVersion != null) {
//                                            new UpAppUtils(mView, appVersion);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void negtive() {
//                                        mView.finish();
//                                    }
//                                });
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
                EventBus.getDefault().postSticky(new bc.juhaohd.com.bean.Message(0,"yes,that's all right~!"));


                if (AppUtils.isEmpty(jsonObject))
                    return;

                //                String parent_name =jsonObject.getString(Constance.parent_name);
                //                mView.operator_tv.setText(parent_name);
//                mView.tel_tv.setText("联系方式:" + jsonObject.getString(Constance.phone));
//                mView.address_tv.setText("商家地址:" + jsonObject.getString(Constance.address));
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
                IssueApplication.mUserObject=mUserObject;
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
//                    mView.operator_tv.setText(mUserObject.getString("parent_name"));
                } else {
//                    mView.operator_tv.setText(mUserObject.getString("nickname"));
                }


                String yaoqing = mUserObject.getString(Constance.invite_code);
//                if (!AppUtils.isEmpty(yaoqing))
//                    mView.two_code_tv.setText("注册邀请码:" + yaoqing);
                sendShopAddress(userId);
//                sendArticle();
                break;
            case NetWorkConst.VERSION_URL:
                LogUtils.logE("URL",""+ans);
                break;
            case NetWorkConst.ARTICLELIST:
                if(pd!=null&&pd.isShowing())pd.dismiss();
//                mView.bottom_bar.setOnClickListener(mView.mBottomBarClickListener);
//                mView.initTab();
//
                mArticlesArray = ans.getJSONArray(Constance.articles);
                for (int i = 0; i < mArticlesArray.length(); i++) {
                    JSONObject jsonObject = mArticlesArray.getJSONObject(i);
                    if (jsonObject.getInt(Constance.article_type) == 1) {
                        int id = jsonObject.getInt(Constance.id);
                        String title = jsonObject.getString(Constance.title);
                        String url = jsonObject.getString(Constance.url);
                        mArticlesBeans.add(new ArticlesBean(id, title, url));
                    }
                }
                IssueApplication.articlesBeans=mArticlesBeans;
//
                EventBus.getDefault().postSticky(new bc.juhaohd.com.bean.Message(0,"yes,that's all right~!"));
                if (mArticlesBeans.size() == 0)
                    return;
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.PROFILE:
//                UIUtils.showSystemStopDialog(mView,"尊敬的客户，您好！由于你长时间没有登录钜豪商城，你的账号已经被冻结，如需重新开通账号，请致电400-0760-888");
            break;
        }
        if (null == mView || mView.isFinishing())
            return;

    }
}
