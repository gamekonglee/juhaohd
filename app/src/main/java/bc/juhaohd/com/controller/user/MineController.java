package bc.juhaohd.com.controller.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.SettingActivity;
import bc.juhaohd.com.ui.activity.SettingNewActivity;
import bc.juhaohd.com.ui.activity.user.CollectNewActivity;
import bc.juhaohd.com.ui.activity.user.MessageActivity;
import bc.juhaohd.com.ui.activity.user.MyOrderNewActivity;
import bc.juhaohd.com.ui.activity.user.PerfectMydataActivity;
import bc.juhaohd.com.ui.activity.user.PerfectMydataNewActivity;
import bc.juhaohd.com.ui.activity.user.UserAddrNewActivity;
import bc.juhaohd.com.ui.activity.user.UserLogNewActivity;
import bc.juhaohd.com.ui.fragment.MineFragment;
import bc.juhaohd.com.ui.fragment.Home.MineNewFragment;
import bc.juhaohd.com.utils.ImageLoadProxy;
import bc.juhaohd.com.utils.ShareUtil;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.DataCleanUtil;
import bocang.utils.IntentUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: Jun
 * @date : 2017/1/21 15:08
 * @description :
 */
public class MineController extends BaseController implements INetworkCallBack {
    private MineNewFragment mView;
    private CircleImageView head_cv;
    private TextView nickname_tv,cache_num_tv,version_tv;
    public JSONObject mUserObject;
    public Intent mIntent;
    private String mTotalCacheSize;
    private TextView unMessageReadTv, unMessageRead02Tv, unMessageRead03Tv,level_tv;

    public MineController(MineFragment v) {
//        mView = v;
        initView();
        initViewData();
    }

    public MineController(MineNewFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        getTotalCacheSize();
        String localVersion = CommonUtil.localVersionName(mView.getActivity());
//        version_tv.setText("V"+localVersion);

    }

    private void initView() {
        head_cv = (CircleImageView) mView.getActivity().findViewById(R.id.head_cv);
        nickname_tv = (TextView) mView.getActivity().findViewById(R.id.nickname_tv);
        cache_num_tv = (TextView) mView.getActivity().findViewById(R.id.cache_num_tv);
        version_tv = (TextView) mView.getActivity().findViewById(R.id.version_tv);
        unMessageReadTv = (TextView) mView.getView().findViewById(R.id.unMessageReadTv);
        unMessageRead02Tv = (TextView) mView.getView().findViewById(R.id.unMessageRead02Tv);
        unMessageRead03Tv = (TextView) mView.getView().findViewById(R.id.unMessageRead03Tv);
        level_tv = (TextView) mView.getView().findViewById(R.id.level_tv);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        new AlertDialog.Builder(mView.getActivity()).setTitle("清除缓存?").setMessage("确认清除您所有的缓存？")
                .setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mTotalCacheSize.equals("0K")){
                            AppDialog.messageBox("没有缓存可以清除!");
                            return;
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DataCleanUtil.clearAllCache(mView.getActivity());
                                mView.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDialog.messageBox("清除缓存成功!");
                                        getTotalCacheSize();
                                    }
                                });
                            }
                        }).start();

                    }
                })
                .setNegativeButton("取消", null).show();
    }

    /**
     * 查看缓存大小
     */
    private void getTotalCacheSize(){
        try {
            mTotalCacheSize = DataCleanUtil.getTotalCacheSize(mView.getActivity());
//            cache_num_tv.setText(mTotalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 头像
     */
    public void setHead() {
        IntentUtil.startActivity(mView.getActivity(),PerfectMydataNewActivity.class,false);
    }

    /**
     * 设置
     */
    public void setSetting() {
        IntentUtil.startActivity(mView.getActivity(), SettingNewActivity.class, false);
    }

    /**
     * 我的订单
     */
    public void setOrder() {
        IntentUtil.startActivity(mView.getActivity(), MyOrderNewActivity.class, false);
    }

    /**
     * 我的收藏
     */
    public void setCollect() {
        IntentUtil.startActivity(mView.getActivity(), CollectNewActivity.class,false);

    }

    /**
     * 联系客服
     */
    public void sendCall() {
        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
    }




    /**
     * 代收货
     */
    public void setReceiving() {
        mIntent=new Intent(mView.getActivity(), MyOrderNewActivity.class);
        mIntent.putExtra(Constance.order_type,3);
        mView.startActivity(mIntent);
    }

    /**
     * 管理收货地址
     */
    public void setAddress() {
        IntentUtil.startActivity(mView.getActivity(), UserAddrNewActivity.class, false);
    }

    /**
     * 管理物流地址
     */
    public void setStream() {
        IntentUtil.startActivity(mView.getActivity(), UserLogNewActivity.class, false);
    }

    /**
     * 消息中心
     */
    public void SetMessage() {
        IntentUtil.startActivity(mView.getActivity(), MessageActivity.class,false);
    }

    /**
     * 待付款
     */
    public void setPayMen() {
        mIntent=new Intent(mView.getActivity(), MyOrderNewActivity.class);
        mIntent.putExtra(Constance.order_type, 1);
        mView.startActivity(mIntent);
    }

    /**
     * 待发货
     */
    public void setDelivery() {
        mIntent=new Intent(mView.getActivity(), MyOrderNewActivity.class);
        mIntent.putExtra(Constance.order_type,2);
        mView.startActivity(mIntent);
    }

    /**
     * 获取用户信息
     */
    public void sendUser() {
        mNetWork.sendUser(this);
    }

    /**
     * 分享给好友
     */
    public void getShareApp() {
        String title = "来自 " + UIUtils.getString(R.string.app_name) + " App的分享";
        ShareUtil.showShare(mView.getActivity(), title, NetWorkConst.APK_URL, NetWorkConst.SHAREIMAGE);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.PROFILE:
                mUserObject= ans.getJSONObject(Constance.user);
                IssueApplication.mUserObject=mUserObject;
                String avatar = NetWorkConst.SCENE_HOST+mUserObject.getString(Constance.avatar);
                if (!AppUtils.isEmpty(avatar))
                    ImageLoadProxy.displayHeadIcon(avatar, head_cv);

                String username = IssueApplication.mUserObject.getString(Constance.username);
                String nickName = IssueApplication.mUserObject.getString(Constance.nickname);
                int level = IssueApplication.mUserObject.getInt(Constance.level);
                String levelValue = "";
                if (level == 0) {
                    levelValue = "一级";
                } else if (level == 1) {
                    levelValue = "二级";
                } else if (level == 2) {
                    levelValue = "三级";
                } else {
                    levelValue = "消费者";
                }
                level_tv.setText(levelValue);

                if (AppUtils.isEmpty(nickName)) {
                    nickname_tv.setText(username);
                    IntentUtil.startActivity(mView.getActivity(), PerfectMydataActivity.class, false);
                    return;
                } else {
                    nickname_tv.setText(nickName);
                }
                JSONArray countArray = ans.getJSONArray("count");
                String count01 = countArray.get(0).toString();
                String count02 = countArray.get(1).toString();
                String count03 = countArray.get(2).toString();
                nickname_tv.setText(nickName);
                unMessageReadTv.setText(countArray.get(0).toString());
                unMessageRead02Tv.setText(countArray.get(1).toString());
                unMessageRead03Tv.setText(countArray.get(2).toString());
                unMessageReadTv.setVisibility(Integer.parseInt(count01) > 0 ? View.VISIBLE : View.GONE);
                unMessageRead02Tv.setVisibility(Integer.parseInt(count02) > 0 ? View.VISIBLE : View.GONE);
                unMessageRead03Tv.setVisibility(Integer.parseInt(count03) > 0 ? View.VISIBLE : View.GONE);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        if (null == mView || mView.getActivity().isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
//        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }

    public void clear() {
        level_tv.setText("");
        nickname_tv.setText("");
        nickname_tv.setText("");
        unMessageReadTv.setText("");
        unMessageRead02Tv.setText("");
        unMessageRead03Tv.setText("");
        unMessageReadTv.setVisibility(View.GONE);
        unMessageRead02Tv.setVisibility(View.GONE);
        unMessageRead03Tv.setVisibility(View.GONE);

    }
}
