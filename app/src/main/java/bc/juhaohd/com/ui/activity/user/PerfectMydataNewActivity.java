package bc.juhaohd.com.ui.activity.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.controller.user.PerfectMydataController;
import bc.juhaohd.com.ui.view.ShowDialog;
import bc.juhaohd.com.ui.view.TimerButton;
import bocang.utils.IntentUtil;
import bocang.utils.PermissionUtils;

/**
 * @author: Jun
 * @date : 2017/1/21 15:40
 * @description :完善个人资料
 */
public class PerfectMydataNewActivity extends BaseActivity implements View.OnClickListener {
    public RelativeLayout head_rl, name_rl, sex_rl, birthday_rl, phone_rl, telephone_rl, email_rl, update_password_rl,save_rl;
    public PerfectMydataController mController;
    public ImageView topLeftBtn;
    public ImageView iv_head;
    public Button btn_save_head;
    public LinearLayout mydata_section_username;
    public LinearLayout mydata_section_head;
    public LinearLayout mydata_section_sex;
    public LinearLayout mydata_section_password;
    public int mUI;
    public EditText et_username;
    public Button btn_save_username;
    public RadioButton tv_man;
    public RadioButton tv_women;
    public Button btn_save_sex;
    public EditText et_old;
    public EditText et_new;
    public Button btn_save_password;
    public EditText et_tel;
    public TimerButton tv_get_code;
    public EditText et_code;
    private RadioGroup rg_sex;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new PerfectMydataController(this);
        rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.tv_man:
                        mController.sexType=0;
                        break;
                    case R.id.tv_women:
                        mController.sexType=1;
                        break;
                }
            }
        });

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_perfect_mydata_new);
        head_rl = (RelativeLayout) findViewById(R.id.head_rl);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        name_rl = (RelativeLayout) findViewById(R.id.name_rl);
        sex_rl = (RelativeLayout) findViewById(R.id.sex_rl);
        birthday_rl = (RelativeLayout) findViewById(R.id.birthday_rl);
        phone_rl = (RelativeLayout) findViewById(R.id.phone_rl);
        telephone_rl = (RelativeLayout) findViewById(R.id.telephone_rl);
        email_rl = (RelativeLayout) findViewById(R.id.email_rl);
        update_password_rl = (RelativeLayout) findViewById(R.id.update_password_rl);
        save_rl = (RelativeLayout) findViewById(R.id.save_rl);
        btn_save_head = (Button) findViewById(R.id.btn_save_head);
        mydata_section_head = (LinearLayout) findViewById(R.id.mydata_section_head);
        mydata_section_username = (LinearLayout) findViewById(R.id.mydata_section_username);
        mydata_section_sex = (LinearLayout) findViewById(R.id.mydata_section_sex);
        mydata_section_password = (LinearLayout) findViewById(R.id.mydata_section_password);
        et_username = (EditText) findViewById(R.id.et_username);
        btn_save_username = (Button) findViewById(R.id.btn_save_username);
        tv_man = (RadioButton) findViewById(R.id.tv_man);
        tv_women = (RadioButton) findViewById(R.id.tv_man);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        btn_save_sex = (Button) findViewById(R.id.btn_save_sex);
        et_old = (EditText) findViewById(R.id.et_old);
        et_new = (EditText) findViewById(R.id.et_new);
        btn_save_password = (Button) findViewById(R.id.btn_save_password);
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_code = (EditText) findViewById(R.id.et_code);
        tv_get_code = (TimerButton) findViewById(R.id.tv_get_code);
        tv_get_code.setOnClickListener(this);
        mUI = -1;
        head_rl.setOnClickListener(this);
        iv_head.setOnClickListener(this);
        name_rl.setOnClickListener(this);
        birthday_rl.setOnClickListener(this);
        phone_rl.setOnClickListener(this);
        telephone_rl.setOnClickListener(this);
        email_rl.setOnClickListener(this);
        update_password_rl.setOnClickListener(this);
        sex_rl.setOnClickListener(this);
        save_rl.setOnClickListener(this);
        btn_save_head.setOnClickListener(this);
        btn_save_username.setOnClickListener(this);
        btn_save_sex.setOnClickListener(this);
        btn_save_password.setOnClickListener(this);

//        topLeftBtn.setOnClickListener(this);

    }


    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_rl:
                mUI=0;
                refreshUI();
                break;
            case R.id.iv_head:
                mController.setHead();
                break;
            case R.id.name_rl:
                mUI=1;
                refreshUI();
//                mController.setIntent("用户名", 001);
                break;
            case R.id.sex_rl:
                mUI=2;
                refreshUI();
//                mController.selectSex();
                break;
            case R.id.birthday_rl:
//                mController.selectBirthday();
                break;
            case R.id.phone_rl:
                mController.setIntent("手机号", 004);
                break;
            case R.id.telephone_rl:
                mController.setIntent("固定电话", 005);
                break;
            case R.id.email_rl:
                mController.setIntent("电子邮箱", 006);
                break;
            case R.id.update_password_rl:
                mUI=3;
                refreshUI();
//                IntentUtil.startActivity(this, UpdatePasswordActivity.class, false);
                break;
            case R.id.save_rl:
                mController.sendUpdateUser();
            case R.id.btn_save_head:
                mUI=-1;
                refreshUI();
                break;
            case R.id.btn_save_sex:
                if(tv_man.isChecked()){
                    mController.sexType=0;
                }else {
                    mController.sexType=1;
                }
                mUI=-1;
                refreshUI();
                mController.sendUpdateUser();
                break;
            case R.id.btn_save_username:
                String name=et_username.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(PerfectMydataNewActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                mController.name_tv.setText(name);
                mUI=-1;
                refreshUI();
                mController.sendUpdateUser();
                break;
            case R.id.btn_save_password:
                mController.sendUpdatePwd();
                break;
            case R.id.tv_get_code:
                mController.requestYZM();
                break;
        }
    }

    public void refreshUI() {
        switch (mUI){
            case -1:   mydata_section_head.setVisibility(View.GONE);
                mydata_section_username.setVisibility(View.GONE);
                mydata_section_sex.setVisibility(View.GONE);
                mydata_section_password.setVisibility(View.GONE);
                break;
            case 0:
                mydata_section_head.setVisibility(View.VISIBLE);
                mydata_section_username.setVisibility(View.GONE);
                mydata_section_sex.setVisibility(View.GONE);
                mydata_section_password.setVisibility(View.GONE);
                break;
            case 1:
                mydata_section_head.setVisibility(View.GONE);
                mydata_section_username.setVisibility(View.VISIBLE);
                mydata_section_sex.setVisibility(View.GONE);
                mydata_section_password.setVisibility(View.GONE);
                break;
            case 2:
                mydata_section_head.setVisibility(View.GONE);
                mydata_section_username.setVisibility(View.GONE);
                mydata_section_sex.setVisibility(View.VISIBLE);
                mydata_section_password.setVisibility(View.GONE);
                break;
            case 3:
                mydata_section_head.setVisibility(View.GONE);
                mydata_section_username.setVisibility(View.GONE);
                mydata_section_sex.setVisibility(View.GONE);
                mydata_section_password.setVisibility(View.VISIBLE);
                break;

        }
    }

    public void goBack(View v){
        ShowDialog mDialog=new ShowDialog();
        mDialog.show(this, "提示", "你是否放弃个人信息编辑?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                finish();
            }

            @Override
            public void negtive() {

            }
        });
    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mController.ActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
                mController.setHead();
            }
        });
    }


}
