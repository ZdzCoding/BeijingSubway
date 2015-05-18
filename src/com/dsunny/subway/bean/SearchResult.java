package com.dsunny.subway.bean;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {

    private static final long serialVersionUID = 1L;

    // 起点车站
    public String startSName;
    // 终点车站
    public String endSName;
    // 结果Code
    public int code;
    // 路线数
    public int count;
    // 所有路径详细信息
    public List<TransPathDetail> lstTpd;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(startSName).append("-");
        sb.append(endSName).append("-");
        sb.append(code).append("-");
        sb.append(count).append("-");
        sb.append(lstTpd.toString());

        return sb.toString();
    }
}
