package bc.juhaohd.com.controller;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.adapter.BaseAdapterHelper;
import bc.juhaohd.com.adapter.QuickAdapter;
import bc.juhaohd.com.bean.ArticlesBean;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.ArticleActivity;
import bc.juhaohd.com.ui.activity.user.MessageDetailActivity;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

/**
 * Created by bocang on 18-7-5.
 */

public class ArticleController extends BaseController implements PullToRefreshLayout.OnRefreshListener {

    private final ArticleActivity mView;
    private GridViewForScrollView listview;
    private PullToRefreshLayout mPullToRefreshLayout;
    private QuickAdapter adapter;
    private  List<ArticlesBean> mArticlesBeans;
    private int page;
    private int pagePer;

    public ArticleController(ArticleActivity articleActivity) {
        mView = articleActivity;
        initUI();
        mArticlesBeans = new ArrayList<>();
        initData();
    }

    private void initUI() {

        listview = mView.findViewById(R.id.listview);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        adapter = new QuickAdapter<ArticlesBean>(mView, R.layout.item_article){
            @Override
            protected void convert(BaseAdapterHelper helper, ArticlesBean item) {
                helper.setText(R.id.title,item.getTitle());
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = mArticlesBeans.get(i).getUrl();
                Intent intent = new Intent(mView, MessageDetailActivity.class);
                intent.putExtra(Constance.url, url);
                mView.startActivity(intent);
            }
        });
    }
    private void initData() {
        page = 1;
        pagePer = 20;
        sendArticle();
    }
    public void sendArticle(){
//        LogUtils.logE("art:",page+"");
        mNetWork.sendArticle(page, 20, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                JSONArray mArticlesArray = ans.getJSONArray(Constance.articles);

                if (1 == page){
                    mArticlesBeans=new ArrayList<>();
                    for(int i=0;i<mArticlesArray.length();i++){
                        JSONObject jsonObject = mArticlesArray.getJSONObject(i);
//                        if (jsonObject.getInt(Constance.article_type) == 1) {
                            mArticlesBeans.add(new Gson().fromJson(String.valueOf(mArticlesArray.getJSONObject(i)), ArticlesBean.class));
//                        }
                    }
                }
                else if (null != mArticlesBeans) {
                    for (int i = 0; i < mArticlesArray.length(); i++) {
                        JSONObject jsonObject = mArticlesArray.getJSONObject(i);
//                        if (jsonObject.getInt(Constance.article_type) == 1) {
                            mArticlesBeans.add(new Gson().fromJson(String.valueOf(mArticlesArray.getJSONObject(i)), ArticlesBean.class));
//                        }
                    }

                    if (AppUtils.isEmpty(mArticlesArray))
                        MyToast.show(mView, "没有更多内容了");
                }

                adapter.replaceAll(mArticlesBeans);
                adapter.notifyDataSetChanged();
                if (mArticlesBeans.size() == 0)
                    return;

            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
            }
        });
    }
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page=1;
        sendArticle();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        sendArticle();
    }
    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }
    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

}
