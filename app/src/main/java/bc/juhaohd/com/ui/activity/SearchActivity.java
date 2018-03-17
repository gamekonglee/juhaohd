package bc.juhaohd.com.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bc.juhaohd.com.R;
import bc.juhaohd.com.common.BaseActivity;
import bc.juhaohd.com.cons.Constance;

public class SearchActivity extends BaseActivity {

    private ImageView iv_back;
    private EditText et_search;
    private TextView tv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void InitDataView() {

    }

    @Override
    protected void initController() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

    }

    private void search() {
        String keyword= String.valueOf(et_search.getText());
//        keyword="2606";
        if(TextUtils.isEmpty(keyword.trim())){
            Toast.makeText(SearchActivity.this, "请输入搜索关键词", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(this,MainNewActivity.class);
        intent.putExtra(Constance.keyword,keyword);
        startActivity(intent);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
