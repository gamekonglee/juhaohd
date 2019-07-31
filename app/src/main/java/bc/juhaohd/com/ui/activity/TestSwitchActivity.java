package bc.juhaohd.com.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import bc.juhaohd.com.R;
import bc.juhaohd.com.bean.ArticlesBean;
import bc.juhaohd.com.bean.Message;
import bc.juhaohd.com.cons.Constance;
import bc.juhaohd.com.ui.activity.user.MessageDetailActivity;
import bocang.utils.LogUtils;

public class TestSwitchActivity extends AppCompatActivity {

    private TextSwitcher textSwitcher_title;
    private List<ArticlesBean> mArticlesBeans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_switch);
        textSwitcher_title = findViewById(R.id.textSwitcher_title);
        EventBus.getDefault().register(this);
    }

    private int mNewsPoistion = 0;
    private int curStr;
    /**
     * 滚动新闻
     */
    private void getNews() {
        try {

            textSwitcher_title.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    final TextView tv = new TextView(TestSwitchActivity.this);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    tv.setGravity(Gravity.CENTER_VERTICAL);
                    tv.setTextColor(Color.WHITE);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = mArticlesBeans.get(mNewsPoistion).getUrl();
                            Intent intent = new Intent(TestSwitchActivity.this, MessageDetailActivity.class);
                            intent.putExtra(Constance.url, url);
                            startActivity(intent);
                        }
                    });
                    return tv;
                }
            });

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mNewsPoistion = curStr++ % mArticlesBeans.size();
                    textSwitcher_title.setText(mArticlesBeans.get(mNewsPoistion).getTitle());
                    handler.postDelayed(this, 5000);
                }
            }, 1000);
        } catch (Exception e) {

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventA(Message msg) {
        LogUtils.logE("event bus",msg.getMsg()+"");
        mArticlesBeans = IssueApplication.articlesBeans;
        if(mArticlesBeans ==null|| mArticlesBeans.size()==0){
            return;
        }
        getNews();
    /* Do something */
    }
}
