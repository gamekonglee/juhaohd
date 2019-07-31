package bc.juhaohd.com.controller;

import android.os.Message;

import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.fragment.Home.SpaceFragment;
import bc.juhaohd.com.utils.MyShare;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;

import static bc.juhaohd.com.ui.activity.IssueApplication.mUserObject;

/**
 * Created by bocang on 18-1-26.
 */

public class SpaceController extends  BaseController implements INetworkCallBack {

    private final SpaceFragment mView;

    public SpaceController(SpaceFragment spaceFragment) {

        mView = spaceFragment;
//        sendUser();
    }

    @Override
    public void sendAsyncMessage(int action, Object... values) {
        super.sendAsyncMessage(action, values);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 获取用户信息
     */
    public void sendUser() {
        mNetWork.sendUser(this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode){
        case NetWorkConst.PROFILE:
        mUserObject = ans.getJSONObject(Constance.user);

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
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {

    }
}
