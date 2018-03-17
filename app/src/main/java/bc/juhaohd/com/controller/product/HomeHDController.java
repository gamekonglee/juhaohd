package bc.juhaohd.com.controller.product;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.google.gson.Gson;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.adapter.BaseAdapterHelper;
import bc.juhaohd.com.adapter.QuickAdapter;
import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.GoodsBean;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.product.ProductDetailHDActivity;
import bc.juhaohd.com.ui.adapter.ProductDropMenuAdapter;
import bc.juhaohd.com.ui.fragment.HomeHDFragment;
import bc.juhaohd.com.utils.ImageLoadProxy;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/3/30 17:26
 * @description :
 */
public class HomeHDController extends BaseController implements OnFilterDoneListener, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, INetworkCallBack {
    private HomeHDFragment mView;
    private DropDownMenu dropDownMenu;
    private JSONArray sceneAllAttrs;
    private PullToRefreshLayout mPullToRefreshLayout;
//    private ProAdapter mProAdapter;
    private PullableGridView mGoodsSv;
    public int page = 1;
//    private JSONArray goodses;
    public boolean initFilterDropDownView;
    private String imageURL = "";
    private Intent mIntent;
    public String keyword;
    private ProgressBar pd;
    public String mSortKey;
    public String mSortValue;
    private QuickAdapter<GoodsBean> goodsBeanQuickAdapter;
    private List<GoodsBean> goodsBeen;
    boolean isScrollToTop;


    public HomeHDController(HomeHDFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    public void initViewData() {
        page = 1;
        initFilterDropDownView = true;
        sendAttrList();
        pd.setVisibility(View.VISIBLE);
        if (mView.isAttrloadData) {
            return;
        }
        if (mView.isTypeloadData) {
            return;
        }
        selectProduct(page, "20", null, null, null);

    }

    private void initView() {
        dropDownMenu = (DropDownMenu) mView.getView().findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        mGoodsSv = (PullableGridView) mView.getView().findViewById(R.id.gridView);
//        mProAdapter = new ProAdapter();
        goodsBeen = new ArrayList<>();
        goodsBeanQuickAdapter = new QuickAdapter<GoodsBean>(mView.getContext(),R.layout.item_gridview_fm_product) {
            @Override
            protected void convert(BaseAdapterHelper helper, GoodsBean item) {
                Object name=item.getName();
                if(null!=name){
                    helper.setText(R.id.name_tv,item.getName().toString());
                }
                helper.setText(R.id.old_price_tv,"￥"+item.getPrice());
                helper.setText(R.id.price_tv,"￥"+item.getCurrent_price());
                ((TextView)helper.getView(R.id.old_price_tv)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                GoodsBean.Default_photo default_photo=item.getDefault_photo();
                if(null!=default_photo){
                    String imageUrl=item.getDefault_photo().getLarge();
                    ImageLoadProxy.displayImage(imageUrl, (ImageView) helper.getView(R.id.imageView));

                }
            }
        };
//        mGoodsSv.setAdapter(mProAdapter);
        mGoodsSv.setAdapter(goodsBeanQuickAdapter);
        mGoodsSv.setOnItemClickListener(this);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd);

    }

    private List<Integer> itemPosList = new ArrayList<>();//有选中值的itemPos列表，长度为3

    private void initFilterDropDownView(JSONArray sceneAllAttrs) {
        if (itemPosList.size() < sceneAllAttrs.length()) {
            itemPosList.add(0);
            itemPosList.add(0);
            itemPosList.add(0);
        }
        ProductDropMenuAdapter dropMenuAdapter = new ProductDropMenuAdapter(mView.getContext(), sceneAllAttrs, itemPosList, this);
        dropDownMenu.setMenuAdapter(dropMenuAdapter);

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
        mNetWork.sendGoodsList(page, per_page, brand, mView.category, mView.filter_attr, shop, keyword, mSortKey, mSortValue, this);
    }

    public void getDropDownMenuText() {
        if (AppUtils.isEmpty(sceneAllAttrs) || AppUtils.isEmpty(mView.filter_attr_name))
            return;
        String[] filterNames = mView.filter_attr_name.split(",");
        for (int i = 0; i < filterNames.length; i++) {
            dropDownMenu.setPositionIndicatorText(i, filterNames[i]);
        }
    }


    //    public void sendSceneType() {
    //        mNetWork.sendSceneType(this);
    //    }

    public void sendAttrList() {
        mNetWork.sendAttrList("yes", this);

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onFilterDone(int titlePos, int itemPos, String itemStr) {
        dropDownMenu.close();
        if (0 == itemPos)
            itemStr = sceneAllAttrs.getJSONObject(titlePos).getString(Constance.filter_attr_name);
        dropDownMenu.setPositionIndicatorText(titlePos, itemStr);

        if (titlePos < itemPosList.size())
            itemPosList.remove(titlePos);
        itemPosList.add(titlePos, itemPos);
        int index = sceneAllAttrs.getJSONObject(titlePos).getInt(Constance.index);
        String filter = "";
        for (int i = 0; i < index + 1; i++) {
            if (i == index) {
                filter += sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attr_list).getJSONObject(itemPos)
                        .getString(Constance.id);
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

    public void onBackPressed() {
        dropDownMenu.close();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        initFilterDropDownView = false;
        selectProduct(page, "20", null, null, null);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        initFilterDropDownView = false;
        page = page + 1;
        selectProduct(page, "20", null, null, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mIntent = new Intent(mView.getActivity(), ProductDetailHDActivity.class);
//        int productId = goodses.getJSONObject(position).getInt(Constance.id);
        String productId = goodsBeen.get(position).getId();
        mIntent.putExtra(Constance.product, productId);
        mView.startActivity(mIntent);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        switch (requestCode) {
            case NetWorkConst.PRODUCT:
                pd.setVisibility(View.GONE);
                //                if (null == mView || mView.getActivity().isFinishing())
                //                    return;

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);

                if (AppUtils.isEmpty(goodsList)) {
                    if (page == 1) {
                        MyToast.show(mView.getActivity(), "数据查询不到");
//                        goodses = new JSONArray();
                        goodsBeen=new ArrayList<>();
                    } else {
                        MyToast.show(mView.getActivity(), "数据已经到底啦!");
                    }
                    //


                    dismissRefesh();
                    return;
                }

                getDataSuccess(goodsList);

                break;
            case NetWorkConst.ATTRLIST:
                sceneAllAttrs = ans.getJSONArray(Constance.goods_attr_list);
                if (initFilterDropDownView) {
                    initFilterDropDownView(sceneAllAttrs);
                    getDropDownMenuText();
                }

                break;

        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        pd.setVisibility(View.GONE);
        if (null == mView || mView.getActivity().isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            return;
        }
        this.page--;
        if (null != mPullToRefreshLayout) {
            dismissRefesh();
        }
    }


    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private void getDataSuccess(JSONArray array) {
        List<GoodsBean> temp=new ArrayList<>();
//        LogUtils.logE("temp:",array.toString());

        for (int i = 0; i < array.length(); i++) {
            temp.add(new Gson().fromJson(String.valueOf(array.getJSONObject(i)).replace("=\\","").replace("\\",""),GoodsBean.class));
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
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }
//        mProAdapter.notifyDataSetChanged();
        goodsBeanQuickAdapter.replaceAll(goodsBeen);
        if(isScrollToTop)
        {
            mGoodsSv.smoothScrollToPositionFromTop(0,0);
            isScrollToTop=false;
        }
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
        }
        page = 1;
        selectProduct(1, "20", null, null, null);
    }


//    private class ProAdapter extends BaseAdapter {
//        public ProAdapter() {
//        }
//
//        @Override
//        public int getCount() {
//            if (null == goodses)
//                return 0;
//            return goodses.length();
//        }
//
//        @Override
//        public JSONObject getItem(int position) {
//            if (null == goodses)
//                return null;
//            return goodses.getJSONObject(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                convertView = View.inflate(mView.getActivity(), R.layout.item_gridview_fm_product, null);
//
//                holder = new ViewHolder();
//                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
//                holder.textView = (TextView) convertView.findViewById(R.id.name_tv);
//                holder.old_price_tv = (TextView) convertView.findViewById(R.id.old_price_tv);
//                holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
//                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            holder.textView.setText(goodses.getJSONObject(position).getString(Constance.name));
//            String imageUrl="";
//            try {
//               imageUrl=goodses.getJSONObject(position).getJSONObject(Constance.default_photo).getString(Constance.large);
//
//            } catch (Exception e) {
//            }
//            ImageLoadProxy.displayImage(imageUrl, holder.imageView);
//
//            holder.old_price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.price));
//            holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            holder.price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.current_price));
//            return convertView;
//        }
//
//        class ViewHolder {
//            ImageView imageView;
//            TextView textView;
//            TextView old_price_tv;
//            TextView price_tv;
//        }
//    }
}
