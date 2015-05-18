package com.dsunny.subway.constant;

import android.os.Environment;

/**
 * @author m 数据库信息
 * 
 */
public class DBConst {
    public static final String PACKAGE_NAME = "com.dsunny.subway";
    public static final String DB_NAME = "subway.db";
    public static final String DB_SQL = "subway.sql";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath()
            + "/" + PACKAGE_NAME;
    public static final String DB_FILE = DB_PATH + "/" + DB_NAME;
    public static final int DB_VERSION = 1;
}
