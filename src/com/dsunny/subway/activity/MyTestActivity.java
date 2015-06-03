package com.dsunny.subway.activity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.dsunny.subway.R;
import com.dsunny.subway.db.StationDao;
import com.dsunny.subway.engine.TransPath;

/**
 * @author m 测试类
 * 
 */
public class MyTestActivity extends BaseActivity {

	private TextView tv_test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mytest);
		tv_test = (TextView) findViewById(R.id.tv_test);
		new MyTest().execute();
	}

	/**
	 * @author m
	 * 
	 */
	private class MyTest extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {

			String start = "";
			String end = "";
			try {
				StationDao dao = new StationDao();
				List<String> lstSNames = dao.getAllSNames();
				for (String s1 : lstSNames) {
					for (String s2 : lstSNames) {
						if (!s1.equals(s2)) {
							start = s1;
							end = s2;
							TransPath tp = new TransPath();
							tp.getTransPaths(s1, s2);
						}
					}
				}
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				return start + "-" + end + "\r\n" + sw.toString();
			}

			return "OK";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			tv_test.setText(result);
		}

	}
}
