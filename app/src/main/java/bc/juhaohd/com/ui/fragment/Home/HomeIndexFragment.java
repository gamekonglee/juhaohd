package bc.juhaohd.com.ui.fragment.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.HomeIndexController;
import bc.juhaohd.com.ui.activity.ArticleActivity;
import bc.juhaohd.com.ui.activity.CartActivity;
import bc.juhaohd.com.ui.activity.HomeShowNewActivity;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.MainActivity;
import bc.juhaohd.com.ui.activity.MainNewActivity;
import bc.juhaohd.com.ui.activity.MainNewForJuHaoActivity;
import bc.juhaohd.com.ui.activity.TaoCanHomeActivity;
import bc.juhaohd.com.ui.activity.TestSwitchActivity;
import bc.juhaohd.com.ui.activity.TimeBuyActivity;
import bc.juhaohd.com.ui.activity.programme.DiyActivity;
import bc.juhaohd.com.ui.activity.programme.MatchHomeActivity;
import bc.juhaohd.com.ui.activity.programme.SelectSceneActivity;
import bc.juhaohd.com.ui.activity.user.LoginActivity;
import bc.juhaohd.com.ui.activity.user.MessageDetailActivity;
import bc.juhaohd.com.ui.activity.user.WebActivity;
import bc.juhaohd.com.ui.view.popwindow.VideoPopWindow;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.UIUtils;
import bocang.utils.AppUtils;
import bocang.utils.IntentUtil;
import bocang.utils.LogUtils;

import static bc.juhaohd.com.ui.activity.IssueApplication.mUserObject;

/**
 * Created by DEMON on 2018/1/18.
 */
public class HomeIndexFragment extends BaseFragment implements View.OnClickListener {

    private TextView tv_juhao;
    private TextView tv_solution;
    private TextView tv_buytime;
    private TextView tv_match;
    private TextView tv_360;
    private TextView tv_cart;
    private TextView tv_audio;
    private ImageView iv_code;
    private HomeIndexController mController;
    public TextView tv_server;
    private TextSwitcher textSwitcher_title;
    private List<ArticlesBean> mArticlesBeans;
    private TextView tv_code;
    private Bitmap bitmap_audio;
    private Bitmap bitmap_juhao;
    private Bitmap bitmap_buytime;
    private Bitmap bitmap_cart;
    private Bitmap bitmap_solution;
    private Bitmap bitmap_match;
    private Bitmap bitmap_360;
    private Bitmap bitmap_code_qr;
    private ImageView iv_audio;
    private ImageView iv_juhao;
    private ImageView iv_buytime;
    private ImageView iv_cart;
    private ImageView iv_solution;
    private ImageView iv_match;
    private ImageView iv_360;
    private ImageView iv_code_qr;
    private TextView tv_cantao;
    private ImageView iv_caotan;
    private Bitmap bitmap_taocan;
    private TextView tv_more_news;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frament_home_index,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void initController() {
        mController = new HomeIndexController(this);
    }

    @Override
    protected void initViewData() {

    }
    @Override
    protected void initView() {
        tv_juhao = (TextView) getView().findViewById(R.id.tv_juhao);
        tv_solution = (TextView) getView().findViewById(R.id.tv_solution);
        tv_buytime = (TextView) getView().findViewById(R.id.tv_buytime);
        tv_match = (TextView) getView().findViewById(R.id.tv_match);
        tv_360 = (TextView) getView().findViewById(R.id.tv_360);
        tv_cart = (TextView) getView().findViewById(R.id.tv_cart);
        tv_audio = (TextView) getView().findViewById(R.id.tv_audio);
        iv_code = (ImageView) getView().findViewById(R.id.iv_code);
        tv_server = getView().findViewById(R.id.tv_server);
        textSwitcher_title = getView().findViewById(R.id.textSwitcher_title);
        tv_cantao = getView().findViewById(R.id.tv_taocan);

        tv_code = getView().findViewById(R.id.tv_code);
        iv_audio = getView().findViewById(R.id.iv_audio);
        iv_juhao = getView().findViewById(R.id.iv_juhao);
        iv_buytime = getView().findViewById(R.id.iv_buytime);
        iv_cart = getView().findViewById(R.id.iv_cart);
        iv_solution = getView().findViewById(R.id.iv_solution);
        iv_match = getView().findViewById(R.id.iv_suiyipei);
        iv_360 = getView().findViewById(R.id.iv_360);
        iv_code_qr = getView().findViewById(R.id.iv_code_qr);
        iv_caotan = getView().findViewById(R.id.iv_taocan);
        tv_more_news = getView().findViewById(R.id.tv_more_news);

        tv_juhao.setOnClickListener(this);
        tv_solution.setOnClickListener(this);
        tv_buytime.setOnClickListener(this);
        tv_match.setOnClickListener(this);
        tv_360.setOnClickListener(this);
        tv_cart.setOnClickListener(this);
        tv_audio.setOnClickListener(this);
        tv_cantao.setOnClickListener(this);
        tv_more_news.setOnClickListener(this);
        bitmap_audio = UIUtils.readBitMap(getActivity(), R.mipmap.icon_audio);
        iv_audio.setImageBitmap(bitmap_audio);

        bitmap_juhao = UIUtils.readBitMap(getActivity(), R.mipmap.icon_news);
        iv_juhao.setImageBitmap(bitmap_juhao);

        bitmap_buytime = UIUtils.readBitMap(getActivity(), R.mipmap.icon_buytime);
        iv_buytime.setImageBitmap(bitmap_buytime);

        bitmap_cart = UIUtils.readBitMap(getActivity(), R.mipmap.icon_cart);
        iv_cart.setImageBitmap(bitmap_cart);

        bitmap_solution = UIUtils.readBitMap(getActivity(), R.mipmap.icon_solution);
        iv_solution.setImageBitmap(bitmap_solution);

        bitmap_match = UIUtils.readBitMap(getActivity(), R.mipmap.icon_suiyipei);
        iv_match.setImageBitmap(bitmap_match);

        bitmap_360 = UIUtils.readBitMap(getActivity(), R.mipmap.icon_360zt);
        iv_360.setImageBitmap(bitmap_360);

        bitmap_code_qr = UIUtils.readBitMap(getActivity(), R.mipmap.icon_code);
        iv_code_qr.setImageBitmap(bitmap_code_qr);

        bitmap_taocan = UIUtils.readBitMap(getActivity(), R.mipmap.icon_taocan);
        iv_caotan.setImageBitmap(bitmap_taocan);
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
//
//    }
    @Subscribe (threadMode = ThreadMode.MAIN, sticky = true)
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
            tv_code.setText("");
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
    public void onClick(View v) {
        String token = MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);
        switch (v.getId()){
        case R.id.tv_juhao:
            startActivity(new Intent(getActivity(), MainNewForJuHaoActivity.class));
//            if (AppUtils.isEmpty(token)) {
//                IntentUtil.startActivity(getActivity(), LoginActivity.class, false);
//            } else {
//
//                IntentUtil.startActivity(getActivity(), MainNewActivity.class, false);
//            }
            break;
        case R.id.tv_solution:
            startActivity(new Intent(getActivity(), MatchHomeActivity.class));
            break;
        case R.id.tv_buytime:
            startActivity(new Intent(getActivity(), TimeBuyActivity.class));
            break;
        case R.id.tv_match:
            if(!isToken()){
            startActivity(new Intent(getActivity(), DiyActivity.class));
            }
            break;
        case R.id.tv_360:
            Intent mIntent = new Intent(getActivity(), WebActivity.class);
            mIntent.putExtra(Constance.url, "http://vr.justeasy.cn/view/273599.html");
            this.startActivity(mIntent);
            break;
        case R.id.tv_cart:
            startActivity(new Intent(getActivity(),CartActivity.class));
            break;
        case R.id.tv_audio:
            ((BaseActivity)getActivity()).countDownTimer.cancel();
            VideoPopWindow popWindow = new VideoPopWindow(getActivity().getBaseContext(), getActivity());
            popWindow.onShow(((HomeShowNewActivity)getActivity()).main_rl);
            break;
            case R.id.tv_taocan:
                startActivity(new Intent(getActivity(), TaoCanHomeActivity.class));
                break;
            case R.id.tv_more_news:
                startActivity(new Intent(getActivity(), ArticleActivity.class));
                break;
    }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(bitmap_audio!=null)bitmap_audio.recycle();
        if(bitmap_cart!=null)bitmap_cart.recycle();
        if(bitmap_match!=null)bitmap_match.recycle();
        if(bitmap_360!=null)bitmap_360.recycle();
        if(bitmap_buytime!=null)bitmap_buytime.recycle();
        if(bitmap_code_qr!=null)bitmap_code_qr.recycle();
        if(bitmap_juhao!=null)bitmap_juhao.recycle();
        if(bitmap_solution!=null)bitmap_solution.recycle();
        if(bitmap_taocan!=null)bitmap_taocan.recycle();
        bitmap_audio=null;
        bitmap_cart=null;
        bitmap_match=null;
        bitmap_360=null;
        bitmap_buytime=null;
        bitmap_code_qr=null;
        bitmap_juhao=null;
        bitmap_solution=null;
        bitmap_taocan=null;
        EventBus.getDefault().unregister(this);
    }
}
