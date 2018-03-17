package bc.juhaohd.com.ui.activity.programme;

import android.os.Bundle;
import android.view.View;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.ui.fragment.CartFragment;
import bc.juhaohd.com.ui.fragment.ProgrammeFragment;

/**
 * @author: Jun
 * @date : 2017/2/16 14:46
 * @description :购物车
 */
public class MatchHomeActivity extends BaseActivity {
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
        setContentView(R.layout.activity_match_home);
        //必需继承FragmentActivity,嵌套fragment只需要这行代码

        ProgrammeFragment cartFragment=new ProgrammeFragment();
        Bundle bundle = new Bundle();
//        bundle.putBoolean(Constance.product, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,cartFragment).commit();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
