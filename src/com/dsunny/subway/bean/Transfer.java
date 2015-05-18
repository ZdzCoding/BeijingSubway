package com.dsunny.subway.bean;

import java.io.Serializable;

/**
 * @author m 换乘表(TRANSFER)
 * 
 */
public class Transfer implements Serializable {

    private static final long serialVersionUID = 1L;

    // 换乘ID
    public String TID;
    // From线路
    public String FromLID;
    // To线路
    public String ToLID;
    // 换乘站ID
    public String SID;
    // 有效标识
    public String IsValid;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TID).append("-");
        sb.append(FromLID).append("-");
        sb.append(ToLID).append("-");
        sb.append(SID).append("-");
        sb.append(IsValid);

        return sb.toString();
    }

}
