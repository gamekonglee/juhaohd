package bc.juhaohd.com.controller;

import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Set;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.fragment.Home.HomeIndexFragment;
import bc.juhaohd.com.utils.MyShare;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;

import static bc.juhaohd.com.ui.activity.IssueApplication.mUserObject;

/**
 * Created by DEMON on 2018/1/20.
 */
public class HomeIndexController extends BaseController implements INetworkCallBack {

    private final HomeIndexFragment mView;
    private TextView tv_code;

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public HomeIndexController(HomeIndexFragment fragment){
        mView = fragment;
        initView();
        initData();


    }

    private void initView() {
        tv_code = (TextView) mView.getView().findViewById(R.id.tv_code);

    }

    private void initData() {
        sendUser();
    }
    /**
     * 获取用户信息
     */
    public void sendUser() {
        mNetWork.sendUser(this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.PROFILE:
                mUserObject = ans.getJSONObject(Constance.user);
//                int userId = 0;
//                if (!AppUtils.isEmpty(mUserObject.getString("parent_id"))) {
//                    if (mUserObject.getInt("parent_id") == 0) {
//                        MyShare.get(mView.getActivity()).putInt(Constance.USERCODEID, mUserObject.getInt("id"));
//                        userId = mUserObject.getInt("id");
//                    } else {
//                        MyShare.get(mView.getActivity()).putInt(Constance.USERCODEID, mUserObject.getInt("parent_id"));
//                        userId = mUserObject.getInt("parent_id");
//                    }
//
//                }

                if (!AppUtils.isEmpty(mUserObject.getString("parent_name"))) {
//                    mView.operator_tv.setText(mUserObject.getString("parent_name"));
                } else {
//                    mView.operator_tv.setText(mUserObject.getString("nickname"));
                }

                String yaoqing = mUserObject.getString(Constance.invite_code);
                if (!AppUtils.isEmpty(yaoqing))
                    tv_code.setText("注册邀请码:" + yaoqing);


                IssueApplication.mUserObject = mUserObject;
                if (!AppUtils.isEmpty(mUserObject.getString("parent_id"))) {
                    if (mUserObject.getInt("parent_id") == 0) {
                        MyShare.get(mView.getActivity()).putInt(Constance.USERCODEID, mUserObject.getInt("id"));
                    } else {
                        MyShare.get(mView.getActivity()).putInt(Constance.USERCODEID, mUserObject.getInt("parent_id"));
                    }

                }
                if (!AppUtils.isEmpty(mUserObject.getString("parent_name"))) {
                    MyShare.get(mView.getActivity()).putString(Constance.USERCODE, mUserObject.getString("parent_name"));
                } else {
                    MyShare.get(mView.getActivity()).putString(Constance.USERCODE, mUserObject.getString("nickname"));
                }

                String user_name = MyShare.get(mView.getActivity()).getString(Constance.USERCODE);
                String name = mUserObject.getString(Constance.username);
                if (AppUtils.isEmpty(user_name)) {

                    mView.tv_server.setText(name);
                } else {

                    mView.tv_server.setText(user_name);
                }

                String aliasId = IssueApplication.mUserObject.getString(Constance.id);

                int level = mUserObject.getInt(Constance.level);
                String inviteCode = mUserObject.getString(Constance.invite_code);
                MyShare.get(mView.getActivity()).putString(Constance.invite_code, inviteCode);
//                if (level < 3) {
//                    if (AppUtils.isEmpty(inviteCode)) {
//                        IntentUtil.startActivity(mView.getActivity(), SetInviteCodeActivity.class, false);
//                    }
//                }
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        if (null == mView.getActivity() || mView.getActivity().isFinishing())
            return;
    }
}
