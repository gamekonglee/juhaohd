package bc.juhaohd.com.controller;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.TaoCanHomeActivity;
import bc.juhaohd.com.ui.activity.product.ProductDetailHDNewActivity;
import bc.juhaohd.com.ui.view.EndOfGridView;
import bc.juhaohd.com.ui.view.EndOfListView;
import bc.juhaohd.com.ui.view.PMSwipeRefreshLayout;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * Created by bocang on 18-6-15.
 */

public class TaoCanHomeController extends BaseController implements EndOfListView.OnEndOfListListener, SwipeRefreshLayout.OnRefreshListener {

    private final TaoCanHomeActivity mView;
    private EndOfGridView listview;
    private PMSwipeRefreshLayout pullToRefresh;
    private int page;
    private JSONArray goodses;
    private ProAdapter mProAdapter;

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }
    public  TaoCanHomeController(TaoCanHomeActivity canHomeActivity){
        mView = canHomeActivity;
        initView();
        page = 1;
        sendTaocanList();
    }

    private void sendTaocanList() {
        pullToRefresh.setRefreshing(true);
        mNetWork.sendGoodsList(page, "20", null, "224", null, null, null, null, null, new INetworkCallBack() {
            @Override
            public void onSuccessListener(String requestCode, JSONObject ans) {
            if(null==mView||mView.isFinishing()){
                return;
            }
            if(null!=pullToRefresh){
                dismissRefresh();
            }
                JSONArray goodsList=ans.getJSONArray(Constance.goodsList);
            if(AppUtils.isEmpty(goodsList)||goodsList.length()==0){
                if(page==1){

                }else {
                    MyToast.show(mView,"没有更多数据了");
                }
                dismissRefresh();
                return;
            }
            getDataSuccess(goodsList);
            }

            @Override
            public void onFailureListener(String requestCode, JSONObject ans) {
                dismissRefresh();
            }
        });
    }

    private void getDataSuccess(JSONArray array) {

        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.length(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    private void dismissRefresh() {
        pullToRefresh.post(new Runnable() {
            @Override
            public void run() {
            if(pullToRefresh.isRefreshing()){
                pullToRefresh.setRefreshing(false);
            }
            }
        });
    }

    private void initView() {
        listview = mView.findViewById(R.id.listView);
        mProAdapter = new ProAdapter();
        listview.setAdapter(mProAdapter);
        listview.setOnEndOfListListener(this);
        pullToRefresh = mView.findViewById(R.id.pulltoRefresh);
        pullToRefresh.setColorSchemeColors(Color.BLUE,Color.YELLOW,Color.GREEN,Color.RED);
        pullToRefresh.setOnRefreshListener(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(mView, ProductDetailHDNewActivity.class);
                if(goodses==null||goodses.length()<=0){
                    return;
                }
                String productId = goodses.getJSONObject(position).getInt(Constance.id)+"";
                intent.putExtra(Constance.product, productId);
                mView.startActivity(intent);
            }
        });

    }

    @Override
    public void onEndOfList(Object lastItem) {
        if(page==1&&goodses.length()==0){
            return;
        }
        page++;
        sendTaocanList();
    }

    @Override
    public void onRefresh() {
        page=1;
        sendTaocanList();
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
                convertView = View.inflate(mView, R.layout.item_gridview_fm_product_taocan, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.check_iv = (ImageView) convertView.findViewById(R.id.check_iv);
                holder.textView = (TextView) convertView.findViewById(R.id.name_tv);
                holder.groupbuy_tv = (TextView) convertView.findViewById(R.id.groupbuy_tv);
                holder.old_price_tv = (TextView) convertView.findViewById(R.id.old_price_tv);
                holder.price_tv = (TextView) convertView.findViewById(R.id.price_tv);
                RelativeLayout.LayoutParams lLp = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
                lLp.setMargins(10,10,10,10);
                float w = (UIUtils.getScreenWidth(mView)-UIUtils.dip2PX(380))/2;
                lLp.width = (int) w;
                lLp.height= (int) (w*(230+86)/345);
                holder.imageView.setLayoutParams(lLp);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            try {
                String name = goodses.getJSONObject(position).getString(Constance.name);
                holder.textView.setText(name);
                //                holder.imageView.setImageResource(R.drawable.bg_default);
                ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST+goodses.getJSONObject(position).getString(Constance.original_img)
                        , holder.imageView);

                JSONObject groupBuyObject = goodses.getJSONObject(position).getJSONObject(Constance.group_buy);
                int isFinished=-1;
                if(!AppUtils.isEmpty(groupBuyObject))
                {
                    isFinished=groupBuyObject.getInt(Constance.is_finished);
                }

                holder.groupbuy_tv.setVisibility(isFinished==0? View.VISIBLE : View.GONE);
                double old_Price=0;
                JSONArray propertieArray = goodses.getJSONObject(position).getJSONArray(Constance.properties);
                if (!AppUtils.isEmpty(propertieArray)&&propertieArray.length()>0) {
                    JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
                    int price = attrsArray.getJSONObject(0).getInt(Constance.attr_price);
                    double currentPrice = price;
                    old_Price=currentPrice;
                    holder.price_tv.setText("￥" + currentPrice);
                } else {
                    old_Price= Double.parseDouble(goodses.getJSONObject(position).getString(Constance.current_price));
                    holder.price_tv.setText("￥" + goodses.getJSONObject(position).getString(Constance.current_price));
                }
                old_Price=old_Price*1.6;
                DecimalFormat df=new DecimalFormat("###.00");
                holder.old_price_tv.setText("￥" + df.format(old_Price));
                holder.old_price_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.check_iv.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            ImageView check_iv;
            TextView textView;
            TextView groupbuy_tv;
            TextView old_price_tv;
            TextView price_tv;

        }
    }
}
