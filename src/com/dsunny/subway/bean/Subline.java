package com.dsunny.subway.bean;

import java.io.Serializable;

/**
 * @author m 子线路表(LINE_XX)
 * 
 */
public class Subline implements Serializable {

    private static final long serialVersionUID = 1L;

    // 车站ID
    public String SID;
    // 乘车距离(米)
    public int Meters;
    // 换乘标识
    public String IsTransfer;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SID).append("-");
        sb.append(Meters).append("-");
        sb.append(IsTransfer);

        return sb.toString();
    }

}
