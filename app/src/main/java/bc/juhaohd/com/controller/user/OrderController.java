package bc.juhaohd.com.controller.user;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alipay.sdk.app.PayTask;
import com.lib.common.hxp.view.ListViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.OrderInfo;
import bc.juhaohd.com.bean.PayResult;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.listener.INetworkCallBack02;
import bc.juhaohd.com.listener.ITwoCodeListener;
import bc.juhaohd.com.ui.activity.OrderPaySuccessActivity;
import bc.juhaohd.com.ui.activity.user.OrderDetailActivity;
import bc.juhaohd.com.ui.adapter.OrderGvAdapter;
import bc.juhaohd.com.ui.fragment.OrderFragment;
import bc.juhaohd.com.ui.view.MyWebView;
import bc.juhaohd.com.ui.view.ShowDialog;
import bc.juhaohd.com.ui.view.popwindow.WebSharePopWindow;
import bc.juhaohd.com.utils.DateUtils;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

import static bc.juhaohd.com.cons.Constance.error_code;

/**
 * @author: Jun
 * @date : 2017/2/6 15:13
 * @description :
 */
public class OrderController extends BaseController implements PullToRefreshLayout.OnRefreshListener, INetworkCallBack {
    private OrderFragment mView;
    private com.alibaba.fastjson.JSONArray goodses;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private ListViewForScrollView order_sv;
    private int page = 1;
    private LinearLayout main_ll;
    private View mNullView;
    private View mNullNet;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private int per_pag = 20;
    private ProgressBar pd;
    private int mPosition;

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
                        page = 1;
                        sendOrderList(page);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            AppDialog.messageBox("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            AppDialog.messageBox("支付失败");


                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private String[] codeArray;
    private Timer timer;
    private int pay_type;


    public OrderController(OrderFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        page = 1;
        pd.setVisibility(View.VISIBLE);
        sendOrderList(page);
    }

    private void sendOrderList(final int page) {
        mNetWork.sendorderList(page, per_pag, mView.list.get(mView.flag), new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                pd.setVisibility(View.GONE);
                switch (requestCode) {
                    case NetWorkConst.ORDERLIST:
                        if (null == mView.getActivity() || mView.getActivity().isFinishing())
                            return;
                        if (null != mPullToRefreshLayout) {
                            dismissRefesh();
                        }
                        break;
                }

                com.alibaba.fastjson.JSONArray goodsList = ans.getJSONArray(Constance.orders);
                if (AppUtils.isEmpty(goodsList)|| goodsList.size() == 0) {
                    if (page == 1) {
                        mNullView.setVisibility(View.VISIBLE);
                    }

                    dismissRefesh();
                    return;
                }

                mNullView.setVisibility(View.GONE);
                mNullNet.setVisibility(View.GONE);
                getDataSuccess(goodsList);
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                pd.setVisibility(View.GONE);
//                MyToast.show(mView.getActivity(), "数据异常!");
            }
        });
    }

    private void initView() {
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (ListViewForScrollView) mView.getView().findViewById(R.id.order_sv);
//        order_sv.setDivider(null);//去除listview的下划线
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        mNullView = mView.getView().findViewById(R.id.null_view);
        mNullNet = mView.getView().findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd);
        main_ll = (LinearLayout) mView.getView().findViewById(R.id.main_ll);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        sendOrderList(page);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        sendOrderList(++page);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void getDataSuccess(com.alibaba.fastjson.JSONArray array) {
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.size(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }
        codeArray = new String[goodses.size()];
        mProAdapter.notifyDataSetChanged();
//        UIUtils.initListViewHeight(order_sv);
    }

    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    public void onRefresh() {
        page = 1;
        sendOrderList(page);
        //        sendGoodsList(IssueApplication.mCId, page, 1, "is_best", null);
    }


    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (null == mView.getActivity() || mView.getActivity().isFinishing())
            return;
        this.page--;

        if (AppUtils.isEmpty(ans)) {
            mNullNet.setVisibility(View.VISIBLE);
            mRefeshBtn.setOnClickListener(mRefeshBtnListener);
            return;
        }

        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }


    /**
     * 支付订单
     *
     * @param order
     * @param code
     */
    private void sendPayment(final String order, String code) {
        mView.hideLoading();
        WebSharePopWindow window = new WebSharePopWindow(mView.getActivity(), mView.getActivity(),pay_type);
        window.getPayOrder(order);
        window.onShow(main_ll);
        window.setListener(new ITwoCodeListener() {
            @Override
            public void onTwoCodeChanged(String path) {
                if("支付成功".equals(path)){
                    MyToast.show(mView.getContext(),"支付成功");
                    Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                    intent.putExtra(Constance.order, goodses.getJSONObject(mPosition).toJSONString());
                    intent.putExtra(Constance.state, 1);
                    mView.getActivity().startActivity(intent);
                }else{
                    AppDialog.messageBox("支付失败");
                    mView.hideLoading();
                    Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                    intent.putExtra(Constance.order, goodses.getJSONObject(mPosition).toJSONString());
                    mView.startActivity(intent);
                }
            }
        });
       /* mNetWork.sendPayment(order, code, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {

            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                MyToast.show(mView.getActivity(), "支付失败!");
            }
        });*/

    }
    //标记是支付
    private static final int SDK_PAY_FLAG = 1;
    private static final String TAG = "PayActivity";

    /**
     * 支付宝支付
     */
    private void SubmitAliPay(String notifyUrl) {
        //开始支付
        aliPay(notifyUrl);
    }

    /**
     * create the order info. 创建订单信息
     */
    private String createOrderInfo(OrderInfo order) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + order.getPartner() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + order.getSeller_id() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order.getOut_trade_no() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + order.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + order.getBody() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + order.getTotal_fee() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + order.getNotify_url() + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * 开始-支付宝支付
     */
    private void aliPay(final String ss) {
        //        try {
        //            /**
        //             * 仅需对sign 做URL编码
        //             */
        //            sign = URLEncoder.encode(sign, "UTF-8");
        //        } catch (UnsupportedEncodingException e) {
        //            e.printStackTrace();
        //        }
        //
        //        /**
        //         * 完整的符合支付宝参数规范的订单信息
        //         */
        //        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mView.getActivity());
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

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    String mBody = "";


    /**
     * 确认收货
     *
     * @param order
     */
    private void sendConfirmReceipt(final String order) {
        ShowDialog mDialog = new ShowDialog();
        mDialog.show(mView.getActivity(), "提示", "是否确认收货?", new ShowDialog.OnBottomClickListener() {
            @Override
            public void positive() {
                mView.setShowDialog(true);
                mView.setShowDialog("确认收货中!");
                mView.showLoading();
                mNetWork.sendConfirmReceipt(order, new INetworkCallBack02() {
                    @Override
                    public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                        mView.hideLoading();
                        MyToast.show(mView.getActivity(), "确认收货成功!");
                        page = 1;
                        sendOrderList(page);
                        Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                        intent.putExtra(Constance.order, goodses.getJSONObject(mPosition).toJSONString());
                        intent.putExtra(Constance.state, 3);
                        mView.getActivity().startActivity(intent);
                    }

                    @Override
                    public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                        mView.hideLoading();
                        MyToast.show(mView.getActivity(), "确认收货失败!");
                    }
                });
            }

            @Override
            public void negtive() {

            }
        });

    }
    private void showPayTypeDialog(final String orderId) {
        final Dialog dialog = new Dialog(mView.getActivity(), R.style.customDialog);
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


    private class ProAdapter extends BaseAdapter implements INetworkCallBack {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == goodses)
                return 0;
            return goodses.size();
        }

        @Override
        public com.alibaba.fastjson.JSONObject getItem(int position) {
            if (null == goodses)
                return null;
            return goodses.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        private void getWebView(MyWebView webView,String htmlValue) {
            webView.setActivity(mView.getActivity());
            String html = htmlValue;
            html = html.replace("200", "500");
            html = "<meta name=\"viewport\" content=\"width=device-width\"> " +
                    "<div style=\"text-align:center\">" + html + " </div>";
            webView.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_order_one_new, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.state_tv = (TextView) convertView.findViewById(R.id.state_tv);
                holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
                holder.do_tv = (TextView) convertView.findViewById(R.id.do_tv);
                holder.do02_tv = (TextView) convertView.findViewById(R.id.do02_tv);
                holder.do03_tv = (TextView) convertView.findViewById(R.id.do03_tv);
                holder.code_tv = (TextView) convertView.findViewById(R.id.code_tv);
                holder.total_tv = (TextView) convertView.findViewById(R.id.total_tv);
                holder.old_money = (TextView) convertView.findViewById(R.id.old_money);
                holder.new_money = (TextView) convertView.findViewById(R.id.new_money);
                holder.lv = (ListView) convertView.findViewById(R.id.lv);
                holder.order_lv = (LinearLayout) convertView.findViewById(R.id.order_lv);
                holder.webView= (MyWebView) convertView.findViewById(R.id.webview);
                holder.rl_code= (RelativeLayout) convertView.findViewById(R.id.rl_code);
                holder.id_gallery= (LinearLayout) convertView.findViewById(R.id.id_gallery);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final com.alibaba.fastjson.JSONObject orderobject = goodses.getJSONObject(position);
            final int state = orderobject.getInteger(Constance.status);
            int tatalNum = 0;
            String total = orderobject.getString(Constance.total);
            final String orderId = orderobject.getString(Constance.id);
            getState(state, holder.state_tv, holder.do_tv, holder.do02_tv, holder.do03_tv);
            holder.code_tv.setText("订单号:" + orderobject.getString(Constance.sn));
            final JSONArray array = orderobject.getJSONArray(Constance.goods);
            holder.time_tv.setText("下单时间:"+DateUtils.getStrTime(orderobject.getString(Constance.created_at)));
            final String score = orderobject.getJSONObject("score").getString("score");
            holder.rl_code.setVisibility(View.GONE);

//            if(state==0){
//                sendPayment(position,orderId, "alipay.app");
//                holder.rl_code.setVisibility(View.VISIBLE);
//            }
//            if(codeArray!=null&&codeArray.length>position&&!TextUtils.isEmpty(codeArray[position])){
//                getWebView(holder.webView,codeArray[position]);
//            }
            String str1 = "市场价:￥" + score;
            holder.old_money.setText(str1);

            Double avg = ((Double.parseDouble(total) / Double.parseDouble(score)) * 100);
            Log.e("520it", position + ":" + avg + "  :" + Double.parseDouble(total) + "  :" + (Double.parseDouble(score)) + "  :" + (Double.parseDouble(total) / Double.parseDouble(score)));
            if (avg == 100) {
                String str = "优惠价:￥" + total;
                int fstart = str.indexOf(total);
                int fend = fstart + total.length();
                SpannableStringBuilder style = new SpannableStringBuilder(str);
                style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.new_money.setText(style);
            } else {
                DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                String p = decimalFormat.format(avg * 0.1);//format 返回的是字符串
                String val = total + "(" + p + "折)";
                String str = "优惠价:￥" + val;
                int fstart = str.indexOf(val);
                int fend = fstart + val.length();
                SpannableStringBuilder style = new SpannableStringBuilder(str);
                style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.new_money.setText(style);
            }

            for (int i = 0; i < array.size(); i++) {
                tatalNum += array.getJSONObject(i).getInteger(Constance.total_amount);
            }

            String str = " ￥" + total + "";
            holder.total_tv.setText(str);

            if(array.size()>1){
                for(int x=0;x<array.size();x++){
                    com.alibaba.fastjson.JSONObject object=array.getJSONObject(x);
                    ImageView img=new ImageView(mView.getActivity());
                    View view=new View(mView.getActivity());
                    view.setBackgroundColor(mView.getResources().getColor(R.color.bg_f6f6f6));
                    ImageLoader.getInstance().displayImage( object.getJSONObject(Constance.product).
                            getJSONArray(Constance.photos).getJSONObject(0).getString(Constance.thumb),img);
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(UIUtils.dip2PX(220),UIUtils.dip2PX(220));
                    LinearLayout.LayoutParams layoutParams2=new LinearLayout.LayoutParams(UIUtils.dip2PX(20),UIUtils.dip2PX(260));
                    view.setLayoutParams(layoutParams2);
                    img.setLayoutParams(layoutParams);
                    holder.id_gallery.addView(img);
                    holder.id_gallery.addView(view);
                }
                holder.lv.setVisibility(View.GONE);
                holder.id_gallery.setVisibility(View.VISIBLE);
            }else {
                OrderGvAdapter maGvAdapter = new OrderGvAdapter(mView.getActivity(), array,state);
                holder.lv.setAdapter(maGvAdapter);
                UIUtils.initListViewHeight(holder.lv);
                holder.lv.setVisibility(View.VISIBLE);
                holder.id_gallery.setVisibility(View.GONE);
            }



            holder.do03_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                }
            });
            holder.do_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    if (state == 1) {
                        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                    } else if (state == 0) {
                        mPosition = position;
                        for (int i = 0; i < array.size(); i++) {
                            mBody += array.getJSONObject(i).getJSONObject(Constance.product).getString(Constance.name) + "  ";
                        }
//                        mView.setShowDialog(true);
//                        mView.setShowDialog("正在付款中!");
//                        mView.showLoading();
                        showPayTypeDialog(orderId);
//                        sendPayment(orderId, "alipay.app");
                    } else if (state == 2) {
                        //TODO 确认收货
                        sendConfirmReceipt(orderId);
                    } else if (state == 5) {
                        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                    } else if (state == 4) {
                        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                    }
                }
            });
            holder.do02_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state == 0) {
                        mView.setShowDialog(true);
                        mView.setShowDialog("正在取消中!");
                        mView.showLoading();
                        sendOrderCancel(orderId, "1");
                    }

                }
            });

            holder.order_lv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                    intent.putExtra(Constance.order, orderobject.toJSONString());
                    mView.getActivity().startActivity(intent);
                }
            });

            holder.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                    intent.putExtra(Constance.order, orderobject.toJSONString());
                    mView.getActivity().startActivity(intent);
                }
            });

            return convertView;

        }


        private void sendOrderCancel(String order, String reason) {
            mNetWork.sendOrderCancel(order, reason, this);
        }

        /**
         * 订单状态
         *
         * @param type
         * @param state_tv
         * @param do_tv
         * @param do02_tv
         */
        private void getState(int type, TextView state_tv, TextView do_tv, TextView do02_tv, TextView do03_tv) {
            do_tv.setVisibility(View.GONE);
            do02_tv.setVisibility(View.GONE);
            do03_tv.setVisibility(View.GONE);
            String stateValue = "";
            switch (type) {
                case 0:
                    stateValue = "【待付款】";
                    do_tv.setVisibility(View.VISIBLE);
                    do02_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("付款");
                    do02_tv.setText("取消订单");

                    break;
                case 1:
                    stateValue = "【待发货】";
                    do_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("联系商家");
                    do_tv.setVisibility(View.GONE);
                    break;
                case 2:
                    do03_tv.setVisibility(View.VISIBLE);
                    stateValue = "【待收货】";
                    do_tv.setVisibility(View.VISIBLE);
                    do_tv.setText("确认收货");
                    do_tv.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    do_tv.setVisibility(View.GONE);
                    do_tv.setText("联系商家");
                    stateValue = "【已完成】";
                    break;
                case 4:
                    do_tv.setVisibility(View.GONE);
                    do_tv.setText("联系商家");
                    stateValue = "【已完成】";
                    break;
                case 5:
                    do_tv.setVisibility(View.GONE);
                    do_tv.setText("联系商家");
                    stateValue = "【已取消】";
                    break;
            }
            state_tv.setText(stateValue);
        }

        @Override
        public void onSuccessListener(String requestCode, JSONObject ans) {
            switch (requestCode) {
                case NetWorkConst.ORDERCANCEL:
                    if (ans.getInt(Constance.error_code) == 0) {
                        page = 1;
                        sendOrderList(page);
                    } else {
                        mView.hideLoading();
                        MyToast.show(mView.getActivity(), "订单取消失败!");
                    }

                    break;
                case NetWorkConst.PAYMENT:
                    break;


            }
        }

        @Override
        public void onFailureListener(String requestCode, JSONObject ans) {
            MyToast.show(mView.getActivity(), "2");
            mView.hideLoading();
            MyToast.show(mView.getActivity(), "支付成功!");

        }

        class ViewHolder {
            ImageView imageView;
            TextView state_tv;
            TextView time_tv;
            TextView do_tv;
            TextView do02_tv;
            TextView do03_tv;
            TextView code_tv;
            TextView total_tv;
            ListView lv;
            LinearLayout order_lv;
            TextView old_money;
            TextView new_money;
            RelativeLayout rl_code;
            MyWebView webView;
            LinearLayout id_gallery;
        }
    }

    private void sendPayment(final int position, final String orderId, String code) {
        mNetWork.sendPayment(orderId, code, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                com.alibaba.fastjson.JSONObject mOrderObject = ans.getJSONObject(Constance.order);
                getPayOrder(position,orderId);


                WebSharePopWindow window = new WebSharePopWindow(mView.getActivity(), mView.getActivity(),pay_type);
//                window.getPayOrder(orderId);
//                window.onShow(main_ll);
                window.setListener(new ITwoCodeListener() {
                    @Override
                    public void onTwoCodeChanged(String path) {
                        if("支付成功".equals(path)){
                            MyToast.show(mView.getContext(),"支付成功");
                            Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                            intent.putExtra(Constance.order, goodses.getJSONObject(mPosition).toJSONString());
                            intent.putExtra(Constance.state, 1);
                            mView.getActivity().startActivity(intent);
                        }else{
                            AppDialog.messageBox("支付失败");
                            mView.hideLoading();
                            Intent intent = new Intent(mView.getActivity(), OrderDetailActivity.class);
                            intent.putExtra(Constance.order, goodses.getJSONObject(mPosition).toJSONString());
                            mView.startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                MyToast.show(mView.getActivity(), "支付失败!");
            }
        });

    }
    /**
     * 支付
     *
     * @param order
     */
    public void getPayOrder(final int position, String order) {
        final String mOrderId = order;
        //TODO二维码支付
        mNetWork.sendTwoCodePay(order, new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                final String qrcode = ans.getString(Constance.qrcode);
//                codeArray[position]=qrcode;
//                mProAdapter.notifyDataSetChanged();
                if(timer==null)timer = new Timer();
                TimerTask task= new TimerTask() {
                    @Override
                    public void run() {
                        mView.getActivity().runOnUiThread(new Runnable() {
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
                int errorCode = ans.getInteger(error_code);
                if (errorCode == 404) {
//                    mListener.onTwoCodeChanged("支付成功");
                }

            }
        });
    }
    /**
     * 支付
     *
     * @param order
     */
    public void getPayOrder02(String order) {
        String mOrderId = order;
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
                   onRefresh();
//                    MyToast.show(mView,"支付成功");
//                    Intent intent = new Intent(mView, OrderPaySuccessActivity.class);
//                    intent.putExtra(Constance.order, mOrderObject.toJSONString());
//                    intent.putExtra(Constance.state, 1);
                    timer.cancel();
//                    mView.startActivity(intent);
//                    mView.finish();
                }

            }
        });
    }
}
