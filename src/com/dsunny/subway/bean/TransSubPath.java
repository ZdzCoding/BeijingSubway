package com.dsunny.subway.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author m 换乘详细信息的子线路信息
 * 
 */
public class TransSubPath implements Serializable {

    private static final long serialVersionUID = 1L;

    // 起始站名
    public String startSName;
    // 终点站名
    public String endSName;
    // 所在线路名
    public String lineName;
    // 运行方向
    public String direction;
    // 运行距离
    public int meters;
    // 经过车站
    public List<String> lstSNames;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(startSName).append("-");
        sb.append(endSName).append("-");
        sb.append(lineName).append("-");
        sb.append(direction).append("-");
        sb.append(meters).append("-");
        sb.append(lstSNames.toString());

        return sb.toString();
    }

}
