package com.dsunny.subway.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.dsunny.subway.util.Logger;

/**
 * @author m 查询TRANSFER表
 * 
 */
public class TransferDao {
    public static final String TAG = "TransferDao";

    private DataBase db;

    public TransferDao() {
        db = DataBase.getInstance();
    }

    /**
     * @param lid
     *            线路ID
     * @return 横穿给定线路的线路ID
     */
    public List<String> getAcrossLids(String lid) {
        List<String> lstResult = new ArrayList<String>();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT T.lid ");
        sql.append(" FROM ( ");
        sql.append(" SELECT ToLID lid, COUNT(ToLID) cnt ");
        sql.append(" FROM TRANSFER ");
        sql.append(" WHERE FromLID =  '").append(lid).append("' ");
        sql.append(" GROUP BY ToLID ");
        sql.append(" ORDER BY ToLID ");
        sql.append(" ) T ");
        sql.append(" WHERE T.cnt > 1 ");

        Cursor c = db.query(sql.toString());
        while (c.moveToNext()) {
            lstResult.add(c.getString(0));
        }

        Logger.d(TAG, sql.toString());
        Logger.d(TAG, lstResult.toString());

        return lstResult;
    }
}
