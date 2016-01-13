package com.meizu.bluetooth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.meizu.client.ClearEditText;
import com.meizu.iamp.client.R;
import com.meizu.info.Properties;

public class TalkHistory extends Fragment {
	
	private Context mContext;
	private ListView mList;
	private ClearEditText mClearEditText;

	private List<Map<String, Object>> mData;

	private MyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mContext = getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.bt_talk_history, null);

		mList = (ListView) view.findViewById(R.id.bt_history_list);
		mClearEditText = (ClearEditText) view.findViewById(R.id.history_filter_edit);

		adapter = new MyAdapter(mContext);

		myHandler.sendEmptyMessage(0);
		
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}
		});

		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				mData = getData(s.toString());
				myHandler.sendEmptyMessage(0);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		return view;
	}

	private List<Map<String, Object>> getData(String filter) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;

		for (int i = 0; i < 50; i++) {
			String name = "蓝牙" + (i + 1);
			String sn = "ABCDEFG" + i;
			String time = "17:" + i;
			if (!filter.equals("") && !name.contains(filter) && !sn.contains(filter))
				continue;
			map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("sn", sn);
			map.put("time", time);
			list.add(map);
		}
		return list;
	}

	public final class ViewHolder {
		public TextView name;
		public TextView sn;
		public TextView time;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.bt_history_list_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.ht_name);
				holder.sn = (TextView) convertView.findViewById(R.id.ht_sn);
				holder.time = (TextView) convertView.findViewById(R.id.ht_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String text = mClearEditText.getText().toString();
			holder.name.setText((highlight((String) mData.get(position).get("name"), text)));
			holder.sn.setText((highlight((String) mData.get(position).get("sn"), text)));
			holder.time.setText((highlight((String) mData.get(position).get("time"), text)));
			return convertView;
		}

	}

	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			mData = getData(mClearEditText.getText().toString());

			mList.setAdapter(adapter);

			super.handleMessage(msg);
		};
	};

	/** 高亮关键字 */
	public SpannableStringBuilder highlight(String text, String target) {
		SpannableStringBuilder spannable = new SpannableStringBuilder(text);
		CharacterStyle span = null;

		if (!target.equals("")) {
			Pattern p = Pattern.compile(target);
			Matcher m = p.matcher(text);
			while (m.find()) {
				span = new ForegroundColorSpan(Properties.GREEN);// 需要重复！
				spannable.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannable;
	}

}
