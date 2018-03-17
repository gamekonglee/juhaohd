package bc.juhaohd.com.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lib.common.hxp.view.GridViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.MainActivity;
import bc.juhaohd.com.ui.activity.product.ProductDetailHDActivity;
import bc.juhaohd.com.ui.fragment.HomePageFragment;
import bc.juhaohd.com.utils.ConvertUtil;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import bocang.utils.UniversalUtil;

/**
 * @author: Jun
 * @date : 2017/9/30 11:06
 * @description :
 */
public class HomePageController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private ConvenientBanner mConvenientBanner;
    private int mScreenWidth;
    private int mScreenHeight;
    private List<String> paths = new ArrayList<>();
    private List<String> ImageLinks = new ArrayList<>();
    private Intent mIntent;
    private GridViewForScrollView mGoodsSv;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    public int page = 1;
    private JSONArray goodses;
    private HomePageFragment mView;
    private JSONArray sceneAllAttrs;
    private JSONArray mStyleAttrList;
    private JSONArray mSpaceAttrList;

    public HomePageController(HomePageFragment v){
        mView=v;
        initView();
        initViewData();
    }

    private void initViewData() {
        sendBanner();
        page = 1;
        sendGoodsList(page, 20, null, null, null, null, null, null);
        sendAttrList();
    }

    private void initView() {
        mConvenientBanner = (ConvenientBanner) mView.getView().findViewById(R.id.convenientBanner);
        mScreenWidth = mView.getActivity().getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = mView.getActivity().getResources().getDisplayMetrics().heightPixels;
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mConvenientBanner.getLayoutParams();
        rlp.width = mScreenWidth;
        rlp.height =mScreenHeight-200;

        mConvenientBanner.setLayoutParams(rlp);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.refresh_view));
        mPullToRefreshLayout.setOnRefreshListener(this);
        mGoodsSv = (GridViewForScrollView) mView.getView().findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        mGoodsSv.setAdapter(mProAdapter);
        mGoodsSv.setOnItemClickListener(this);

    }
    private void sendBanner() {
        mNetWork.sendBanner(this);

    }

    /**
     * 广告图
     */
    private void getAd() {
        mConvenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, paths);
        mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_unfocuse, R.drawable.dot_focuse});
    }
    /**
     * @param page
     * @param per_page
     * @param brand      品牌
     * @param category   类型
     * @param shop
     * @param keyword
     * @param sort_key
     * @param sort_value
     */
    private void sendGoodsList(int page, int per_page, String brand, String category, String shop, String keyword, String sort_key, String sort_value) {
        mNetWork.sendRecommendGoodsList(page, per_page, brand, category, null, shop, keyword, sort_key, sort_value, this);
    }

    public void sendAttrList() {
        mNetWork.sendAttrList("yes", this) ;

    }

    /**
     * 获取产品分类列表
     *
     * @param ans
     */
    private void getGoodsList(JSONObject ans) {

        JSONArray goodsList = ans.getJSONArray(Constance.goodsList);

        if (1 == page)
            goodses = goodsList;
        else if (null != goodses) {
            for (int i = 0; i < goodsList.length(); i++) {
                goodses.add(goodsList.getJSONObject(i));
            }

            if (AppUtils.isEmpty(goodsList))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }

        mProAdapter.notifyDataSetChanged();
    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        switch (requestCode) {
            case NetWorkConst.BANNER:
                if (null == mView.getActivity() || mView.getActivity().isFinishing())
                    return;
                JSONArray babbersArray = ans.getJSONArray(Constance.banners);
                for (int i = 0; i < babbersArray.length(); i++) {
                    JSONObject photoObject = babbersArray.getJSONObject(i).getJSONObject(Constance.photo);
                    String imageUri = photoObject.getString(Constance.large);
                    paths.add(imageUri);
                    ImageLinks.add(babbersArray.getJSONObject(i).getString(Constance.link));
                }
                getAd();
                break;
            case NetWorkConst.RECOMMENDPRODUCT:
                if (null == mView.getActivity() || mView.getActivity().isFinishing())
                    return;
                if (null != mPullToRefreshLayout) {
                    mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
                getGoodsList(ans);
                break;
            case NetWorkConst.ATTRLIST:
                sceneAllAttrs = ans.getJSONArray(Constance.goods_attr_list);
                mStyleAttrList= sceneAllAttrs.getJSONObject(2).getJSONArray(Constance.attr_list);
                mSpaceAttrList= sceneAllAttrs.getJSONObject(1).getJSONArray(Constance.attr_list);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        if (null == mView.getActivity() || mView.getActivity().isFinishing())
            return;
        this.page--;
        if (null != mPullToRefreshLayout) {
            mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        int per_page = 20;
        String brand = "";
        String category = "";
        String shop = "";
        String keyword = "";
        String sort_key = "";
        String sort_value = "";
        sendGoodsList(page, per_page, brand, category, shop, keyword, sort_key, sort_value);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        int per_page = 20;
        String brand = "";
        String category = "";
        String shop = "";
        String keyword = "";
        String sort_key = "";
        String sort_value = "";
        sendGoodsList(++page, per_page, brand, category, shop, keyword, sort_key, sort_value);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mIntent= new Intent(mView.getActivity(), ProductDetailHDActivity.class);
        int productId=goodses.getJSONObject(position).getInt(Constance.id);
        mIntent.putExtra(Constance.product,productId);
        mView.startActivity(mIntent);
    }

    public void selectTypeProduct(String filter,int type) {
        if(type==0){
            for(int i=0;i<mStyleAttrList.length();i++){
                String attrValue=mStyleAttrList.getJSONObject(i).getString(Constance.attr_value);
                String id=mStyleAttrList.getJSONObject(i).getString(Constance.id);
                if(filter.equals(attrValue)){
                    ((MainActivity)mView.getActivity()).mBottomBar.selectItem(R.id.frag_product_ll);
                    ((MainActivity)mView.getActivity()).clickTab1Layout();
                    ((MainActivity)mView.getActivity()).mHomeFragment.filter_attr="0.0.0.0.0.0.0.0.0.0.0."+id;
                    ((MainActivity)mView.getActivity()).mHomeFragment.filter_attr_name="类型,空间,"+filter;
                    ((MainActivity)mView.getActivity()).mHomeFragment.isAttrloadData=true;
                    ((MainActivity)mView.getActivity()).mHomeFragment.getAttrData();
                    return;

                }
            }
        } else{
            if("光源".equals(filter)){
                ((MainActivity)mView.getActivity()).mBottomBar.selectItem(R.id.frag_product_ll);
                ((MainActivity)mView.getActivity()).clickTab1Layout();
                ((MainActivity)mView.getActivity()).mHomeFragment.category="197";
                ((MainActivity)mView.getActivity()).mHomeFragment.isAttrloadData=true;
                ((MainActivity)mView.getActivity()).mHomeFragment.getAttrData();
                return;
            }
            for(int i=0;i<mSpaceAttrList.length();i++){
                String attrValue=mSpaceAttrList.getJSONObject(i).getString(Constance.attr_value);
                String id=mSpaceAttrList.getJSONObject(i).getString(Constance.id);
                if(filter.equals(attrValue)){
                    ((MainActivity)mView.getActivity()).mBottomBar.selectItem(R.id.frag_product_ll);
                    ((MainActivity)mView.getActivity()).clickTab1Layout();
                    ((MainActivity)mView.getActivity()).mHomeFragment.filter_attr_name="类型,"+filter+",风格";
                    ((MainActivity)mView.getActivity()).mHomeFragment.filter_attr="0.0.0.0.0."+id;
                    ((MainActivity)mView.getActivity()).mHomeFragment.isAttrloadData=true;
                    ((MainActivity)mView.getActivity()).mHomeFragment.getAttrData();
                }
            }
            return;

        }
        MyToast.show(mView.getContext(),"找不到相关条件!");
    }

    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            imageView.setImageResource(R.drawable.bg_default);
            ImageLoader.getInstance().displayImage(data, imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
////                    //跳转到知道的网址
//                    String link = ImageLinks.get(position);
//                    if (!AppUtils.isEmpty(link)) {
//                        mIntent = new Intent();
//                        mIntent.setAction(Intent.ACTION_VIEW);
//                        mIntent.setData(Uri.parse(ImageLinks.get(position)));
//                        mView.startActivity(mIntent);
//                    }

                }
            });
        }
    }

    public void setResume() {
        // 开始自动翻页
        mConvenientBanner.startTurning(UniversalUtil.randomA2B(3000, 5000));
    }

    public void setPause() {
        // 停止翻页
        mConvenientBanner.stopTurning();
    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_gridview_home_product, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
                float h = (mScreenWidth - ConvertUtil.dp2px(mView.getActivity(), 45.8f)) / 2;
                lLp.height = (int) h;
                holder.imageView.setLayoutParams(lLp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String imagePath =goodses.getJSONObject(position).getJSONObject(Constance.app_img).getString(Constance.phone_img);
            ImageLoader.getInstance().displayImage(imagePath, holder.imageView);
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;

        }
    }
}
