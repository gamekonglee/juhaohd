package bc.juhaohd.com.controller.classify;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.AttrList;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.buy.FilterTypeActivity;
import bc.juhaohd.com.ui.activity.product.ClassifyGoodsActivity;
import bc.juhaohd.com.ui.adapter.FilterGoodsAdapter;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;

/**
 * @author: Jun
 * @date : 2017/1/21 9:45
 * @description :
 */
public class ClassifyGoods02Controller extends BaseController implements INetworkCallBack, AdapterView.OnItemClickListener {
    private ClassifyGoodsActivity mView;
    private ListView listView;
    public FilterGoodsAdapter mAdapter;
    public JSONArray mFilterList;
    private Intent mIntent;
    private ArrayList<AttrList> mAttrListList = new ArrayList<>();
    private int mPosition = 0;

    public ClassifyGoods02Controller(ClassifyGoodsActivity v) {
        mView = v;
        initData();
        initView();
        initViewData();
    }

    private void initData() {
    }


    private void initViewData() {
    }

    private void initView() {
        listView = (ListView) mView.findViewById(R.id.listView);
        mAdapter = new FilterGoodsAdapter(mView);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.showContentView();
        switch (requestCode) {
            case NetWorkConst.ATTRLIST:
                mFilterList = ans.getJSONArray(Constance.goods_attr_list);

                mAdapter.setData(mFilterList);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
    }


    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void sendAttrList() {
        mNetWork.sendAttrList(this);

    }

    public void clearData() {
        mAdapter.setClearAttrList();
        mAdapter.notifyDataSetChanged();
    }

    public void selectGoods() {
        mIntent = new Intent();
        String filter = "";
        for (int i = 0; i < mFilterList.length(); i++) {
            if (i == mFilterList.length() - 1) {
                filter += mAdapter.mAttrList.get(i).getId();
            } else {
                filter += mAdapter.mAttrList.get(i).getId() + ".";
            }
        }
        Toast.makeText(mView, filter, Toast.LENGTH_SHORT).show();
        mIntent.putExtra(Constance.filter_attr, filter);
        mView.setResult(103, mIntent);//告诉原来的Activity 将数据传递给它
        mView.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        final JSONObject object = mFilterList.getJSONObject(position);
//        final String name = object.getString(Constance.filter_attr_name);
//        JSONArray attrArray = object.getJSONArray(Constance.attr_list);
//        if (AppUtils.isEmpty(attrArray))
//            return;
//        mPosition = position;
//        mIntent = new Intent(mView, FilterTypeActivity.class);
//        mIntent.putExtra(Constance.attr_list, attrArray);
//        mIntent.putExtra(Constance.filter_attr_name, name);
//        mIntent.putExtra(Constance.type, 1);
//        mView.startActivity(mIntent);
        final JSONObject object = mFilterList.getJSONObject(position);
        final String name = object.getString(Constance.filter_attr_name);
        JSONArray attrArray = object.getJSONArray(Constance.attr_list);
        if (AppUtils.isEmpty(attrArray))
            return;
        mPosition = position;
        mIntent = new Intent(mView, FilterTypeActivity.class);
        mIntent.putExtra(Constance.attr_list, attrArray);
        mIntent.putExtra(Constance.filter_attr_name, name);
        mIntent.putExtra(Constance.type, 1);
        mView.startActivity(mIntent);
    }

    public void onResume() {
        mAdapter.setAttrList(mView.mAttrList, mPosition);
        mView.mAttrList = null;
    }

}
