package bc.juhaohd.com.controller.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baiiu.filter.util.UIUtil;
import com.lib.common.hxp.view.ListViewForScrollView;
import com.lib.common.hxp.view.PullToRefreshLayout;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.user.UserAddrActivity;
import bc.juhaohd.com.ui.activity.user.UserAddrAddActivity;
import bc.juhaohd.com.ui.activity.user.UserAddrAddNewActivity;
import bc.juhaohd.com.ui.activity.user.UserAddrNewActivity;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/2/22 14:13
 * @description :收货地址
 */
public class UserAddrController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private UserAddrNewActivity mView;

    private JSONArray addresses;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private ListViewForScrollView order_sv;
    private int page = 1;

    private View mNullView;
    private View mNullNet;
    private Button mRefeshBtn;
    private TextView mNullNetTv;
    private TextView mNullViewTv;
    private ProgressBar pd;
    private ProgressDialog progressDialog;

    public UserAddrController(UserAddrActivity v){
//        mView=v;
        initView();
        initViewData();
    }

    public UserAddrController(UserAddrNewActivity userAddrNewActivity) {
        mView=userAddrNewActivity;
        initView();
        initViewData();
    }

    private void initViewData() {
        mView.showLoadingPage("", R.drawable.ic_loading);
    }

    private void initView() {
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.contentView));
        mPullToRefreshLayout.setOnRefreshListener(this);

        order_sv = (ListViewForScrollView) mView.findViewById(R.id.order_sv);
//        order_sv.setDivider(null);//去除listview的下划线
        order_sv.setOnItemClickListener(this);
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);

        mNullView = mView.findViewById(R.id.null_view);
        mNullNet = mView.findViewById(R.id.null_net);
        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        pd = (ProgressBar) mView.findViewById(R.id.pd);



    }

    public void sendAddressList(){
        mNetWork.sendAddressList(this);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        mView.showContentView();
        switch (requestCode){
            case NetWorkConst.CONSIGNEELIST:
                if (null == mView || mView.isFinishing())
                    return;
                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                JSONArray consigneeList=ans.getJSONArray(Constance.consignees);
                if (AppUtils.isEmpty(consigneeList)) {
                    if (page == 1) {
                        mNullView.setVisibility(View.VISIBLE);
                    }

                    dismissRefesh();
                    return;
                }

                mNullView.setVisibility(View.GONE);
                mNullNet.setVisibility(View.GONE);
                getDataSuccess(consigneeList);
            break;
            case NetWorkConst.CONSIGNEEDELETE:
                MyToast.show(mView, "删除成功!");
                progressDialog.dismiss();
                break;
        }
    }



    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        if (AppUtils.isEmpty(ans)) {
            mNullNet.setVisibility(View.VISIBLE);
            mRefeshBtn.setOnClickListener(mRefeshBtnListener);
            return;
        }

        if (null != mPullToRefreshLayout) {
            dismissRefesh();
        }
    }

    private void getDataSuccess(JSONArray array){
        if (1 == page)
            addresses = array;
        else if (null != addresses){
            for (int i = 0; i < array.length(); i++) {
                addresses.add(array.getJSONObject(i));
            }

            if(AppUtils.isEmpty(array))
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    public void onRefresh() {
        sendAddressList();
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        pd.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        sendAddressList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        sendAddressList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mView.isSelectAddress){
            Intent intent=new Intent();
            intent.putExtra(Constance.address, addresses.getJSONObject(position));
            mView.setResult(Constance.FROMADDRESS, intent);//告诉原来的Activity 将数据传递给它
            mView.finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
        }else{
            Intent intent=new Intent(mView, UserAddrAddNewActivity.class);
            intent.putExtra(Constance.address,addresses.getJSONObject(position));
            intent.putExtra(Constance.UpdateModele,true);
            mView.startActivity(intent);
        }


    }

    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == addresses)
                return 0;
            return addresses.length();
        }

        @Override
        public JSONObject getItem(int position) {
            if (null == addresses)
                return null;
            return addresses.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_user_address_new_me, null);

                holder = new ViewHolder();
                holder.consignee_tv = (TextView) convertView.findViewById(R.id.consignee_tv);
                holder.address_tv = (TextView) convertView.findViewById(R.id.address_tv);
                holder.phone_tv = (TextView) convertView.findViewById(R.id.phone_tv);
                holder.edit_tv = (TextView) convertView.findViewById(R.id.edit_tv);
                holder.default_addr_tv = (TextView) convertView.findViewById(R.id.default_addr_tv);
                holder.delete_tv= (TextView) convertView.findViewById(R.id.delete_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String name=addresses.getJSONObject(position).getString(Constance.name);
            holder.consignee_tv.setText(name);
            holder.address_tv.setText(addresses.getJSONObject(position).getString(Constance.address));
            holder.phone_tv.setText(addresses.getJSONObject(position).getString(Constance.mobile));
            boolean isdefault=addresses.getJSONObject(position).getBoolean(Constance.is_default);
            Drawable drawable=mView.getResources().getDrawable(R.mipmap.cb_normal_cart);
            holder.default_addr_tv.setTextColor(mView.getResources().getColor(R.color.tv_444444));
            if(isdefault){
                drawable=mView.getResources().getDrawable(R.mipmap.cb_selected_cart);
                holder.default_addr_tv.setTextColor(mView.getResources().getColor(R.color.orange_theme));
            }
            drawable.setBounds(0,0, UIUtils.dip2PX(60),UIUtils.dip2PX(60));
            drawable.getMinimumHeight();
            holder.default_addr_tv.setCompoundDrawables(drawable,null,null,null);
            holder.default_addr_tv.setVisibility(isdefault==true?View.VISIBLE:View.GONE);
            holder.edit_tv.setVisibility(mView.isSelectAddress?View.GONE:View.VISIBLE);
            holder.delete_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                UIUtils.showSingleWordDialogOrange(mView, "确定要删除该地址吗", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog = ProgressDialog.show(mView,"","删除中...");
                        mNetWork.sendDeleteAddress(addresses.getJSONObject(position).getString(Constance.id),UserAddrController.this);

                    }
                });
                }
            });
            holder.edit_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mView, UserAddrAddNewActivity.class);
                    intent.putExtra(Constance.address,addresses.getJSONObject(position));
                    intent.putExtra(Constance.UpdateModele,true);
                    mView.startActivity(intent);
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView consignee_tv;
            TextView address_tv;
            TextView phone_tv;
            TextView default_addr_tv;
            TextView edit_tv;
            TextView delete_tv;

        }
    }
}
