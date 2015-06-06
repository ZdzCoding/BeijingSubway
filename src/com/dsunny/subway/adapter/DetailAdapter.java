package com.dsunny.subway.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsunny.subway.R;
import com.dsunny.subway.bean.TransPathDetail;
import com.dsunny.subway.bean.TransSubPath;
import com.dsunny.subway.constant.Message;

/**
 * @author m 换乘详情页ListView适配器
 *
 */
public class DetailAdapter extends BaseAdapter {

	private static final int TYPE_STATION = 0;
	private static final int TYPE_TRANSFER = 1;
	private static final int TYPE_COUNT = 2;

	private Context mContext;
	private List<DetailAdapter.Item> lstStations;
	private LayoutInflater mInflater;

	private class Item {
		public String name;
		public int type;
	}

	public DetailAdapter(Context context, TransPathDetail tpd) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initLstStations(tpd);
	}

	@Override
	public int getCount() {
		return lstStations.size();
	}

	@Override
	public Object getItem(int position) {
		return lstStations.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vs = null;
		View vt = null;
		ViewHolderStation vhs = null;
		ViewHolderTransfer vht = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			RelativeLayout rl = new RelativeLayout(mContext);
			switch (type) {
				case TYPE_STATION:
					vhs = new ViewHolderStation();
					vs = mInflater.inflate(R.layout.item_station, rl, true);
					vhs.tv_station = (TextView) vs.findViewById(R.id.tv_station);
					vhs.tv_station.setText(lstStations.get(position).name);
					vs.setTag(vhs);
					convertView = vs;
					break;
				case TYPE_TRANSFER:
					vht = new ViewHolderTransfer();
					vt = mInflater.inflate(R.layout.item_transfer, rl, true);
					vht.tv_transfer = (TextView) vt.findViewById(R.id.tv_transfer);
					vht.tv_transfer.setText(lstStations.get(position).name);
					vt.setTag(vht);
					convertView = vt;
					break;
				default:
					break;
			}
		} else {
			switch (type) {
				case TYPE_STATION:
					vhs = (ViewHolderStation) convertView.getTag();
					vhs.tv_station.setText(lstStations.get(position).name);
					break;
				case TYPE_TRANSFER:
					vht = (ViewHolderTransfer) convertView.getTag();
					vht.tv_transfer.setText(lstStations.get(position).name);
					break;
				default:
					break;
			}
		}
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return lstStations.get(position).type;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	/**
	 * @param tpd
	 *            换乘详细信息
	 */
	private void initLstStations(TransPathDetail tpd) {
		lstStations = new ArrayList<DetailAdapter.Item>();
		for (TransSubPath tsp : tpd.lstTransSubPath) {
			DetailAdapter.Item head = new DetailAdapter.Item();
			String text = new String(Message.FORMAT_DETAIL);
			text = text.replaceFirst(Message.WORD_REPLACE, tsp.startSName);
			text = text.replaceFirst(Message.WORD_REPLACE, tsp.lineName);
			head.name = text;
			head.type = TYPE_STATION;
			lstStations.add(head);
			for (String s : tsp.lstSNames) {
				DetailAdapter.Item item = new DetailAdapter.Item();
				item.name = s;
				item.type = TYPE_TRANSFER;
				lstStations.add(item);
			}
		}
		DetailAdapter.Item head = new DetailAdapter.Item();
		TransSubPath tsp = tpd.lstTransSubPath.get(tpd.lstTransSubPath.size() - 1);
		head.name = tsp.endSName;
		head.type = TYPE_TRANSFER;
		lstStations.add(head);
	}

	private class ViewHolderStation {
		public TextView tv_station;
	}

	private class ViewHolderTransfer {
		public TextView tv_transfer;
	}

}
