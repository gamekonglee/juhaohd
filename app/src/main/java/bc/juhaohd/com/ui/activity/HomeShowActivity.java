package bc.juhaohd.com.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.HomeShowController;
import bc.juhaohd.com.ui.activity.user.LoginActivity;
import bc.juhaohd.com.ui.activity.user.WebActivity;
import bc.juhaohd.com.ui.view.popwindow.VideoPopWindow;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.UIUtils;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.IntentUtil;
import bocang.utils.UniversalUtil;

import static bc.juhaohd.com.R.id.convenientBanner;

/**
 * @author: Jun
 * @date : 2017/9/28 10:20
 * @description :
 */
public class HomeShowActivity extends BaseActivity {
    private RelativeLayout main_rl;
    private ImageView shangcheng_iv, shipin_iv, guanwang_iv, zhanting360_iv;
    private HomeShowController mController;
    public TextView two_code_tv, operator_tv, address_tv, tel_tv;
    private ConvenientBanner mConvenientBanner;
    private Intent mIntent;
    private List<String> mbannerPath;

    @Override
    protected void InitDataView() {
        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, mbannerPath);
        mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});
        String localVersion = CommonUtil.localVersionName(this);
    }

    @Override
    protected void initController() {
        mController = new HomeShowController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_home);
        main_rl = (RelativeLayout) findViewById(R.id.main_rl);
        shangcheng_iv = getViewAndClick(R.id.shangcheng_iv);
        shipin_iv = getViewAndClick(R.id.shipin_iv);
        guanwang_iv = getViewAndClick(R.id.guanwang_iv);
        zhanting360_iv = getViewAndClick(R.id.zhanting360_iv);
        two_code_tv = (TextView) findViewById(R.id.two_code_tv);
        operator_tv = (TextView) findViewById(R.id.operator_tv);
        address_tv = (TextView) findViewById(R.id.address_tv);
        tel_tv = (TextView) findViewById(R.id.tel_tv);
        mConvenientBanner = (ConvenientBanner) findViewById(convenientBanner);
        ActivityCompat.requestPermissions(this,
                new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void initData() {
        mbannerPath = new ArrayList<>();
        mbannerPath.add("http://www.juhao.com/data/afficheimg/20170104wlavqe.jpg");
        mbannerPath.add("http://www.juhao.com/data/afficheimg/20170104ticsev.jpg");
        mbannerPath.add("http://www.juhao.com/data/afficheimg/20170104strofs.jpg");
        mbannerPath.add("http://www.juhao.com/data/afficheimg/20170104pxtpto.jpg");
        mbannerPath.add("http://www.juhao.com/data/afficheimg/20170104gyearn.jpg");
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开始自动翻页
        mConvenientBanner.startTurning(UniversalUtil.randomA2B(3000, 5000));
    }

    @Override
    public void onPause() {
        super.onPause();
        // 停止翻页
        mConvenientBanner.stopTurning();
    }


    @Override
    protected void onViewClick(View v) {
        String token = MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);
        switch (v.getId()) {
            case R.id.shipin_iv://视频
                VideoPopWindow popWindow = new VideoPopWindow(this.getBaseContext(), this);
                popWindow.onShow(main_rl);
                break;
            case R.id.shangcheng_iv://钜豪商城
                if (AppUtils.isEmpty(token)) {
                    IntentUtil.startActivity(HomeShowActivity.this, LoginActivity.class, true);
                } else {

                    IntentUtil.startActivity(HomeShowActivity.this, MainActivity.class, true);
                }
                break;
            case R.id.guanwang_iv://官网
                mIntent = new Intent(this, WebActivity.class);
                mIntent.putExtra(Constance.url, "http://guanwang.juhao.com/");
                mIntent.putExtra(Constance.PC, true);
                this.startActivity(mIntent);
                break;
            case R.id.zhanting360_iv://展厅
                mIntent = new Intent(this, WebActivity.class);
                mIntent.putExtra(Constance.url, "http://vr.justeasy.cn/view/273599.html");
                this.startActivity(mIntent);
                break;

        }

    }

    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            imageView.setImageResource(R.drawable.bg_default);
            ImageLoader.getInstance().displayImage(data, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
