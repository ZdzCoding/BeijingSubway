package com.dsunny.subway.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.dsunny.subway.util.Logger;
import com.dsunny.subway.util.Utils;

/**
 * @author m 查询LINE表
 * 
 */
public class LineDao {
    public static final String TAG = "LineDao";

    private DataBase db;

    public LineDao() {
        db = DataBase.getInstance();
    }

    /**
     * @param lid
     *            线路ID
     * @return 线路是否是回路
     */
    public boolean isLoopLine(String lid) {
        boolean result = false;

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT LID ");
        sql.append(" FROM LINE ");
        sql.append(" WHERE LID = '").append(lid).append("' ");
        sql.append(" AND IsLoop = '1' ");

        Cursor c = db.query(sql.toString());
        result = c.getCount() > 0 ? true : false;

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, String.valueOf(result));

        c.close();
        return result;
    }

    /**
     * @param lid
     *            线路ID
     * @return 线路是否存在回路
     */
    public boolean isLineHasLoop(String lid) {
        boolean result = false;

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT LID ");
        sql.append(" FROM LINE ");
        sql.append(" WHERE LID = '").append(lid).append("' ");
        sql.append(" AND IsLoop <> '0' ");

        Cursor c = db.query(sql.toString());
        result = c.getCount() > 0 ? true : false;

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, String.valueOf(result));

        c.close();
        return result;
    }

    /**
     * @param lid
     *            线路ID
     * @return 线路名
     */
    public String getLineName(String lid) {
        String result = "";

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT LName ");
        sql.append(" FROM LINE ");
        sql.append(" WHERE LID = '").append(lid).append("' ");

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
     * @param sid
     *            车站ID(线路)
     * @return 与车站ID临近的换乘站ID或线路终点站[小，大]
     */
    public List<String> getAdjacentSids(String sid) {
        List<String> lstResult = new ArrayList<String>();
        String lid = Utils.getLID(sid);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT MAX(L.SID) ");
        sql.append(" FROM STATION S, LINE_").append(lid).append(" L ");
        sql.append(" WHERE L.SID < '").append(sid).append("' ");
        sql.append(" AND L.IsTransfer <> '0' ");
        sql.append(" AND S.SID = L.SID ");
        sql.append(" AND S.IsValid = '1' ");
        sql.append(" UNION ALL ");
        sql.append(" SELECT MIN(L.SID) ");
        sql.append(" FROM STATION S, LINE_").append(lid).append(" L ");
        sql.append(" WHERE L.SID > '").append(sid).append("' ");
        sql.append(" AND IsTransfer <> '0' ");
        sql.append(" AND S.SID = L.SID ");
        sql.append(" AND S.IsValid = '1' ");

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
     *            终止车站ID(线路)
     * @return 起始车站到终止车站间的距离
     */
    public int getAdjacentSMeters(String startSid, String endSid) {
        int result = 0;
        String lid = Utils.getLID(startSid);

        String minSid = startSid;
        String maxSid = endSid;

        if (startSid.compareTo(endSid) > 0) {
            minSid = endSid;
            maxSid = startSid;
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(Meters) ");
        sql.append(" FROM LINE_").append(lid).append(" ");
        sql.append(" WHERE SID >= '").append(minSid).append("' ");
        sql.append(" AND SID < '").append(maxSid).append("' ");

        Cursor c = db.query(sql.toString());
        if (c.moveToFirst()) {
            result = c.getInt(0);
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, String.valueOf(result));

        c.close();
        return result;
    }
}
