package com.dsunny.subway.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsunny.subway.R;
import com.dsunny.subway.bean.SearchResult;
import com.dsunny.subway.bean.TransPathDetail;
import com.dsunny.subway.bean.TransSubPath;
import com.dsunny.subway.constant.Message;

public class ResultPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LinearLayout[] mViews;
    private LayoutInflater mInflater;

    public ResultPagerAdapter(Context context, SearchResult sr) {
        this.mContext = context;
        mViews = new LinearLayout[sr.lstTpd.size()];
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private void initViews(SearchResult sr) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        int cur = 0;
        for (TransPathDetail tpd : sr.lstTpd) {
            LinearLayout ll = new LinearLayout(mContext);
            ll.setLayoutParams(lp);
            ll.setOrientation(LinearLayout.VERTICAL);
            for (TransSubPath tsp : tpd.lstTransSubPath) {
                ll.addView(getStationView(tsp));
                ll.addView(getTransferView(tsp));
            }
            ll.addView(getStationView(tpd.lstTransSubPath.get(tpd.lstTransSubPath.size() - 1).endSName));

            mViews[cur++] = ll;
        }
    }

    /**
     * @param tsp
     *            换乘详细信息的子线路信息
     * @return 换乘站名View
     */
    private View getStationView(TransSubPath tsp) {
        RelativeLayout rl = new RelativeLayout(mContext);
        View view = mInflater.inflate(R.layout.item_station, rl, true);
        TextView textView = (TextView) view.findViewById(R.id.tv_station);
        textView.setText(tsp.startSName);
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
        TextView textView = (TextView) view.findViewById(R.id.tv_station);
        textView.setText(sName);
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
        TextView textView = (TextView) view.findViewById(R.id.tv_transfer);
        String text = new String(Message.FORMAT_TRANSFER);
        text = text.replaceFirst(Message.WORD_REPLACE, tsp.lineName);
        text = text.replaceFirst(Message.WORD_REPLACE, tsp.direction);
        text = text.replaceFirst(Message.WORD_REPLACE, String.valueOf(tsp.lstSNames.size()));
        textView.setText(text);
        return view;
    }
}
