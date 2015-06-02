package com.dsunny.subway.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.dsunny.subway.R;
import com.dsunny.subway.adapter.ResultPagerAdapter;
import com.dsunny.subway.bean.SearchResult;
import com.dsunny.subway.constant.Message;

/**
 * @author m 检索结果页面
 * 
 */
public class ResultActivity extends BaseActivity {
    public static final String TAG = "ResultActivity";

    private static final String DefaultCurrent = "1";

    private ViewPager vp_result;
    private TextView tv_destination, tv_current;
    private ResultPagerAdapter adapter;
    private OnPageChangeListener opcl;
    private SearchResult mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initViews();
        initDatas();
        initListener();
        registerListener();
    }

    /**
     * 初始化页面控件
     */
    private void initViews() {
        vp_result = (ViewPager) findViewById(R.id.vp_result);
        tv_destination = (TextView) findViewById(R.id.tv_destination);
        tv_current = (TextView) findViewById(R.id.tv_current);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        Intent intent = getIntent();
        mResult = (SearchResult) intent.getSerializableExtra(Message.KEY_RESULT);
        adapter = new ResultPagerAdapter(mContext, mResult);
        vp_result.setAdapter(adapter);
        vp_result.setOffscreenPageLimit(0);
        vp_result.setCurrentItem(0);

        String destination = new String(Message.FORMAT_DESTINATION);
        destination = destination.replaceFirst(Message.WORD_REPLACE, mResult.startSName);
        destination = destination.replaceFirst(Message.WORD_REPLACE, mResult.endSName);
        tv_destination.setText(destination);

        String current = new String(Message.FORMAT_CURRENT);
        current = current.replaceFirst(Message.WORD_REPLACE, DefaultCurrent);
        current = current.replaceFirst(Message.WORD_REPLACE, String.valueOf(mResult.count));
        tv_current.setText(current);
    }

    /**
     * 定义Listener变量
     */
    private void initListener() {
        opcl = new OnPageChangeListener() {

            /*
             * This method will be invoked when a new page becomes selected.
             */
            @Override
            public void onPageSelected(int position) {
                String current = new String(Message.FORMAT_CURRENT);
                current = current.replaceFirst(Message.WORD_REPLACE, String.valueOf(position + 1));
                current = current.replaceFirst(Message.WORD_REPLACE, String.valueOf(mResult.count));
                tv_current.setText(current);
            }

            /*
             * This method will be invoked when the current page is scrolled,
             * either as part of a programmatically initiated smooth scroll or a
             * user initiated touch scroll.
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /*
             * Called when the scroll state changes.
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                // Indicates that the pager is in an idle, settled state. The
                // current page is fully in view and no animation is in
                // progress.
                case ViewPager.SCROLL_STATE_IDLE:
                    break;
                // Indicates that the pager is currently being dragged by the
                // user.
                case ViewPager.SCROLL_STATE_DRAGGING:
                    break;
                // Indicates that the pager is in the process of settling to
                // a final position.
                case ViewPager.SCROLL_STATE_SETTLING:
                    break;
                default:
                    break;
                }
            }
        };
    }

    /**
     * 注册Listener
     */
    private void registerListener() {
        vp_result.setOnPageChangeListener(opcl);
    }

}
