package bc.juhaohd.com.controller.programme;

import android.content.ClipboardManager;
import android.content.Context;
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
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bc.juhao.com.ui.view.HorizontalListView;
import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.Programme;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.ui.activity.programme.ImageDetailActivity;
import bc.juhaohd.com.ui.adapter.MatchItemAdapter;
import bc.juhaohd.com.ui.adapter.ProgrammeDropMenuAdapter;
import bc.juhaohd.com.ui.fragment.ProgrammeFragment;
import bc.juhaohd.com.utils.ShareUtil;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;

/**
 * @author: Jun
 * @date : 2017/3/10 17:49
 * @description :
 */
public class ProgrammeController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, OnFilterDoneListener {
    private ProgrammeFragment mView;
    private DropDownMenu dropDownMenu;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private PullableGridView order_sv;
    public int page = 1;
    private int per_page = 20;
    public JSONArray mSchemes;
    private boolean initFilterDropDownView;
    private ProgressBar pd;
    private String mStyle;
    private String mSpace;
    private int mDeleteIndex = 0;


    public ProgrammeController(ProgrammeFragment v) {
        mView = v;
        initView();
        initViewData();
    }


    private void initViewData() {
        mView.showLoadingPage("", R.drawable.ic_loading);
        initFilterDropDownView = true;
        setropownMenuData();
        if (initFilterDropDownView)//重复setMenuAdapter会报错
            initFilterDropDownView(mView.mProgrammes);
    }


    private void setropownMenuData() {
        mView.mProgrammes = new ArrayList<>();
        String[] styleArrs = UIUtils.getStringArr(R.array.style);
        String[] spaceArrs = UIUtils.getStringArr(R.array.space);
        Programme programme = new Programme();
        programme.setAttr_name(UIUtils.getString(R.string.style_name));
        programme.setAttrVal(Arrays.asList(styleArrs));
        mView.mProgrammes.add(programme);
        Programme programme2 = new Programme();
        programme2.setAttr_name(UIUtils.getString(R.string.splace_name));
        programme2.setAttrVal(Arrays.asList(spaceArrs));
        mView.mProgrammes.add(programme2);
    }

    private void initView() {
        dropDownMenu = (DropDownMenu) mView.getView().findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.getView().findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        order_sv = (PullableGridView) mView.getView().findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        order_sv.setAdapter(mProAdapter);
        order_sv.setOnItemClickListener(this);
        pd = (ProgressBar) mView.getView().findViewById(R.id.pd2);
        //        mNullView = mView.getActivity().findViewById(R.id.null_view);
        //        mNullNet = mView.getActivity().findViewById(R.id.null_net);
        //        mRefeshBtn = (Button) mNullNet.findViewById(R.id.refesh_btn);
        //        mNullNetTv = (TextView) mNullNet.findViewById(R.id.tv);
        //        mNullViewTv = (TextView) mNullView.findViewById(R.id.tv);
        //        go_btn = (Button) mNullView.findViewById(R.id.go_btn);
    }

    private List<Integer> itemPosList = new ArrayList<>();//有选中值的itemPos列表，长度为3

    private void initFilterDropDownView(List<Programme> programmes) {
        if (itemPosList.size() < programmes.size()) {
            itemPosList.add(0);
            itemPosList.add(0);
        }
        ProgrammeDropMenuAdapter dropMenuAdapter = new ProgrammeDropMenuAdapter(mView.getActivity(), programmes, itemPosList, this);
        dropDownMenu.setMenuAdapter(dropMenuAdapter);
    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }


    public void sendFangAnList() {

        mPullToRefreshLayout.isMove = false;
        mNetWork.sendFangAnList(page, per_page, mStyle, mSpace, this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {
        mView.hideLoading();
        mView.showContentView();
        //        go_btn.setVisibility(View.GONE);
        switch (requestCode) {
            case NetWorkConst.FANGANLIST:
                if (null == mView || mView.getActivity().isFinishing())
                    return;

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                JSONArray goodsList = ans.getJSONArray(Constance.fangan);
                if (AppUtils.isEmpty(goodsList)) {
                    if (page == 1) {
                        //                        mNullView.setVisibility(View.VISIBLE);
                        mPullToRefreshLayout.isMove = true;
                        //                        go_btn.setVisibility(View.VISIBLE);
                        //                        go_btn.setOnClickListener(new View.OnClickListener() {
                        //                            @Override
                        //                            public void onClick(View v) {
                        //                                IntentUtil.startActivity(mView.getActivity(), DiyActivity.class,false);
                        //                            }
                        //                        });
                    }
                    mSchemes = new JSONArray();
                    dismissRefesh();
                    pd.setVisibility(View.GONE);
                    return;
                }
                //                mNullView.setVisibility(View.GONE);
                //                mNullNet.setVisibility(View.GONE);
                getDataSuccess(goodsList);
                pd.setVisibility(View.GONE);
                break;
            case NetWorkConst.FANGANDELETE:
                mView.showContentView();
                mSchemes.delete(mDeleteIndex);
                mProAdapter.notifyDataSetChanged();
                //                sendFangAnList();
                break;

        }
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        if (null == mView ||mView.getActivity()==null|| mView.getActivity().isFinishing())
            return;
        if (AppUtils.isEmpty(ans)) {
            //            AppDialog.messageBox(UIUtils.getString(R.string.server_error));
            //            mNullNet.setVisibility(View.VISIBLE);
            mPullToRefreshLayout.isMove = true;
            //            mRefeshBtn.setOnClickListener(mRefeshBtnListener);
            return;
        }
        //        go_btn.setVisibility(View.GONE);
        this.page--;
        if (null != mPullToRefreshLayout) {
            dismissRefesh();
        }
    }

    private void dismissRefesh() {
        mPullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mPullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    private View.OnClickListener mRefeshBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRefresh();
        }
    };

    public void onRefresh() {
        pd.setVisibility(View.VISIBLE);
        page = 1;
        sendFangAnList();
    }

    private void getDataSuccess(JSONArray array) {
        if (1 == page)
            mSchemes = array;
        else if (null != mSchemes) {
            for (int i = 0; i < array.length(); i++) {
                mSchemes.add(array.getJSONObject(i));
            }

            if (AppUtils.isEmpty(array))
                MyToast.show(mView.getActivity(), "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    public void ActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onFilterDone(int titlePos, int itemPos, String itemStr) {
        dropDownMenu.close();
        itemStr = mView.mProgrammes.get(titlePos).getAttr_name();
        dropDownMenu.setPositionIndicatorText(titlePos, itemStr);

        if (titlePos < itemPosList.size())
            itemPosList.remove(titlePos);
        itemPosList.add(titlePos, itemPos);
        if (titlePos == 0) {
            mStyle = mView.mProgrammes.get(titlePos).getAttrVal().get(itemPos);
        } else if (titlePos == 1) {
            mSpace = mView.mProgrammes.get(titlePos).getAttrVal().get(itemPos);
        }

        if (itemPos == 0) {
            mStyle = "";
            mSpace = "";
        }
        //        pd.setVisibility(View.VISIBLE);
        page = 1;
        pd.setVisibility(View.VISIBLE);
        sendFangAnList();

    }

    public void onBackPressed() {
        dropDownMenu.close();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pd.setVisibility(View.VISIBLE);
        page = 1;
        initFilterDropDownView = false;
        sendFangAnList();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pd.setVisibility(View.VISIBLE);
        initFilterDropDownView = false;
        page = page + 1;

        sendFangAnList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        String path= NetWorkConst.SCENE_HOST+
        //                goodses.getJSONObject(position).getJSONObject(Constance.scene).getString(Constance.original_img);
        //        mIntent=new Intent();
        //        mIntent.putExtra(Constance.SCENE, path);
        //        mView.setResult(Constance.FROMDIY02, mIntent);//告诉原来的Activity 将数据传递给它
        //        mView.finish();//一定要调用该方法 关闭新的AC 此时 老是AC才能获取到Itent里面的值
    }


    public void sendDeleteFangan(int id) {
        mView.setShowDialog(true);
        mView.setShowDialog("删除中...");
        mView.showLoading();
        mNetWork.sendDeleteFangan(id, this);
    }


    private class ProAdapter extends BaseAdapter {
        public ProAdapter() {
        }

        @Override
        public int getCount() {
            if (null == mSchemes)
                return 0;
            return mSchemes.length();
        }

        @Override
        public JSONObject getItem(int position) {
            if (null == mSchemes)
                return null;
            return mSchemes.getJSONObject(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView.getActivity(), R.layout.item_match, null);

                holder = new ViewHolder();
                holder.close_iv = (ImageView) convertView.findViewById(R.id.close_iv);
                holder.share_iv = (ImageView) convertView.findViewById(R.id.share_iv);
                holder.match_iv = (ImageView) convertView.findViewById(R.id.match_iv);
                holder.match_name_tv = (TextView) convertView.findViewById(R.id.match_name_tv);
                holder.match_name02_tv = (TextView) convertView.findViewById(R.id.match_name02_tv);
                holder.horizon_listview = (HorizontalListView) convertView.findViewById(R.id.horizon_listview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final JSONObject jsonObject = mSchemes.getJSONObject(position);
            String style = jsonObject.getString(Constance.style);
            String space = jsonObject.getString(Constance.space);
            final String path = NetWorkConst.SCENE_HOST + jsonObject.getString(Constance.path);
            holder.match_name_tv.setText(jsonObject.getString(Constance.name));
            holder.match_name02_tv.setText(style + "/" + space);
            ImageLoader.getInstance().displayImage(path
                    , holder.match_iv);
            JSONArray jsonArray = jsonObject.getJSONArray(Constance.goodsInfo);

            holder.close_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDeleteIndex = position;
                    sendDeleteFangan(jsonObject.getInt(Constance.id));
                }
            });

            //分享
            holder.share_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertView(null, null, "取消", null,
                            new String[]{"复制链接", "分享图片", "分享链接"},
                            mView.getContext(), AlertView.Style.ActionSheet, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                            switch (position) {
                                case 0:
                                    if (!mView.isToken()) {
                                        ClipboardManager cm = (ClipboardManager) mView.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                        cm.setText(path);
                                    }
                                    break;
                                case 1:
                                    String title1 = "来自 " + jsonObject.getString(Constance.name) + " 方案的分享";
                                    ShareUtil.showShare01(mView.getActivity(), title1, "1", path);
                                    break;
                                case 2:
                                    String title = "来自 " + jsonObject.getString(Constance.name) + " 方案的分享";
                                    ShareUtil.showShare(mView.getActivity(), title, path, path);
                                    break;
                            }
                        }
                    }).show();


                }
            });

            MatchItemAdapter matchItemAdapter = new MatchItemAdapter(mView.getActivity(), jsonArray);
            holder.horizon_listview.setAdapter(matchItemAdapter);


            holder.match_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mView.getActivity(), ImageDetailActivity.class);
                    intent.putExtra(Constance.photo, path);
                    mView.getActivity().startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView close_iv, match_iv, share_iv;
            HorizontalListView horizon_listview;
            TextView match_name_tv, match_name02_tv;
        }
    }


}
