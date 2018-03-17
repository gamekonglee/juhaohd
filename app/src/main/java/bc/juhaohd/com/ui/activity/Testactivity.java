package bc.juhaohd.com.ui.activity;

import android.view.View;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.ui.view.PullUpToLoadMore;

/**
 * @author: Jun
 * @date : 2017/3/24 11:37
 * @description :
 */
public class Testactivity extends BaseActivity {
    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.fm_product_introduce);
        final PullUpToLoadMore ptlm= (PullUpToLoadMore) findViewById(R.id.ptlm);
        ptlm.setmListener(new PullUpToLoadMore.ScrollListener() {
            @Override
            public void onScrollToBottom(int currPosition) {
//                if(currPosition==0){
//                    title_tv.setText("标题");
//                }else{
//                    title_tv.setText("商品详情");
//                }
            }
        });
//        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ptlm.scrollToTop();
//            }
//        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
