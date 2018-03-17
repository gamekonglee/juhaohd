package bc.juhaohd.com.ui.activity.programme;

import android.content.Intent;
import android.view.View;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.utils.ImageLoadProxy;
import uk.co.senab.photoview.PhotoView;

/**
 * @author: Jun
 * @date : 2017/3/28 9:40
 * @description :
 */
public class ImageDetailActivity extends BaseActivity {
    private String mImagePath="";
    private PhotoView photo_iv;
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_image_detail);
        photo_iv = (PhotoView)findViewById(R.id.photo_iv);
        ImageLoadProxy.displayImage(mImagePath,photo_iv);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mImagePath=intent.getStringExtra(Constance.photo);
    }

    @Override
    protected void onViewClick(View v) {

    }
}
