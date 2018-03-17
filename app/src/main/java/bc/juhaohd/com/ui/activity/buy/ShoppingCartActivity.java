package bc.juhaohd.com.ui.activity.buy;

import android.os.Bundle;
import android.view.View;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.ui.fragment.CartFragment;

/**
 * @author: Jun
 * @date : 2017/2/16 14:46
 * @description :购物车
 */
public class ShoppingCartActivity extends BaseActivity {
//    private ShoppingCartController mController;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {
//        mController=new ShoppingCartController(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_shopping_cart);
        //必需继承FragmentActivity,嵌套fragment只需要这行代码

        CartFragment cartFragment=new CartFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constance.product, true);
        cartFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,cartFragment).commitAllowingStateLoss();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
