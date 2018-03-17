package bc.juhaohd.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.Programme;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.controller.programme.ProgrammeController;
import bocang.utils.AppUtils;

/**
 * @author Jun
 * @time 2017/1/5  12:00
 * @desc 配灯方案页面
 */
public class ProgrammeFragment extends BaseFragment implements View.OnClickListener {
    private ProgrammeController mController;
//    private RelativeLayout add_rl;
    public List<Programme> mProgrammes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_programme, null);
    }

    @Override
    protected void initController() {
        mController = new ProgrammeController(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isToken()){
            if(AppUtils.isEmpty(mController.mSchemes)){
                mController.page=1;
                mController.sendFangAnList();
            }
        }
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
//        add_rl = (RelativeLayout) getActivity().findViewById(R.id.add_rl);
//        add_rl.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.add_rl://新增
//                IntentUtil.startActivity(this.getActivity(), DiyActivity.class,false);
//                break;
        }
    }
}
