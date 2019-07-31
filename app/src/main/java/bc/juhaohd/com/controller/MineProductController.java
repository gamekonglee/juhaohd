package bc.juhaohd.com.controller;

import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.adapter.BaseAdapterHelper;
import bc.juhaohd.com.adapter.QuickAdapter;
import bc.juhaohd.com.bean.AttrBean;
import bc.juhaohd.com.bean.Default_photo;
import bc.juhaohd.com.bean.GoodsBean;
import bc.juhaohd.com.bean.GroupBuy;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.MainNewForJuHaoActivity;
import bc.juhaohd.com.ui.activity.product.ProductDetailHDNewActivity;
import bc.juhaohd.com.ui.activity.user.MyProductActivity;
import bc.juhaohd.com.utils.ImageLoadProxy;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/3/17 10:24
 * @description :
 */
public class MineProductController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener, View.OnClickListener {
    private MyProductActivity mView;
    private TextView tv_select_choose;
    private PullToRefreshLayout mFilterContentView;
    private PullableGridView gridView;
    private QuickAdapter goodsAdapter;
    private List<AttrBean.Attr_list> attrBeen;
    private List<GoodsBean> goodsBeen;
    public int page;
    private ProgressBar pd;
    public String mSortKey;
    public String mSortValue;
    private boolean isScrollToTop;
    private int index;
    private int currentPosition=0;
    private List<AttrBean> attrAllBean;
    private int currentTitlePosition;
    private ListView lv_filter;
    private QuickAdapter attrAlladapter;
    private TextView tv_reset;
    private TextView tv_ensure;
    private LinearLayout ll_filter;
    private boolean[][] filterArray;
    private LinearLayout ll_null_product;
    private DecimalFormat df;

    public MineProductController(MyProductActivity v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        page = 1;
//        boolean initFilterDropDownView =  true;

        pd.setVisibility(View.VISIBLE);
        String inviteCode= MyShare.get(mView).getString(Constance.invite_code);
        JSONObject mUserObject=IssueApplication.mUserObject;



        if(!TextUtils.isEmpty(inviteCode)&&inviteCode.equals("bocang")||mUserObject!=null){
            selectProduct(page,"20",null,null,null);
        }else {
            if(pd!=null){
                pd.setVisibility(View.GONE);
            }
                ll_null_product.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
        }

//        if (mView.isAttrloadData) {
//            return;
//        }
//        if (mView.isTypeloadData) {
//            return;
//        }
//        if(!TextUtils.isEmpty(mView.filter_type)){
//            tv_current_select.setText(mView.filter_type);
//        }
//        sendAttrList();
//        selectProduct(page, "20", null, null, null);
    }

    private void initView() {
        tv_select_choose = (TextView) mView.findViewById(R.id.tv_select_choose);
//        et_search.setText(mView.keyword);
        mFilterContentView = (PullToRefreshLayout) mView.findViewById(R.id.mFilterContentView);
        mFilterContentView.setOnRefreshListener(this);
        gridView = (PullableGridView) mView.findViewById(R.id.gridView);
        pd = (ProgressBar) mView.findViewById(R.id.pd);
        ll_filter = (LinearLayout) mView.findViewById(R.id.ll_filter);
        View view1=mView.findViewById(R.id.view_diss);
        View view2=mView.findViewById(R.id.view_diss2);
        lv_filter = (ListView) mView.findViewById(R.id.lv_fitler);
        tv_reset = (TextView) mView.findViewById(R.id.tv_reset);
        tv_ensure = (TextView) mView.findViewById(R.id.tv_ensure);
        ll_null_product = mView.findViewById(R.id.ll_null_product);
        mView.top_iv.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
        tv_ensure.setOnClickListener(this);
        view2.setOnClickListener(this);
        view1.setOnClickListener(this);
        tv_select_choose.setOnClickListener(this);
        attrAllBean = new ArrayList<>();
        attrBeen = new ArrayList<>();
        goodsBeen = new ArrayList<>();
        currentPosition=0;
        currentTitlePosition = 0;
//        unMessageReadTv = (TextView) mView.findViewById(R.id.unMessageReadTv);
        df = new DecimalFormat("###.00");
        goodsAdapter = new QuickAdapter<GoodsBean>(mView, R.layout.item_gridview_goodes){
            @Override
            protected void convert(BaseAdapterHelper helper, GoodsBean item) {
                Object name=item.getName();
                if(null!=name){
                    helper.setText(R.id.tv_name,item.getName().toString());
                }
                    helper.setText(R.id.tv_price," "+item.getCurrent_price());
                helper.setText(R.id.tv_old_price,"¥"+ df.format(Double.parseDouble(item.getCurrent_price())*1.6));
                Default_photo default_photo=item.getDefault_photo();
                if(null!=default_photo){
                    String imageUrl=item.getDefault_photo().getLarge();
                    ImageLoadProxy.displayImage(imageUrl, (ImageView) helper.getView(R.id.iv_photo));
                }
            }
        };

        gridView.setAdapter(goodsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(mView, ProductDetailHDNewActivity.class);
//        int productId = goodses.getJSONObject(position).getInt(Constance.id);
                if(goodsBeen==null||goodsBeen.size()<=0){
                    return;
                }
                String productId = goodsBeen.get(position).getId();
                mIntent.putExtra(Constance.product, productId);
                mView.startActivity(mIntent);
            }
        });
    }

    private void editFilter(int index, int itemPosition) {

        String filter = "";
//        for (int i = 0; i < 3; i++) {
//            if (i == attrAllBean.get(index).getIndex()) {
//                filter += attrAllBean.get(index).getAttr_list().get(itemPosition).getId();
//            } else {
//                if(i==5){
//                    filter += "0";
//                }else {
//                    filter += "0.";
//                }
//            }
//        }
        for(int i=0;i<filterArray.length;i++){
            boolean hasSelect=false;
            for(int j=0;j<filterArray[i].length;j++){
                if(filterArray[i][j]){
                    hasSelect=true;
                    if(i==filterArray.length-1){
                        filter+=attrAllBean.get(i).getAttr_list().get(j).getId();
                    }else {
                        filter+=attrAllBean.get(i).getAttr_list().get(j).getId()+".";
                    }
                    break;
                }
            }
            if(!hasSelect){
                if(i==filterArray.length-1){
                    filter+="0";
                }else {
                    filter+="0.";
                }

            }
        }
//        Toast.makeText(mView, "filter:"+filter, Toast.LENGTH_SHORT).show();
//        for (int i = 0; i < mFilterList.length(); i++) {
//            if (i == mFilterList.length() - 1) {
//                filter += mAdapter.mAttrList.get(i).getId();
//            } else {
//                filter += mAdapter.mAttrList.get(i).getId() + ".";
//            }
//        }

        mView.filter_attr = filter;

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
    /**
     * 产品查询
     *
     * @param page
     * @param per_page
     * @param brand
     * @param category
     * @param shop
     */
    public void selectProduct(int page, String per_page, String brand, String category, String shop) {
        pd.setVisibility(View.VISIBLE);
        isScrollToTop=true;
        mNetWork.selectYIJIProduct(page,20+"", new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
                dismissRefesh();
                if(pd!=null){
                    pd.setVisibility(View.GONE);
                }
                JSONArray array=ans.getJSONArray(Constance.products);
                if(array==null||array.length()==0){
                    ll_null_product.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                }else {
                    ll_null_product.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    goodsBeen = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        try {

                            goodsBeen.add(new Gson().fromJson(String.valueOf(array.getJSONObject(i)),GoodsBean.class));
                        }catch (Exception e){
                            GoodsBean goodsBean=new GoodsBean();
                            goodsBean.setId(String.valueOf(array.getJSONObject(i).getInt(Constance.id)));
                            goodsBean.setName(array.getJSONObject(i).getString(Constance.name));
                            goodsBean.setCurrent_price(array.getJSONObject(i).getString(Constance.current_price));
                            goodsBean.setPrice(array.getJSONObject(i).getString(Constance.price));
                            goodsBean.setOriginal_img(array.getJSONObject(i).getString(Constance.original_img));
                            Default_photo default_photo=new Default_photo();
                            if(array.getJSONObject(i).getJSONObject(Constance.default_photo)!=null){
                                default_photo.setThumb(array.getJSONObject(i).getJSONObject(Constance.default_photo).getString(Constance.thumb));
                            }
                            goodsBean.setDefault_photo(default_photo);
                            goodsBeen.add(goodsBean);
                        }
                    }
                    goodsAdapter.replaceAll(goodsBeen);
                }


            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                if(pd!=null){
                    pd.setVisibility(View.GONE);
                }
                dismissRefesh();
            }
        });
    }




    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.PRODUCT:
                pd.setVisibility(View.GONE);
                //                if (null == mView || mView.getActivity().isFinishing())
                //                    return;

                if (null != mFilterContentView) {
                    dismissRefesh();
                }
                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);

                if (AppUtils.isEmpty(goodsList)) {
                    if (page == 1) {
                        mView.keyword="";
                        MyToast.show(mView, "数据查询不到");
//                        goodses = new JSONArray();
                        goodsBeen=new ArrayList<>();
                    } else {
                        MyToast.show(mView, "数据已经到底啦!");
                    }
                    //


                    dismissRefesh();
                    return;
                }

                getDataSuccess(goodsList);
//                sendAttrList();

                break;
            case NetWorkConst.ATTRLIST:
                JSONArray sceneAllAttrs = ans.getJSONArray(Constance.goods_attr_list);
                if(mView.filter_attr_name.equals("类型")){
                    index=0;
                }else if(mView.filter_attr_name.equals("空间")){
                    index=1;
                }else if(mView.filter_attr_name.equals("风格")){
                    index=4;
                }
                boolean hasFilterType=false;
                for(int i=0;i<sceneAllAttrs.length();i++){
                    if(sceneAllAttrs.getJSONObject(i).getInt(Constance.index)==index){
                        currentTitlePosition=i;
                        JSONArray jsonArray=sceneAllAttrs.getJSONObject(i).getJSONArray(Constance.attr_list);
                        for(int j=0;j<jsonArray.length();j++){

                            attrBeen.add(new Gson().fromJson(String.valueOf(jsonArray.getJSONObject(j)),AttrBean.Attr_list.class));
                            if(!TextUtils.isEmpty(mView.filter_type)&&currentTitlePosition==i
                                    &&attrBeen.get(attrBeen.size()-1).getAttr_value().contains(mView.filter_type)){
                                    currentPosition=j;
                                    hasFilterType=true;
                            }
                        }
                    }
                    attrAllBean.add(new Gson().fromJson(String.valueOf(sceneAllAttrs.getJSONObject(i)),AttrBean.class));
                }

                if(hasFilterType){
                    String filter = "";
                    for (int x = 0; x < index + 1; x++) {
                        if (x == index) {
                            filter += attrAllBean.get(currentTitlePosition).getAttr_list().get(currentPosition).getId();
                        } else {
                            filter += "0.";
                        }
                    }
                    mView.filter_attr = filter;
                    pd.setVisibility(View.VISIBLE);
                    page=1;
                    isScrollToTop=true;
                }
                filterArray = new boolean[attrAllBean.size()][];
                for(int i = 0; i< filterArray.length; i++){
                    filterArray[i]=new boolean[attrAllBean.get(i).getAttr_list().size()];
                }

//                selectProduct(page, "20", null, null, null);
                attrAlladapter.replaceAll(attrAllBean);
                break;
        }
    }

    private void getDataSuccess(JSONArray array) {
        List<GoodsBean> temp=new ArrayList<>();
//        LogUtils.logE("temp:",array.toString());

        for (int i = 0; i < array.length(); i++) {

            try {
                temp.add(new Gson().fromJson(String.valueOf(array.getJSONObject(i)).replace("=\\","").replace("\\\"",""),GoodsBean.class));
            }catch (Exception e){
                GoodsBean goodsBean=new GoodsBean();
                goodsBean.setName(array.getJSONObject(i).getString(Constance.name));
                goodsBean.setCurrent_price(array.getJSONObject(i).getString(Constance.current_price));
                goodsBean.setDefault_photo(new Gson().fromJson(String.valueOf(array.getJSONObject(i).getJSONObject(Constance.default_photo)), Default_photo.class));
                goodsBean.setId(array.getJSONObject(i).getString(Constance.id));
                goodsBean.setGroup_buy(new Gson().fromJson(String.valueOf(array.getJSONObject(i).getJSONObject(Constance.group_buy)),GroupBuy.class));
                temp.add(goodsBean);
            }
        }

        if (1 == page)
            goodsBeen=temp;
//            goodses = array;
        else if (null != goodsBeen&&goodsBeen.size()>0) {
//            for (int i = 0; i < array.length(); i++) {
//                goodses.add(array.getJSONObject(i));
//            }
            goodsBeen.addAll(temp);

            if (AppUtils.isEmpty(temp))
                MyToast.show(mView, "没有更多内容了");
        }
//        mProAdapter.notifyDataSetChanged();
        goodsAdapter.replaceAll(goodsBeen);
        if(isScrollToTop)
        {
            gridView.setSelection(0);
            gridView.smoothScrollToPositionFromTop(0,0);
            isScrollToTop=false;
        }
    }
    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        pd.setVisibility(View.GONE);
        if (null == mView || mView.isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        this.page--;
        if (null != mFilterContentView) {
            dismissRefesh();
        }
    }
    private void dismissRefesh() {
        mFilterContentView.refreshFinish(PullToRefreshLayout.SUCCEED);
        mFilterContentView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }
//    public void selectSortType(int type) {
//
//        switch (type) {
//            case R.id.competitive_tv:
//                mSortKey = "3";//精品
//                mSortValue = "1";//排序
//                break;
//            case R.id.new_tv:
//                mSortKey = "5";//新品
//                mSortValue = "1";//排序
//                break;
//            case R.id.hot_tv:
//                mSortKey = "4";//热销
//                mSortValue = "1";//排序
//                break;
//            case R.id.tv_none_sort:
//                mSortKey="";//综合
//                mSortValue="";//综合
//                break;
//        }
//        page = 1;
//        selectProduct(1, "20", null, null, null);
//    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
//        initFilterDropDownView = false;
        selectProduct(page, "20", null, null, null);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//        initFilterDropDownView = false;
        page = page + 1;
        selectProduct(page, "20", null, null, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_iv:
                gridView.smoothScrollToPositionFromTop(0,0);
                if(goodsAdapter!=null)goodsAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_select_choose:
                if(attrAllBean==null||attrAllBean.size()<=0){
                    Toast.makeText(mView, "加载中，请稍等", Toast.LENGTH_SHORT).show();
                    return;
                }
                ll_filter.setVisibility(View.VISIBLE);
                tv_select_choose.setSelected(true);
                break;
            case R.id.view_diss:
            case R.id.view_diss2:
                ll_filter.setVisibility(View.GONE);
                tv_select_choose.setSelected(false);
                break;
            case R.id.tv_reset:
               for(int i=0;i<filterArray.length;i++){
                   for(int j=0;j<filterArray[i].length;j++){
                       filterArray[i][j]=false;
                   }
               }
                if(attrAlladapter!=null){
                    attrAlladapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_ensure:
                ll_filter.setVisibility(View.GONE);
                tv_select_choose.setSelected(false);
                break;
        }
    }


}
