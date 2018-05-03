package bc.juhaohd.com.ui.fragment.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.baiiu.filter.util.UIUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.ArticlesBean;
import bc.juhaohd.com.bean.Message;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.TypeController;
import bc.juhaohd.com.ui.activity.HomeShowNewActivity;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.MainNewActivity;
import bc.juhaohd.com.ui.activity.user.MessageDetailActivity;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.UIUtils;
import bocang.utils.AppUtils;

import static bc.juhaohd.com.ui.activity.IssueApplication.mUserObject;

/**
 * Created by DEMON on 2018/1/18.
 */
public class TypeFragment extends BaseFragment implements View.OnClickListener {

    private TextView tv_diaodeng;
    private TextView tv_taideng;
    private TextView tv_bideng;
    private TextView tv_luodideng;
    private TextView tv_xidingdeng;
    private TextView tv_shangyezhaoming;
    private TextView tv_quanbu;
    public TextView tv_server;
    private TypeController typeController;
    private ImageView iv_diaodeng;
    private ImageView iv_taideng;
    private ImageView iv_bideng;
    private ImageView iv_shangye;
    private ImageView iv_quanbu;
    private ImageView iv_luodideng;
    private ImageView iv_xidingdeng;
    private Bitmap bitmap_diaodeng;
    private Bitmap bitmap_taideng;
    private Bitmap bitmap_bideng;
    private Bitmap bitmap_shangye;
    private Bitmap bitmap_quanbu;
    private Bitmap bitmap_luodideng;
    private Bitmap bitmap_xidingdeng;
    private TextSwitcher textSwitcher_title;
    private List<ArticlesBean> mArticlesBeans;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_type,null);
    }

    @Override
    protected void initController() {
        typeController = new TypeController(this);

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
        tv_diaodeng = (TextView) getView().findViewById(R.id.tv_diaodeng);
        tv_taideng = (TextView) getView().findViewById(R.id.tv_taideng);
        tv_bideng = (TextView) getView().findViewById(R.id.tv_bideng);
        tv_luodideng = (TextView) getView().findViewById(R.id.tv_luodideng);
        tv_xidingdeng = (TextView) getView().findViewById(R.id.tv_xidingdeng);
        tv_shangyezhaoming = (TextView) getView().findViewById(R.id.tv_shangyezhaoming);
        tv_quanbu = (TextView) getView().findViewById(R.id.tv_quanbu);
        tv_server = getView().findViewById(R.id.tv_server);
        iv_diaodeng = getView().findViewById(R.id.iv_diaodeng);
        iv_taideng = getView().findViewById(R.id.iv_taideng);
        iv_bideng = getView().findViewById(R.id.iv_bideng);
        iv_shangye = getView().findViewById(R.id.iv_shangye);
        iv_quanbu = getView().findViewById(R.id.iv_quanbu);
        iv_luodideng = getView().findViewById(R.id.iv_luodideng);
        iv_xidingdeng = getView().findViewById(R.id.iv_xidingdeng);
        textSwitcher_title = getView().findViewById(R.id.textSwitcher_title);

        if(bitmap_diaodeng==null)bitmap_diaodeng = UIUtils.readBitMap(getActivity(), R.mipmap.icon_type_dd);
        iv_diaodeng.setImageBitmap(bitmap_diaodeng);

        if(bitmap_taideng==null)bitmap_taideng = UIUtils.readBitMap(getActivity(), R.mipmap.icon_type_td);
        iv_taideng.setImageBitmap(bitmap_taideng);

        if(bitmap_bideng==null)bitmap_bideng = UIUtils.readBitMap(getActivity(), R.mipmap.icon_type_bd);
        iv_bideng.setImageBitmap(bitmap_bideng);

        if(bitmap_shangye==null)bitmap_shangye = UIUtils.readBitMap(getActivity(), R.mipmap.icon_type_syzm);
        iv_shangye.setImageBitmap(bitmap_shangye);

        if(bitmap_quanbu==null)bitmap_quanbu = UIUtils.readBitMap(getActivity(), R.mipmap.icon_type_qb);
        iv_quanbu.setImageBitmap(bitmap_quanbu);

        if(bitmap_luodideng==null)bitmap_luodideng = UIUtils.readBitMap(getActivity(), R.mipmap.icon_type_ldd);
        iv_luodideng.setImageBitmap(bitmap_luodideng);

        if(bitmap_xidingdeng==null)bitmap_xidingdeng = UIUtils.readBitMap(getActivity(), R.mipmap.icon_type_xdd);
        iv_xidingdeng.setImageBitmap(bitmap_xidingdeng);



        tv_diaodeng.setOnClickListener(this);
        tv_taideng.setOnClickListener(this);
        tv_bideng.setOnClickListener(this);
        tv_luodideng.setOnClickListener(this);
        tv_xidingdeng.setOnClickListener(this);
        tv_shangyezhaoming.setOnClickListener(this);
        tv_quanbu.setOnClickListener(this);
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
    protected void initData() {

    }
//    @Override
//    public void onStart() {
//        super.onStart();
//
//
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
        String filterType="全部";
        switch (v.getId()){
            case  R.id.tv_diaodeng:
                filterType="吊灯";
                break;
            case R.id.tv_taideng:
                filterType="台灯";
                break;
            case R.id.tv_bideng:
                filterType="壁灯";
                break;
            case R.id.tv_luodideng:
                filterType="落地灯";
                break;
            case R.id.tv_xidingdeng:
                filterType="吸顶灯";
                break;
            case R.id.tv_shangyezhaoming:
                filterType="商业照明";
                break;
            case R.id.tv_quanbu:
                filterType="全部";
                break;
        }
        intent.putExtra(Constance.filter_attr_name,filterType);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(bitmap_xidingdeng!=null)bitmap_xidingdeng.recycle();
        if(bitmap_diaodeng!=null)bitmap_diaodeng.recycle();
        if(bitmap_luodideng!=null)bitmap_luodideng.recycle();
        if(bitmap_quanbu!=null)bitmap_quanbu.recycle();
        if(bitmap_shangye!=null)bitmap_shangye.recycle();
        if(bitmap_bideng!=null)bitmap_bideng.recycle();
        if(bitmap_taideng!=null)bitmap_taideng.recycle();
        bitmap_xidingdeng=null;
        bitmap_diaodeng=null;
        bitmap_luodideng=null;
        bitmap_quanbu=null;
        bitmap_shangye=null;
        bitmap_bideng=null;
        bitmap_taideng=null;
    }
}
