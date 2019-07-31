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
import org.w3c.dom.Text;

import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.ArticlesBean;
import bc.juhaohd.com.bean.Message;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.SpaceController;
import bc.juhaohd.com.ui.activity.ArticleActivity;
import bc.juhaohd.com.ui.activity.HomeShowNewActivity;
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
public class SpaceFragment extends BaseFragment implements View.OnClickListener {

    private TextView tv_keting;
    private TextView tv_ertongfang;
    private TextView tv_canting;
    private TextView tv_weiyujian;
    private TextView tv_louti;
    private TextView tv_quanbu;
    private TextView tv_zoulang;
    private TextView tv_woshi;
    private TextView tv_bieshu;
    private TextView tv_shufang;
    private TextView tv_yangtai;
    public TextView tv_server;
    private SpaceController spaceController;
    private Bitmap bitmap_space_rtf;
    private Bitmap bitmap_kt;
    private Bitmap bit_icon_space_ct;
    private Bitmap bitmap_icon_space_wyj;
    private Bitmap bitmap_icon_space_ws;
    private Bitmap bitmap_icon_space_bs;
    private Bitmap bitmap_icon_space_sf;
    private Bitmap bitmap_icon_space_yt;
    private TextSwitcher textSwitcher_title;
    private List<ArticlesBean> mArticlesBeans;
    private TextView tv_more_news;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_space,null);
    }

    @Override
    protected void initController() {
        spaceController = new SpaceController(this);

    }

    @Override
    protected void initViewData() {

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
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
    protected void initView() {
        tv_keting = (TextView) getView().findViewById(R.id.tv_keting);
        tv_ertongfang = (TextView) getView().findViewById(R.id.tv_ertongfang);
        tv_canting = (TextView) getView().findViewById(R.id.tv_canting);
        tv_weiyujian = (TextView) getView().findViewById(R.id.tv_weiyujian);
        tv_louti = (TextView) getView().findViewById(R.id.tv_louti);
        tv_quanbu = (TextView) getView().findViewById(R.id.tv_quanbu);
        tv_zoulang = (TextView) getView().findViewById(R.id.tv_zoulang);
        tv_woshi = (TextView) getView().findViewById(R.id.tv_woshi);
        tv_bieshu = (TextView) getView().findViewById(R.id.tv_bieshu);
        tv_shufang = (TextView) getView().findViewById(R.id.tv_shufang);
        tv_yangtai = (TextView) getView().findViewById(R.id.tv_yangtai);
        textSwitcher_title = getView().findViewById(R.id.textSwitcher_title);
        tv_more_news = getView().findViewById(R.id.tv_more_news);
        tv_server = getView().findViewById(R.id.tv_server);

        ImageView iv_01=getView().findViewById(R.id.iv_01);
        ImageView iv_02=getView().findViewById(R.id.iv_02);
        ImageView iv_03=getView().findViewById(R.id.iv_03);
        ImageView iv_04=getView().findViewById(R.id.iv_04);
        ImageView iv_05=getView().findViewById(R.id.iv_05);
        ImageView iv_06=getView().findViewById(R.id.iv_06);
        ImageView iv_07=getView().findViewById(R.id.iv_07);
        ImageView iv_08=getView().findViewById(R.id.iv_08);

        bitmap_kt = UIUtils.readBitMap(getActivity(), R.mipmap.icon_space_kt);
        iv_01.setImageBitmap(bitmap_kt);

        bitmap_space_rtf = UIUtils.readBitMap(getActivity(), R.mipmap.icon_space_kt);
        iv_02.setImageBitmap(bitmap_space_rtf);

        bit_icon_space_ct = UIUtils.readBitMap(getActivity(), R.mipmap.icon_space_ct);
        iv_03.setImageBitmap(bit_icon_space_ct);

        bitmap_icon_space_wyj = UIUtils.readBitMap(getActivity(), R.mipmap.icon_space_wyj);
        iv_04.setImageBitmap(bitmap_icon_space_wyj);

        bitmap_icon_space_ws = UIUtils.readBitMap(getActivity(), R.mipmap.icon_space_ws);
        iv_05.setImageBitmap(bitmap_icon_space_ws);

        bitmap_icon_space_bs = UIUtils.readBitMap(getActivity(), R.mipmap.icon_space_bs);
        iv_06.setImageBitmap(bitmap_icon_space_bs);

        bitmap_icon_space_sf = UIUtils.readBitMap(getActivity(), R.mipmap.icon_space_sf);
        iv_07.setImageBitmap(bitmap_icon_space_sf);

        bitmap_icon_space_yt = UIUtils.readBitMap(getActivity(), R.mipmap.icon_space_yt);
        iv_08.setImageBitmap(bitmap_icon_space_yt);


        tv_keting.setOnClickListener(this);
        tv_ertongfang.setOnClickListener(this);
        tv_canting.setOnClickListener(this);
        tv_weiyujian.setOnClickListener(this);
        tv_louti.setOnClickListener(this);
        tv_quanbu.setOnClickListener(this);
        tv_zoulang.setOnClickListener(this);
        tv_woshi.setOnClickListener(this);
        tv_bieshu.setOnClickListener(this);
        tv_shufang.setOnClickListener(this);
        tv_yangtai.setOnClickListener(this);
        tv_more_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ArticleActivity.class));
            }
        });
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
        String filername="全部";
        switch (v.getId()){
            case R.id.tv_keting:
                filername="客厅";
                break;
            case R.id.tv_ertongfang:
                filername="儿童房";
                break;
            case R.id.tv_canting:
                filername="餐厅";
                break;
            case R.id.tv_weiyujian:
                filername="卫浴间";
                break;
            case R.id.tv_louti:
                filername="楼梯";
                break;
            case R.id.tv_quanbu:
                filername="全部";
                break;
            case R.id.tv_zoulang:
                filername="走廊";
                break;
            case R.id.tv_woshi:
                filername="卧室";
                break;
            case R.id.tv_bieshu:
                filername="别墅";
                break;
            case R.id.tv_shufang:
                filername="书房";
                break;
            case R.id.tv_yangtai:
                filername="阳台";
                break;


        }
        intent.putExtra(Constance.filter_attr_name,filername);
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        if(bitmap_kt!=null)bitmap_kt.recycle();
        if(bitmap_space_rtf!=null)bitmap_space_rtf.recycle();
        if(bitmap_icon_space_bs!=null)bitmap_icon_space_bs.recycle();
        if(bitmap_icon_space_sf!=null)bitmap_icon_space_sf.recycle();
        if(bitmap_icon_space_ws!=null)bitmap_icon_space_ws.recycle();
        if(bitmap_icon_space_wyj!=null)bitmap_icon_space_wyj.recycle();
        if(bitmap_icon_space_yt!=null)bitmap_icon_space_yt.recycle();
        if(bit_icon_space_ct!=null)bit_icon_space_ct.recycle();
        bitmap_kt=null;
        bitmap_space_rtf=null;
        bitmap_icon_space_bs=null;
        bitmap_icon_space_sf=null;
        bitmap_icon_space_wyj=null;
        bitmap_icon_space_ws=null;
        bitmap_icon_space_yt=null;
        bit_icon_space_ct=null;
        super.onDestroy();

    }
}
