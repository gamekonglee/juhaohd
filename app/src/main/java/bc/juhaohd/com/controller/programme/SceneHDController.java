package bc.juhaohd.com.controller.programme;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.programme.DiyActivity;
import bc.juhaohd.com.ui.adapter.SceneDropMenuAdapter;
import bc.juhaohd.com.ui.fragment.SceneHDFragment;
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
public class SceneHDController extends BaseController implements OnFilterDoneListener, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, INetworkCallBack {
    private SceneHDFragment mView;
    private DropDownMenu dropDownMenu;
    private JSONArray sceneAllAttrs;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private PullableGridView order_sv;
    public int page = 1;
    private JSONArray goodses;
    private boolean initFilterDropDownView;
    private String imageURL = "";
    private Intent mIntent;
    public String keyword;
    private ProgressBar pd;

    public SceneHDController(SceneHDFragment v) {
        mView = v;
        initView();
        initViewData();
    }

    private void initViewData() {
        page = 1;
        initFilterDropDownView = true;
        sendSceneList(page);
        sendSceneType();
        pd.setVisibility(View.VISIBLE);
    }

    private void initView() {
        dropDownMenu = (DropDownMenu) mView.getView().findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (PullableGridView) mView.getView().findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        order_sv.setOnItemClickListener(this);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd);
    }

    private List<Integer> itemPosList = new ArrayList<>();//有选中值的itemPos列表，长度为3

    private void initFilterDropDownView(JSONArray sceneAllAttrs) {
        if (itemPosList.size() < sceneAllAttrs.length()) {
            itemPosList.add(0);
            itemPosList.add(0);
            itemPosList.add(0);
        }
        SceneDropMenuAdapter dropMenuAdapter = new SceneDropMenuAdapter(mView.getContext(), sceneAllAttrs, itemPosList, this);
        dropDownMenu.setMenuAdapter(dropMenuAdapter);
    }

    /**
     * 场景列表
     */
    public void sendSceneList(int page) {
        mNetWork.sendSceneList(page, "20", keyword, this);
    }


    public void sendSceneType() {
        mNetWork.sendSceneType(this);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    private String[] SceensNames = new String[]{"", ""};

    @Override
    public void onFilterDone(int titlePos, int itemPos, String itemStr) {
        dropDownMenu.close();
        //        if (0 == itemPos)
        itemStr =  sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attrVal).getString(itemPos);
        dropDownMenu.setPositionIndicatorText(titlePos, itemStr);

        if (titlePos < itemPosList.size())
            itemPosList.remove(titlePos);
        itemPosList.add(titlePos, itemPos);

        SceensNames[titlePos] = sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attrVal).getString(itemPos);

        keyword = "[\"" + SceensNames[0] + "\",\"" + SceensNames[1] + "\"]";
        if (AppUtils.isEmpty(keyword))
            return;
        pd.setVisibility(View.VISIBLE);
        sendSceneList(1);

    }

    public void onBackPressed() {
        dropDownMenu.close();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        initFilterDropDownView = false;
        sendSceneList(page);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        initFilterDropDownView = false;
        sendSceneList(++page);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mView.isToken()){
            return;
        }
        mIntent = new Intent(mView.getActivity(), DiyActivity.class);
        String path = NetWorkConst.SCENE_HOST + goodses.getJSONObject(position).getJSONObject(Constance.scene).getString(Constance.original_img);
        IssueApplication.mSelectScreens.add(goodses.getJSONObject(position));
        mIntent.putExtra(Constance.path, path);
        mView.startActivity(mIntent);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        pd.setVisibility(View.GONE);
        switch (requestCode) {
            case NetWorkConst.SCENELIST:
                if (null == mView || mView.getActivity().isFinishing())
                    return;

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                JSONArray goodsList = ans.getJSONArray(Constance.scene);
                if (AppUtils.isEmpty(goodsList) ||goodsList.length()==0) {
                    if (page == 1) {
                        goodses=new JSONArray();
                        mProAdapter.notifyDataSetChanged();
                        dismissRefesh();
                    }else{
                        MyToast.show(mView.getActivity(), "数据已经到底啦!");
                        dismissRefesh();
                    }

                    return;
                }

                getDataSuccess(goodsList);

                break;
            case NetWorkConst.SCENECATEGORY:
                sceneAllAttrs = ans.getJSONArray(Constance.categories);
                if (initFilterDropDownView)//重复setMenuAdapter会报错
                    initFilterDropDownView(sceneAllAttrs);
                break;

        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
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
        if (1 == page)
            goodses = array;
        else if (null != goodses) {
            for (int i = 0; i < array.length(); i++) {
                goodses.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
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
                convertView = View.inflate(mView.getActivity(), R.layout.item_gridview_fm_scene02, null);

                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            JSONObject object = goodses.getJSONObject(position);
            if (object.getJSONObject(Constance.scene) != null) {
                String name = object.getJSONObject(Constance.scene).getString(Constance.name);
                holder.textView.setText(name);
                String path = NetWorkConst.SCENE_HOST + object.getJSONObject(Constance.scene).getString(Constance.original_img);
                ImageLoader.getInstance().displayImage(path
                        + "!400X400.png", holder.imageView);
            }


            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            TextView textView;


        }
    }
}
