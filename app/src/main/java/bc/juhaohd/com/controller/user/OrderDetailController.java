package bc.juhaohd.com.controller.user;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.lib.common.hxp.view.ListViewForScrollView;

import java.text.DecimalFormat;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.PayResult;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.listener.INetworkCallBack02;
import bc.juhaohd.com.listener.ITwoCodeListener;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.user.OrderDetailActivity;
import bc.juhaohd.com.ui.activity.user.OrderDetailNewActivity;
import bc.juhaohd.com.ui.adapter.OrderGvAdapter;
import bc.juhaohd.com.ui.view.popwindow.WebSharePopWindow;
import bc.juhaohd.com.utils.DateUtils;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/3/20 15:48
 * @description :
 */
public class OrderDetailController extends BaseController implements INetworkCallBack {
    private OrderDetailActivity mView;
    private TextView title_tv,consignee_tv,phone_tv,address_tv,do_tv,do02_tv,do03_tv,order_code_tv,order_time_tv;
    private TextView go_state_tv,total_tv, old_money, new_money, remark_tv,order_remark_tv,log_sv_tv,log_remark_tv,consignment_time_title_tv;
    private ImageView go_state_iv;
    private ListViewForScrollView order_detail_lv;
    private OrderGvAdapter mAGvAdapter;
    private int mStatus;
    private int mOrderId=0;
    private View main_ll;
    private int mOrderLevel=-1;
    private int mLevel=-1;
    private View log_rl;
    private int pay_type;

    public  OrderDetailController(OrderDetailActivity v){
        mView=v;
        initView();
        initViewData();
    }

    public OrderDetailController(OrderDetailNewActivity v) {
//        mView=v;
        initView();
        initViewData();
    }

    private void initView() {
//        title_tv = (TextView)mView.findViewById(R.id.title_tv);
        consignee_tv = (TextView)mView.findViewById(R.id.consignee_tv);
        phone_tv = (TextView)mView.findViewById(R.id.phone_tv);
        address_tv = (TextView)mView.findViewById(R.id.address_tv);
        go_state_tv = (TextView)mView.findViewById(R.id.go_state_tv);
        do_tv = (TextView)mView.findViewById(R.id.do_tv);
        do02_tv = (TextView)mView.findViewById(R.id.do02_tv);
        do03_tv = (TextView)mView.findViewById(R.id.do03_tv);
        order_code_tv = (TextView)mView.findViewById(R.id.order_code_tv);
        order_time_tv = (TextView)mView.findViewById(R.id.order_time_tv);
        main_ll = (View) mView.findViewById(R.id.main_ll);
        total_tv = (TextView)mView.findViewById(R.id.total_tv);
        old_money = (TextView) mView.findViewById(R.id.old_money);
        new_money = (TextView) mView.findViewById(R.id.new_money);
        remark_tv = (TextView) mView.findViewById(R.id.remark_tv);
        order_remark_tv = (TextView) mView.findViewById(R.id.order_remark_tv);
        order_detail_lv = (ListViewForScrollView)mView.findViewById(R.id.order_detail_lv);
        go_state_iv = (ImageView)mView.findViewById(R.id.go_state_iv);
        log_sv_tv = (TextView) mView.findViewById(R.id.log_sv_tv);
        log_remark_tv = (TextView) mView.findViewById(R.id.log_remark_tv);
        consignment_time_title_tv = (TextView) mView.findViewById(R.id.consignment_time_title_tv);
        log_rl = (View) mView.findViewById(R.id.log_rl);
    }

    private void initViewData() {
        if(AppUtils.isEmpty(mView.mOrderObject)) return;

        if( mView.mStatus==-1){
            mView.mStatus = mView.mOrderObject.getInteger(Constance.status);
        }
        mLevel= IssueApplication.mUserObject.getInt(Constance.level);
        getState(mView.mStatus);
        JSONObject consigneeObject= mView.mOrderObject.getJSONObject(Constance.consignee);
        consignee_tv.setText(consigneeObject.getString(Constance.name));
        phone_tv.setText(consigneeObject.getString(Constance.mobile));
        address_tv.setText(consigneeObject.getString(Constance.address));
        order_code_tv.setText(mView.mOrderObject.getString(Constance.sn));
        order_time_tv.setText(DateUtils.getStrTime(mView.mOrderObject.getString(Constance.created_at)));
        JSONArray array= mView.mOrderObject.getJSONArray(Constance.goods);
        if(AppUtils.isEmpty(array)) return;
        mAGvAdapter= new OrderGvAdapter(mView, array);
        order_detail_lv.setAdapter(mAGvAdapter);
        mOrderId=mView.mOrderObject.getInteger(Constance.id);
        int tatalNum = 0;
        String total = mView.mOrderObject.getString(Constance.total);

        final String score = mView.mOrderObject.getJSONObject("score").getString("score");
        old_money.setText("市场价:￥" + score);
        Double avg =  ((Double.parseDouble(total) / Double.parseDouble(score)) * 100);
        if (avg == 100) {
            String str = "优惠价:￥" + total;
            new_money.setText(str);
        } else {
            DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format(avg * 0.1);//format 返回的是字符串
            String val = total + "(" + p + "折)";
            String str = "优惠价:￥" + val;
            new_money.setText(str);
        }

        for (int i = 0; i < array.size(); i++) {
            tatalNum += array.getJSONObject(i).getInteger(Constance.total_amount);
        }
        total_tv.setText("共计 " + tatalNum + " 件商品 合计" + total + "元");

        String user_name = mView.mOrderObject.getString(Constance.user_name);
        mOrderLevel= mView.mOrderObject.getInteger(Constance.level);
        String levelValue = "";


        if (mOrderLevel == 0) {
            levelValue = "一级";
        } else if (mOrderLevel == 1) {
            levelValue = "二级";
        } else if (mOrderLevel == 2) {
            levelValue = "三级";
        } else {
            levelValue = "消费者";
        }
        //        levelValue = levelValue + "(" + user_name + ")";
        //        remark_tv.setText(levelValue);

        if (mLevel == 0) {
            levelValue = levelValue + "(" + user_name + ")";
            remark_tv.setText(levelValue);
        }else{
            remark_tv.setVisibility(View.GONE);
        }

        String postscript=mView.mOrderObject.getString(Constance.postscript);

        order_remark_tv.setText(postscript);

        JSONObject shippingObject = mView.mOrderObject.getJSONObject(Constance.shipping);
        if(!AppUtils.isEmpty(shippingObject)){
            log_sv_tv.setText(shippingObject.getString("invoice_no"));
            log_remark_tv.setText(shippingObject.getString("shipping_name"));
            String shoppingTime=mView.mOrderObject.getString("shipping_at");
            if(!AppUtils.isEmpty(shoppingTime))
                consignment_time_title_tv.setText("发货时间:  "+DateUtils.getStrTime(shoppingTime));
        }
    }


    /**
     * 订单状态
     * @param type
     */
    private void getState(int type) {
        do_tv.setVisibility(View.GONE);
        do02_tv.setVisibility(View.GONE);
        log_rl.setVisibility(View.GONE);
        consignment_time_title_tv.setVisibility(View.GONE);
        do03_tv.setVisibility(View.GONE);
        String stateValue = "订单详情";
        switch (type) {
            case 0:
                do_tv.setVisibility(View.VISIBLE);
                do02_tv.setVisibility(View.VISIBLE);
                do03_tv.setVisibility(View.GONE);
                do_tv.setText("付款");
                do02_tv.setText("取消订单");
                go_state_tv.setText("等待买家付款");
                go_state_iv.setImageResource(R.drawable.wait_pay);
                break;
            case 1:
                do_tv.setVisibility(View.GONE);
                do_tv.setText("联系商家");
                go_state_tv.setText("等待商家发货");
                go_state_iv.setImageResource(R.drawable.wait_send_goos);
                break;
            case 2:
                do_tv.setVisibility(View.VISIBLE);
                do03_tv.setVisibility(View.VISIBLE);
                do_tv.setText("确认收货");
                go_state_tv.setText("等待买家收货");
                log_rl.setVisibility(View.VISIBLE);
                consignment_time_title_tv.setVisibility(View.VISIBLE);
                go_state_iv.setImageResource(R.drawable.wait_receipt_goods);
                break;
            case 3:
                do_tv.setVisibility(View.GONE);
                do_tv.setText("联系商家");
                go_state_tv.setText("订单已完成");
                go_state_iv.setImageResource(R.drawable.al_complete);
                log_rl.setVisibility(View.VISIBLE);
                consignment_time_title_tv.setVisibility(View.VISIBLE);
                break;
            case 4:
                do_tv.setVisibility(View.GONE);
                do_tv.setText("联系商家");
                go_state_tv.setText("订单已完成");
                go_state_iv.setImageResource(R.drawable.al_complete);
                log_rl.setVisibility(View.VISIBLE);
                consignment_time_title_tv.setVisibility(View.VISIBLE);
                break;
            case 5:
                do_tv.setVisibility(View.GONE);
                do_tv.setText("联系商家");
                go_state_tv.setText("订单已取消");
                go_state_iv.setImageResource(R.drawable.al_cancel);
                break;
        }
//        title_tv.setText(stateValue);
    }
    


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private void sendOrderCancel(String order, String reason) {
        mNetWork.sendOrderCancel(order, reason, this);
    }

    public void doOrder() {
        if(mOrderId==0) return;
        //TODO
        if (mStatus == 1) {
            mView.setShowDialog(true);
            mView.setShowDialog("正在取消中!");
            mView.showLoading();
            sendOrderCancel(mOrderId +"", "1");
        }else if(mStatus==0){
            mView.setShowDialog(true);
            mView.setShowDialog("正在付款中!");
            mView.showLoading();
            showPayTypeDialog(String.valueOf(mOrderId));
        }else if(mStatus==2){
            //TODO 确认收货
            MyToast.show(mView,"确认收货");
        }else if(mStatus==5){
            mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
        }else if(mStatus==4){
            mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
        }
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须上传到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息


                    String resultStatus = payResult.getResultStatus();
                    Log.d("TAG", "resultStatus=" + resultStatus);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        MyToast.show(mView,"支付成功");
                        mView.finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            AppDialog.messageBox("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            AppDialog.messageBox("支付失败");
                            mView.hideLoading();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 支付订单
     * @param order
     * @param code
     */
    private void sendPayment(final String order, String code){
        mNetWork.sendPayment(order, code, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                WebSharePopWindow window = new WebSharePopWindow(mView, mView,pay_type);
                window.getPayOrder(order);
                window.onShow(main_ll);
                window.setListener(new ITwoCodeListener() {
                    @Override
                    public void onTwoCodeChanged(String path) {
                        if("支付成功".equals(path)){
                            MyToast.show(mView,"支付成功");
                            mView.mStatus=1;
                            sendOrderDetail(mView.mOrderObject.getString(Constance.sn));
                        }else{
                            AppDialog.messageBox("支付失败");
                            mView.hideLoading();
                        }
                    }
                });
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView, "支付失败!");
            }
        });

    }


    /**
     * 获取订单详情
     * @param orderCode
     */
    private void sendOrderDetail(String orderCode) {
        mNetWork.semdOrderSearch(orderCode, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                switch (requestCode) {
                    case NetWorkConst.ORDERLIST:
                        if (null == mView || mView.isFinishing())
                            return;
                        break;
                }
                mView.mOrderObject=ans.getJSONObject(Constance.orders);
                initViewData();
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView, "网络异常，请重新加载!");
            }
        });
    }



    /**
     * 支付宝支付
     */
    private void SubmitAliPay(String notifyUrl) {
        //开始支付
        aliPay(notifyUrl);
    }

    //标记是支付
    private static final int SDK_PAY_FLAG = 1;
    private static final String TAG = "PayActivity";

    /**
     * 开始-支付宝支付
     */
    private void aliPay(final String ss) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mView);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(ss, true);
                //异步处理支付结果
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    public void do02Order() {
        if(mOrderId==0) return;
        if (mStatus == 0) {
            mView.setShowDialog(true);
            mView.setShowDialog("正在取消中!");
            mView.showLoading();
            sendOrderCancel(mOrderId+"", "1");
        }
    }

    public void do03Order() {
        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
    }

    @Override
    public void onSuccessListener(String requestCode, bocang.json.JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.ORDERCANCEL:
                if(ans.getInt(Constance.error_code)==0){
                    MyToast.show(mView, "取消成功!");
                    mView.finish();
                }else{
                    MyToast.show(mView,"订单取消失败!");
                }

                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, bocang.json.JSONObject ans) {

    }
    private void showPayTypeDialog(final String orderId) {
        final Dialog dialog = new Dialog(mView, R.style.customDialog);
        dialog.setContentView(R.layout.dialog_layout_pay_choose);
        RadioGroup rg=dialog.findViewById(R.id.rg_pay);
        RadioButton rb_ali=dialog.findViewById(R.id.rb_alipay);
        RadioButton rb_wx=dialog.findViewById(R.id.rb_wx_pay);
        pay_type = 0;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_wx_pay:
                        pay_type =1;
                        break;
                    case R.id.rb_alipay:
                        pay_type =0;
                        break;
                }
            }
        });
        TextView btn = (TextView) dialog.findViewById(R.id.tv_ensure);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView.setShowDialog(true);
                mView.setShowDialog("正在付款中!");
                mView.showLoading();
//                        MyToast.show(mView.getContext(),orderId);

                sendPayment(orderId, "alipay.app");

                dialog.dismiss();
            }
        });
        TextView cancel= (TextView) dialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
           /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }
}
