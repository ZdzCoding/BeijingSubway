package com.dsunny.subway.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.widget.Toast;

import com.dsunny.subway.constant.DBConst;

/**
 * @author m 公共方法
 * 
 */
public class Utils {

    /**
     * @param c
     */
    public static void showProcessDialog(Context c) {
    }

    /**
     * @param c
     * @param msg
     */
    public static void toast(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param sid
     *            车站ID
     * @return 车站所在线路ID
     */
    public static String getLID(String sid) {
        return sid.substring(0, 2);
    }

    /**
     * @param count
     *            空格数量
     * @return 空格
     */
    public static String getSpace(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * 拷贝数据库文件
     * 
     * @param c
     */
    public static void copyDBFile(Context context) {
        if (!(new File(DBConst.DB_FILE).exists())) {
            try {
                InputStream is = context.getResources().getAssets().open(DBConst.DB_NAME);
                FileOutputStream fos = new FileOutputStream(DBConst.DB_FILE);
                byte[] buffer = new byte[4096];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
