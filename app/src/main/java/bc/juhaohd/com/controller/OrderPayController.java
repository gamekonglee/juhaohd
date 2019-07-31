package bc.juhaohd.com.controller;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.PayResult;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.listener.INetworkCallBack02;
import bc.juhaohd.com.listener.ITwoCodeListener;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.OrderPayHomeActivity;
import bc.juhaohd.com.ui.activity.OrderPaySuccessActivity;
import bc.juhaohd.com.ui.activity.user.OrderDetailActivity;
import bc.juhaohd.com.ui.view.popwindow.WebSharePopWindow;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

import static bc.juhaohd.com.cons.Constance.error_code;

/**
 * Created by DEMON on 2018/1/22.
 */
public class OrderPayController extends BaseController implements INetworkCallBack02 {
    //标记是支付
    private static final int SDK_PAY_FLAG = 1;
    private final OrderPayHomeActivity mView;
    private com.alibaba.fastjson.JSONObject mOrderObject;
    private String mOrderId;
    private Timer timer;
    private ITwoCodeListener mListener;

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
    public OrderPayController(OrderPayHomeActivity view){
        mView = view;
        initView();
        initData();
    }

    private void initData() {
        mNetWork.sendCheckOutCart(mView.consignee, mView.shipping, mView.mobie, mView.address, mView.idArray, "", this);
    }

    private void initView() {

    }

    @Override
    public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
        switch (requestCode){
            case NetWorkConst.CheckOutCart:
                com.alibaba.fastjson.JSONObject orderObject = ans.getJSONObject(Constance.order);
                if(AppUtils.isEmpty(orderObject)){
                    MyToast.show(mView,"当前没有可支付的数据!");
                    return;
                }
                mOrderObject=orderObject;
                String order= orderObject.getString(Constance.id);
                String osn=orderObject.getString(Constance.sn);
                String total=orderObject.getString(Constance.total);
                com.alibaba.fastjson.JSONObject consin=orderObject.getJSONObject(Constance.consignee);
                String name=consin.getString(Constance.name);
                String mobie=consin.getString(Constance.mobile);
                String address=consin.getString(Constance.address);
                mView.tv_osn.setText(""+osn);
                mView.tv_money.setText("¥"+total);
                mView.tv_mobie.setText(""+mobie);
                mView.tv_name.setText(""+name);
                mView.tv_address.setText(""+address);
//                sendPayment(order, "alipay.app");
                mOrderId=order;
                getPayOrder(order);

                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {

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

            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                if(ans==null){
                    return;
                }
                int errorCode = ans.getInteger(error_code);
                if (errorCode == 404) {
                    MyToast.show(mView,"支付成功");
                    Intent intent = new Intent(mView, OrderPaySuccessActivity.class);
                    intent.putExtra(Constance.order, mOrderObject.toJSONString());
                    intent.putExtra(Constance.state, 1);
                    timer.cancel();
                    mView.setResult(200,intent);
                    mView.finish();
                }

            }
        });
    }
    public void getWebView(String htmlValue) {
        mView.web_wv.setActivity(mView);
        String html = htmlValue;
        html = html.replace("200", "300");
        html = "<meta name=\"viewport\" content=\"width=device-width\"> " +
                "<div style=\"text-align:center\">" + html + " </div>";
        mView.web_wv.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
    }
    /**
     * 支付
     *
     * @param order
     */
    public void getPayOrder(String order) {
        mOrderId = order;
        //TODO二维码支付

        mNetWork.sendTwoCodePay(order, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                final String qrcode = ans.getString(Constance.qrcode);
                getWebView(qrcode);
                sendWxPay(mOrderId);
         /*       timer = new Timer();
                TimerTask task= new TimerTask() {
                    @Override
                    public void run() {
                        mView.runOnUiThread(new Runnable() {
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
                timer.schedule(task, 3000,3000);*/

            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                int errorCode = ans.getInteger(error_code);
                if (errorCode == 404) {
                    mListener.onTwoCodeChanged("支付成功");
                }{
                    MyToast.show(mView,"支付失败");
//                    Intent intent = new Intent(mView, OrderPaySuccessActivity.class);
//                    intent.putExtra(Constance.order, mOrderObject.toJSONString());
//                    intent.putExtra(Constance.state, 1);
//                    timer.cancel();
//                    mView.setResult(200,intent);
//                    mView.finish();
                }

            }
        });
    }
    private void sendWxPay(String order) {

        mNetWork.sendTwoWxCodePay(order, new INetworkCallBack02() {

            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                com.alibaba.fastjson.JSONObject jsonObject=ans.getJSONObject(Constance.qrcode);
                String codeUrl=jsonObject.getString(Constance.code_url);
                final String qrcode = codeUrl;
//                MyToast.show(mView,"wxQrSuccess");
                getWebView2(qrcode);
                timer = new Timer();
                TimerTask task= new TimerTask() {
                    @Override
                    public void run() {
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getPayOrderWx02(mOrderId);
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
                LogUtils.logE("failure",ans.toString());
            }
        });
    }

    private void getWebView2(String url) {
        mView.web_wv_wx.setActivity(mView);
//        String html = qrcode;
//        html = html.replace("200", "300");
//        html = "<meta name=\"viewport\" content=\"width=device-width\"> " +
//                "<div style=\"text-align:center\">" + html + " </div>";
////        mView.web_wv_wx.setInitialScale(25);//为25%，最小缩放等级
        mView.web_wv_wx.loadUrl(url);
    }

    /**
     * 支付订单
     *
     * @param order
     * @param code
     */
    private void sendPayment(final String order, String code) {
        mNetWork.sendPayment(order, code, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mOrderObject = ans.getJSONObject(Constance.order);
//                getPayOrder(order);

                WebSharePopWindow window = new WebSharePopWindow(mView, mView,0);
//                window.setListener(new ITwoCodeListener() {
//                    @Override
//                    public void onTwoCodeChanged(String path) {
//                        if("支付成功".equals(path)){
//                            MyToast.show(mView,"支付成功");
//                            Intent intent = new Intent(mView, OrderDetailActivity.class);
//                            intent.putExtra(Constance.order, mOrderObject.toJSONString());
//                            intent.putExtra(Constance.state, 1);
//                            mView.startActivity(intent);
//                            mView.finish();
//                        }else{
//                            AppDialog.messageBox("支付失败");
////                            mView.hideLoading();
//                            Intent intent = new Intent(mView, OrderDetailActivity.class);
//                            intent.putExtra(Constance.order, mOrderObject.toJSONString());
//                            mView.startActivity(intent);
//                            mView.finish();
//                        }
//                    }
//                });
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
//                mView.hideLoading();
                MyToast.show(mView, "支付失败!");
            }
        });

    }
    private void getPayOrderWx02(String mOrderId) {
        mNetWork.sendTwoCodePay(mOrderId, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                if(ans==null)return;
//                getWebView2(ans.getString(Constance.qrcode));
//                String qrcode = ans.getString(Constance.qrcode);
//                getWebView(qrcode);
//                MyToast.show(mView,"支付失败");
//                Intent intent = new Intent(mView, OrderPaySuccessActivity.class);
//                intent.putExtra(Constance.order, mOrderObject.toJSONString());
//                intent.putExtra(Constance.state, 1);
//                timer.cancel();
//                mView.setResult(200,intent);
//                mView.finish();

            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                if(ans==null)return;
                int errorCode = ans.getInteger(error_code);
                if (errorCode == 404) {
                    MyToast.show(mView, "支付成功");
                    Intent intent = new Intent(mView, OrderPaySuccessActivity.class);
                    intent.putExtra(Constance.order, mOrderObject.toJSONString());
                    intent.putExtra(Constance.state, 1);
                    timer.cancel();
                    mView.setResult(200, intent);
                    mView.finish();
                }
            }
        });
    }
}
