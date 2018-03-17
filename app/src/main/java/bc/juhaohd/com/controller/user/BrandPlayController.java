package bc.juhaohd.com.controller.user;

import android.os.Message;

import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.ui.activity.user.BrandPlayActivity;


/**
 * @author: Jun
 * @date : 2017/5/9 10:30
 * @description :公司视频
 */
public class BrandPlayController extends BaseController {
    private BrandPlayActivity mView;



    public BrandPlayController(BrandPlayActivity v){
        mView=v;
        initView();
        initViewData();

    }




    private void initViewData() {
    }

    private void initView() {

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
}
