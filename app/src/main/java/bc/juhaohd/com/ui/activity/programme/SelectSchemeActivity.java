package bc.juhaohd.com.ui.activity.programme;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.controller.programme.SelectSchemeController;

/**
 * @author: Jun
 * @date : 2017/3/14 11:01
 * @description :选择方案类型
 */
public class SelectSchemeActivity extends BaseActivity {
    private SelectSchemeController mController;
    private RelativeLayout save_rl;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        mController=new SelectSchemeController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scheme_type);
        save_rl=getViewAndClick(R.id.save_rl);
    }

    @Override
    protected void initData() {

    }
    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideLoading();
            mController.saveScheme();
        }
    };
    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.save_rl:
                showLoading();
                InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                // 强制隐藏软键盘
                imm.hideSoftInputFromWindow(mController.title_tv.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mController.remark_tv.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(1500);
                            handler.sendEmptyMessage(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        super.run();
                    }
                }.start();
            break;
        }
    }
}
