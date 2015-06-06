package com.dsunny.subway.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsunny.subway.R;
import com.dsunny.subway.activity.DetailActivity;
import com.dsunny.subway.bean.SearchResult;
import com.dsunny.subway.bean.TransPathDetail;
import com.dsunny.subway.bean.TransSubPath;
import com.dsunny.subway.constant.Message;

/**
 * @author m 查询结果页ViewPager适配器
 * 
 */
public class ResultPagerAdapter extends PagerAdapter {

	private Context mContext;
	private LinearLayout[] mViews;
	private LayoutInflater mInflater;
	private String destination;

	public ResultPagerAdapter(Context context, SearchResult sr) {
		this.mContext = context;
		this.mViews = new LinearLayout[sr.lstTpd.size()];
		this.mInflater = (LayoutInflater) mContext
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.destination = new String(Message.FORMAT_DESTINATION);
		destination = destination.replaceFirst(Message.WORD_REPLACE, sr.startSName);
		destination = destination.replaceFirst(Message.WORD_REPLACE, sr.endSName);
		initViews(sr);
	}

	@Override
	public int getCount() {
		return mViews.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mViews[position]);
		return mViews[position];
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mViews[position]);
	}

	/**
	 * @param sr
	 *            所有换乘信息
	 */
	private void initViews(SearchResult sr) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		        LayoutParams.WRAP_CONTENT);
		int cur = 0;
		for (TransPathDetail tpd : sr.lstTpd) {
			LinearLayout ll = new LinearLayout(mContext);
			ll.setLayoutParams(lp);
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.addView(getLineView());
			ll.addView(getSummaryView(tpd, sr.price));
			for (TransSubPath tsp : tpd.lstTransSubPath) {
				ll.addView(getStationView(tsp.startSName));
				ll.addView(getTransferView(tsp));
			}
			ll.addView(getStationView(tpd.lstTransSubPath.get(tpd.lstTransSubPath.size() - 1).endSName));
			ll.addView(getLineView());
			mViews[cur++] = ll;
		}
	}

	/**
	 * @return 线
	 */
	private View getLineView() {
		RelativeLayout rl = new RelativeLayout(mContext);
		View view = mInflater.inflate(R.layout.item_line, rl, true);
		return view;
	}

	/**
	 * @param tpd
	 *            换乘详细信息
	 * @return 换乘简介View
	 */
	private View getSummaryView(final TransPathDetail tpd, int price) {
		RelativeLayout rl = new RelativeLayout(mContext);
		View view = mInflater.inflate(R.layout.item_summary, rl, true);
		final TextView tv = (TextView) view.findViewById(R.id.tv_summary);
		String summary = new String(Message.FORMAT_SUMMARY);
		summary = summary.replaceFirst(Message.WORD_REPLACE, String.valueOf(tpd.minutes));
		summary = summary.replaceFirst(Message.WORD_REPLACE,
		        String.valueOf(tpd.lstTransSubPath.size()));
		summary = summary.replaceFirst(Message.WORD_REPLACE, String.valueOf(price));
		tv.setText(summary);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DetailActivity.class);
				intent.putExtra(Message.KEY_DESTINATION, destination);
				intent.putExtra(Message.KEY_SUMMARY, tv.getText().toString().trim());
				intent.putExtra(Message.KEY_DETAIL, tpd);
				mContext.startActivity(intent);
			}
		});
		return view;
	}

	/**
	 * @param sName
	 *            换乘站名
	 * @return 换乘站名View
	 */
	private View getStationView(String sName) {
		RelativeLayout rl = new RelativeLayout(mContext);
		View view = mInflater.inflate(R.layout.item_station, rl, true);
		TextView tv = (TextView) view.findViewById(R.id.tv_station);
		tv.setText(sName);
		return view;
	}

	/**
	 * @param tsp
	 *            换乘详细信息的子线路信息
	 * @return 换乘信息View
	 */
	private View getTransferView(TransSubPath tsp) {
		RelativeLayout rl = new RelativeLayout(mContext);
		View view = mInflater.inflate(R.layout.item_transfer, rl, true);
		TextView tv = (TextView) view.findViewById(R.id.tv_transfer);
		String text = new String(Message.FORMAT_TRANSFER);
		text = text.replaceFirst(Message.WORD_REPLACE, tsp.lineName);
		text = text.replaceFirst(Message.WORD_REPLACE, tsp.direction);
		text = text.replaceFirst(Message.WORD_REPLACE, String.valueOf(tsp.lstSNames.size()));
		tv.setText(text);
		return view;
	}

}
