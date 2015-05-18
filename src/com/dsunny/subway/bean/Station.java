package com.dsunny.subway.bean;

import java.io.Serializable;

/**
 * @author m 车站表(STATION)
 * 
 */
public class Station implements Serializable {

    private static final long serialVersionUID = 1L;

    // 车站ID
    public String SID;
    // 车站名
    public String SName;
    // 缩写
    public String SAbbr;
    // 有效标识
    public String IsValid;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SID).append("-");
        sb.append(SName).append("-");
        sb.append(SAbbr).append("-");
        sb.append(IsValid);

        return sb.toString();
    }

}
