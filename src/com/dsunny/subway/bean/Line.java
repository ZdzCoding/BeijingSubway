package com.dsunny.subway.bean;

import java.io.Serializable;

/**
 * @author m 线路表(LINE)
 * 
 */
public class Line implements Serializable {

    private static final long serialVersionUID = 1L;

    // 线路ID
    public String LID;
    // 线路名
    public String LName;
    // 是否环路
    public String IsLoop;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LID).append("-");
        sb.append(LName).append("-");
        sb.append(IsLoop);

        return sb.toString();
    }

}
