package bc.juhaohd.com.controller;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.adapter.BaseAdapterHelper;
import bc.juhaohd.com.adapter.QuickAdapter;
import bc.juhaohd.com.bean.AppVersion;
import bc.juhaohd.com.bean.AttrBean;
import bc.juhaohd.com.bean.GoodsBean;
import bc.juhaohd.com.bean.GroupBuy;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.MainActivity;
import bc.juhaohd.com.ui.activity.MainNewActivity;
import bc.juhaohd.com.ui.activity.MainNewForJuHaoActivity;
import bc.juhaohd.com.ui.activity.product.ProductDetailHDActivity;
import bc.juhaohd.com.ui.activity.product.ProductDetailHDNewActivity;
import bc.juhaohd.com.ui.view.ShowDialog;
import bc.juhaohd.com.utils.ImageLoadProxy;
import bc.juhaohd.com.utils.NetWorkUtils;
import bc.juhaohd.com.utils.UIUtils;
import bc.juhaohd.com.utils.upload.UpAppUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.CommonUtil;
import bocang.utils.LogUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/3/17 10:24
 * @description :
 */
public class MainNewController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener, View.OnClickListener {
    private TextView unMessageReadTv;
    private MainNewActivity mView;
    private String mAppVersion;
    private TextView tv_select_choose;
    private ListView lv_type;
    private TextView tv_current_select;
    private TextView tv_none_sort;
    private RelativeLayout rl_search;
    private EditText et_search;
    private PullToRefreshLayout mFilterContentView;
    private PullableGridView gridView;
    private QuickAdapter typeAdapter;
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
    private String current_three_filter;

    public MainNewController(MainNewActivity v) {
        mView = v;
        initView();
        initViewData();
    }


    private void initViewData() {
        page = 1;
//        boolean initFilterDropDownView =  true;

        pd.setVisibility(View.VISIBLE);
        if (mView.isAttrloadData) {
            return;
        }
        if (mView.isTypeloadData) {
            return;
        }
        if(!TextUtils.isEmpty(mView.filter_type)){
            tv_current_select.setText(mView.filter_type);
        }
        sendAttrList();
//        selectProduct(page, "20", null, null, null);
    }
    public void sendAttrList() {
        mNetWork.sendAttrList("yes", this);

    }
    private void initView() {
        tv_select_choose = (TextView) mView.findViewById(R.id.tv_select_choose);
        lv_type = (ListView) mView.findViewById(R.id.lv_type);
        tv_current_select = (TextView) mView.findViewById(R.id.tv_current_select);
        tv_none_sort = (TextView) mView.findViewById(R.id.tv_none_sort);

        rl_search = (RelativeLayout) mView.findViewById(R.id.rl_search);
        et_search = (EditText) mView.findViewById(R.id.et_search);
        et_search.setText(mView.keyword);
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
        typeAdapter = new QuickAdapter<AttrBean.Attr_list>(mView, R.layout.item_attr){

            @Override
            protected void convert(BaseAdapterHelper helper, AttrBean.Attr_list item) {
            helper.setText(R.id.tv_attr,""+item.getAttr_value());
                if(helper.getPosition()==currentPosition){
                    helper.setBackgroundColor(R.id.ll_bg,mView.getResources().getColor(R.color.bg_item_selected));
                }else {
                    helper.setBackgroundColor(R.id.ll_bg,mView.getResources().getColor(R.color.bg_item_normal));
                }
            }
        };
        goodsAdapter = new QuickAdapter<GoodsBean>(mView, R.layout.item_gridview_goodes){
            @Override
            protected void convert(BaseAdapterHelper helper, GoodsBean item) {
                Object name=item.getName();
                if(null!=name){
                    helper.setText(R.id.tv_name,item.getName().toString());
                }
                helper.setText(R.id.tv_price," "+item.getCurrent_price());
                GoodsBean.Default_photo default_photo=item.getDefault_photo();
                if(null!=default_photo){
                    String imageUrl=item.getDefault_photo().getLarge();
                    ImageLoadProxy.displayImage(imageUrl, (ImageView) helper.getView(R.id.iv_photo));
                }
            }
        };
        attrAlladapter = new QuickAdapter<AttrBean>(mView, R.layout.item_attr_all){

            @Override
            protected void convert(final BaseAdapterHelper basehelper, AttrBean item) {
                basehelper.setText(R.id.tv_title,item.getFilter_attr_name());
                final GridView gridView=basehelper.getView(R.id.gv_item);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        for(int i=0;i<filterArray[basehelper.getPosition()].length;i++){
                            filterArray[basehelper.getPosition()][i]=false;
                        }
                        filterArray[basehelper.getPosition()][position]=true;
                        ((QuickAdapter)gridView.getAdapter()).notifyDataSetChanged();
                        editFilter(basehelper.getPosition(),position);

                    }
                });

                gridView.setAdapter(new QuickAdapter<AttrBean.Attr_list>(mView,R.layout.item_text) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, AttrBean.Attr_list item) {
                        TextView textView=helper.getView(R.id.textview);
                        if(filterArray[basehelper.getPosition()][helper.getPosition()]){
                            textView.setSelected(true);
                            helper.setVisible(R.id.iv_delete,true);
                        }else {
                            textView.setSelected(false);
                            helper.setVisible(R.id.iv_delete,false);
                        }
                        helper.setText(R.id.textview,item.getAttr_value());
                    }
                });
                ((QuickAdapter)gridView.getAdapter()).replaceAll(item.getAttr_list());
                ViewGroup.LayoutParams layoutParams= gridView.getLayoutParams();
                double count=item.getAttr_list().size();
                if(count>0){
                    int row= (int) Math.ceil(count/3.0);
                    layoutParams.height=UIUtils.dip2PX(68*row)+UIUtils.dip2PX(30*(row-1));
                    gridView.setLayoutParams(layoutParams);
                }

            }
        };
        lv_type.setAdapter(typeAdapter);
        gridView.setAdapter(goodsAdapter);
        lv_filter.setAdapter(attrAlladapter);

        lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = position;
                typeAdapter.notifyDataSetChanged();
                tv_current_select.setText(""+attrBeen.get(position).getAttr_value());
//                mView.category=attrBeen.get(position).getAttr_value();
                if(null==attrAllBean||attrAllBean.size()<=0){
                    sendAttrList();
                    return;
                }
                String filter = "";
                for (int i = 0; i < index + 1; i++) {
                    if (i == index) {
                        filter += attrAllBean.get(currentTitlePosition).getAttr_list().get(position).getId();
                    } else {
                        filter += "0.";
                    }
                }
                mView.filter_attr = filter;
                if (AppUtils.isEmpty(mView.filter_attr))
                    return;
                pd.setVisibility(View.VISIBLE);
                page=1;
                isScrollToTop=true;
                selectProduct(page, "20", null, null, null);
            }
        });
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

        current_three_filter = "";
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
                        current_three_filter +=attrAllBean.get(i).getAttr_list().get(j).getId();
                    }else {
                        current_three_filter +=attrAllBean.get(i).getAttr_list().get(j).getId()+".";
                    }
                    break;
                }
            }
            if(!hasSelect){
                if(i==filterArray.length-1){
                    current_three_filter +="0";
                }else {
                    current_three_filter +="0.";
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

        mView.filter_attr = current_three_filter;

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
        mNetWork.sendGoodsList(page, per_page, brand, mView.category, mView.filter_attr, shop, mView.keyword, mSortKey, mSortValue, this);
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

                selectProduct(page, "20", null, null, null);


                typeAdapter.replaceAll(attrBeen);
                attrAlladapter.replaceAll(attrAllBean);
                break;
        }
    }

    private void getDataSuccess(JSONArray array) {
        List<GoodsBean> temp=new ArrayList<>();
//        LogUtils.logE("temp:",array.toString());

        for (int i = 0; i < array.length(); i++) {
            try {

                temp.add(new Gson().fromJson(String.valueOf(array.getJSONObject(i)).replace("=\\",""),GoodsBean.class));
            }catch(Exception e){
                GoodsBean goodsBean=new GoodsBean();
                goodsBean.setName(array.getJSONObject(i).getString(Constance.name));
                goodsBean.setCurrent_price(array.getJSONObject(i).getString(Constance.current_price));
                goodsBean.setDefault_photo(new Gson().fromJson(String.valueOf(array.getJSONObject(i).getJSONObject(Constance.default_photo)), GoodsBean.Default_photo.class));
                goodsBean.setId((array.getJSONObject(i).getString(Constance.id)));
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
    public void selectSortType(int type) {

        switch (type) {
            case R.id.competitive_tv:
                mSortKey = "3";//精品
                mSortValue = "1";//排序
                break;
            case R.id.new_tv:
                mSortKey = "5";//新品
                mSortValue = "1";//排序
                break;
            case R.id.hot_tv:
                mSortKey = "4";//热销
                mSortValue = "1";//排序
                break;
            case R.id.tv_none_sort:
                mSortKey="";//综合
                mSortValue="";//综合
                break;
        }
        page = 1;
        selectProduct(1, "20", null, null, null);
    }

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
                page=1;
                selectProduct(page,"20",null,null,null);
                break;
        }
    }


}
