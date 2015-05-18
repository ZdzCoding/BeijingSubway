package com.dsunny.subway.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dsunny.subway.constant.DBConst;

/**
 * @author m 数据库
 * 
 */
public class DataBase {

    private SQLiteDatabase db;
    private static DataBase subway;

    /**
     * 单例模式
     */
    private DataBase() {
        db = SQLiteDatabase.openOrCreateDatabase(DBConst.DB_FILE, null);
    }

    /**
     * @return 数据库实例
     */
    public static DataBase getInstance() {
        if (subway == null) {
            subway = new DataBase();
        }
        return subway;
    }

    /**
     * @param sql
     *            SQL文
     * @return 检索结果(Cursor)
     */
    public Cursor query(String sql) {
        return db.rawQuery(sql, null);
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        db.close();
    }
}
