package com.dsunny.subway.constant;

/**
 * @author m 地铁常量
 * 
 */
public class SubwayConst {
    public static final String Line_01 = "01";
    public static final String Line_02 = "02";
    public static final String Line_04 = "04";
    public static final String Line_05 = "05";
    public static final String Line_06 = "06";
    public static final String Line_07 = "07";
    public static final String Line_08 = "08";
    public static final String Line_09 = "09";
    public static final String Line_10 = "10";
    public static final String Line_13 = "13";
    public static final String Line_14 = "14";
    public static final String Line_14a = "14a";
    public static final String Line_14b = "14b";
    public static final String Line_15 = "15";
    public static final String Line_94 = "94";
    public static final String Line_95 = "95";
    public static final String Line_96 = "96";
    public static final String Line_97 = "97";
    public static final String Line_98 = "98";
    public static final String Line_99 = "99";

    public static final String[] LineLid = { "01", "02", "04", "05", "06", "07", "08", "09", "10",
            "13", "14", "15", "94", "95", "96", "97", "98", "99" };
    public static final String[] LineName = { "1号线", "2号线", "4号线", "5号线", "6号线", "7号线", "8号线",
            "9号线", "10号线", "13号线", "14号线", "15号线", "八通线", "昌平线", "亦庄线", "大兴线", "房山线", "机场线" };
    public static final String[] Lines = { "01", "02", "04", "05", "06", "07", "08", "09", "10",
            "13", "14a", "14b", "15", "94", "95", "96", "97", "98", "99" };
    public static final int[][] Graph = {
            { 0, 1, 1, 1, 2, 2, 2, 1, 1, 2, 2, 3, 3, 1, 3, 2, 2, 2, 2 },
            { 1, 0, 1, 1, 1, 2, 1, 2, 2, 1, 3, 2, 2, 2, 2, 2, 2, 3, 1 },
            { 1, 1, 0, 2, 1, 1, 2, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2 },
            { 1, 1, 2, 0, 1, 1, 2, 2, 1, 1, 2, 2, 2, 2, 2, 1, 3, 3, 2 },
            { 2, 1, 1, 1, 0, 2, 1, 1, 1, 2, 2, 1, 2, 3, 2, 2, 2, 2, 2 },
            { 2, 2, 1, 1, 2, 0, 3, 1, 2, 2, 2, 3, 3, 3, 3, 2, 2, 2, 3 },
            { 2, 1, 2, 2, 1, 3, 0, 2, 1, 1, 2, 2, 1, 3, 1, 2, 3, 3, 2 },
            { 1, 2, 1, 2, 1, 1, 2, 0, 1, 2, 1, 2, 3, 2, 3, 2, 2, 1, 2 },
            { 1, 2, 1, 1, 1, 2, 1, 1, 0, 1, 1, 2, 2, 2, 2, 1, 2, 2, 1 },
            { 2, 1, 1, 1, 2, 2, 1, 2, 1, 0, 2, 2, 1, 3, 1, 2, 2, 3, 1 },
            { 2, 3, 2, 2, 2, 2, 2, 1, 1, 2, 0, 3, 3, 3, 3, 2, 3, 2, 2 },
            { 3, 2, 2, 2, 1, 3, 2, 2, 2, 2, 3, 0, 1, 4, 3, 3, 3, 3, 3 },
            { 3, 2, 2, 2, 2, 3, 1, 3, 2, 1, 3, 1, 0, 4, 2, 3, 3, 4, 2 },
            { 1, 2, 2, 2, 3, 3, 3, 2, 2, 3, 3, 4, 4, 0, 4, 3, 3, 3, 3 },
            { 3, 2, 2, 2, 2, 3, 1, 3, 2, 1, 3, 3, 2, 4, 0, 3, 3, 4, 2 },
            { 2, 2, 2, 1, 2, 2, 2, 2, 1, 2, 2, 3, 3, 3, 3, 0, 3, 3, 2 },
            { 2, 2, 1, 3, 2, 2, 3, 2, 2, 2, 3, 3, 3, 3, 3, 3, 0, 3, 3 },
            { 2, 3, 2, 3, 2, 2, 3, 1, 2, 3, 2, 3, 4, 3, 4, 3, 3, 0, 3 },
            { 2, 1, 2, 2, 2, 3, 2, 2, 1, 1, 2, 3, 2, 3, 2, 2, 3, 3, 0 } };
}
