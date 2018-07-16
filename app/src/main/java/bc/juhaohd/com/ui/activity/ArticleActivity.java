package bc.juhaohd.com.ui.activity;

import android.view.View;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.controller.ArticleController;

/**
 * Created by bocang on 18-7-5.
 */

public class ArticleActivity extends BaseActivity {

    private ArticleController articleController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
        articleController = new ArticleController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_article);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
