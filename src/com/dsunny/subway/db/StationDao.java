package com.dsunny.subway.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.text.TextUtils;

import com.dsunny.subway.util.Logger;
import com.dsunny.subway.util.Utils;

/**
 * @author m 查询STATION表
 * 
 */
public class StationDao {
    public static final String TAG = "StationDao";

    private DataBase db;

    public StationDao() {
        db = DataBase.getInstance();
    }

    /**
     * @param sName
     *            车站名
     * @return 车站是否存在
     */
    public boolean isStationExist(String sName) {
        boolean result = false;

        result = getAllSidsByName(sName).size() > 0 ? true : false;

        Logger.d(TAG, String.valueOf(result));

        return result;
    }

    /**
     * @param sName
     *            车站名
     * @return 车站是否是换乘站
     */
    public boolean isTransferStation(String sName) {
        boolean result = false;

        result = getAllSidsByName(sName).size() > 1 ? true : false;

        Logger.d(TAG, String.valueOf(result));

        return result;
    }

    /**
     * @param startSid
     *            起点站ID(线路)
     * @param endSid
     *            终点站ID(线路)
     * @return 共同线路ID
     */
    public String getCommonLidById(String startSid, String endSid) {
        String result = "";

        List<String> lstStartSids = getAllSidsById(startSid);
        List<String> lstStartLids = new ArrayList<String>();
        for (String s : lstStartSids) {
            lstStartLids.add(Utils.getLID(s));
        }

        List<String> lstEndSids = getAllSidsById(endSid);
        List<String> lstEndLids = new ArrayList<String>();
        for (String s : lstEndSids) {
            lstEndLids.add(Utils.getLID(s));
        }

        lstStartLids.retainAll(lstEndLids);
        result = lstStartLids.size() > 0 ? lstStartLids.get(0) : "";

        Logger.d(TAG, result);

        return result;
    }

    /**
     * @param startName
     *            起点站名
     * @param endName
     *            终点站名
     * @return 共同线路ID
     */
    public String getCommonLidByName(String startSName, String endSName) {
        String result = "";

        List<String> lstStartSids = getAllSidsByName(startSName);
        List<String> lstStartLids = new ArrayList<String>();
        for (String s : lstStartSids) {
            lstStartLids.add(Utils.getLID(s));
        }

        List<String> lstEndSids = getAllSidsByName(endSName);
        List<String> lstEndLids = new ArrayList<String>();
        for (String s : lstEndSids) {
            lstEndLids.add(Utils.getLID(s));
        }

        lstStartLids.retainAll(lstEndLids);
        result = lstStartLids.size() > 0 ? lstStartLids.get(0) : "";

        Logger.d(TAG, result);

        return result;
    }

    /**
     * @return 所有车站名
     */
    public List<String> getAllSNames() {
        List<String> lstResult = new ArrayList<String>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT SName ");
        sql.append(" FROM STATION ");
        sql.append(" WHERE IsValid = '1' ");
        sql.append(" ORDER BY SID ");

        Cursor c = db.query(sql.toString());
        while (c.moveToNext()) {
            lstResult.add(c.getString(0));
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, lstResult.toString());

        c.close();
        return lstResult;
    }

    /**
     * @param startSid
     *            起点站ID(线路)
     * @param endSid
     *            终点站ID(线路)
     * @return 换乘方向
     */
    public String getTransDirection(String startSid, String endSid) {
        String result = "";
        String lid = Utils.getLID(startSid);

        StringBuilder sql = new StringBuilder();
        if (startSid.compareTo(endSid) > 0) {
            sql.append(" SELECT SName ");
            sql.append(" FROM STATION ");
            sql.append(" WHERE SID LIKE '").append(lid).append("%' ");
            sql.append(" AND SID <= '").append(endSid).append("' ");
            sql.append(" AND IsValid = '1' ");
            sql.append(" ORDER BY SID ASC ");
        } else {
            sql.append(" SELECT SName ");
            sql.append(" FROM STATION ");
            sql.append(" WHERE SID LIKE '").append(lid).append("%' ");
            sql.append(" AND SID >= '").append(startSid).append("' ");
            sql.append(" AND IsValid = '1' ");
            sql.append(" ORDER BY SID DESC ");
        }

        Cursor c = db.query(sql.toString());
        if (c.moveToFirst()) {
            result = c.getString(0);
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, result);

        c.close();
        return result;
    }

    /**
     * @param lid
     *            线路ID
     * @param sName
     *            车站名
     * @return 车站所在线路的车站ID
     */
    public String getLineSIDByName(String lid, String sName) {
        String result = "";

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SID ");
        sql.append(" FROM STATION ");
        sql.append(" WHERE SID LIKE '").append(lid).append("%' ");
        sql.append(" AND SName = '").append(sName).append("' ");

        Cursor c = db.query(sql.toString());
        if (c.moveToFirst()) {
            result = c.getString(0);
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, result);

        c.close();
        return result;
    }

    /**
     * @param lid
     *            线路ID
     * @param sid
     *            车站ID
     * @return 车站所在线路的车站ID
     */
    public String getLineSIDById(String lid, String sid) {
        String result = "";

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t1.SID ");
        sql.append(" FROM STATION t1, STATION t2 ");
        sql.append(" WHERE t1.SID LIKE '").append(lid).append("%' ");
        sql.append(" AND t2.SID = '").append(sid).append("' ");
        sql.append(" AND t1.SName = t2.SName ");

        Cursor c = db.query(sql.toString());
        if (c.moveToFirst()) {
            result = c.getString(0);
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, result);

        c.close();
        return result;
    }

    /**
     * @param sName
     *            车站名
     * @return 图的车站ID(如果车站为非换乘站，则线路车站ID与图的车站ID相同)
     */
    public String getGraphSidByName(String sName) {
        String result = "";

        List<String> lstSids = getAllSidsByName(sName);
        if (lstSids.size() > 0) {
            result = lstSids.get(0);
        }

        Logger.d(TAG, result);

        return result;
    }

    /**
     * @param sid
     *            车站ID(线路)
     * @return 图的车站ID
     */
    public String getGraphSidById(String sid) {
        String result = "";

        List<String> lstSids = getAllSidsById(sid);
        if (lstSids.size() > 0) {
            result = lstSids.get(0);
        }

        Logger.d(TAG, result);

        return result;
    }

    /**
     * @param sName
     *            车站名
     * @return 所有相同车站名的车站ID
     */
    public List<String> getAllSidsByName(String sName) {
        List<String> lstResult = new ArrayList<String>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SID ");
        sql.append(" FROM STATION ");
        sql.append(" WHERE SName = '").append(sName).append("' ");
        sql.append(" AND IsValid = 1 ");

        Cursor c = db.query(sql.toString());
        while (c.moveToNext()) {
            lstResult.add(c.getString(0));
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, lstResult.toString());

        c.close();
        return lstResult;
    }

    /**
     * @param sid
     *            车站ID(线路/图)
     * @return 所有相同车站名的车站ID
     */
    public List<String> getAllSidsById(String sid) {
        List<String> lstResult = new ArrayList<String>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t1.SID ");
        sql.append(" FROM STATION t1, STATION t2 ");
        sql.append(" WHERE t2.SID = '").append(sid).append("' ");
        sql.append(" AND t1.SName = t2.SName ");

        Cursor c = db.query(sql.toString());
        if (c.moveToNext()) {
            lstResult.add(c.getString(0));
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, lstResult.toString());

        c.close();
        return lstResult;
    }

    /**
     * @param sid
     *            车站ID(线路)
     * @return 车站名
     */
    public String getSName(String sid) {
        String result = "";

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SName ");
        sql.append(" FROM STATION ");
        sql.append(" WHERE SID = '").append(sid).append("' ");

        Cursor c = db.query(sql.toString());
        if (c.moveToFirst()) {
            result = c.getString(0);
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, result.toString());

        c.close();
        return result;
    }

    /**
     * @param startSid
     *            起始车站ID(线路)
     * @param endSid
     *            终点车站ID(线路)
     * @return 起始车站到终点车站之间的车站名(不包括起始车站与终点车站)
     */
    public List<String> getSNamesBetween(String startSid, String endSid) {
        List<String> lstResult = new ArrayList<String>();

        StringBuilder sql = new StringBuilder();
        if (startSid.compareTo(endSid) > 0) {
            sql.append(" SELECT SName ");
            sql.append(" FROM STATION ");
            sql.append(" WHERE SID > '").append(endSid).append("' ");
            sql.append(" AND SID < '").append(startSid).append("' ");
            sql.append(" AND IsValid = '1' ");
            sql.append(" ORDER BY SID DESC ");
        } else {
            sql.append(" SELECT SName ");
            sql.append(" FROM STATION ");
            sql.append(" WHERE SID > '").append(startSid).append("' ");
            sql.append(" AND SID < '").append(endSid).append("' ");
            sql.append(" AND IsValid = '1' ");
            sql.append(" ORDER BY SID ASC ");
        }

        Cursor c = db.query(sql.toString());
        while (c.moveToNext()) {
            lstResult.add(c.getString(0));
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, lstResult.toString());

        c.close();
        return lstResult;
    }

    /**
     * @param startSid
     *            起始车站ID(线路)
     * @param endSid
     *            终点车站ID(线路)
     * @return 包含环线起点站与终点站的车站名
     */
    public List<String> getSNamesWith(String startSid, String endSid) {
        List<String> lstResult = new ArrayList<String>();
        String lid = startSid.substring(0, 2);

        StringBuilder sql = new StringBuilder();
        if (startSid.compareTo(endSid) > 0) {
            sql.append(" SELECT * FROM ( ");
            sql.append(" SELECT SName ");
            sql.append(" FROM STATION ");
            sql.append(" WHERE SID LIKE '").append(lid).append("%' ");
            sql.append(" AND SID > '").append(startSid).append("' ");
            sql.append(" AND IsValid = '1' ");
            sql.append(" ORDER BY SID ASC ");
            sql.append(" ) ");
            sql.append(" UNION ALL ");
            sql.append(" SELECT * FROM ( ");
            sql.append(" SELECT SName ");
            sql.append(" FROM STATION ");
            sql.append(" WHERE SID LIKE '").append(lid).append("%' ");
            sql.append(" AND SID < '").append(endSid).append("' ");
            sql.append(" AND IsValid = '1' ");
            sql.append(" ORDER BY SID ASC ");
            sql.append(" ) ");
        } else {
            sql.append(" SELECT * FROM ( ");
            sql.append(" SELECT SName ");
            sql.append(" FROM STATION ");
            sql.append(" WHERE SID LIKE '").append(lid).append("%' ");
            sql.append(" AND SID < '").append(startSid).append("' ");
            sql.append(" AND IsValid = '1' ");
            sql.append(" ORDER BY SID DESC ");
            sql.append(" ) ");
            sql.append(" UNION ALL ");
            sql.append(" SELECT * FROM ( ");
            sql.append(" SELECT SName ");
            sql.append(" FROM STATION ");
            sql.append(" WHERE SID LIKE '").append(lid).append("%' ");
            sql.append(" AND SID > '").append(endSid).append("' ");
            sql.append(" AND IsValid = '1' ");
            sql.append(" ORDER BY SID DESC ");
            sql.append(" ) ");
        }

        Cursor c = db.query(sql.toString());
        while (c.moveToNext()) {
            lstResult.add(c.getString(0));
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, lstResult.toString());

        return lstResult;
    }

    /**
     * @param startSid
     *            起始车站ID(线路)
     * @param endSid
     *            终点车站ID(线路)
     * @param containSName
     *            包含的车站名
     * @return 包含指定车站名的起点站与终点站间的车站名
     */
    public List<String> getSNamesContain(String startSid, String endSid, String containSName) {
        List<String> lstResult = new ArrayList<String>();

        List<String> lst1 = getSNamesBetween(startSid, endSid);
        List<String> lst2 = getSNamesWith(startSid, endSid);

        if (TextUtils.isEmpty(containSName)) {
            lstResult = lst1.size() < lst2.size() ? lst1 : lst2;
        } else {
            lstResult = lst1.contains(containSName) ? lst1 : lst2;
        }

        Logger.d(TAG, lstResult.toString());

        return lstResult;
    }
}
