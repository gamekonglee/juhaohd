package bc.juhaohd.com.cons;

import bc.juhaohd.com.R;
import bc.juhaohd.com.ui.activity.IssueApplication;

/**
 * @author Jun
 * @time 2017/1/7  21:46
 * @desc 地址
 */
public class NetWorkConst {

    public static  final String APPNAME="juhao";
    //主地址
    public static final String API_HOST = "http://api.juhao.com/";

    //图片主地址
    public static final String SCENE_HOST = "http://www.juhao.com/";

    //分享APP
    public static final String APK_URL ="http://app.bocang.cc/Ewm/index/url/aokete.bocang.cc";

    public static final String DOWN_APK_URL ="http://app.08138.com/juhaoHD.apk";
    //抢购广告
    public static final String GROUPBANNER = API_HOST + "/v2/ecapi.banner.group";
    //抢购广告图片
    public static final String TIME_BUY_BANNER_HOST = "http://www."+APPNAME+".com/data/afficheimg/";

    public static final String GROUPLIST = API_HOST + "/v2/ecapi.goods.grouplist";
    public static final String TWO_CODE = "https://www.kuaizhan.com/common/encode-png?large=true&data=";
    public static final String TOKEN_QUERY = API_HOST + "/v2/ecapi.user.hd.query";
    public static final String PRODUCTYIJI = API_HOST + "/v2/ecapi.goods.agentlist";
    public static final String CATEGORY_SCENE = API_HOST + "/v2/ecapi.scene.category.list";
    public static final String CHECK_SYSTEM = API_HOST + "/v2/ecapi.system.check";
    public static final String SHAREPRODUCT_NEW = SCENE_HOST + "/mobile/index.php?m=default&c=goods&a=index&id=";

    public static String APK_NAME="juhaoHD_v";

    //分享APP图片
    public final static  String SHAREIMAGE="http://app.08138.com/icon.jpg";

    //获取app最新版本号接口
    public static final String VERSION_URL ="http://app.08138.com/version/versioninfo.php?bc_ver_name2=juhaoHDa";
    public static final String VERSION_URL_CONTENT ="http://app.08138.com/version/versioninfo.php?bc_ver_name2=juhaoHDa&bejson=1";


    public final static  String SHAREIMAGE_LOGO=SCENE_HOST+"logo.png";

    //产品卡
    public final static  String WEB_PRODUCT_CARD=SCENE_HOST+"phone_goods_show.php?id=";

    //分享产品
//    public final static String SHAREPRODUCT=SCENE_HOST+"goods.php?id=";
    public final static String SHAREPRODUCT=SCENE_HOST+"/mobile/index.php?m=default&c=goods&a=index&id=";

    //邀请码用户信息
    public static final String USER_SHOP_ADDRESS=API_HOST+"/v2/ecapi.shop.address.";

    //获取产品列表
    public static final String GOODSLIST = API_HOST + "/Interface/get_goods_list";

    //产品url
    public static final String PRODUCT_URL = API_HOST + "/App/simon/Public/uploads/goods/";

    //产品类别
    public static final String GOODSCLASS = API_HOST + "/Interface/get_goods_class";

    //登录
    public static final String LOGIN = API_HOST + "/v2/ecapi.auth.signin";

    //广告
    public static final String BANNER = API_HOST + "/v2/ecapi.banner.list";
    public static final String BANNER_INDEX = API_HOST + "/v2/ecapi.banner.index";
    //注册
    public static final String REGIEST = API_HOST + "/v2/ecapi.auth.mobile.signup";

    //重置密码
    public static final String RESET = API_HOST + "/v2/ecapi.auth.mobile.reset";

    //查询订单
    public static final String ORDERSEARCH = API_HOST + "/v2/ecapi.order.search";

    //验证码
    public static final String VERIFICATIONCOE = API_HOST + "/v2/ecapi.auth.mobile.send";

    //产品
    public static final String PRODUCT = API_HOST + "/v2/ecapi.product.list";

    //分享方案
    public static final String SHARE_FANGAN = SCENE_HOST + "/fangan.php?id=";

    //推荐产品
    public static final String RECOMMENDPRODUCT = API_HOST + "/v2/ecapi.recommend.product.list";

    //产品分类
    public static final String CATEGORY = API_HOST + "/v2/ecapi.category.list";

    //返回用户信息
    public static final String PROFILE = API_HOST + "/v2/ecapi.user.profile.get";

    //返回用户信息
    public static final String UPDATEPROFILE = API_HOST + "/v2/ecapi.user.profile.update";

    //获取收藏产品列表
    public static final String LIKEDPRODUCT = API_HOST + "/v2/ecapi.product.liked.list";

    //取消收藏产品
    public static final String ULIKEDPRODUCT = API_HOST + "/v2/ecapi.product.unlike";

    //添加收藏产品
    public static final String ADDLIKEDPRODUCT = API_HOST + "/v2/ecapi.product.like";

    //订单列表
    public static final String ORDERLIST = API_HOST + "/v2/ecapi.order.list";

    //取消订单
    public static final String ORDERCANCEL = API_HOST + "/v2/ecapi.order.cancel";

    //产品详情
    public static final String PRODUCTDETAIL = API_HOST + "/v2/ecapi.product.get";

    //场景列表
    public static final String SCENELIST = API_HOST + "/v2/ecapi.scene.list";

    //场景分类
    public static final String SCENECATEGORY = API_HOST + "/v2/ecapi.scene.category.list";

    //加入购物车
    public static final String ADDCART = API_HOST + "/v2/ecapi.cart.add";

    //购物车列表
    public static final String GETCART = API_HOST + "/v2/ecapi.cart.get";

    //删除购物车
    public static final String DeleteCART = API_HOST + "/v2/ecapi.cart.delete";

    //修改购物车
    public static final String UpdateCART = API_HOST + "/v2/ecapi.cart.update";

    //结算购物车
    public static final String CheckOutCart = API_HOST + "/v2/ecapi.cart.checkout";

    //收货地址列表
    public static final String CONSIGNEELIST = API_HOST + "/v2/ecapi.consignee.list";

    //新增收货地址
    public static final String CONSIGNEEADD = API_HOST + "/v2/ecapi.consignee.add";

    //删除收货地址
    public static final String CONSIGNEEDELETE = API_HOST + "/v2/ecapi.consignee.delete";

    //默认收货地址
    public static final String CONSIGNEEDEFAULT = API_HOST + "/v2/ecapi.consignee.setDefault";

    //修改收货地址
    public static final String CONSIGNEEUPDATE = API_HOST + "/v2/ecapi.consignee.update";

    //查询区域
    public static final String ADDRESSlIST = API_HOST + "/v2/ecapi.region.list";

    //货物物流列表
    public static final String LOGISTICS = API_HOST + "/v2/ecapi.logistics.list";

    //上传头像
    public static final String UPLOADAVATAR = API_HOST + "/v2/ecapi.user.avatar.upload";

    //筛选列表
    public static final String ATTRLIST = API_HOST + "/v2/ecapi.goods.attr.list";

    //附近商家
    public static final String NEARBYLIST = API_HOST + "/v2/ecapi.server.nearby.list";

    //文章列表
    public static final String ARTICLELIST = API_HOST + "/v2/ecapi.article.list";

    //文章列表
    public static final String NOTICELIST = API_HOST + "/v2/ecapi.notice.list";

    //上传方案
    public static final String FANGANUPLOAD = API_HOST + "/v2/ecapi.fangan.upload";

    //方案列表
    public static final String FANGANLIST = API_HOST + "/v2/ecapi.fangan.list";

    //删除方案
    public static final String FANGANDELETE = API_HOST + "/v2/ecapi.fangan.delete";

    //支付订单
    public static final String PAYMENT = API_HOST + "/v2/ecapi.payment.pay";

    //支付参数信息
    public static final String PAYMENTINFO = API_HOST + "/v2/ecapi.payment.types.list";

    //场景图地址
    public static final String SCENEPATH ="http://bocang.oss-cn-shenzhen.aliyuncs.com/scene/";

    public static String  QQ="194701";

    //场景图地址
    public static final String QQURL ="mqqwpa://im/chat?chat_type=wpa&uin="+QQ+"&version=1";

    //客服QQ
    public static final String CUSTOM =API_HOST+"/v2/ecapi.get.custom";

    //上传自定义场景
    public static final String TWOCOE =SCENE_HOST+"/qr_post.php?token=";

    //监听自定义场景
    public static final String QEAPI =SCENE_HOST+"/qr_api.php?token=";

    //二维码支付
    public static final String TWO_CODE_Pay =API_HOST+"v2/ecapi.payment.dmf.pay";

    //确认收货
    public static final String ORDERCONFIRM_URL=API_HOST+"/v2/ecapi.order.confirm";


}



