package com.dsunny.subway.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dsunny.subway.constant.DBConst;

/**
 * @author m 数据库
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    public DBHelper(Context context) {
        super(context, DBConst.DB_NAME, null, DBConst.DB_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!(new File(DBConst.DB_FILE).exists())) {
            initDB(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * @param db
     */
    private void initDB(SQLiteDatabase db) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(mContext.getAssets().open(DBConst.DB_SQL)));
            String line = null;
            String sql = "";
            while ((line = in.readLine()) != null) {
                sql += line;
                if (line.trim().endsWith(";")) {
                    db.execSQL(sql.replace(";", ""));
                    sql = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
