package bc.juhaohd.com.controller.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baiiu.filter.util.UIUtil;
import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.ListViewForScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.listener.INetworkCallBack02;
import bc.juhaohd.com.listener.INumberInputListener;
import bc.juhaohd.com.listener.ITwoCodeListener;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.product.ProductDetailHDActivity;
import bc.juhaohd.com.ui.activity.product.ProductDetailHDNewActivity;
import bc.juhaohd.com.ui.activity.programme.DiyActivity;
import bc.juhaohd.com.ui.activity.programme.ImageDetailActivity;
import bc.juhaohd.com.ui.adapter.ParamentAdapter;
import bc.juhaohd.com.ui.view.NumberInputView;
import bc.juhaohd.com.ui.view.popwindow.TwoCodeSharePopWindow;
import bc.juhaohd.com.utils.ImageUtil;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.UIUtils;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/4/1 14:39
 * @description :
 */
public class ProductDetailHDController extends BaseController implements INetworkCallBack {
    private ProductDetailHDNewActivity mView;
    private ConvenientBanner mConvenientBanner;
    private List<String> paths = new ArrayList<>();
    private int mIsLike = 0;
    private ListViewForScrollView properties_lv;
    private JSONArray propertiesList;
    private ProAdapter mAdapter;
    private JSONObject itemObject;
    private TextView name_tv, proPriceTv;
    private ParamentAdapter mParamentAdapter;
    private ListViewForScrollView parameter_lv;
    public WebView mWebView;
    private ImageView collectIv;
    private NumberInputView mNumberInputView;
    private String mPrice = "";
    private Intent mIntent;
    private int mAmount = 1;
    private String mProperty = "";
    private ImageView two_code_iv;
    private TextView old_price;
    private String mOldPrice;


    public ProductDetailHDController(ProductDetailHDActivity v) {
//        mView = v;
        initView();
        initViewData();
    }

    public ProductDetailHDController(ProductDetailHDNewActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        sendProductDetail();
        sendProductDetail02();

    }


    /**
     * 产品详情
     */
    public void sendProductDetail() {
        mView.setShowDialog(true);
        mView.setShowDialog("载入中...");
        mView.showLoading();

        String id = mView.productId;
        if (AppUtils.isEmpty(id))
            return;
        mNetWork.sendProductDetail02(id + "", new INetworkCallBack02() {
            @Override
            public void onSuccessListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                getProductDetail(ans.getJSONObject(Constance.product));
//                sendGoCart();
            }

            @Override
            public void onFailureListener(String requestCode, com.alibaba.fastjson.JSONObject ans) {
                mView.hideLoading();
                MyToast.show(mView, "数据异常!");
            }
        });
    }


    /**
     * 产品详情
     */
    public void sendProductDetail02() {
        mNetWork.sendProductDetail(mView.productId, this);
    }

    /**
     * 获取产品详情信息
     */
    private void getProductDetail(com.alibaba.fastjson.JSONObject productObject) {
        final String value = productObject.getString(Constance.goods_desc);
        mIsLike = productObject.getInteger(Constance.is_liked);
        final String productName = productObject.getString(Constance.name);
        mPrice = productObject.getString(Constance.current_price);
        mOldPrice = new DecimalFormat("###.00").format(Double.parseDouble(mPrice)*1.6)+"";

        name_tv.setText(productName);
        com.alibaba.fastjson.JSONArray array = productObject.getJSONArray(Constance.photos);
        for (int i = 0; i < array.size(); i++) {
            paths.add(array.getJSONObject(0).getString(Constance.large));
        }
        getWebView(value);
        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, paths);
        mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});
        propertiesList = productObject.getJSONArray(Constance.properties);
        mAdapter = new ProAdapter();
        properties_lv.setAdapter(mAdapter);
        JSONArray attachArray = productObject.getJSONArray(Constance.attachments);
        mParamentAdapter = new ParamentAdapter(attachArray, mView);
        parameter_lv.setAdapter(mParamentAdapter);
        parameter_lv.setDivider(null);//去除listview的下划线
        selectCollect();
        JSONArray propertieArray = productObject.getJSONArray(Constance.properties);


        ProductDetailHDNewActivity.isXianGou=false;
        com.alibaba.fastjson.JSONObject group_buy=productObject.getJSONObject(Constance.group_buy);
        LogUtils.logE("group:buty", String.valueOf(group_buy));
        if(null!=group_buy&&!"212".equals(""+group_buy)){
            int isFinish=group_buy.getInteger(Constance.is_finished);
            if(isFinish==0){
                ProductDetailHDNewActivity.isXianGou=true;
            }
        }
        if(ProductDetailHDNewActivity.isXianGou) {
//            time_ll.setVisibility(View.VISIBLE);
            mOldPrice = productObject.getString(Constance.current_price);
            try {
                mPrice=group_buy.getJSONObject(Constance.ext_info).getJSONArray(Constance.price_ladder).getJSONObject(0).getString(Constance.price);
            }catch (Exception e){
                mPrice=mOldPrice;
            }

        }
        old_price.setText("￥" +mOldPrice);
        old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        proPriceTv.setText("￥" + mPrice);

        if (propertieArray.size() > 0) {
            JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
            final String name = propertieArray.getJSONObject(0).getString(Constance.name);
            if (!AppUtils.isEmpty(attrsArray)) {
                int price = attrsArray.getJSONObject(0).getInteger(Constance.attr_price);
                String parament = attrsArray.getJSONObject(0).getString(Constance.attr_name);
//                double currentPrice = Double.parseDouble(mPrice) + price;
//                mOldPrice=mOldPrice+price;
                proPriceTv.setText("￥" + mPrice);
                old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                old_price.setText("￥" +mOldPrice);
                mProperty = attrsArray.getJSONObject(0).toJSONString();
            }
        }else {

        }


    }

    /**
     * 收藏图标状态
     */
    private void selectCollect() {
        if (mIsLike == 0) {
            collectIv.setImageResource(R.drawable.ic_collect_normal);
        } else {
            collectIv.setImageResource(R.drawable.ic_collect_press);
        }
    }

    /**
     * 分享产品
     */
    public void sendShareProduct() {
        shareUrl();
    }

    /**
     * 联系客服
     */
    public void sendCall() {
        mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
    }

    /**
     * 购物车列表
     */
    public void sendcollectProduct() {
        mView.setShowDialog(true);
        mView.setShowDialog("正在收藏中!");
        mView.showLoading();
        String id = mView.productId + "";
        if (mIsLike == 0) {
            mNetWork.sendAddLikeCollect(id, this);
            mIsLike = 1;
        } else {
            mNetWork.sendUnLikeCollect(id, this);
            mIsLike = 0;
        }
    }

    /**
     * 配配看
     */
    public void sendDiy() {
        if (AppUtils.isEmpty(mView.goodses)) {
            MyToast.show(mView, "还没加载完毕，请稍后再试");
            return;
        }
        mIntent = new Intent(mView, DiyActivity.class);
        mIntent.putExtra(Constance.product, mView.goodses);
        mIntent.putExtra(Constance.property, mView.mProperty);
        IssueApplication.mSelectProducts.add(mView.goodses);
        mView.startActivity(mIntent);
        mView.finish();
    }

    /**
     * 加入购物车
     */
    public void sendGoCart() {
        mView.setShowDialog(true);
        mView.setShowDialog("正在加入购物车中...");
        mView.showLoading();

        sendGoShoppingCart(mView.productId + "", mProperty, mAmount);
    }


    private void shareUrl() {
        if (AppUtils.isEmpty(mView.goodses))
            return;
        String title = "来自 " + mView.goodses.getString(Constance.name) + " 产品的分享";
        MyShare.get(mView).putString(Constance.sharePath, mView.goodses.getString(Constance.share_url));
        MyShare.get(mView).putString(Constance.shareRemark, title);
        TwoCodeSharePopWindow popWindow = new TwoCodeSharePopWindow(mView, mView);
        popWindow.onShow(mView.main_ll);
        popWindow.setListener(new ITwoCodeListener() {
            @Override
            public void onTwoCodeChanged(String path) {

            }
        });
    }

    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    /**
     * 加入购物车
     *
     * @param product
     * @param property
     * @param mount
     */
    private void sendGoShoppingCart(String product, String property, int mount) {
        LogUtils.logE("sendShopcar","product:"+product+",propert:"+property+",mount:"+mount);
        mNetWork.sendShoppingCart(product, property, mount, this);
    }

    @Override
    public void onSuccessListener(String requestCode, bocang.json.JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.ADDCART:
                MyToast.show(mView, UIUtils.getString(R.string.go_cart_ok));
                sendShoppingCart();
                break;
            case NetWorkConst.GETCART:
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    IssueApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                            .getJSONObject(0).getJSONArray(Constance.goods).length();
                } else {
                    IssueApplication.mCartCount = 0;
                }
                EventBus.getDefault().post(Constance.CARTCOUNT);
                break;
            case NetWorkConst.ADDLIKEDPRODUCT:
                selectCollect();
                break;
            case NetWorkConst.ULIKEDPRODUCT:
                selectCollect();
                break;
            case NetWorkConst.PRODUCTDETAIL:
                mView.goodses = ans.getJSONObject(Constance.product);
                if(mView.goodses!=null){
                    String title = "来自 " + mView.goodses.getString(Constance.name) + " 产品的分享";
//                    String url=mView.goodses.getString(Constance.share_url);
                    String url=NetWorkConst.SHAREPRODUCT_NEW+mView.goodses.getString(Constance.id);

                    MyShare.get(mView).putString(Constance.sharePath, url);
                    MyShare.get(mView).putString(Constance.shareRemark, title);
//                    LogUtils.logE("shareUrl",url);
                    two_code_iv.setImageBitmap(ImageUtil.createQRImage(url, UIUtils.dip2PX(164), UIUtils.dip2PX(164)));
                }

                break;
        }

    }

    @Override
    public void onFailureListener(String requestCode, bocang.json.JSONObject ans) {
        mView.hideLoading();
        if (null == mView || mView.isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        AppDialog.messageBox(ans.getString(Constance.error_desc));
    }


    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            // 你可以通过layout文件来创建，也可以像我一样用代码创建z，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            imageView.setImageResource(R.drawable.bg_default);
            ImageLoader.getInstance().displayImage(data, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mView, ImageDetailActivity.class);
                    intent.putExtra(Constance.photo, paths.get(position));
                    mView.startActivity(intent);
                }
            });
        }
    }


    private void initView() {
        if(mView==null){
            return;
        }
        mConvenientBanner = (ConvenientBanner) mView.findViewById(R.id.convenientBanner);
        properties_lv = (ListViewForScrollView) mView.findViewById(R.id.properties_lv);
        name_tv = (TextView) mView.findViewById(R.id.name_tv);
        parameter_lv = (ListViewForScrollView) mView.findViewById(R.id.parameter_lv);
        proPriceTv = (TextView) mView.findViewById(R.id.proPriceTv);
        collectIv = (ImageView) mView.findViewById(R.id.collectIv);
        old_price = (TextView) mView.findViewById(R.id.old_price);

        mWebView = (WebView) mView.findViewById(R.id.webView);
        two_code_iv = (ImageView) mView.findViewById(R.id.iv_code);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mNumberInputView = (NumberInputView) mView.findViewById(R.id.number_input_et);
        mNumberInputView.setMax(10000);
        mNumberInputView.setListener(new INumberInputListener() {
            @Override
            public void onTextChange(int index) {
                mAmount = index;
            }
        });

    }

    /**
     * 加载网页
     *
     * @param htmlValue
     */
    private void getWebView(String htmlValue) {
        String html = htmlValue;
        html=html.replace("src=\"http://www.juhao.com","src=\"");
        html=html.replace("src=\"", "src=\"" + NetWorkConst.SCENE_HOST);
//        html = html.replace("<p><img src=\"", "<img src=\"" + NetWorkConst.SCENE_HOST);
//        html = html.replace("</p>", "");
        html = html.replace("jpg\"/>", "jpg\"/><br/>");

        html = "<meta name=\"viewport\" content=\"width=device-width\"> <div style=\"text-align:center\">" + html + " </div>";
        LogUtils.logE("html",html);
        mWebView.loadData(html, "text/html; charset=UTF-8", null);//这种写法可以正确解析中文
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private class ProAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (null == propertiesList)
                return 0;
            return propertiesList.size();
        }


        @Override
        public JSONObject getItem(int position) {
            if (null == propertiesList)
                return null;
            return propertiesList.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_properties, null);
                //
                holder = new ViewHolder();
                holder.properties_name = (TextView) convertView.findViewById(R.id.properties_name);
                holder.itemGridView = (GridViewForScrollView) convertView.findViewById(R.id.itemGridView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String name = propertiesList.getJSONObject(position).getString(Constance.name);
            holder.properties_name.setText(name + ":");

            if (holder.itemGridView != null) {
                final JSONArray attrsList = propertiesList.getJSONObject(position).getJSONArray(Constance.attrs);
                final ItemProAdapter gridViewAdapter = new ItemProAdapter(attrsList);
                holder.itemGridView.setAdapter(gridViewAdapter);
                holder.itemGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        itemObject = attrsList.getJSONObject(position);
                        int ProperPrice = itemObject.getInteger(Constance.attr_price);
                        gridViewAdapter.mCurrentPoistion = position;
                        gridViewAdapter.notifyDataSetChanged();
                        proPriceTv.setText("￥" + (Double.parseDouble(mPrice) + ProperPrice));
                        old_price.setText("￥" + (Double.parseDouble(mOldPrice) + ProperPrice));
                        old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        mProperty = itemObject.toJSONString();
                    }
                });
            }

            return convertView;
        }


        class ViewHolder {
            TextView properties_name;
            GridViewForScrollView itemGridView;
        }
    }


    private class ItemProAdapter extends BaseAdapter {
        public int mCurrentPoistion = 0;
        JSONArray mDatas;

        public ItemProAdapter(JSONArray datas) {
            this.mDatas = datas;
        }

        @Override
        public int getCount() {
            if (null == mDatas)
                return 0;
            return mDatas.size();
        }


        @Override
        public JSONObject getItem(int position) {
            if (null == mDatas)
                return null;
            return mDatas.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_properties02, null);
                //
                holder = new ViewHolder();
                holder.item_tv = (TextView) convertView.findViewById(R.id.item_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.item_tv.setText(mDatas.getJSONObject(position).getString(Constance.attr_name).trim());
            holder.item_tv.setSelected(mCurrentPoistion == position ? true : false);

            return convertView;
        }

        class ViewHolder {
            TextView item_tv;
        }
    }
}
