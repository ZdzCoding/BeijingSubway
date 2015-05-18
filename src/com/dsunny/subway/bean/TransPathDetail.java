package com.dsunny.subway.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author m 换乘详细信息
 * 
 */
public class TransPathDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    // 经过车站数
    public int stations;
    // 运行距离
    public int meters;
    // 运行时间
    public int minutes;
    // 票价
    public int price;
    // 换乘信息
    public List<TransSubPath> lstTransSubPath;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(stations).append("-");
        sb.append(meters).append("-");
        sb.append(minutes).append("-");
        sb.append(price).append("-");
        sb.append(lstTransSubPath.toString());

        return sb.toString();
    }

}
