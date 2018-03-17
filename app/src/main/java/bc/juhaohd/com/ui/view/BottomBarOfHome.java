package bc.juhaohd.com.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import bc.juhaohd.com.R;


/**
 * @author Jun
 * @time 2016/11/2 15:54
 */
public class BottomBarOfHome extends LinearLayout implements View.OnClickListener {
    private TextView frag_home_tv;
    private TextView frag_type_tv;
    private TextView frag_place_tv;
    private TextView frag_style_tv;
    private TextView frag_search_tv;
    private TextView frag_sceen_tv;
    private TextView frag_mine_tv;

    private IBottomBarItemClickListener mListener;

    private int mCurrenTabId;

    public BottomBarOfHome(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        frag_home_tv = (TextView) findViewById(R.id.frag_home_tv);
        frag_type_tv = (TextView) findViewById(R.id.frag_type_tv);
        frag_place_tv = (TextView) findViewById(R.id.frag_space_tv);
        frag_style_tv = (TextView) findViewById(R.id.frag_style_tv);
        frag_search_tv = (TextView) findViewById(R.id.frag_search_tv);
        frag_mine_tv = (TextView) findViewById(R.id.frag_mine_tv);
        frag_sceen_tv= (TextView) findViewById(R.id.frag_sceen_tv);
        frag_home_tv.setOnClickListener(this);
        frag_type_tv.setOnClickListener(this);
        frag_place_tv.setOnClickListener(this);
        frag_style_tv.setOnClickListener(this);
        frag_search_tv.setOnClickListener(this);
        frag_sceen_tv.setOnClickListener(this);
        frag_mine_tv.setOnClickListener(this);

        findViewById(R.id.frag_home_tv).performClick();
    }

    @Override
    public void onClick(View v) {
        //	设置 如果电机的是当前的的按钮 再次点击无效
        if (mCurrenTabId != 0 && mCurrenTabId == v.getId()) {
            return;
        }
        //点击前先默认全部不被选中
        defaultTabStyle();

        mCurrenTabId = v.getId();
        switch (v.getId()) {
            case R.id.frag_home_tv:
                frag_home_tv.setSelected(true);
                break;
            case R.id.frag_type_tv:
                frag_type_tv.setSelected(true);
                break;
            case R.id.frag_space_tv:
                frag_place_tv.setSelected(true);
                break;
            case R.id.frag_style_tv:
                frag_style_tv.setSelected(true);
                break;
            case R.id.frag_search_tv:
                frag_search_tv.setSelected(true);
                break;
            case R.id.frag_sceen_tv:
                frag_sceen_tv.setSelected(true);
                break;
            case R.id.frag_mine_tv:
                frag_mine_tv.setSelected(true);
                break;
        }

        if (mListener != null) {
            mListener.OnItemClickListener(v.getId());
        }

    }

    /**
     * 选择指定的item
     *
     * @param currenTabId
     */
    public void selectItem(int currenTabId) {
        //	设置 如果电机的是当前的的按钮 再次点击无效
        if (mCurrenTabId != 0 && mCurrenTabId == currenTabId) {
            return;
        }
        //点击前先默认全部不被选中
        defaultTabStyle();

        mCurrenTabId = currenTabId;
        switch (currenTabId) {
            case R.id.frag_home_tv:
                frag_home_tv.setSelected(true);
                break;
            case R.id.frag_type_tv:
                frag_type_tv.setSelected(true);
                break;
            case R.id.frag_space_tv:
                frag_place_tv.setSelected(true);
                break;
            case R.id.frag_style_tv:
                frag_style_tv.setSelected(true);
                break;
            case R.id.frag_search_tv:
                frag_search_tv.setSelected(true);
            case R.id.frag_sceen_tv:
                frag_sceen_tv.setSelected(true);
                break;
            case R.id.frag_mine_tv:
                frag_mine_tv.setSelected(true);
                break;
        }

        if (mListener != null) {
            mListener.OnItemClickListener(currenTabId);
        }
    }


    public void setOnClickListener(IBottomBarItemClickListener listener) {
        this.mListener = listener;
    }

    public interface IBottomBarItemClickListener {
        void OnItemClickListener(int resId);
    }

    /**
     * 默认全部不被选中
     */
    private void defaultTabStyle() {
        frag_home_tv.setSelected(false);
        frag_type_tv.setSelected(false);
        frag_place_tv.setSelected(false);
        frag_style_tv.setSelected(false);
        frag_search_tv.setSelected(false);
        frag_mine_tv.setSelected(false);
        frag_sceen_tv.setSelected(false);
    }
}
