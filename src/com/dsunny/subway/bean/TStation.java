package com.dsunny.subway.bean;

import java.io.Serializable;

/**
 * @author m 车站换乘信息(TSTATION)
 * 
 */
public class TStation implements Serializable {

    private static final long serialVersionUID = 1L;

    // 起始车站
    public String StartSID;
    // 到达车站
    public String EndSID;
    // 所在线路
    public String LID;
    // 距离
    public int Meters;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StartSID).append("-");
        sb.append(EndSID).append("-");
        sb.append(LID).append("-");
        sb.append(Meters);

        return sb.toString();
    }

}
