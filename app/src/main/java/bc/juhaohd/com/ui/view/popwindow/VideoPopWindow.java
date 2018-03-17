package bc.juhaohd.com.ui.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.user.BrandPlayActivity;
import bc.juhaohd.com.utils.Network;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class VideoPopWindow extends BasePopwindown implements View.OnClickListener {
    private Activity mActivity;
    private RelativeLayout main_rl;
    private ImageView xingxiang_iv,juhaozhige_iv,gongcheng_iv,xinmoshi_iv;
    private Intent mIntent;

    public VideoPopWindow(Context context, Activity view) {
        super(context);
        mActivity=view;

    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_video, null);
        main_rl = (RelativeLayout) contentView.findViewById(R.id.main_rl);
        main_rl.setOnClickListener(this);
        xingxiang_iv = (ImageView) contentView.findViewById(R.id.xingxiang_iv);
        xingxiang_iv.setOnClickListener(this);
        juhaozhige_iv = (ImageView) contentView.findViewById(R.id.juhaozhige_iv);
        juhaozhige_iv.setOnClickListener(this);
        gongcheng_iv = (ImageView) contentView.findViewById(R.id.gongcheng_iv);
        gongcheng_iv.setOnClickListener(this);
        xinmoshi_iv = (ImageView) contentView.findViewById(R.id.xinmoshi_iv);
        xinmoshi_iv.setOnClickListener(this);
        mNetWork=new Network();
        initUI(contentView);

    }

    private void initUI(View contentView) {
        mPopupWindow = new PopupWindow(contentView, -1, -1);
        // 1.让mPopupWindow内部的控件获取焦点
        mPopupWindow.setFocusable(true);
        // 2.mPopupWindow内部获取焦点后 外部的所有控件就失去了焦点
        mPopupWindow.setOutsideTouchable(true);
        //只有加载背景图还有效果
        // 3.如果不马上显示PopupWindow 一般建议刷新界面
        mPopupWindow.update();
        // 设置弹出窗体显示时的动画，从底部向上弹出
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_rl:
                onDismiss();
                break;
            case R.id.xingxiang_iv:
                IssueApplication.noAd=true;
                mIntent=new Intent(mActivity, BrandPlayActivity.class);
                mIntent.putExtra(Constance.url,"http://7xt9qi.com1.z0.glb.clouddn.com/juhao1");
                mActivity.startActivity(mIntent);
                onDismiss();
                break;
            case R.id.juhaozhige_iv:
                IssueApplication.noAd=true;
                mIntent=new Intent(mActivity, BrandPlayActivity.class);
                mIntent.putExtra(Constance.url,"http://7xt9qi.com1.z0.glb.clouddn.com/juhaogongcheng1");
                mActivity.startActivity(mIntent);
                onDismiss();
                break;
            case R.id.gongcheng_iv:
                IssueApplication.noAd=true;
                mIntent=new Intent(mActivity, BrandPlayActivity.class);
                mIntent.putExtra(Constance.url,"http://7xt9qi.com1.z0.glb.clouddn.com/juhao1");
                mActivity.startActivity(mIntent);
                onDismiss();
                break;
            case R.id.xinmoshi_iv:
                IssueApplication.noAd=true;
                mIntent=new Intent(mActivity, BrandPlayActivity.class);
                mIntent.putExtra(Constance.url,"http://7xt9qi.com1.z0.glb.clouddn.com/job_juhaodv");
                mActivity.startActivity(mIntent);
                onDismiss();
                break;
        }
    }

    @Override
    public void onDismiss() {
        ((BaseActivity)mActivity).countDownTimer.start();
        super.onDismiss();
    }
}
