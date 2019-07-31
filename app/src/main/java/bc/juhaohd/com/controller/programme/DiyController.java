package bc.juhaohd.com.controller.programme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.lib.common.hxp.view.PullToRefreshLayout;
import com.lib.common.hxp.view.PullableGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pgyersdk.crash.PgyCrashManager;

import org.greenrobot.eventbus.EventBus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import bc.juhaohd.com.R;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.cons.NetWorkConst;
import bc.juhaohd.com.controller.BaseController;
import bc.juhaohd.com.listener.IDiyProductInfoListener;
import bc.juhaohd.com.listener.INetworkCallBack;
import bc.juhaohd.com.listener.ISelectScreenListener;
import bc.juhaohd.com.listener.ITwoCodeListener;
import bc.juhaohd.com.ui.activity.HomeShowNewActivity;
import bc.juhaohd.com.ui.activity.IssueApplication;
import bc.juhaohd.com.ui.activity.MainNewActivity;
import bc.juhaohd.com.ui.activity.WebViewCpActivity;
import bc.juhaohd.com.ui.activity.product.ProductDetailHDActivity;
import bc.juhaohd.com.ui.activity.product.SelectGoodsActivity;
import bc.juhaohd.com.ui.activity.programme.DiyActivity;
import bc.juhaohd.com.ui.activity.programme.SelectSceneActivity;
import bc.juhaohd.com.ui.activity.programme.SelectSchemeActivity;
import bc.juhaohd.com.ui.adapter.ProductDropMenuAdapter;
import bc.juhaohd.com.ui.adapter.SceneDropMenuAdapter;
import bc.juhaohd.com.ui.view.StickerView;
import bc.juhaohd.com.ui.view.TouchView02;
import bc.juhaohd.com.ui.view.popwindow.DiyProductInfoPopWindow;
import bc.juhaohd.com.ui.view.popwindow.SelectScreenPopWindow;
import bc.juhaohd.com.ui.view.popwindow.TwoCodePopWindow;
import bc.juhaohd.com.ui.view.popwindow.TwoCodeSharePopWindow;
import bc.juhaohd.com.utils.FileUtil;
import bc.juhaohd.com.utils.ImageUtil;
import bc.juhaohd.com.utils.MyShare;
import bc.juhaohd.com.utils.UIUtils;
import bocang.json.JSONArray;
import bocang.json.JSONObject;
import bocang.utils.AppDialog;
import bocang.utils.AppUtils;
import bocang.utils.MyToast;
import bocang.utils.PermissionUtils;

/**
 * @author: Jun
 * @date : 2017/2/18 10:33
 * @description :场景配灯
 */
public class DiyController extends BaseController implements INetworkCallBack, PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, OnFilterDoneListener {
    private DiyActivity mView;
    private ImageView sceneBgIv;
    private String imageURL = "";
    private RelativeLayout diyContainerRl, data_rl, detail_rl;
    private Boolean isFullScreen = false;
    private Intent mIntent;
    private int mScreenWidth;
    private FrameLayout mFrameLayout, main_fl;
    private ProgressBar pd2;
    private String mStyle = "";
    private String mSpace = "";
    private int TIME_OUT = 10 * 1000;   //超时时间
    private String CHARSET = "utf-8"; //设置编码
    private String mTitle = "";
    private String mContent = "";
    private SeekBar seekbar;
    private int mSeekNum = 50;
    private LinearLayout seekbar_ll, botton_ll;

    private DropDownMenu dropDownMenu;
    private JSONArray sceneAllAttrs;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ProAdapter mProAdapter;
    private PullableGridView mGoodsSv;
    private int page = 1;
    private JSONArray goodses;
    private boolean initFilterDropDownView;
    public String keyword;
    private ProgressBar pd;
    public String mSortKey;
    public String mSortValue;
    public int mSelectType = 0;
    private TextView product_name_tv, product_properties_tv, product_price_tv;
    private String filter_attr = "";
    private ImageView two_code_iv;
    private JSONArray propertiesList;
    private ProAdapter mAdapter;
    private ListView product_lv, diy_lv;
    //当前处于编辑状态的贴纸
    private StickerView mCurrentView;
    //存储贴纸列表
    private ArrayList<View> mViews;
    private Bitmap mSceneBg;
    private DiyProductInfoPopWindow mPopWindow;

    public DiyController(DiyActivity v) {
        mView = v;
        initView();
        initViewData();
    }


    private void initViewData() {
        initFilterDropDownView = true;
        if (!AppUtils.isEmpty(mView.mGoodsObject)) {
            displayCheckedGoods(mView.mGoodsObject);
            mView.mSelectType = 0;
        } else {
            if (AppUtils.isEmpty(mView.mSceenPath))
                return;
            displaySceneBg(mView.mSceenPath);
            mView.mSelectType = 1;
        }
        mView.switchProOrDiy();
        selectShowData();

    }


    private void initView() {
        sceneBgIv = (ImageView) mView.findViewById(R.id.sceneBgIv);
        diyContainerRl = (RelativeLayout) mView.findViewById(R.id.diyContainerRl);
        data_rl = (RelativeLayout) mView.findViewById(R.id.data_rl);
        detail_rl = (RelativeLayout) mView.findViewById(R.id.detail_rl);
        mScreenWidth = mView.getResources().getDisplayMetrics().widthPixels;
        mFrameLayout = (FrameLayout) mView.findViewById(R.id.sceneFrameLayout);
        main_fl = (FrameLayout) mView.findViewById(R.id.main_fl);
        pd2 = (ProgressBar) mView.findViewById(R.id.pd2);
        seekbar = (SeekBar) mView.findViewById(R.id.seekbar);
        seekbar_ll = (LinearLayout) mView.findViewById(R.id.seekbar_ll);
        initImageLoader();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekNum = progress;
                //                ImageUtil.changeLight(sceneBgIv, mSeekNum - 50);

                if (!AppUtils.isEmpty(mSceneBg))
                    ImageUtil.changeLight02(sceneBgIv, mSceneBg, mSeekNum);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dropDownMenu = (DropDownMenu) mView.findViewById(R.id.dropDownMenu);
        mPullToRefreshLayout = ((PullToRefreshLayout) mView.findViewById(R.id.mFilterContentView));
        mPullToRefreshLayout.setOnRefreshListener(this);
        mGoodsSv = (PullableGridView) mView.findViewById(R.id.gridView);
        mProAdapter = new ProAdapter();
        mGoodsSv.setAdapter(mProAdapter);
        mGoodsSv.setOnItemClickListener(this);
        pd = (ProgressBar) mView.findViewById(R.id.pd);
        botton_ll = (LinearLayout) mView.findViewById(R.id.botton_ll);
        product_name_tv = (TextView) mView.findViewById(R.id.product_name_tv);
        product_properties_tv = (TextView) mView.findViewById(R.id.product_properties_tv);
        product_price_tv = (TextView) mView.findViewById(R.id.product_price_tv);
        two_code_iv = (ImageView) mView.findViewById(R.id.two_code_iv);
        product_lv = (ListView) mView.findViewById(R.id.product_lv);
        diy_lv = (ListView) mView.findViewById(R.id.diy_lv);
        mViews = new ArrayList<>();

    }

    @Override
    protected void handleMessage(int action, Object[] values) {

    }

    @Override
    protected void myHandleMessage(Message msg) {

    }

    public void selectShowData() {
        if (mView.mSelectType == 0) {
            goodses = IssueApplication.mSelectProducts;
            mAdapter = new ProAdapter();
            product_lv.setAdapter(mAdapter);
        } else {
            goodses = IssueApplication.mSelectScreens;
            mAdapter = new ProAdapter();
            product_lv.setAdapter(mAdapter);
        }
    }

    /**
     * 保存
     */
    public void saveDiy() {
        mIntent = new Intent(mView, SelectSchemeActivity.class);
        mView.startActivityForResult(mIntent, Constance.FROMSCHEME);
    }

    /**
     * 保存方案
     */
    private void saveData() {
        //                产品ID的累加
        StringBuffer goodsid = new StringBuffer();

        for (int i = 0; i < IssueApplication.mSelectedLightSA.size(); i++) {
            if(IssueApplication.mSelectedLightSA.valueAt(i)==null|| TextUtils.isEmpty(IssueApplication.mSelectedLightSA.valueAt(i).getString(Constance.id))){
                IssueApplication.mSelectedLightSA.removeAt(i);
            }
        }
        for (int i = 0; i < IssueApplication.mSelectedLightSA.size(); i++) {
            if(IssueApplication.mSelectedLightSA.valueAt(i)==null)
            {
                MyToast.show(mView,"请重新添加产品");
                return;
            }
            goodsid.append(IssueApplication.mSelectedLightSA.valueAt(i).getString(Constance.id) + "");
            if (i < IssueApplication.mSelectedLightSA.size() - 1) {
                goodsid.append(",");
            }
        }
        if (!AppUtils.isEmpty(mCurrentView))
            mCurrentView.setInEdit(false);

        diyContainerRl.setVisibility(View.INVISIBLE);
        mView.select_ll.setVisibility(View.INVISIBLE);
        //截图
        //        final Bitmap imageData = ;
        InputMethodManager imm = (InputMethodManager) mView.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindow().getDecorView().getWindowToken(), 0);
        final Bitmap imageData = ImageUtil.compressImage02(ImageUtil.takeScreenShot(mView));

        diyContainerRl.setVisibility(View.VISIBLE);
        mView.select_ll.setVisibility(View.VISIBLE);
        mView.setShowDialog(true);
        mView.setShowDialog("正在保存中...");
        mView.showLoading();
        int id = MyShare.get(mView).getInt(Constance.USERCODEID);
        final String url = NetWorkConst.FANGANUPLOAD;//地址
        final Map<String, String> params = new HashMap<String, String>();
        params.put("name", mTitle);
        //        params.put("scene_id", "android");
        params.put("goods_id", goodsid.toString());
        params.put("content", mContent);
        params.put("style", mStyle);
        params.put("space", mSpace);
        params.put("parent_id", id + "");
//        Exception e=new Exception("name:"+mTitle+",goods_id:"+goodsid.toString()+",content:"+mContent+",style:"+mStyle+",space:"+mSpace+",parent_id:"+id+"");
//        PgyCrashManager.reportCaughtException(mView,e);
        final String imageName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".png";
        new Thread(new Runnable() { //开启线程上传文件
            @Override
            public void run() {
                final String resultJson = uploadFile(imageData, url, params, imageName);
                //分享的操作
                mView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideLoading();
                        if (AppUtils.isEmpty(resultJson)) {
                            MyToast.show(mView, "保存失败!");
                            return;
                        }

                        com.alibaba.fastjson.JSONObject object = JSON.parseObject(resultJson);
                        int isResult = object.getInteger(Constance.error_code);
                        if (isResult != 0) {
                            MyToast.show(mView, "保存失败!");
                            return;
                        }
                        final String path = NetWorkConst.SHARE_FANGAN + object.getJSONObject(Constance.fangan).getString(Constance.id);

                        String title = "来自 " + UIUtils.getString(R.string.app_name) + " 配灯的分享";
                        MyShare.get(mView).putString(Constance.sharePath, path);
                        MyShare.get(mView).putString(Constance.shareRemark, title);
                        TwoCodeSharePopWindow popWindow = new TwoCodeSharePopWindow(mView, mView);
                        popWindow.onShow(mFrameLayout);
                        popWindow.setListener(new ITwoCodeListener() {
                            @Override
                            public void onTwoCodeChanged(String path) {
                            }
                        });
                    }
                });

            }
        }).start();
    }

    /**
     * 照相
     */
    public void goPhoto() {
        FileUtil.openImage(mView);
    }

    /**
     * 删除
     */
    public void goDetele() {
        mFrameLayout.removeView(mFrameLayout.findViewWithTag(IssueApplication.mLightIndex));
        IssueApplication.mSelectedLightSA.removeAt(IssueApplication.mLightIndex);
    }


    private boolean isGoCart = false;

    /**
     * 保存购物车
     */
    public void goShoppingCart() {
        if (IssueApplication.mSelectedLightSA.size() == 0) {
            MyToast.show(mView, "请选择产品");
            return;
        }
        mView.setShowDialog(true);
        mView.setShowDialog("正在加入购物车中...");
        mView.showLoading();
        isGoCart = false;
        for (int i = 0; i < IssueApplication.mSelectedLightSA.size(); i++) {
            JSONObject object = IssueApplication.mSelectedLightSA.valueAt(i);
            String id = object.getString(Constance.id);
            JSONArray propertieArray = object.getJSONArray(Constance.properties);
            if (propertieArray.length() == 0) {
                sendGoShoppingCart(id, "", 1);
            } else {
                if (!AppUtils.isEmpty(mView.mProductId) && id.equals(mView.mProductId)) {
                    sendGoShoppingCart(id, mView.mProperty, 1);
                } else {
                    JSONArray attrsArray = propertieArray.getJSONObject(0).getJSONArray(Constance.attrs);
                    String propertie = attrsArray.getJSONObject(0).toString();
                    sendGoShoppingCart(id, propertie, 1);
                }
            }
            if (i == IssueApplication.mSelectedLightSA.size() - 1) {
                isGoCart = true;
            }
        }
    }

    private void sendGoShoppingCart(String product, String property, int mount) {
        mNetWork.sendShoppingCart(product, property, mount, this);
    }


    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    private String uploadFile(Bitmap file, String RequestURL, Map<String, String> param, String imageName) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        String token = MyShare.get(UIUtils.getContext()).getString(Constance.TOKEN);
        // 显示进度框
        //      showProgressDialog();
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("X-bocang-Authorization", token);
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();

                String params = "";
                if (param != null && param.size() > 0) {
                    Iterator<String> it = param.keySet().iterator();
                    while (it.hasNext()) {
                        sb = null;
                        sb = new StringBuffer();
                        String key = it.next();
                        String value = param.get(key);
                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                        sb.append(value).append(LINE_END);
                        params = sb.toString();
                        dos.write(params.getBytes());
                    }
                }
                sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"").append("image").append("\"")
                        .append(";filename=\"").append(imageName).append("\"\n");
                sb.append("Content-Type: image/png");
                sb.append(LINE_END).append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = ImageUtil.Bitmap2InputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }


                is.close();
                //                dos.write(file);
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);

                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */

                int res = conn.getResponseCode();
                System.out.println("res=========" + res);
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                } else {
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 选产品
     */
    public void selectProduct() {
        //        mSelectType = 0;
        //        data_rl.setVisibility(View.VISIBLE);
        //        goodses = null;
        //        keyword = "";
        //        mProAdapter.notifyDataSetChanged();
        //        page = 1;
        //        selectProduct(page, "20", null, null, null);
        //        sendAttrList();
        //        pd.setVisibility(View.VISIBLE);
        mIntent = new Intent(mView, MainNewActivity.class);
        mIntent.putExtra(Constance.ISSELECTGOODS, true);
        mIntent.putExtra(Constance.filter_attr_name,"吊灯");
        HomeShowNewActivity.mFragmentState=1;
        mView.startActivityForResult(mIntent, Constance.FROMDIY);
    }


    /**
     * 选场景
     */
    public void selectScreen() {
        //        new AlertView(null, null, "取消", null,
        //                new String[]{"场景", "请上传自家场景"},
        //                mView, AlertView.Style.ActionSheet, new OnItemClickListener() {
        //            @Override
        //            public void onItemClick(Object o, int position) {
        //                switch (position) {
        //                    case 0:
        //                        PermissionUtils.requestPermission(mView, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
        //                            @Override
        //                            public void onPermissionGranted(int requestCode) {
        //                                data_rl.setVisibility(View.VISIBLE);
        //                                goodses = null;
        //                                keyword = "";
        //                                mProAdapter.notifyDataSetChanged();
        //                                mSelectType = 1;
        //                                page = 1;
        //                                sendSceneList(page);
        //                                sendSceneType();
        //                                pd.setVisibility(View.VISIBLE);
        //                            }
        //                        });
        //                        break;
        //                    case 1:
        //                        TwoCodePopWindow popWindow = new TwoCodePopWindow(mView, mView);
        //                        popWindow.onShow(mFrameLayout);
        //                        popWindow.setListener(new ITwoCodeListener() {
        //                            @Override
        //                            public void onTwoCodeChanged(String path) {
        //                                if (AppUtils.isEmpty(path))
        //                                    return;
        //                                mView.mSceenPath = path;
        //                                displaySceneBg(mView.mSceenPath);
        //                            }
        //                        });
        //                        break;
        //                }
        //            }
        //        }).show();

        SelectScreenPopWindow popWindow = new SelectScreenPopWindow(mView, mView);
        popWindow.onShow(main_fl);
        popWindow.setListener(new ISelectScreenListener() {
            @Override
            public void onSelectScreenChanged(int type) {
                if (type == 0) {
                    selectSceneDatas();
                } else {
                    TwoCodePopWindow popWindow = new TwoCodePopWindow(mView, mView);
                    popWindow.onShow(mFrameLayout);
                    popWindow.setListener(new ITwoCodeListener() {
                        @Override
                        public void onTwoCodeChanged(String path) {
                            if (AppUtils.isEmpty(path))
                                return;
                            mView.mSceenPath = path;
                            displaySceneBg(mView.mSceenPath);
                        }
                    });
                }
            }
        });


    }


    /**
     * 场景列表
     */
    public void sendSceneList(int page) {
        mNetWork.sendSceneList(page, "20", keyword, this);
    }


    public void ActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == mView.RESULT_OK) { // 返回成功
            switch (requestCode) {
                case Constance.PHOTO_WITH_CAMERA: {// 拍照获取图片
                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) { // 是否有SD卡
                        File imageFile = new File(IssueApplication.cameraPath, IssueApplication.imagePath + ".jpg");
                        if (imageFile.exists()) {
                            imageURL = "file://" + imageFile.toString();
                            IssueApplication.imagePath = null;
                            IssueApplication.cameraPath = null;
                            displaySceneBg(imageURL);
                            //                            displayCheckedGoods(mGoodsObject);
                            //                            ImageLoader.getInstance().displayImage(imageURL
                            //                                   , sceneBgIv);
                        } else {
                            AppDialog.messageBox("读取图片失败！");
                        }
                    } else {
                        AppDialog.messageBox("没有SD卡！");
                    }
                    break;
                }
                case Constance.PHOTO_WITH_DATA: // 从图库中选择图片
                    // 照片的原始资源地址
                    imageURL = data.getData().toString();
                    displaySceneBg(imageURL);
                    //                    displayCheckedGoods(mView.mGoodsObject);
                    //                    ImageLoader.getInstance().displayImage(imageURL
                    //                            , sceneBgIv);
                    break;
            }
        } else if (requestCode == Constance.FROMDIY) {
            mView.mSelectType = 0;
            mView.switchProOrDiy();
            selectShowData();
        } else if (requestCode == Constance.FROMDIY02) {
            if (AppUtils.isEmpty(data))
                return;
            mView.mPath = data.getStringExtra(Constance.SCENE);
            if (!AppUtils.isEmpty(mView.mPath)) {
                displaySceneBg(mView.mPath);
            }
            selectShowData();
        } else if (requestCode == Constance.FROMSCHEME) {
            if (AppUtils.isEmpty(data))
                return;
            mStyle = data.getStringExtra(Constance.style);
            mSpace = data.getStringExtra(Constance.space);
            mContent = data.getStringExtra(Constance.content);
            mTitle = data.getStringExtra(Constance.title);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        mView.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                saveData();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }if(requestCode==300){
            displayCheckedGoods03("file://"+data.getStringExtra("path"));
        }
    }

    private void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                // 设置图片下载期间显示的图片
                .showImageOnLoading(R.drawable.bg_default)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(R.drawable.bg_default)
                // 设置图片加载或解码过程中发生错误显示的图片
                .showImageOnFail(R.drawable.bg_default)
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(false)
                //设置图片的质量
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                //                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片可以放大（要填满ImageView必须配置memoryCacheExtraOptions大于Imageview）
                // 图片加载好后渐入的动画时间
                // .displayer(new FadeInBitmapDisplayer(100))
                .build(); // 构建完成

        // 得到ImageLoader的实例(使用的单例模式)
        imageLoader = ImageLoader.getInstance();
    }

    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private int mLightNumber = -1;// 点出来的灯的编号
    private int mLightId = 0;//点出来的灯的序号
    private int leftMargin = 0;

    private void displayCheckedGoods(final JSONObject goods) {
        if (AppUtils.isEmpty(goods)||goods.getJSONObject(Constance.app_img)==null)
            return;
        String path = goods.getJSONObject(Constance.app_img).getString(Constance.img);
        imageLoader.loadImage(path, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        pd2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        pd2.setVisibility(View.GONE);
                        MyToast.show(mView, failReason.getCause() + "请重试！");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        pd2.setVisibility(View.GONE);
                        // 被点击的灯的编号加1
                        mLightNumber++;
                        // 把点击的灯放到集合里
                        IssueApplication.mSelectedLightSA.put(mLightNumber, goods);
//                        MyToast.show(mView,""+IssueApplication.mSelectedLightSA.size());
                        addStickerView(ImageUtil.zoomBitmap(loadedImage,loadedImage.getWidth()/2,loadedImage.getHeight()/2));

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        pd2.setVisibility(View.GONE);
                    }
                });
    }


    //添加表情
    private void addStickerView(Bitmap bitmap) {


        final StickerView stickerView = new StickerView(mView);
        stickerView.setBitmap(bitmap);
        stickerView.mLightCount = mLightNumber;
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                mFrameLayout.removeView(stickerView);
                IssueApplication.mSelectedLightSA.remove(IssueApplication.mLightIndex);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        stickerView.setTag(mLightNumber);
        mFrameLayout.addView(stickerView, lp2);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);


    }

    /**
     * 设置当前处于编辑模式的贴纸
     */
    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    /**
     * 加载场景背景图
     *
     * @param path 场景img_url
     */
    private void displaySceneBg(String path) {
        imageURL = path;
        imageLoader.displayImage(path, sceneBgIv, options,
                new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        pd2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        pd2.setVisibility(View.GONE);
                        MyToast.show(mView, failReason.getCause() + "请重试！");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        pd2.setVisibility(View.GONE);
                        mSceneBg = ImageUtil.drawable2Bitmap(sceneBgIv.getDrawable());
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        pd2.setVisibility(View.GONE);
                    }
                });
    }


    /**
     * 添加图片
     *
     * @param bitmap
     */
    private void addImageView(Bitmap bitmap) {
        pd2.setVisibility(View.GONE);
        // 被点击的灯的编号加1
        mLightNumber++;
        IssueApplication.mLightIndex = mLightNumber;
        // 设置灯图的ImageView的初始宽高和位置
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                mScreenWidth / 3 * 2 / 3,
                (mScreenWidth / 3 * 2 / 3 * bitmap.getHeight()) / bitmap.getWidth());
        // 设置灯点击出来的位置
        if (IssueApplication.mSelectedLightSA.size() == 1) {
            leftMargin = mScreenWidth / 3 * 2 / 3;
        } else if (IssueApplication.mSelectedLightSA.size() == 2) {
            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
        } else if (IssueApplication.mSelectedLightSA.size() == 3) {
            leftMargin = 0;
        }
        lp.setMargins(mScreenWidth / 2 - (mScreenWidth / 3 * 2 / 6), 20, 0, 0);


        TouchView02 touchView = new TouchView02(mView);
        touchView.setLayoutParams(lp);
        touchView.setImageBitmap(bitmap);// 设置被点击的灯的图片
        touchView.setmLightCount(mLightNumber);// 设置被点击的灯的编号
        touchView.setTag(mLightNumber);
        FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        FrameLayout newFrameLayout = new FrameLayout(mView);
        newFrameLayout.setLayoutParams(newLp);
        newFrameLayout.addView(touchView);
        newFrameLayout.setTag(mLightNumber);
        mFrameLayout.addView(newFrameLayout);

        touchView.setContainer(mFrameLayout, newFrameLayout);
    }


    private void displayCheckedGoods03(String path) {
        if (AppUtils.isEmpty(path))
            return;
        imageLoader.loadImage(path, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        pd2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        pd2.setVisibility(View.GONE);
                        MyToast.show(mView, failReason.getCause() + "请重试！");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        pd2.setVisibility(View.GONE);
                        // 被点击的灯的编号加1
                        mLightNumber++;
                        // 把点击的灯放到集合里
                        // 设置灯图的ImageView的初始宽高和位置
                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                                mScreenWidth / 3 * 2 / 3,
                                (mScreenWidth / 3 * 2 / 3 * loadedImage.getHeight()) / loadedImage.getWidth());
                        // 设置灯点击出来的位置
                        if (IssueApplication.mSelectedLightSA.size() == 1) {
                            leftMargin = mScreenWidth / 3 * 2 / 3;
                        } else if (IssueApplication.mSelectedLightSA.size() == 2) {
                            leftMargin = mScreenWidth / 3 * 2 / 3 * 2;
                        } else if (IssueApplication.mSelectedLightSA.size() == 3) {
                            leftMargin = 0;
                        }
                        lp.setMargins(mScreenWidth / 2 - (mScreenWidth / 3 * 2 / 6), 20, 0, 0);


                        TouchView02 touchView = new TouchView02(mView);
                        touchView.setLayoutParams(lp);
                        touchView.setImageBitmap(loadedImage);// 设置被点击的灯的图片
                        touchView.setmLightCount(mLightNumber);// 设置被点击的灯的编号
                        touchView.setTag(mLightNumber);
                        FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT);

                        FrameLayout newFrameLayout = new FrameLayout(mView);
                        newFrameLayout.setLayoutParams(newLp);
                        newFrameLayout.addView(touchView);
                        newFrameLayout.setTag(mLightNumber);
                        mFrameLayout.addView(newFrameLayout);

                        touchView.setContainer(mFrameLayout, newFrameLayout);


                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        pd2.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 判断是否满屏
     */
    public void selectIsFullScreen() {
        seekbar_ll.setVisibility(View.GONE);
        data_rl.setVisibility(View.GONE);

        dropDownMenu.close();
        if (!isFullScreen) {
            diyContainerRl.setVisibility(View.INVISIBLE);
            isFullScreen = true;
            mView.select_ll.setVisibility(View.GONE);
        } else {
            diyContainerRl.setVisibility(View.VISIBLE);
            mView.select_ll.setVisibility(View.VISIBLE);
            isFullScreen = false;
        }
        if (AppUtils.isEmpty(mCurrentView))
            return;
        mCurrentView.setInEdit(false);
    }

    /**
     * 获取购物车列表
     */
    public void sendShoppingCart() {
        mNetWork.sendShoppingCart(this);
    }

    @Override
    public void onSuccessListener(String requestCode, JSONObject ans) {

        switch (requestCode) {
            case NetWorkConst.ADDCART:
                if (isGoCart) {
                    mView.hideLoading();
                    MyToast.show(mView, "加入购物车成功!");
                    sendShoppingCart();
                }
                break;
            case NetWorkConst.GETCART:
                if (ans.getJSONArray(Constance.goods_groups).length() > 0) {
                    IssueApplication.mCartCount = ans.getJSONArray(Constance.goods_groups)
                            .getJSONObject(0).getJSONArray(Constance.goods).length();
                } else {
                    IssueApplication.mCartCount = 0;
                }
                EventBus.getDefault().post(Constance.CARTCOUNT);
                break;

            case NetWorkConst.PRODUCT:
                pd.setVisibility(View.GONE);
                if (null == mView || mView.isFinishing())
                    return;

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                JSONArray goodsList = ans.getJSONArray(Constance.goodsList);
                if (AppUtils.isEmpty(goodsList)) {
                    if (page == 1) {

                    }
                    goodses = new JSONArray();
                    dismissRefesh();
                    return;
                }

                getDataSuccess(goodsList);

                break;
            case NetWorkConst.SCENECATEGORY:
                pd.setVisibility(View.GONE);
                sceneAllAttrs = ans.getJSONArray(Constance.categories);
                if (initFilterDropDownView)//重复setMenuAdapter会报错
                    initFilterDropDownView(sceneAllAttrs);
                break;

            case NetWorkConst.SCENELIST:
                if (null == mView || mView.isFinishing())
                    return;

                if (null != mPullToRefreshLayout) {
                    dismissRefesh();
                }
                JSONArray sceneList = ans.getJSONArray(Constance.scene);
                if (AppUtils.isEmpty(sceneList)) {
                    if (page == 1) {

                    }
                    MyToast.show(mView, "数据已经到底啦!");
                    dismissRefesh();
                    return;
                }

                getDataSuccess(sceneList);

                break;
            case NetWorkConst.ADDLIKEDPRODUCT:
                mView.hideLoading();
                MyToast.show(mView, "加入成功!");
                break;
            case NetWorkConst.ATTRLIST:
                sceneAllAttrs = ans.getJSONArray(Constance.goods_attr_list);
                if (initFilterDropDownView)//重复setMenuAdapter会报错
                    initFilterDropDownView(sceneAllAttrs);
                break;
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
                MyToast.show(mView, "没有更多内容了");
        }
        mProAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailureListener(String requestCode, JSONObject ans) {
        pd.setVisibility(View.GONE);
    }

    /**
     * 背景图镜像
     */
    public void sendBackgroudImage() {
        ImageUtil.changeLight(sceneBgIv, 0);
        Bitmap mBitmap = ImageUtil.drawable2Bitmap(sceneBgIv.getDrawable());
        if (mBitmap != null) {

            Bitmap temp = ImageUtil.convertBmp(mBitmap);
            if (temp != null) {
                sceneBgIv.setImageBitmap(temp);
                mBitmap.recycle();
            }
            ImageUtil.changeLight(sceneBgIv, mSeekNum - 50);
        }

    }


    /**
     * 产品镜像
     */
    public void sendProductImage() {

        try {
            StickerView stickerView = (StickerView) mFrameLayout.findViewWithTag(IssueApplication.mLightIndex);
            stickerView.getFlipView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 调节图片亮度
     */
    public void goBrightness() {
        seekbar_ll.setVisibility(View.VISIBLE);
    }

    //
    //    查询产品和场景
    //

    /**
     * 查询数据
     */
    public void selectData() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mSelectType == 0) {
            mView.mGoodsObject = goodses.getJSONObject(position);
            displayCheckedGoods(mView.mGoodsObject);
        } else {
            mView.mPath = NetWorkConst.SCENE_HOST + goodses.getJSONObject(position).getJSONObject(Constance.scene).getString(Constance.original_img);
            displaySceneBg(mView.mPath);
        }
    }

    private List<Integer> itemPosList = new ArrayList<>();//有选中值的itemPos列表，长度为3

    private void initFilterDropDownView(JSONArray sceneAllAttrs) {
        if (itemPosList.size() < sceneAllAttrs.length()) {
            itemPosList.add(0);
            itemPosList.add(0);
            itemPosList.add(0);
        }
        if (mSelectType == 0) {
            ProductDropMenuAdapter dropMenuAdapter = new ProductDropMenuAdapter(mView, sceneAllAttrs, itemPosList, this);
            dropDownMenu.setMenuAdapter(dropMenuAdapter);
        } else {
            SceneDropMenuAdapter dropMenuAdapter = new SceneDropMenuAdapter(mView, sceneAllAttrs, itemPosList, this);
//            dropDownMenu.setMenuAdapter(dropMenuAdapter, true);
            dropDownMenu.setMenuAdapter(dropMenuAdapter);

        }

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
        mNetWork.sendGoodsList(page, per_page, brand, "", filter_attr, shop, keyword, mSortKey, mSortValue, this);
    }

    public void sendSceneType() {
        mNetWork.sendSceneType(this);
    }

    public void sendAttrList() {
        mNetWork.sendAttrList("yes", this);

    }


    @Override
    public void onFilterDone(int titlePos, int itemPos, String itemStr) {
        dropDownMenu.close();
        //        if (0 == itemPos)
        if (mSelectType == 0) {
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
            filter_attr = filter;
            if (AppUtils.isEmpty(filter_attr))
                return;
            pd.setVisibility(View.VISIBLE);
            selectProduct(1, "20", null, null, null);

        } else {
            itemStr = sceneAllAttrs.getJSONObject(titlePos).getString(Constance.attr_name);
            dropDownMenu.setPositionIndicatorText(titlePos, itemStr);

            if (titlePos < itemPosList.size())
                itemPosList.remove(titlePos);
            itemPosList.add(titlePos, itemPos);
            keyword = "[\"" + sceneAllAttrs.getJSONObject(titlePos).getJSONArray(Constance.attrVal).getString(itemPos) + "\"]";
            if (AppUtils.isEmpty(keyword))
                return;
            pd.setVisibility(View.VISIBLE);
            page = 1;
            sendSceneList(page);
        }


    }

    public void onBackPressed() {
        dropDownMenu.close();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        initFilterDropDownView = false;
        if (mSelectType == 0) {
            selectProduct(page, "20", null, null, null);
        } else {
            sendSceneList(page);
        }
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        initFilterDropDownView = false;

        if (mSelectType == 0) {
            selectProduct(++page, "20", null, null, null);
        } else {
            sendSceneList(++page);
        }
    }

    /**
     * 获取相册
     */
    public void goPhotoImage() {
        PermissionUtils.requestPermission(mView, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
                FileUtil.pickPhoto(mView);
            }
        });
    }

    /**
     * 获取产品详情
     */
    public void senDetail() {
        //        detail_rl.setVisibility(View.VISIBLE);
        //        JSONObject productObject = IssueApplication.mSelectedLightSA.get(IssueApplication.mLightIndex);
        //        if (AppUtils.isEmpty(productObject))
        //            return;
        //        String name = productObject.getString(Constance.name);
        //        String price = productObject.getString(Constance.current_price);
        //        propertiesList = productObject.getJSONArray(Constance.properties);
        //        product_name_tv.setText("名称:" + name);
        //
        //        if (!AppUtils.isEmpty(propertiesList) && propertiesList.length() > 0) {
        //            JSONArray attrsArray = propertiesList.getJSONObject(0).getJSONArray(Constance.attrs);
        //            if (!AppUtils.isEmpty(attrsArray)) {
        //                int attrprice = attrsArray.getJSONObject(0).getInt(Constance.attr_price);
        //                String parament = attrsArray.getJSONObject(0).getString(Constance.attr_name);
        //                double currentPrice = Double.parseDouble(price) + attrprice;
        //                product_price_tv.setText("价格:￥" + currentPrice);
        //                product_properties_tv.setText("规格:" + parament);
        //                return;
        //            }
        //        } else {
        //            product_price_tv.setText("价格:￥" + price);
        //        }
        mPopWindow = new DiyProductInfoPopWindow(mView, mView);
        final JSONObject jsonObject = IssueApplication.mSelectedLightSA.get(IssueApplication.mLightIndex);
        if (AppUtils.isEmpty(jsonObject)) {
            MyToast.show(mView, "请选择产品!");
            return;
        }


        mPopWindow.productObject = jsonObject;
        mPopWindow.initViewData();
        mPopWindow.onShow(main_fl);
        mPopWindow.setListener(new IDiyProductInfoListener() {
            @Override
            public void onDiyProductInfo(int type, String msg) {
                getShowProductType(jsonObject, type, msg);
            }
        });


    }

    private void getShowProductType(JSONObject jsonObject, int type, String msg) {
        String productId = jsonObject.getString(Constance.id);
        switch (type) {
            case 0://二维码

                String name = jsonObject.getString(Constance.name);
                String path = NetWorkConst.SHAREPRODUCT + productId;
                addImageView(ImageUtil.getTowCodeImage(ImageUtil.createQRImage(path, 150, 150), name));
                break;
            case 1://参数
                addImageView(ImageUtil.textAsBitmap(msg));
                break;
            case 2://Logo
                String logoPath = NetWorkConst.SHAREIMAGE_LOGO;
                displayCheckedGoods03(logoPath);
                break;
            case 3://产品卡
                String cardPath = NetWorkConst.WEB_PRODUCT_CARD + productId;
                Intent intent=new Intent(mView, WebViewCpActivity.class);
                intent.putExtra(Constance.url,cardPath);
                mView.startActivityForResult(intent,300);
                break;
        }
    }

    /**
     * 选择场景
     */
    public void selectSceneDatas() {
        mIntent = new Intent(mView, SelectSceneActivity.class);
        mIntent.putExtra(Constance.ISSELECTSCRENES, true);
        mView.startActivityForResult(mIntent, Constance.FROMDIY02);
    }

    /**
     * 跳转产品详情页面
     */
    public void goDetail() {
        mIntent = new Intent(mView, ProductDetailHDActivity.class);
        int productId = IssueApplication.mSelectedLightSA.get(IssueApplication.mLightIndex).getInt(Constance.id);
        mIntent.putExtra(Constance.product, productId);
        mView.startActivity(mIntent);
    }

    /**
     * 加入收藏
     */
    public void sendCollect() {
        mView.setShowDialog(true);
        mView.setShowDialog("正在收藏中!");
        mView.showLoading();
        mIntent = new Intent(mView, ProductDetailHDActivity.class);
        int productId = IssueApplication.mSelectedLightSA.get(IssueApplication.mLightIndex).getInt(Constance.id);
        mNetWork.sendAddLikeCollect(productId + "", this);

    }

    public void searchData(String value) {
        page = 1;
        keyword = value;
        selectProduct(1, "20", null, null, null);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mView, R.layout.item_gridview_diy, null);


                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.delete_iv = (ImageView) convertView.findViewById(R.id.delete_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imageView.setImageResource(R.drawable.bg_default);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            try {
                if (mView.mSelectType == 0) {
                    ImageLoader.getInstance().displayImage(goodses.getJSONObject(position).getJSONObject(Constance.app_img).getString(Constance.img)
                            , holder.imageView);
                    holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } else {
                    holder.imageView.setImageResource(R.drawable.bg_default);
                    ImageLoader.getInstance().displayImage(NetWorkConst.SCENE_HOST +
                            goodses.getJSONObject(position).getJSONObject(Constance.scene).getString(Constance.original_img)
                            + "!400X400.png", holder.imageView);
                    holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            } catch (Exception e) {

            }


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mView.mSelectType == 0) {
                        if (AppUtils.isEmpty(goodses.getJSONObject(position)))
                            return;
                        mView.mGoodsObject = goodses.getJSONObject(position);
                        displayCheckedGoods(mView.mGoodsObject);
                    } else {
                        if (AppUtils.isEmpty(goodses.getJSONObject(position)))
                            return;
                        mView.mPath = NetWorkConst.SCENE_HOST + goodses.getJSONObject(position).getJSONObject(Constance.scene).getString(Constance.original_img);
                        if (!AppUtils.isEmpty(mView.mPath)) {
                            displaySceneBg(mView.mPath);
                        }
                    }

                    //                    try {
                    //                        ((SingleTouchView) (mFrameLayout.findViewWithTag(IssueApplication.mLightIndex))).isScale = false;
                    //                    } catch (Exception e) {
                    //
                    //                    }
                }
            });

            holder.delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mView.mSelectType == 0) {
                        IssueApplication.mSelectProducts.delete(position);
                        notifyDataSetChanged();

                    } else {
                        IssueApplication.mSelectScreens.delete(position);
                        notifyDataSetChanged();
                    }

                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
            ImageView delete_iv;
        }
    }
}
