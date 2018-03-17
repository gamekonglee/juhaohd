package bc.juhaohd.com.controller.user;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.MainActivity;
import bc.juhaohd.com.ui.activity.user.RegiestActivity;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.HyUtil;
import bocang.utils.IntentUtil;
import bocang.utils.MyToast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * @author: Jun
 * @date : 2017/2/8 17:25
 * @description :注册第二步
 */
public class RegiestController extends BaseController implements INetworkCallBack {
    private RegiestActivity mView;
    private EditText edtPhone,edtCode,edPwd,edtAffirmPwd;
    private String mPhone;

    public RegiestController(RegiestActivity v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
        SMSSDK.initSDK(mView, "1bd5af3b7fb5a", "1007574aad33692f6fc542937631e3bb");
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        String deviceId=UIUtils.getLocalMac(mView);
                        String code=edtCode.getText().toString()+"11";
                        String pwd=edPwd.getText().toString();
                        mNetWork.sendRegiest(deviceId,mPhone,pwd,code,mView.yaoqing,RegiestController.this);
                        //提交验证码成功
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        final String des = object.getString("detail");//错误描述
                        int status = object.getInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            Log.e("asd", "des: " + des);
                            mView.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyToast.show(mView, des);
                                }
                            });

                            mView.hideLoading();
                            return;
                        }
                    } catch (Exception e) {
                        //do something
                    }
                }
            }
        };

        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void initView() {
        edtPhone = (EditText) mView.findViewById(R.id.edtPhone);
        edtCode = (EditText) mView.findViewById(R.id.edtCode);
        edPwd = (EditText) mView.findViewById(R.id.edPwd);
        edtAffirmPwd = (EditText) mView.findViewById(R.id.edtAffirmPwd);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void sendRegiest() {
        mPhone=edtPhone.getText().toString();
        String code=edtCode.getText().toString();
        String pwd=edPwd.getText().toString();
        String affirmPwd=edtAffirmPwd.getText().toString();


        if (AppUtils.isEmpty(mPhone)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_phone));
            return;
        }
        if (AppUtils.isEmpty(code)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_verification_code));
            return;
        }
        if (AppUtils.isEmpty(pwd)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_pwd));
            return;
        }
        if (AppUtils.isEmpty(affirmPwd)) {
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_affirm_pwd));
            return;
        }

        // 做个正则验证手机号
        if (!CommonUtil.isMobileNO(mPhone)) {
            AppDialog.messageBox(UIUtils.getString(R.string.mobile_assert));
            return;
        }

        if(!affirmPwd.equals(pwd)){
            AppDialog.messageBox(UIUtils.getString(R.string.compare_pwd_affirm));
            return;
        }
        mView.setShowDialog(true);
        mView.setShowDialog("正在注册中..");
        mView.showLoading();
        String deviceId=UIUtils.getLocalMac(mView);
        mNetWork.sendRegiest(deviceId, mPhone, pwd, code, mView.yaoqing, RegiestController.this);

    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.REGIEST:
                String token=ans.getString(Constance.TOKEN);
                MyShare.get(mView).putString(Constance.TOKEN, token);//保存TOKEN
                MyShare.get(mView).putString(Constance.USERNAME, mPhone);//保存帐号
                AppDialog.messageBox(UIUtils.getString(R.string.regiest_ok));
                IntentUtil.startActivity(mView, MainActivity.class, true);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView || mView.isFinishing())
            return;
        if(AppUtils.isEmpty(ans)){
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }

    public void requestYZM() {
        mPhone=edtPhone.getText().toString();
        if (HyUtil.isEmpty(mPhone)){
            MyToast.show(mView, "请输入手机号码");
            return;
        }
        if(!CommonUtil.isMobileNO(mPhone)){
            MyToast.show(mView,"请输入正确的手机号码");
            return;
        }
        mView.find_pwd_btnGetCode.start(60);
//

//        //打开注册页面
//        RegisterPage registerPage = new RegisterPage();
//        registerPage.setRegisterCallback(new EventHandler() {
//            public void afterEvent(int event, int result, Object data) {
//                // 解析注册结果
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    @SuppressWarnings("unchecked")
//                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                    String country = (String) phoneMap.get("country");
//                    String phone = (String) phoneMap.get("phone");
//
//                    // 提交用户信息（此方法可以不调用）
////                    registerUser(country, phone);
//                }
//            }
//        });
//        registerPage.show(mView);


        mNetWork.sendRequestYZM(mPhone,this);

//        mNetWork.sendRequestYZM(mPhone,this);


    }
}
