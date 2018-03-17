package bc.juhaohd.com.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseFragment;
import bc.juhaohd.com.controller.programme.SceneHDController;


/**
 * @author: Jun
 * @date : 2017/3/30 13:50
 * @description :
 */
public class SceneHDFragment extends BaseFragment {
    private SceneHDController mController;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_scene_hd, null);
    }


    @Override
    protected void initController() {
        mController = new SceneHDController(this);
    }

    @Override
    protected void initViewData() {

    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.onBackPressed();
    }

//    public void searchData(String keyword){
//        keyword="[\""+keyword+"\"]";
//        mController.keyword=keyword;
//        mController.page=1;
//        mController.sendSceneList(1);
//    }


}
