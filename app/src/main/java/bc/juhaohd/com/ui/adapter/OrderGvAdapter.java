package bc.juhaohd.com.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.view.MyWebView;
import bocang.utils.AppUtils;

import static bc.juhaohd.com.R.id.property_tv;

/**
 * @author: Jun
 * @date : 2017/1/21 13:46
 * @description : 选择方案类型
 */
public class OrderGvAdapter extends BaseAdapter {
    private JSONArray  mOrderes;
    private Activity mContext;
    private Intent mIntent;
    private List<Boolean> mIsClick;
    private int mState;

    public OrderGvAdapter(Activity context, JSONArray orderes) {
        mContext=context;
        mOrderes=orderes;
        mIsClick=new ArrayList<>();
        for(int i=0;i<mOrderes.size();i++){
            mIsClick.add(false);
        }

    }

    public OrderGvAdapter(FragmentActivity context, JSONArray orderes, int state) {
        mContext=context;
        mOrderes=orderes;
        mIsClick=new ArrayList<>();
        for(int i=0;i<mOrderes.size();i++){
            mIsClick.add(false);
        }
        mState = state;
    }


    @Override
    public int getCount() {
        if (null == mOrderes)
            return 0;
        return mOrderes.size();
    }

    @Override
    public JSONObject getItem(int position) {
        if (null == mOrderes)
            return null;
        return mOrderes.getJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_order_new, null);

            holder = new ViewHolder();

            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.goods_summoney_tv = (TextView) convertView.findViewById(R.id.goods_summoney_tv);
            holder.goods_sum_tv = (TextView) convertView.findViewById(R.id.goods_sum_tv);
            holder.property_tv = (TextView) convertView.findViewById(property_tv);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.tv_cut_price= (TextView) convertView.findViewById(R.id.tv_cut_price);
            holder.tv_mark_price= (TextView) convertView.findViewById(R.id.tv_mark_price);
//            holder.rl_webview= (RelativeLayout) convertView.findViewById(R.id.rl_code);
//            holder.myWebView= (MyWebView) convertView.findViewById(R.id.webview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject object = mOrderes.getJSONObject(position);
        String num=object.getString(Constance.total_amount);
        String totalMoney=object.getString(Constance.total_price);
        holder.goods_sum_tv.setText("X" +num+ "件");
        holder.goods_summoney_tv.setText("￥"+object.getString( Constance.product_price ));
        holder.name_tv.setText(object.getJSONObject(Constance.product).getString(Constance.name));
        String property=object.getJSONObject(Constance.product).getString(Constance.property);
        double original_price;
        try{
            original_price= Double.parseDouble(object.getString(Constance.original_price));
        }catch (Exception e){
            original_price= Double.parseDouble(object.getString(Constance.product_price));
        }
        double product_price= Double.parseDouble(object.getString( Constance.product_price ));
        double money=(original_price-product_price)*Integer.parseInt(num);

        if(money==0){
            holder.tv_cut_price.setVisibility(View.GONE);
        }else {

            holder.tv_cut_price.setVisibility(View.VISIBLE);
        holder.tv_cut_price.setText("已优惠：￥"+df.format(money));
        }
        holder.tv_mark_price.setText("优惠价：￥"+df.format(original_price*Integer.parseInt(num)));
//        holder.rl_webview.setVisibility(View.GONE);
//        if(mState==0){
//            if(position==0){
//                holder.rl_webview.setVisibility(View.VISIBLE);
//            }
//        }
        if(!AppUtils.isEmpty(property)){
            holder.property_tv.setText(property);
            holder.property_tv.setVisibility(View.VISIBLE);
        }else{
            holder.property_tv.setVisibility(View.GONE);
        }

        try {
            ImageLoader.getInstance().displayImage(object.getJSONObject(Constance.product).
                    getJSONArray(Constance.photos).getJSONObject(0).getString(Constance.thumb), holder.imageView, IssueApplication.getImageLoaderOption());
        } catch (Exception e) {

        }

        return convertView;
    }
    DecimalFormat df=new DecimalFormat("###.00");
    class ViewHolder {
        ImageView imageView;
        TextView name_tv;
        TextView goods_summoney_tv;
        TextView goods_sum_tv;
        TextView property_tv;
        TextView tv_mark_price;
        TextView tv_cut_price;
        RelativeLayout rl_webview;
        MyWebView myWebView;

    }




}
