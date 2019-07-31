package bc.juhaohd.com.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.adapter.BaseAdapterHelper;
import bc.juhaohd.com.adapter.QuickAdapter;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.controller.TaoCanHomeController;
import bc.juhaohd.com.ui.activity.product.ProductDetailHDNewActivity;
import bc.juhaohd.com.ui.activity.user.BrandPlayActivity;
import bc.juhaohd.com.ui.view.EndOfGridView;

/**
 * Created by bocang on 18-8-31.
 */

public class VideoHomeActivity extends BaseActivity {

    private EndOfGridView listview;

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {






    }

    @Override
    protected void initView() {
            setContentView(R.layout.activity_video_home);
        listview = findViewById(R.id.listView);
        QuickAdapter<String> adapter=new QuickAdapter<String>(this,R.layout.item_video) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                switch (helper.getPosition()){
                    case 0:
                        helper.setImageResource(R.id.iv_video,R.mipmap.img_four);
                        break;
                    case 1:
                        helper.setImageResource(R.id.iv_video,R.mipmap.img_two);
                        break;
                    case 2:
                        helper.setImageResource(R.id.iv_video,R.mipmap.img_three);
                        break;
                    case 3:
                        helper.setImageResource(R.id.iv_video,R.mipmap.img_one);
                        break;
                    case 4:
                        helper.setImageResource(R.id.iv_video,R.mipmap.img_five);
                        break;
                    case 5:
                        helper.setImageResource(R.id.iv_video,R.mipmap.img_six);
                        break;

                }

            }
        };
        listview.setAdapter(adapter);
        final List<String >  urls=new ArrayList<>();

        urls.add("http://cdn.08138.com/juhao1");
        urls.add("http://cdn.08138.com/job_juhaodv");
        urls.add("http://cdn.08138.com/juhaogongcheng1");
        urls.add("http://cdn.juhao.com/job_zhuantipian");
        urls.add("http://cdn.juhao.com/30s");
        urls.add("http://cdn.juhao.com/10s");
        adapter.replaceAll(urls);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent=new Intent(VideoHomeActivity.this, BrandPlayActivity.class);
                mIntent.putExtra(Constance.url,urls.get(position));
                startActivity(mIntent);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
