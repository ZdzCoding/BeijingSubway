package com.dsunny.subway.util;

import android.util.Log;

/**
 * @author m 日志
 * 
 */
public class Logger {
    private static final boolean showLog = true;

    /**
     * @param tag
     *            标签
     * @param msg
     *            信息
     */
    public static void v(String tag, String msg) {
        if (showLog) {
            Log.v(tag, msg);
        }
    }

    /**
     * @param tag
     *            标签
     * @param msg
     *            信息
     */
    public static void d(String tag, String msg) {
        if (showLog) {
            Log.d(tag, msg);
        }
    }

    /**
     * @param tag
     *            标签
     * @param msg
     *            信息
     */
    public static void i(String tag, String msg) {
        if (showLog) {
            Log.i(tag, msg);
        }
    }

    /**
     * @param tag
     *            标签
     * @param msg
     *            信息
     */
    public static void w(String tag, String msg) {
        if (showLog) {
            Log.i(tag, msg);
        }
    }

    /**
     * @param tag
     *            标签
     * @param msg
     *            信息
     */
    public static void e(String tag, String msg) {
        if (showLog) {
            Log.i(tag, msg);
        }
    }
}
