package com.dsunny.subway.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsunny.subway.R;
import com.dsunny.subway.adapter.DetailAdapter;
import com.dsunny.subway.bean.TransPathDetail;
import com.dsunny.subway.constant.Message;

/**
 * @author m 换乘信息详情
 *
 */
public class DetailActivity extends BaseActivity {
	public static final String TAG = "DetailActivity";

	private TextView tv_destination;
	private ListView lv_detail;
	private DetailAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		initViews();
		initDatas();
	}

	/**
	 * 初始化页面控件
	 */
	private void initViews() {
		tv_destination = (TextView) findViewById(R.id.tv_destination);
		lv_detail = (ListView) findViewById(R.id.lv_detail);
	}

	/**
	 * 初始化数据
	 */
	private void initDatas() {
		Intent intent = getIntent();
		tv_destination.setText(intent.getStringExtra(Message.KEY_DESTINATION));
		LayoutInflater inflater = (LayoutInflater) mContext
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout rl = new RelativeLayout(mContext);
		View view = inflater.inflate(R.layout.item_summary, rl, true);
		TextView tv = (TextView) view.findViewById(R.id.tv_summary);
		tv.setText(intent.getStringExtra(Message.KEY_SUMMARY));
		lv_detail.addHeaderView(view);
		adapter = new DetailAdapter(mContext,
		        (TransPathDetail) intent.getSerializableExtra(Message.KEY_DETAIL));
		lv_detail.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

}
