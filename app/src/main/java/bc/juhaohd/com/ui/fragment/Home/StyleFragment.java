package bc.juhaohd.com.ui.fragment.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.ArticlesBean;
import bc.juhaohd.com.bean.Message;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.StyleController;
import bc.juhaohd.com.ui.activity.ArticleActivity;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.MainNewActivity;
import bc.juhaohd.com.ui.activity.user.MessageDetailActivity;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.UIUtils;
import bocang.utils.AppUtils;

import static bc.juhaohd.com.ui.activity.IssueApplication.mUserObject;

/**
 * Created by DEMON on 2018/1/19.
 */
public class StyleFragment extends BaseFragment implements View.OnClickListener {

    private TextView tv_xiandaijianyue;
    private TextView tv_oushi;
    private TextView tv_zhongshi;
    private TextView tv_xinzhongshi;
    private TextView tv_quanbu;
    private TextView tv_meishi;
    private TextView tv_yijia;
    private TextView tv_houxiandai;
    private TextView tv_tianyuan;
    public TextView tv_server;
    private StyleController styleController;
    private Bitmap icon_style_xdjy;
    private Bitmap icon_style_os;
    private Bitmap icon_style_zs;
    private Bitmap icon_style_xzs;
    private Bitmap icon_style_ms;
    private Bitmap icon_style_yj;
    private Bitmap icon_style_hxd;
    private Bitmap icon_style_ty;
    private Bitmap icon_style_qb;
    private TextSwitcher textSwitcher_title;
    private List<ArticlesBean> mArticlesBeans;
    private TextView tv_more_news;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_style,null);
    }

    @Override
    protected void initController() {
        styleController = new StyleController(this);
    }

    @Override
    protected void initViewData() {

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    @Override
    protected void initView() {
        tv_xiandaijianyue = (TextView) getView().findViewById(R.id.tv_xiandaijianyue);
        tv_oushi = (TextView) getView().findViewById(R.id.tv_oushi);
        tv_zhongshi = (TextView) getView().findViewById(R.id.tv_zhongshi);
        tv_xinzhongshi = (TextView) getView().findViewById(R.id.tv_xinzhongshi);
        tv_quanbu = (TextView) getView().findViewById(R.id.tv_quanbu);
        tv_meishi = (TextView) getView().findViewById(R.id.tv_meishi);
        tv_yijia = (TextView) getView().findViewById(R.id.tv_yijia);
        tv_houxiandai = (TextView) getView().findViewById(R.id.tv_houxiandai);
        tv_tianyuan = (TextView) getView().findViewById(R.id.tv_tianyuan);
        tv_server = getView().findViewById(R.id.tv_server);
        tv_more_news = getView().findViewById(R.id.tv_more_news);
        ImageView iv_01=getView().findViewById(R.id.iv_01);
        ImageView iv_02=getView().findViewById(R.id.iv_02);
        ImageView iv_03=getView().findViewById(R.id.iv_03);
        ImageView iv_04=getView().findViewById(R.id.iv_04);
        ImageView iv_05=getView().findViewById(R.id.iv_05);
        ImageView iv_06=getView().findViewById(R.id.iv_06);
        ImageView iv_07=getView().findViewById(R.id.iv_07);
        ImageView iv_08=getView().findViewById(R.id.iv_08);
        ImageView iv_09=getView().findViewById(R.id.iv_09);
        textSwitcher_title = getView().findViewById(R.id.textSwitcher_title);

        icon_style_xdjy = UIUtils.readBitMap(getActivity(), R.mipmap.icon_style_xdjy);
        iv_01.setImageBitmap(icon_style_xdjy);

        icon_style_os = UIUtils.readBitMap(getActivity(), R.mipmap.icon_style_os);
        iv_02.setImageBitmap(icon_style_os);

        icon_style_zs = UIUtils.readBitMap(getActivity(), R.mipmap.icon_style_zs);
        iv_03.setImageBitmap(icon_style_zs);

        icon_style_xzs = UIUtils.readBitMap(getActivity(), R.mipmap.icon_style_xzs);
        iv_04.setImageBitmap(icon_style_xzs);

        icon_style_ms = UIUtils.readBitMap(getActivity(), R.mipmap.icon_style_ms);
        iv_05.setImageBitmap(icon_style_ms);

        icon_style_yj = UIUtils.readBitMap(getActivity(), R.mipmap.icon_style_yj);
        iv_06.setImageBitmap(icon_style_yj);

        icon_style_hxd = UIUtils.readBitMap(getActivity(), R.mipmap.icon_style_hxd);
        iv_07.setImageBitmap(icon_style_hxd);

        icon_style_ty = UIUtils.readBitMap(getActivity(), R.mipmap.icon_style_ty);
        iv_08.setImageBitmap(icon_style_ty);

        icon_style_qb = UIUtils.readBitMap(getActivity(), R.mipmap.icon_style_qb);
        iv_09.setImageBitmap(icon_style_qb);

        tv_xiandaijianyue.setOnClickListener(this);
        tv_oushi.setOnClickListener(this);
        tv_zhongshi.setOnClickListener(this);
        tv_xinzhongshi.setOnClickListener(this);
        tv_quanbu.setOnClickListener(this);
        tv_meishi.setOnClickListener(this);
        tv_yijia.setOnClickListener(this);
        tv_houxiandai.setOnClickListener(this);
        tv_tianyuan.setOnClickListener(this);
        tv_more_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ArticleActivity.class));
            }
        });

    }

    private int mNewsPoistion = 0;
    private int curStr;
    /**
     * 滚动新闻
     */
    private void getNews() {
        try {

            textSwitcher_title.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    final TextView tv = new TextView(getActivity());
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    tv.setGravity(Gravity.CENTER_VERTICAL);
                    tv.setTextColor(Color.WHITE);
                    tv.setLines(1);
                    tv.setEllipsize(TextUtils.TruncateAt.END);
                    tv.setLayoutParams(new FrameLayout.LayoutParams(UIUtils.dip2PX(360), FrameLayout.LayoutParams.WRAP_CONTENT));
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = mArticlesBeans.get(mNewsPoistion).getUrl();
                            Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                            intent.putExtra(Constance.url, url);
                            startActivity(intent);
                        }
                    });
                    return tv;
                }
            });

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNewsPoistion = curStr++ % mArticlesBeans.size();
                    textSwitcher_title.setText(mArticlesBeans.get(mNewsPoistion).getTitle());
                    handler.postDelayed(this, 5000);
                }
            }, 1000);
        } catch (Exception e) {

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventA(Message msg) {
//        LogUtils.logE("event bus",msg.getMsg()+"");
//        startActivity(new Intent(getActivity(),TestSwitchActivity.class));
        mArticlesBeans = IssueApplication.articlesBeans;
        if(mArticlesBeans ==null|| mArticlesBeans.size()==0){
            return;
        }
        getNews();
        if(mUserObject==null||mUserObject.getInt(Constance.level)==2)
        {
            tv_server.setText("");
            return;
        }
        if (!AppUtils.isEmpty(mUserObject.getString("parent_id"))) {
            if (mUserObject.getInt("parent_id") == 0) {
                MyShare.get(getActivity()).putInt(Constance.USERCODEID, mUserObject.getInt("id"));
            } else {
                MyShare.get(getActivity()).putInt(Constance.USERCODEID, mUserObject.getInt("parent_id"));
            }

        }
        if (!AppUtils.isEmpty(mUserObject.getString("parent_name"))) {
            MyShare.get(getActivity()).putString(Constance.USERCODE, mUserObject.getString("parent_name"));
        } else {
            MyShare.get(getActivity()).putString(Constance.USERCODE, mUserObject.getString("nickname"));
        }

        String user_name = MyShare.get(getActivity()).getString(Constance.USERCODE);
        String name = mUserObject.getString(Constance.username);
        if (AppUtils.isEmpty(user_name)) {

            tv_server.setText(name);
        } else {

            tv_server.setText(user_name);
        }
    /* Do something */
    }
    @Override
    public void onDestroy() {
        if(icon_style_xdjy!=null)icon_style_xdjy.recycle();
        if(icon_style_os!=null)icon_style_os.recycle();
        if(icon_style_zs!=null)icon_style_zs.recycle();
        if(icon_style_xzs!=null)icon_style_xzs.recycle();
        if(icon_style_ms!=null)icon_style_ms.recycle();
        if(icon_style_yj!=null)icon_style_yj.recycle();
        if(icon_style_hxd!=null)icon_style_hxd.recycle();
        if(icon_style_ty!=null)icon_style_ty.recycle();
        if(icon_style_qb!=null)icon_style_qb.recycle();
        icon_style_xdjy=null;
        icon_style_os=null;
        icon_style_zs=null;
        icon_style_xzs=null;
        icon_style_ms=null;
        icon_style_yj=null;
        icon_style_hxd=null;
        icon_style_ty=null;
        icon_style_qb=null;

        super.onDestroy();
    }

    @Override
    protected void initData() {

    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        if(mUserObject==null)
//        {
//            tv_server.setText("");
//            return;
//        }
//        if (!AppUtils.isEmpty(mUserObject.getString("parent_id"))) {
//            if (mUserObject.getInt("parent_id") == 0) {
//                MyShare.get(getActivity()).putInt(Constance.USERCODEID, mUserObject.getInt("id"));
//            } else {
//                MyShare.get(getActivity()).putInt(Constance.USERCODEID, mUserObject.getInt("parent_id"));
//            }
//
//        }
//        if (!AppUtils.isEmpty(mUserObject.getString("parent_name"))) {
//            MyShare.get(getActivity()).putString(Constance.USERCODE, mUserObject.getString("parent_name"));
//        } else {
//            MyShare.get(getActivity()).putString(Constance.USERCODE, mUserObject.getString("nickname"));
//        }
//
//        String user_name = MyShare.get(getActivity()).getString(Constance.USERCODE);
//        String name = mUserObject.getString(Constance.username);
//        if (AppUtils.isEmpty(user_name)) {
//
//            tv_server.setText(name);
//        } else {
//
//            tv_server.setText(user_name);
//        }
//    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(), MainNewActivity.class);
        String filterType="";
        switch (v.getId()){
            case R.id.tv_xiandaijianyue:
                filterType="现代简约";
                break;
            case R.id.tv_oushi:
                filterType="欧式";
                break;
            case R.id.tv_zhongshi:
                filterType="中式";
                break;
            case R.id.tv_xinzhongshi:
                filterType="新中式";
                break;
            case R.id.tv_quanbu:
                filterType="全部";
                break;
            case R.id.tv_meishi:
                filterType="美式";
                break;
            case R.id.tv_yijia:
                filterType="宜家";
                break;
            case R.id.tv_houxiandai:
                filterType="轻奢";
                break;
            case R.id.tv_tianyuan:
                filterType="田园";
                break;
            case R.id.tv_more_news:
                startActivity(new Intent(getActivity(), ArticleActivity.class));
                break;
        }
        intent.putExtra(Constance.filter_attr_name,filterType);
        startActivity(intent);

    }

}
