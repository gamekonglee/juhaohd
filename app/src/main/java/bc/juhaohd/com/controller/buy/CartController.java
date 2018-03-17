package bc.juhaohd.com.controller.buy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.lib.common.hxp.view.ListViewForScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenu;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuCreator;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuItem;
import com.yjn.swipelistview.swipelistviewlibrary.widget.SwipeMenuListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.adapter.BaseAdapterHelper;
import bc.juhaohd.com.adapter.QuickAdapter;
import bc.juhaohd.com.bean.AddressBean;
import bc.juhaohd.com.bean.ShopcarGoodsBean;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.CartActivity;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.buy.ConfirmOrderActivity;
import bc.juhaohd.com.ui.activity.buy.ConfirmOrderNewActivity;
import bc.juhaohd.com.ui.activity.buy.ExInventoryActivity;
import bc.juhaohd.com.ui.fragment.CartFragment;
import bc.juhaohd.com.ui.view.NumberInputView;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/21 14:35
 * @description :
 */
public class CartController extends BaseController implements INetworkCallBack {
    private CartActivity mView;
    private ListViewForScrollView mListView;
    private JSONArray goodses;
    private MyAdapter myAdapter;
    private CheckBox checkAll;
    private TextView money_tv,settle_tv,edit_tv;
    private boolean isStart=false;
    private LinearLayout sum_ll;
    private Boolean isEdit=false;
    private  JSONArray goods;
//    private ProgressBar pd;
    private  JSONObject mAddressObject;
    private ProgressDialog pd;
    private TextView money_suply_tv;
    private TextView tv_count;
    private int count;
    private LinearLayout step_one;
//    private LinearLayout step_two;
//    private TextView tv_tips;
//    private ListViewForScrollView lv_address;
    private int currentStep;
    private TextView tv_jiesuan;
//    private TextView tv_zhifu;
    private int currentSelectedAddress;
    private QuickAdapter addressAdapter;
    private List<AddressBean> addressBeen;
//    private RadioGroup rg_express;
//    private ListView lv_order_product;
    private LinearLayout step_one_cart;
//    private LinearLayout step_two_order;
//    private LinearLayout step_three;
    private LinearLayout step_one_two;
    private QuickAdapter<ShopcarGoodsBean> goodsAdapter;
    private List goodsList;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;

    public CartController(CartFragment v) {
//        mView = v;
        initView();
        initViewData();
    }

    public CartController(CartActivity cartActivity) {
        mView=cartActivity;
        initView();
        initViewData();
    }

    private void initViewData() {
        if(pd==null){
            pd = ProgressDialog.show(mView,"","加载中");
        }else {
            if(!pd.isShowing())pd.show();
        }
        sendAddressList();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                mView.getResources().getDisplayMetrics());
    }


    private void initView() {
        mListView =  mView.findViewById(R.id.cart_lv);
        mListView.setDivider(null);//去除listview的下划线

        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);

//        final SwipeMenuCreator creator = new SwipeMenuCreator() {
//            @Override
//            public void create(SwipeMenu menu) {
//                SwipeMenuItem deleteItem = new SwipeMenuItem(mView);
//
//                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#fe3c3a")));
//                deleteItem.setWidth(dp2px(80));
//                deleteItem.setTitle("删除");
//                deleteItem.setTitleColor(Color.WHITE);
//                deleteItem.setTitleSize(20);
//                menu.addMenuItem(deleteItem);
//            }
//        };
//        mListView.setMenuCreator(creator);
//
//        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                if (index == 0) {
//                    if(pd==null){
//                        pd = ProgressDialog.show(mView,"","正在删除");
//                    }else {
//                        if(!pd.isShowing())pd.show();
//                    }
//                    String id = goodses.getJSONObject(position).getString(Constance.id);
////                    mDeleteIndex=position;
//                    isLastDelete = false;
//                    deleteShoppingCart(id);
//                }
//                return false;
//            }
//        });
        checkAll = (CheckBox) mView.findViewById(R.id.checkAll);
        money_tv = (TextView) mView.findViewById(R.id.totla_money_tv);
        money_suply_tv = (TextView) mView.findViewById(R.id.summoney_tv);
        settle_tv = (TextView) mView.findViewById(R.id.settle_tv);
        edit_tv = (TextView) mView.findViewById(R.id.edit_tv);
        sum_ll = (LinearLayout) mView.findViewById(R.id.sum_ll);
        tv_count = (TextView) mView.findViewById(R.id.tv_count);
        tv_jiesuan = (TextView) mView.findViewById(R.id.tv_jiesuan);
//        tv_zhifu = (TextView) mView.findViewById(R.id.tv_zhifu);

//        pd = (ProgressBar) mView.getActivity().findViewById(R.id.pd);
//        pd.setVisibility(View.VISIBLE);



        mExtView= (ViewGroup) LayoutInflater.from(mView).inflate(R.layout.alertext_form, null);
        etNum = (EditText) mExtView.findViewById(R.id.etName);
        etNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        imm = (InputMethodManager)mView.getSystemService(Context.INPUT_METHOD_SERVICE);
        step_one_cart = (LinearLayout) mView.findViewById(R.id.step_one_cart);
//        step_two_order = (LinearLayout) mView.findViewById(R.id.step_two_order);
//        step_three = (LinearLayout) mView.findViewById(R.id.step_three);
        step_one = (LinearLayout) mView.findViewById(R.id.step_one);
//        step_two = (LinearLayout) mView.findViewById(R.id.step_two);
        step_one_two = (LinearLayout) mView.findViewById(R.id.step_one_two);
//        tv_tips = (TextView) mView.findViewById(R.id.tv_tips);
//        lv_address = (ListViewForScrollView) mView.findViewById(R.id.order_sv);

//        rg_express = (RadioGroup) mView.findViewById(R.id.radioGroup);
//        rg_express.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.rb_wuliu:
//                        break;
//                    case R.id.rb_kuaidi:
//                        break;
//                }
//            }
//        });
        // 设置图片下载期间显示的图片
// 设置图片Uri为空或是错误的时候显示的图片
// 设置图片加载或解码过程中发生错误显示的图片
// .showImageOnFail(R.drawable.ic_error)
// 设置下载的图片是否缓存在内存中
// 设置下载的图片是否缓存在SD卡中
// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
// 是否考虑JPEG图像EXIF参数（旋转，翻转）
// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
// .displayer(new FadeInBitmapDisplayer(100))//
// 图片加载好后渐入的动画时间
// 构建完成
        options = new DisplayImageOptions.Builder()
                // 设置图片下载期间显示的图片
                .showImageOnLoading(R.drawable.bg_default)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.bg_default)
                // 设置图片加载或解码过程中发生错误显示的图片
                // .showImageOnFail(R.drawable.ic_error)
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
                // .displayer(new FadeInBitmapDisplayer(100))//
                // 图片加载好后渐入的动画时间
                .build();

        // 得到ImageLoader的实例(使用的单例模式)
        imageLoader = ImageLoader.getInstance();
//        lv_order_product = (ListView) mView.findViewById(R.id.listView);
        goodsList = new ArrayList();
        goodsAdapter = new QuickAdapter<ShopcarGoodsBean>(mView, R.layout.item_lv_cart_order) {

            @Override
            protected void convert(BaseAdapterHelper helper, ShopcarGoodsBean item) {
                helper.setText(R.id.nameTv,""+item.getProduct().getName());
                helper.setText(R.id.SpecificationsTv,item.getProperty());
                helper.setText(R.id.numTv,""+item.getAmount());
                helper.setText(R.id.priceTv,"￥"+item.getPrice());
                ImageView imageView=helper.getView(R.id.imageView);
                imageLoader.displayImage(item.getProduct().getDefault_photo().getLarge()
                        , imageView, options);
            }
        };
//        lv_order_product.setAdapter(goodsAdapter);
        currentSelectedAddress = 0;
        addressBeen = new ArrayList<>();
        addressAdapter = new QuickAdapter<AddressBean>(mView, R.layout.item_user_address_new) {
            @Override
            protected void convert(BaseAdapterHelper helper, AddressBean item) {
            helper.setText(R.id.consignee_tv,item.getName());
                helper.setText(R.id.phone_tv,item.getMobile());
                helper.setText(R.id.address_tv,item.getAddress());
                ImageView imageView=helper.getView(R.id.iv_select);
                if(helper.getPosition()==currentSelectedAddress){
                    imageView.setImageDrawable(mView.getResources().getDrawable(R.mipmap.cb_selected_cart));
                }else {
                    imageView.setImageDrawable(mView.getResources().getDrawable(R.mipmap.cb_normal_cart));
                }
            }
        };
//        lv_address.setAdapter(addressAdapter);
    }

    int mDeleteIndex =-1;

    /**
     * 导出清单
     */
    public void exportData() {
        myAdapter.getCartGoodsCheck();
        if(goods.length()==0){
            MyToast.show(mView,"请选择产品");
            return;
        }
        Intent intent=new Intent(mView,ExInventoryActivity.class);
        intent.putExtra(Constance.goods, goods);
        mView.startActivity(intent);
    }


    private void deleteShoppingCart(String goodId) {
        mNetWork.sendDeleteCart(goodId, this);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    /**
     * 编辑
     */
    public void setEdit() {
        if(!isEdit){
            sum_ll.setVisibility(View.GONE);
            settle_tv.setText("删除");
            edit_tv.setText("完成");
            isEdit=true;

        }else{
            sum_ll.setVisibility(View.VISIBLE);
            settle_tv.setText("结算");
            edit_tv.setText("编辑");
            isEdit=false;
        }
    }

    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    public void sendUpdateCart(String good, String amount) {
        mNetWork.sendUpdateCart(good, amount, this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
//        pd.setVisibility(View.INVISIBLE);
        mView.showContentView();
        switch (requestCode) {
            case NetWorkConst.DeleteCART:
                if(isLastDelete==true){
                    isLastDelete=false;
//                    isCheckList.remove(mDeleteIndex);
                    sendShoppingCart();
                    return;
                }
                if(pd!=null&&pd.isShowing())pd.dismiss();
                break;
            case NetWorkConst.UpdateCART:
                sendShoppingCart();
                break;
            case NetWorkConst.GETCART:
                if(pd!=null&&pd.isShowing())pd.dismiss();
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    goodses = ans.getJSONArray(Constance.goods_groups).getJSONObject(0).getJSONArray(Constance.goods);
                    myAdapter.addIsCheckAll(false);
                    myAdapter.notifyDataSetChanged();
                    myAdapter.getTotalMoney();
                    IssueApplication.mCartCount=goodses.length();
                } else {
                    goodses = null;
                    myAdapter.notifyDataSetChanged();
                    myAdapter.getTotalMoney();
                    IssueApplication.mCartCount=0;
                }
//                setCkeckAll(true);
//                sendSettle();

                EventBus.getDefault().post(Constance.CARTCOUNT);

                isStart=true;
                break;
            case NetWorkConst.CONSIGNEELIST:
                if(pd!=null&&pd.isShowing())pd.dismiss();
                JSONArray consigneeList = ans.getJSONArray(Constance.consignees);
                if (consigneeList.length() == 0)
                    return;
                addressBeen=new ArrayList<>();
                for(int i=0;i<consigneeList.length();i++){
                    addressBeen.add(new Gson().fromJson(String.valueOf(consigneeList.getJSONObject(i)),AddressBean.class));
                }
                mAddressObject=consigneeList.getJSONObject(0);
                break;
        }

    }



    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        isLastDelete=false;
        if(pd!=null&&pd.isShowing())pd.dismiss();
        mView.showContentView();
//        pd.setVisibility(View.INVISIBLE);
    }


//    private List<Integer> buyNum = new ArrayList<>();

    private ArrayList<Boolean> isCheckList = new ArrayList<>();

    public void setCkeckAll(Boolean isCheck) {
        if (myAdapter == null || myAdapter.isEmpty()) {
            return;
        }
        myAdapter.setIsCheckAll(isCheck);
        myAdapter.getTotalMoney();
        myAdapter.notifyDataSetChanged();

    }



    /**
     * 结算/删除
     */
    public void sendSettle() {
        myAdapter.getCartGoodsCheck();
        if(goods.length()==0){
            MyToast.show(mView,"请选择产品");
            return;
        }
        currentStep = 2;
//        refreshUI();

        if(!isEdit){
            Intent intent=new Intent(mView,ConfirmOrderNewActivity.class);
            intent.putExtra(Constance.goods,goods);
            intent.putExtra(Constance.money,mMoney);
            intent.putExtra(Constance.address,mAddressObject);
            mView.startActivityForResult(intent,200);

        }else{
            sendDeleteCart();
        }
    }

    private void refreshUI() {
        switch (currentStep){
            case 1:
                Drawable drawable= mView.getResources().getDrawable(R.mipmap.icon_cart_step_two_next);
                drawable.setBounds(0, 0, UIUtils.dip2PX(36), UIUtils.dip2PX(36));
                tv_jiesuan.setCompoundDrawables(drawable,null,null,null);
                tv_jiesuan.setTextColor(mView.getResources().getColor(R.color.tv_999999));

                Drawable drawable2= mView.getResources().getDrawable(R.mipmap.icon_cart_step_three_next);
                drawable2.setBounds(0, 0, UIUtils.dip2PX(36), UIUtils.dip2PX(36));
//                tv_zhifu.setCompoundDrawables(drawable2,null,null,null);
//                tv_zhifu.setTextColor(mView.getResources().getColor(R.color.tv_999999));
                step_one.setVisibility(View.VISIBLE);
                step_one_cart.setVisibility(View.VISIBLE);
                step_one_two.setVisibility(View.VISIBLE);
//                step_two.setVisibility(View.GONE);
//                step_two.setVisibility(View.GONE);
//                step_three.setVisibility(View.GONE);
                break;
            case 2:
                Drawable drawable22= mView.getResources().getDrawable(R.mipmap.icon_cart_step_two_current);
                drawable22.setBounds(0, 0, UIUtils.dip2PX(36), UIUtils.dip2PX(36));
                tv_jiesuan.setCompoundDrawables(drawable22,null,null,null);
                tv_jiesuan.setTextColor(mView.getResources().getColor(R.color.orange_theme));

                Drawable drawable33= mView.getResources().getDrawable(R.mipmap.icon_cart_step_three_next);
                drawable33.setBounds(0, 0, UIUtils.dip2PX(36), UIUtils.dip2PX(36));
//                tv_zhifu.setCompoundDrawables(drawable33,null,null,null);
//                tv_zhifu.setTextColor(mView.getResources().getColor(R.color.tv_999999));
                addressAdapter.replaceAll(addressBeen);
                step_one.setVisibility(View.GONE);
                step_one_cart.setVisibility(View.GONE);
//                step_two.setVisibility(View.VISIBLE);
//                step_two_order.setVisibility(View.VISIBLE);
                step_one_two.setVisibility(View.VISIBLE);
//                step_three.setVisibility(View.GONE);
                for(int i=0;i<goods.length();i++){
                    goodsList.add(new Gson().fromJson(String.valueOf(goods.getJSONObject(i)).replace("=\\"," "),ShopcarGoodsBean.class));
                }
                goodsAdapter.replaceAll(goodsList);
                if(addressBeen==null||addressBeen.size()<=0){
//                    tv_tips.setVisibility(View.VISIBLE);
//                    lv_address.setVisibility(View.GONE);
                }else {
//                    tv_tips.setVisibility(View.GONE);
//                    lv_address.setVisibility(View.VISIBLE);
                }

                break;
            case 3:
                Drawable drawable222= mView.getResources().getDrawable(R.mipmap.icon_cart_step_two_current);
                drawable222.setBounds(0, 0, UIUtils.dip2PX(36), UIUtils.dip2PX(36));
                tv_jiesuan.setCompoundDrawables(drawable222,null,null,null);
                tv_jiesuan.setTextColor(mView.getResources().getColor(R.color.orange_theme));

                Drawable drawable333= mView.getResources().getDrawable(R.mipmap.icon_cart_step_three_curretn);
                drawable333.setBounds(0, 0, UIUtils.dip2PX(36), UIUtils.dip2PX(36));
//                tv_zhifu.setCompoundDrawables(drawable333,null,null,null);
//                tv_zhifu.setTextColor(mView.getResources().getColor(R.color.tv_999999));
                step_one.setVisibility(View.GONE);
                step_one_cart.setVisibility(View.GONE);
//                step_two.setVisibility(View.GONE);
//                step_two_order.setVisibility(View.GONE);
                step_one_two.setVisibility(View.GONE);
//                step_three.setVisibility(View.VISIBLE);
                break;
        }
    }

    private Boolean isLastDelete=false;

    /**
     * 删除购物车数据
     */
    public  void sendDeleteCart(){
        if(pd==null){
            pd=ProgressDialog.show(mView,"","正在删除");
        }else {
            if(!pd.isShowing())pd.show();
        }
        ArrayList<String> deleteList=new ArrayList<>();
        for(int i=0;i<isCheckList.size();i++){
            if(isCheckList.get(i)){
                String id = goodses.getJSONObject(i).getString(Constance.id);
                deleteList.add(id);
            }
        }
        for(int j=0;j<deleteList.size();j++){
            if(j==deleteList.size()-1){
                isLastDelete=true;
            }
//            mDeleteIndex=j;
            deleteShoppingCart(deleteList.get(j));
        }
    }

    float mMoney=0;

    /**
     * 获取收货地址
     */
    public void sendAddressList() {
        mNetWork.sendAddressList(this);
        mNetWork.sendShoppingCart(this);
    }


    private class MyAdapter extends BaseAdapter {
        private DisplayImageOptions options;
        private ImageLoader imageLoader;

        public MyAdapter() {
            options = new DisplayImageOptions.Builder()
                    // 设置图片下载期间显示的图片
                    .showImageOnLoading(R.drawable.bg_default)
                            // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageForEmptyUri(R.drawable.bg_default)
                            // 设置图片加载或解码过程中发生错误显示的图片
                            // .showImageOnFail(R.drawable.ic_error)
                            // 设置下载的图片是否缓存在内存中
                    .cacheInMemory(true)
                            // 设置下载的图片是否缓存在SD卡中
                    .cacheOnDisk(true)
                            // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                            // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
                            // .displayer(new FadeInBitmapDisplayer(100))//
                            // 图片加载好后渐入的动画时间
                    .build(); // 构建完成

            // 得到ImageLoader的实例(使用的单例模式)
            imageLoader = ImageLoader.getInstance();
        }

        public void setIsCheckAll(Boolean isCheck) {
            if(AppUtils.isEmpty(goodses)) return;
            for (int i = 0; i < goodses.length(); i++) {
                isCheckList.set(i, isCheck);
            }
        }

        public void addIsCheckAll(Boolean isCheck) {
            isCheckList=new ArrayList<>();
            for (int i = 0; i < goodses.length(); i++) {
                isCheckList.add(isCheck);
            }

        }

        public void getCartGoodsCheck(){
            goods=new JSONArray();
            for(int i = 0; i < isCheckList.size(); i++){
                if(isCheckList.get(i)){
                    goods.add(goodses.getJSONObject(i));
                }
            }
        }


        public void setIsCheck(int poistion, Boolean isCheck) {

            if(poistion<isCheckList.size())isCheckList.set(poistion, isCheck);
            getTotalMoney();


        }

        /**
         * 获取到总金额
         */
        public void getTotalMoney(){
            float isSumMoney = 0;
            count = 0;
            if(AppUtils.isEmpty(goodses)){
                checkAll.setChecked(false);
                money_tv.setText("￥" +0 + "");
                money_suply_tv.setText("￥" +0 + "");
                tv_count.setText(""+0);
                return;
            }
            for (int i = 0; i < goodses.length(); i++) {
                if (isCheckList.get(i) == true) {
                    double price = goodses.getJSONObject(i).getInt(Constance.price);
                    int num=goodses.getJSONObject(i).getInt(Constance.amount);
                    isSumMoney += (num * price);
                    count++;
                }
            }
            mMoney=isSumMoney;
            money_tv.setText("￥" +isSumMoney + "");
            money_suply_tv.setText("￥" +isSumMoney + "");
            tv_count.setText(""+count);
        }

        @Override
        public int getCount() {
            if (null == goodses)
                return 0;
            return goodses.length();
        }


        @Override
        public JSONObject getItem(int position) {
            if (null == goodses)
                return null;
            return goodses.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_lv_cart, null);

                holder = new ViewHolder();
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.leftTv = (ImageView) convertView.findViewById(R.id.leftTv);
                holder.rightTv = (ImageView) convertView.findViewById(R.id.rightTv);
                holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
                holder.contact_service_tv = (TextView) convertView.findViewById(R.id.contact_service_tv);
                holder.SpecificationsTv = (TextView) convertView.findViewById(R.id.SpecificationsTv);
                holder.numTv = (EditText) convertView.findViewById(R.id.numTv);
                holder.priceTv = (TextView) convertView.findViewById(R.id.priceTv);
                holder.number_input_et = (NumberInputView) convertView.findViewById(R.id.number_input_et);
                holder.iv_delete= (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final JSONObject goodsObject = goodses.getJSONObject(position);
            holder.nameTv.setText(goodsObject.getJSONObject(Constance.product).getString(Constance.name));
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pd==null){
                        pd = ProgressDialog.show(mView,"","正在删除");
                    }else {
                        if(!pd.isShowing())pd.show();
                    }
                    String id = goodses.getJSONObject(position).getString(Constance.id);
//                    mDeleteIndex=position;
                    isLastDelete = true;
                    deleteShoppingCart(id);
                }
            });
            imageLoader.displayImage(goodsObject.getJSONObject(Constance.product).getJSONObject(Constance.default_photo).getString(Constance.large)
                    , holder.imageView, options);

            String property = goodsObject.getString(Constance.property);
            holder.SpecificationsTv.setText(property);
            String price = goodsObject.getString(Constance.price);
            holder.priceTv.setText("￥" + price);
            holder.number_input_et.setMax(10000);//设置数量的最大值

            holder.numTv.setText(goodsObject.getInt(Constance.amount)+"");

            holder.numTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mAlertViewExt==null){
                        mAlertViewExt = new AlertView("提示", "修改购买数量！", "取消", null, new String[]{"完成"},
                                mView, AlertView.Style.Alert, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int position) {
                                if(position != AlertView.CANCELPOSITION){
                                    String num=etNum.getText().toString();
                                    if(num.equals("0")){
                                        MyToast.show(mView,"不能等于0");
                                        return;
                                    }
                                    pd=ProgressDialog.show(mView,"","正在处理中...");
                                    sendUpdateCart(goodsObject.getString(Constance.id), num);
                                }
                            }
                        });

                        etNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                //输入框出来则往上移动
                                boolean isOpen=imm.isActive();
                                mAlertViewExt.setMarginBottom(isOpen&&hasFocus ? 120 :0);
                                System.out.println(isOpen);
                            }
                        });
                        mAlertViewExt.addExtView(mExtView);
                    }
                    etNum.setText(goodsObject.getInt(Constance.amount)+"");
                    mAlertViewExt.show();

                }
            });

            holder.checkBox.setChecked(isCheckList.get(position));

            holder.rightTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    pd=ProgressDialog.show(mView,"","正在处理中...");
                    sendUpdateCart(goodsObject.getString(Constance.id),(goodsObject.getInt(Constance.amount)+1)+"");
                }
            });
            holder.leftTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(goodsObject.getInt(Constance.amount)==1){
                        MyToast.show(mView,"亲,已经到底啦!");
                        return;
                    }
                    pd=ProgressDialog.show(mView,"","正在处理中...");
                    sendUpdateCart(goodsObject.getString(Constance.id),(goodsObject.getInt(Constance.amount)-1)+"");
                }
            });


            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setIsCheck(position, isChecked);
                    getTotalMoney();

                }
            });
            holder.contact_service_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetWorkConst.QQURL)));
                }
            });

            return convertView;
        }

        class ViewHolder {
            CheckBox checkBox;
            ImageView imageView;
            TextView nameTv;
            TextView priceTv;
            TextView SpecificationsTv;
            NumberInputView number_input_et;
            EditText numTv;
            ImageView leftTv,rightTv;
            TextView contact_service_tv;
            ImageView iv_delete;
        }
    }

    private AlertView mAlertViewExt;//窗口拓展例子
    private EditText etNum;//拓展View内容
    private InputMethodManager imm;
    private ViewGroup mExtView;

}
