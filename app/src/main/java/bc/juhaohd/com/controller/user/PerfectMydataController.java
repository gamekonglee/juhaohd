package bc.juhaohd.com.controller.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;

import java.io.File;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.EditValueActivity;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.user.PerfectMydataActivity;
import bc.juhaohd.com.ui.activity.user.PerfectMydataNewActivity;
import bc.juhaohd.com.utils.ImageLoadProxy;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.NetWorkUtils;
import bc.juhaohd.com.utils.UIUtils;
import bc.juhaohd.com.utils.photo.CameraUtil;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.HyUtil;
import bocang.utils.MyLog;
import bocang.utils.MyToast;
import bocang.utils.PermissionUtils;
import cn.qqtheme.framework.picker.DatePicker;
import de.hdodenhof.circleimageview.CircleImageView;

import static bc.juhaohd.com.ui.activity.IssueApplication.mUserObject;

/**
 * @author: Jun
 * @date : 2017/1/21 16:45
 * @description :
 */
public class PerfectMydataController extends BaseController implements OnItemClickListener, INetworkCallBack {
    public PerfectMydataNewActivity mView;
    public Intent mIntent;
    public TextView name_tv, sex_tv, birthday_tv, phone_tv, telephone_tv, email_tv;
    public AlertView mSexView;
    public AlertView mHeadView;
    public CircleImageView head_iv;
    public int sexType = 0;

    public PerfectMydataController(PerfectMydataActivity v) {
//        mView = v;
        initView();
        initViewData();
    }

    public PerfectMydataController(PerfectMydataNewActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        JSONObject mUserObject = IssueApplication.mUserObject;
        fillData(mUserObject);

    }

    private void fillData(JSONObject mUserObject) {
        if(AppUtils.isEmpty(mUserObject)) return;
        if(!AppUtils.isEmpty(mUserObject.getString(Constance.avatar))){
            String avatar = NetWorkConst.SCENE_HOST + mUserObject.getString(Constance.avatar);
            if (!AppUtils.isEmpty(avatar))
                ImageLoadProxy.displayHeadIcon(avatar, head_iv);
        }

        String nickname = mUserObject.getString(Constance.nickname);
        name_tv.setText(nickname);
        int sex = mUserObject.getInt(Constance.gender);
        sexType = sex;
        sex_tv.setText(sex == 0 ? "男" : "女");
        if(sex==0){
            mView.tv_man.setChecked(true);
//            mView.tv_women.setChecked(false);
        }else {
//            mView.tv_man.setChecked(false);
            mView.tv_women.setChecked(true);
        }
        birthday_tv.setText(mUserObject.getString(Constance.birthday));
    }

    public void sendUser(){
        mNetWork.sendUser(this);
    }

    private void initView() {
        name_tv = (TextView) mView.findViewById(R.id.name_tv);
        sex_tv = (TextView) mView.findViewById(R.id.sex_tv);
        birthday_tv = (TextView) mView.findViewById(R.id.birthday_tv);
        phone_tv = (TextView) mView.findViewById(R.id.phone_tv);
        telephone_tv = (TextView) mView.findViewById(R.id.telephone_tv);
        email_tv = (TextView) mView.findViewById(R.id.email_tv);
        mSexView = new AlertView(null, null, "取消", null,
                Constance.SEXTYPE,
                mView, AlertView.Style.ActionSheet, this);
        mHeadView = new AlertView(null, null, "取消", null,
                Constance.CAMERTYPE,
                mView, AlertView.Style.ActionSheet, this);
        head_iv = (CircleImageView) mView.findViewById(R.id.head_iv);
    }

    /**
     * 编辑页面
     *
     * @param title
     * @param type
     */
    public void setIntent(String title, int type) {
        mIntent = new Intent(mView, EditValueActivity.class);
        mIntent.putExtra(Constance.TITLE, title);
        mIntent.putExtra(Constance.CODE, type);
        mView.startActivityForResult(mIntent, type);
    }

    public void ActivityResult(int requestCode, int resultCode, Intent data) {
        if (camera != null)
            camera.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 001) {
            String value = data.getStringExtra(Constance.VALUE);
            if (AppUtils.isEmpty(value))
                return;
            name_tv.setText(value);
        } else if (resultCode == 003) {
            String value = data.getStringExtra(Constance.VALUE);
            if (AppUtils.isEmpty(value))
                return;
            birthday_tv.setText(value);
        } else if (resultCode == 004) {
            String value = data.getStringExtra(Constance.VALUE);
            if (AppUtils.isEmpty(value))
                return;
            phone_tv.setText(value);
        } else if (resultCode == 005) {
            String value = data.getStringExtra(Constance.VALUE);
            if (AppUtils.isEmpty(value))
                return;
            telephone_tv.setText(value);
        } else if (resultCode == 006) {
            String value = data.getStringExtra(Constance.VALUE);
            if (AppUtils.isEmpty(value))
                return;
            email_tv.setText(value);
        }
    }

    @Override
    public void onItemClick(Object o, final int position) {
        if (mSexView.toString().equals(o.toString())) {
            selectSex(position);
        } else if (mHeadView.toString().equals(o.toString())) {
            openImage(position);

        }
    }

    public void openImage(int position) {
        switch (position) {
            case 0:
                if (camera != null)
                    PermissionUtils.requestPermission(mView, PermissionUtils.CODE_CAMERA, new PermissionUtils.PermissionGrant() {
                        @Override
                        public void onPermissionGranted(int requestCode) {
                            camera.onDlgCameraClick();
                        }
                    });

                break;
            case 1:
                if (camera != null)
                    PermissionUtils.requestPermission(mView, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
                        @Override
                        public void onPermissionGranted(int requestCode) {
                            camera.onDlgPhotoClick();
                        }
                    });
                break;
        }
    }

    /**
     * 选择性别
     *
     * @param position
     */
    private void selectSex(int position) {
        switch (position) {
            case 0:
                sex_tv.setText("男");
                sexType = 0;
                break;
            case 1:
                sex_tv.setText("女");
                sexType = 1;
                break;
        }
    }

    /**
     * 选择性别
     */
    public void selectSex() {
        mSexView.show();
    }

    /**
     * 选择生日
     */
    public void selectBirthday() {
        DatePicker picker = new DatePicker(mView);
        picker.setRange(CommonUtil.getYear(), 1920);//年份范围
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override

            public void onDatePicked(String year, String month, String day) {
                birthday_tv.setText(year + "-" + month + "-" + day);
            }

        });
        picker.show();
    }

    /**
     * 修改用户信息
     */
    public void sendUpdateUser() {
        String birthday = birthday_tv.getText().toString();
        String values = "";
        String nickname = name_tv.getText().toString();
        int gender = sexType;
        if (AppUtils.isEmpty(nickname)) {
            MyToast.show(mView, "请输入用户名");
            return;
        }
        if (AppUtils.isEmpty(birthday)) {
            MyToast.show(mView, "请选择出生日期");
            return;
        }
        if (AppUtils.isEmpty(gender)) {
            MyToast.show(mView, "请选择性别");
            return;
        }

        mView.setShowDialog(true);
        mView.setShowDialog("正在保存中..");
        mView.showLoading();

        mNetWork.sendUpdateUser(values, nickname, birthday, gender, this);
    }

    private CameraUtil camera;

    /**
     * 头像
     */
    public void setHead() {
        if (camera == null) {
            camera = new CameraUtil(mView, new CameraUtil.CameraDealListener() {
                @Override
                public void onCameraTakeSuccess(String path) {
                    MyLog.e("onCameraTakeSuccess: " + path);
                    camera.cropImageUri(1, 1, 256);
                }

                @Override
                public void onCameraPickSuccess(String path) {
                    MyLog.e("onCameraPickSuccess: " + path);
                    Uri uri = Uri.parse("file://" + path);
                    camera.cropImageUri(uri, 1, 1, 256);
                }

                @Override
                public void onCameraCutSuccess(final String uri) {
                    File file = new File(uri);
                    head_iv.setImageURI(Uri.fromFile(file));

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String resultJson = NetWorkUtils.uploadFile(head_iv.getDrawable(), NetWorkConst.UPLOADAVATAR, null, uri.toString());
                            //                            //分享的操作
                            mView.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //                                    if(TextUtils.isEmpty(result.getResult())||result.getResult()=="0"){
                                    //                                        return;
                                    //                                    }
                                    //                                    mLodingDailog.dismiss();
                                    //                                    tip("上传成功!");
                                    //                                    mImageIv.setImageResource(R.mipmap.jia);
                                    //                                    mRemarkEt.setText("");
                                    //                                    mProductTypeEt.setText("");
                                }
                            });
                        }
                    }).start();
                }
            });
        }

        mHeadView.show();
    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.UPDATEPROFILE:
                AppDialog.messageBox(UIUtils.getString(R.string.update_save));
                mView.mUI=-1;
                mView.refreshUI();
                sendUser();
                break;
            case NetWorkConst.RESET:
                String token=ans.getString(Constance.TOKEN);
                MyShare.get(mView).putString(Constance.TOKEN, token);//保存TOKEN
                AppDialog.messageBox(UIUtils.getString(R.string.reset_ok));
                mView.mUI=-1;
                mView.refreshUI();
                break;
            case NetWorkConst.PROFILE:
                mUserObject = ans.getJSONObject(Constance.user);
                fillData(mUserObject);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView || mView.isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }

    /**
     * 获取验证码
     */
    public void requestYZM() {
        if(mView.et_tel==null||TextUtils.isEmpty(mView.et_tel.getText())){
            MyToast.show(mView,"请输入手机号码");
            return;
        }
        String mPhone=String.valueOf(mView.et_tel.getText());
        if (HyUtil.isEmpty(mPhone)){
            MyToast.show(mView,"请输入手机号码");
            return;
        }
        if(!CommonUtil.isMobileNO(mPhone)){
            MyToast.show(mView,"请输入正确的手机号码");
            return;
        }
        mView.tv_get_code.start(60);

//        mNetWork.sendRequestYZM(phone, this);
        mNetWork.sendRequestYZM(mPhone, this);

    }

    /**
     * 修改密码
     */
    public void sendUpdatePwd() {
        String mPhone=mView.et_tel.getText().toString();
        String code=mView.et_code.getText().toString();
        String newPwd=mView.et_new.getText().toString();
        String affirmPwd=mView.et_old.getText().toString();

        if (HyUtil.isEmpty(mPhone)){
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_phone));
            return;
        }
        if(!CommonUtil.isMobileNO(mPhone)){
            AppDialog.messageBox(UIUtils.getString(R.string.mobile_assert));
            return;
        }
        if(HyUtil.isEmpty(code)){
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_verification_code));
            return;
        }
        if(HyUtil.isEmpty(newPwd)){
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_pwd));
            return;
        }
        if(HyUtil.isEmpty(affirmPwd)){
            AppDialog.messageBox(UIUtils.getString(R.string.isnull_affirm_pwd));
            return;
        }

        if(!affirmPwd.equals(newPwd)){
            AppDialog.messageBox(UIUtils.getString(R.string.compare_pwd_affirm));
            return;
        }

        //TODO修改密码
        mView.setShowDialog(true);
        mView.setShowDialog("正在重置中..");
        mView.showLoading();
        String pwd=mView.et_new.getText().toString();
        mNetWork.sendUpdatePwd(mPhone, pwd, code, PerfectMydataController.this);
    }
}
