package bc.juhaohd.com.ui.view.popwindow;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.listener.INetworkCallBack02;
import bc.juhaohd.com.listener.ITwoCodeListener;
import bc.juhaohd.com.net.ApiClient;
import bc.juhaohd.com.ui.activity.OrderPaySuccessActivity;
import bc.juhaohd.com.ui.view.MyWebView;
import bc.juhaohd.com.ui.view.popwindow.BasePopwindown;
import bc.juhaohd.com.utils.Network;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;
import okhttp3.Call;
import okhttp3.Response;

import static bc.juhaohd.com.cons.Constance.error_code;


/**
 * @author: Jun
 * @date : 2017/2/16 15:12
 * @description :
 */
public class WebSharePopWindow extends BasePopwindown implements View.OnClickListener {
    private ImageView two_code_iv;
    private RelativeLayout main_rl;
    public Activity mActivity;
    private MyWebView webView;
    protected Network mNetWork;
    private String mOrderId = "";
    private ITwoCodeListener mListener;
    private Timer timer;
    private TextView tv_pay_text;
    private final int paytype;

    public void setListener(ITwoCodeListener listener) {
        mListener = listener;
    }

    public WebSharePopWindow(Context context, Activity view,int type) {
        super(context);
        mActivity = view;
        mNetWork = new Network();
        paytype = type;

    }

    @Override
    protected void initView(Context context) {
        View contentView = View.inflate(mContext, R.layout.pop_two_code_pay, null);
        initUI(contentView);
        initViewData();

    }

    /**
     * 支付
     *
     * @param order
     */
    public void getPayOrder(String order) {
        mOrderId = order;
        //TODO二维码支付
        if(paytype==0){
            tv_pay_text.setText("请使用支付宝支付");
            mNetWork.sendTwoCodePay(order, new INetworkCallBack02() {
                @Override
                public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                    final String qrcode = ans.getString(Constance.qrcode);
                    getWebView(qrcode);
                    timer=new Timer();
                    TimerTask task= new TimerTask() {
                        @Override
                        public void run() {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getPayOrder02(mOrderId);
                                }
                            });
                        }
                    };

                    //time为Date类型：在指定时间执行一次。

                    //firstTime为Date类型,period为long，表示从firstTime时刻开始，每隔period毫秒执行一次。
                    //delay 为long类型：从现在起过delay毫秒执行一次。
                    timer.schedule(task, 3000,3000);

                }

                @Override
                public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                    if(ans==null)
                    {
                        MyToast.show(mContext,"生成二维码失败");
                        return;
                    }
                    int errorCode = ans.getInteger(error_code);
                    if (errorCode == 404) {
                        mListener.onTwoCodeChanged("支付成功");
                        onDismiss();
                    }

                }
            });
        }else {
            tv_pay_text.setText("请使用微信支付");
            sendWxPay(order);
        }

    }
    private void sendWxPay(final String order) {

        ApiClient.sendWxPayment(order, new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response, int id) throws Exception {
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                org.json.JSONObject ans= null;
                try {
                    ans = new org.json.JSONObject(e.getMessage());
                    int errorCode = ans.getInt(error_code);
                    if (errorCode == 404) {
                        mListener.onTwoCodeChanged("支付成功");
                        onDismiss();
                        timer.cancel();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }

            @Override
            public String onResponse(String response, int id) {
                String result=response;
                if(result.contains("\"wxpay.dmf\"")){
                    result=result.substring(result.indexOf("\"wxpay.dmf\"")+11);
                }
//                LogUtils.logE("result:",result);
//                JSONObject ans=new JSONObject(result);
                org.json.JSONObject ans= null;
                try {
                    ans = new org.json.JSONObject(result);
                    org.json.JSONObject jsonObject=ans.getJSONObject(Constance.qrcode);
                    String codeUrl=jsonObject.getString(Constance.code_url);
                    final String qrcode = codeUrl.replace("\\/","/").replace("300", "500");;
//                    LogUtils.logE("wx_Success",codeUrl);
                    getWebView2(qrcode);
                    timer=new Timer();
                    TimerTask task= new TimerTask() {
                        @Override
                        public void run() {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getPayOrderWx02(mOrderId);
                                }
                            });
                        }
                    };

                    //time为Date类型：在指定时间执行一次。

                    //firstTime为Date类型,period为long，表示从firstTime时刻开始，每隔period毫秒执行一次。
                    //delay 为long类型：从现在起过delay毫秒执行一次。
                    timer.schedule(task, 3000,3000);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }

    private void getWebView2(String qrcode) {
        webView.setActivity(mActivity);
        String html = qrcode;
        html = html.replace("200", "500");
        html = "<meta name=\"viewport\" content=\"width=device-width\"> " +
                "<div style=\"text-align:center\">" + html + " </div>";
//        mView.web_wv_wx.setInitialScale(25);//为25%，最小缩放等级
//        mView.web_wv_wx.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
        webView.loadUrl(qrcode);
    }
    /**
     * 支付
     *
     * @param order
     */
    public void getPayOrder02(String order) {
        mOrderId = order;
        //TODO二维码支付
        mNetWork.sendTwoCodePay(order, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
//                String qrcode = ans.getString(Constance.qrcode);
//                getWebView(qrcode);
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                int errorCode = ans.getInteger(error_code);
                if (errorCode == 404) {
                    mListener.onTwoCodeChanged("支付成功");
                    onDismiss();
                    timer.cancel();
                }

            }
        });
    }
    private void getPayOrderWx02(String mOrderId) {
        ApiClient.sendWxPayment(mOrderId, new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response, int id) throws Exception {
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

//                MyToast.show(mActivity,e.getMessage());
//                org.json.JSONObject ans= null;
//                try {
//                    ans = new org.json.JSONObject(e.getMessage());
//                    int errorCode = ans.getInt(error_code);
//                    if (errorCode == 404) {
//                        mListener.onTwoCodeChanged("支付成功");
//                        onDismiss();
//                        timer.cancel();
//                    }
//                } catch (JSONException e1) {
//                    e1.printStackTrace();
//                }

            }

            @Override
            public String onResponse(String response, int id) {
                org.json.JSONObject ans= null;
                try {
                    ans = new org.json.JSONObject(response);
                    int errorCode = ans.getInt(error_code);
                    if (errorCode == 404) {
                        mListener.onTwoCodeChanged("支付成功");
                        onDismiss();
                        timer.cancel();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return null;
            }
        });
    }
    private void initViewData() {
    }


    public void getWebView(String htmlValue) {
        webView.setActivity(mActivity);
        String html = htmlValue;
        html = html.replace("200", "500");
        html = "<meta name=\"viewport\" content=\"width=device-width\"> " +
                "<div style=\"text-align:center\">" + html + " </div>";
        webView.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
    }

    private void initUI(View contentView) {
        webView = (MyWebView) contentView.findViewById(R.id.web_wv);
        main_rl = (RelativeLayout) contentView.findViewById(R.id.main_rl);
        tv_pay_text = contentView.findViewById(R.id.tv_pay_text);
        main_rl.setOnClickListener(this);
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
                mListener.onTwoCodeChanged("支付不成功");
                onDismiss();
                if(timer!=null)timer.cancel();
                break;
        }
    }


}
