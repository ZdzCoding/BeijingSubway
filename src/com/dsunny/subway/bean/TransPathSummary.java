package com.dsunny.subway.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author m 换乘简要信息
 * 
 */
public class TransPathSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    // 起点ID
    public String startSid;
    // 终点ID
    public String endSid;
    // 所在线路ID
    public String lid;
    // 距离
    public int meters;
    // 起点到终点之间的车站ID(不包括起点与终点)
    public List<String> lstSids;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(startSid).append("-");
        sb.append(endSid).append("-");
        sb.append(lid).append("-");
        sb.append(meters).append("-");
        sb.append(lstSids.toString());

        return sb.toString();
    }

}
