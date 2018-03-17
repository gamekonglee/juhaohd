package bc.juhaohd.com.ui.activity.buy;

import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.listener.ITwoCodeListener;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.view.popwindow.TwoCodeSharePopWindow;
import bc.juhaohd.com.utils.MyShare;
import bocang.json.JSONArray;

/**
 * @author: Jun
 * @date : 2017/8/7 9:27
 * @description :
 */
public class ExInventoryActivity extends BaseActivity {
    private RelativeLayout share_rl;
    private LinearLayout cotent_ll,main_ll;
    public JSONArray goodsList;
    public JSONObject orderObject;
    private LinearLayout wechat_ll, wechatmoments_ll, share_qq_ll, save_ll;
    private ScrollView sv;
    private int mShareType = 0;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    private String mPath="";

    @Override
    protected void initView() {
        setContentView(R.layout.activity_cart_inventory);
        share_rl = getViewAndClick(R.id.share_rl);
        wechat_ll = getViewAndClick(R.id.wechat_ll);
        wechatmoments_ll = getViewAndClick(R.id.wechatmoments_ll);
        share_qq_ll = getViewAndClick(R.id.share_qq_ll);
        save_ll = getViewAndClick(R.id.save_ll);
        sv = (ScrollView) findViewById(R.id.sv);
        cotent_ll = (LinearLayout) findViewById(R.id.cotent_ll);
        main_ll = (LinearLayout) findViewById(R.id.main_ll);
        for (int i = 0; i < mUids.size(); i++) {
            WebView mView = new WebView(this);
            WebSettings settings = mView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setAppCacheEnabled(true);
            settings.setDatabaseEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            settings.setAllowFileAccess(true);
            //        //设置WebView可触摸放大缩小
            mView.setInitialScale(100);
            mView.setDrawingCacheEnabled(true);
            mView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            cotent_ll.addView(mView);
        }

        if (mShareType == 0) {
            String id = IssueApplication.mUserObject.getString(Constance.id);
            for (int i = 0; i < cotent_ll.getChildCount(); i++) {

                mPath= NetWorkConst.SCENE_HOST + "order_show.php?uid=" + id + "&goods=" + mUids.get(i) + "&number=" + mNumbers.get(i) + "&page=" + (1);
                ((WebView) cotent_ll.getChildAt(i)).loadUrl(mPath);
            }
        } else {
            for (int i = 0; i < cotent_ll.getChildCount(); i++) {
                mPath = NetWorkConst.SCENE_HOST + "order_show.php?goods=" + mUids.get(i) + "&number=" + mNumbers.get(i) + "&page=" + (1) + morderUserUrl;
                ((WebView) cotent_ll.getChildAt(i)).loadUrl(mPath);
            }
        }

    }


    private int mPage = 1;
    private List<String> mUids = new ArrayList<>();
    private List<String> mNumbers = new ArrayList<>();

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mShareType = intent.getIntExtra(Constance.pdfType, 0);
        if (mShareType == 1) {
            orderObject = JSON.parseObject(intent.getStringExtra(Constance.goods));
        } else {
            goodsList = (JSONArray) intent.getSerializableExtra(Constance.goods);
        }


        getInventoryData();
    }

    String morderUserUrl = "";


    private void getInventoryData() {
        if (mShareType == 0) {
            if (goodsList.length() <= 8) {
                String uid = "";
                String number = "";
                mPage = 1;
                for (int i = 0; i < goodsList.length(); i++) {
                    uid = uid + goodsList.getJSONObject(i).getJSONObject(Constance.product).getString(Constance.id) + ",";
                    number = number + goodsList.getJSONObject(i).getString(Constance.amount) + ",";
                }
                uid = uid.substring(0, uid.length() - 1);
                number = number.substring(0, number.length() - 1);
                mUids.add(uid);
                mNumbers.add(number);
            } else {
                mPage = (goodsList.length() % 8 == 0) ? goodsList.length() / 8 : (goodsList.length() / 8 + 1);
                for (int i = 0; i < mPage; i++) {
                    String uid = "";
                    String number = "";
                    for (int j = 8 * i; j < 8 * (i + 1); j++) {
                        if (j > goodsList.length() - 1)
                            break;
                        uid = uid + goodsList.getJSONObject(j).getJSONObject(Constance.product).getString(Constance.id) + ",";
                        number = number + goodsList.getJSONObject(j).getString(Constance.amount) + ",";

                    }
                    uid = uid.substring(0, uid.length() - 1);
                    number = number.substring(0, number.length() - 1);
                    mUids.add(uid);
                    mNumbers.add(number);
                }
            }
        } else {
            com.alibaba.fastjson.JSONArray goods = orderObject.getJSONArray(Constance.goods);
            if (goods.size() <= 8) {
                String uid = "";
                String number = "";
                mPage = 1;
                for (int i = 0; i < goods.size(); i++) {
                    uid = uid + goods.getJSONObject(i).getJSONObject(Constance.product).getString(Constance.id) + ",";
                    number = number + goods.getJSONObject(i).getString(Constance.total_amount) + ",";
                }
                uid = uid.substring(0, uid.length() - 1);
                number = number.substring(0, number.length() - 1);
                mUids.add(uid);
                mNumbers.add(number);
            } else {
                mPage = (goods.size() % 8 == 0) ? goods.size() / 8 : (goods.size() / 8 + 1);
                for (int i = 0; i < mPage; i++) {
                    String uid = "";
                    String number = "";
                    for (int j = 8 * i; j < 8 * (i + 1); j++) {
                        if (j > goods.size() - 1)
                            break;
                        uid = uid + goods.getJSONObject(j).getJSONObject(Constance.product).getString(Constance.id) + ",";
                        number = number + goods.getJSONObject(j).getString(Constance.total_amount) + ",";

                    }
                    uid = uid.substring(0, uid.length() - 1);
                    number = number.substring(0, number.length() - 1);
                    mUids.add(uid);
                    mNumbers.add(number);
                }
            }

            JSONObject consigneeObject = orderObject.getJSONObject(Constance.consignee);
            String phone = consigneeObject.getString(Constance.mobile);
            String address = consigneeObject.getString(Constance.address);
            String username = consigneeObject.getString(Constance.name);
            morderUserUrl = "&phone=" + phone + "&address=" + address + "&username=" + username;
        }


    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.share_rl://分享
                shareUrl(mPath);
                break;

        }

    }


    private void shareUrl(String url){
        String title = "购物车清单的分享";
        MyShare.get(this).putString(Constance.sharePath,url);
         MyShare.get(this).putString(Constance.shareRemark,title);
        TwoCodeSharePopWindow popWindow = new TwoCodeSharePopWindow(this, this);
        popWindow.onShow(main_ll);
        popWindow.setListener(new ITwoCodeListener() {
            @Override
            public void onTwoCodeChanged(String path) {
            }
        });
    }


}
