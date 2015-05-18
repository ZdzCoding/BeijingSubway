package com.dsunny.subway.bean;

import java.io.Serializable;

/**
 * @author m 生成换乘路径
 * 
 */
public class GraphSubPath implements Serializable {

	private static final long serialVersionUID = 1L;

	// 起始车站ID(边的起点)
	public String startSid;
	// 终止车站ID(边的终点)
	public String endSid;
	// 所在线路ID
	public String lid;
	// 权值(距离)
	public int meters;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(startSid).append("-");
		sb.append(endSid).append("-");
		sb.append(lid).append("-");
		sb.append(meters);

		return sb.toString();
	}
}
