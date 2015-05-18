package com.dsunny.subway.engine;

import java.util.ArrayList;
import java.util.List;

import com.dsunny.subway.db.StationDao;
import com.dsunny.subway.util.Utils;

/**
 * @author m 百度语音输入
 * 
 */
public class BaiduVoice {

    private static final String[] STATIONS = { "四惠东", "天通苑北", "天通苑南", "立水桥南", "立水桥南", "大红门桥",
            "六里桥东", "次渠南", "良乡大学城北", "良乡大学城西" };
    private List<String> lstSNames;
    private static BaiduVoice baiduVoice;

    private BaiduVoice() {
        StationDao sDao = new StationDao();
        lstSNames = sDao.getAllSNames();
    }

    public static BaiduVoice getInstance() {
        if (baiduVoice == null) {
            baiduVoice = new BaiduVoice();
        }
        return baiduVoice;
    }

    private class Record {
        String name;
        int index;
    }

    /**
     * @param mgs
     *            语音输入内容
     * @return 起始车站和终点车站名
     */
    public List<String> getInputSNames(String mgs) {
        List<String> result = new ArrayList<String>();

        int index = 0;
        List<Record> lstRecords = new ArrayList<Record>();

        for (String s : STATIONS) {
            index = mgs.indexOf(s);
            if (index > 0) {
                Record r = new Record();
                r.name = s;
                r.index = index;
                lstRecords.add(r);
                mgs = mgs.replaceAll(s, Utils.getSpace(s.length()));
            }
        }

        for (String s : lstSNames) {
            index = mgs.indexOf(s);
            if (index > 0) {
                Record r = new Record();
                r.name = s;
                r.index = index;
                lstRecords.add(r);
            }
        }

        String startSName = "";
        String endSName = "";

        if (lstRecords.size() == 1) {
            startSName = lstRecords.get(0).name;
            endSName = "";
        }

        if (lstRecords.size() == 2) {
            if (lstRecords.get(0).index < lstRecords.get(1).index) {
                startSName = lstRecords.get(0).name;
                endSName = lstRecords.get(1).name;
            } else {
                startSName = lstRecords.get(1).name;
                endSName = lstRecords.get(0).name;
            }
        }

        result.add(startSName);
        result.add(endSName);
        return result;
    }
}
