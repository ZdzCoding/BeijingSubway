package com.dsunny.subway.constant;

/**
 * @author m 常量
 * 
 */
public class Message {

	public static final String MSG_WARN_EXIT = "再按一次退出程序";

	public static final int CHECK_OK = 0;
	public static final int ERR_START_EMPTY = 1;
	public static final int ERR_END_EMPTY = 2;
	public static final int ERR_SAME_STATION = 3;
	public static final int ERR_START_NOT_EXIST = 4;
	public static final int ERR_END_NOT_EXIST = 5;

	public static final String MSG_ERR_START_EMPTY = "请您输入始发站";
	public static final String MSG_ERR_END_EMPTY = "请您输入终点站";
	public static final String MSG_ERR_SAME_STATION = "您输入的始发站与终点站相同";
	public static final String MSG_ERR_START_NOT_EXIST = "您输入的始发站不可乘坐";
	public static final String MSG_ERR_END_NOT_EXIST = "您输入的终点站不可乘坐";

	public static final String KEY_RESULT = "SearchResult";
	public static final String KEY_DETAIL = "TransPathDetail";
	public static final String KEY_DESTINATION = "destination";
	public static final String KEY_SUMMARY = "summary";

	public static final String FORMAT_DESTINATION = "X-X";
	public static final String FORMAT_CURRENT = "线路X/X";
	public static final String FORMAT_SUMMARY = "大约用时X分钟,换乘X次,票价X元";
	public static final String FORMAT_TRANSFER = "换乘X,X方向,途径X站";
	public static final String FORMAT_DETAIL = "X(X)";

	public static final String WORD_REPLACE = "X";

	public static final String GESTURE_ABOUTME = "AboutMe";
	public static final String GESTURE_FORWARD = "Forward";
	public static final String GESTURE_BACKWARD = "Backward";

}
