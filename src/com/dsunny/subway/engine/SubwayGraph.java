package com.dsunny.subway.engine;

import java.util.ArrayList;
import java.util.List;

import com.dsunny.subway.bean.GraphSubPath;
import com.dsunny.subway.bean.TStation;
import com.dsunny.subway.bean.TransPathSummary;
import com.dsunny.subway.util.Logger;

/**
 * @author m 抽象地铁图
 * 
 */
public class SubwayGraph {
    public static final String TAG = "SubwayGraph";

    private static final int ErrIndex = -1;
    private static final int MaxMeters = Integer.MAX_VALUE;

    /**
     * @author m 内部类-邻接表的边
     * 
     */
    private class EndStation {
        String sid;
        String lid;
        int meters;

        public EndStation(String sid, String lid, int meters) {
            this.sid = sid;
            this.lid = lid;
            this.meters = meters;
        }
    }

    /**
     * @author m 内部类-邻接表的节点
     * 
     */
    private class StartStation {
        String sid;
        List<EndStation> lstEndStation;

        public StartStation(String sid) {
            this.sid = sid;
            lstEndStation = new ArrayList<EndStation>();
        }
    }

    // 邻接表
    private List<StartStation> lstStartStation;

    /**
     * 构造函数
     */
    public SubwayGraph() {
        lstStartStation = new ArrayList<StartStation>();
    }

    /**
     * @param lstTStation
     *            车站换乘信息
     */
    public SubwayGraph(List<TStation> lstTStation) {
        lstStartStation = new ArrayList<StartStation>();
        addListEdges(lstTStation);
    }

    /**
     * 打印图
     */
    public void printGraph() {
        StringBuilder sb = new StringBuilder();
        for (StartStation ss : lstStartStation) {
            sb.append(ss.sid).append("---");
            for (EndStation es : ss.lstEndStation) {
                sb.append("(").append(es.sid).append(",").append(es.meters).append(")->");
            }
            sb.append("\n");
        }
        Logger.d(TAG, sb.toString());
    }

    /**
     * @param lstTStation
     *            车站换乘信息
     */
    public void addListEdges(List<TStation> lstTStation) {
        for (TStation t : lstTStation) {
            addDoubleEdges(t.StartSID, t.EndSID, t.LID, t.Meters);
        }
    }

    /**
     * @param startSid
     *            边的起始节点ID
     * @param endSid
     *            边的终止节点ID
     * @param lid
     *            所在线路ID
     * @param meters
     *            边的权值(起始节点到终止节点的距离)
     */
    public void addEdge(String startSid, String endSid, String lid, int meters) {
        StartStation s = getStartStation(startSid);
        EndStation e = new EndStation(endSid, lid, meters);
        if (s != null) {
            s.lstEndStation.add(e);
        } else {
            StartStation ss = new StartStation(startSid);
            ss.lstEndStation.add(e);
            lstStartStation.add(ss);
        }
    }

    /**
     * @param startSid
     *            边的起始节点ID
     * @param endSid
     *            边的终止节点ID
     * @param lid
     *            所在线路ID
     * @param meters
     *            边的权值(起始节点到终止节点的距离)
     */
    public void addDoubleEdges(String startSid, String endSid, String lid, int meters) {
        addEdge(startSid, endSid, lid, meters);
        addEdge(endSid, startSid, lid, meters);
    }

    /**
     * @param startSid
     *            边的起始节点ID
     * @param endSid
     *            边的终止节点ID
     */
    public void delEdge(String startSid, String endSid) {
        StartStation s = getStartStation(startSid);
        int index = getEndStationIndex(s, endSid);
        if (index != ErrIndex) {
            s.lstEndStation.remove(index);
            if (s.lstEndStation.size() == 0) {
                index = getStartStationIndex(startSid);
                if (index != ErrIndex) {
                    lstStartStation.remove(index);
                }
            }
        }
    }

    /**
     * @param startSid
     *            边的起始节点ID
     * @param endSid
     *            边的终止节点ID
     */
    public void delDoubleEdges(String startSid, String endSid) {
        delEdge(startSid, endSid);
        delEdge(endSid, startSid);
    }

    /**
     * @param sid
     *            车站ID
     * @return 临接表的节点
     */
    public StartStation getStartStation(String sid) {
        StartStation result = null;

        for (StartStation s : lstStartStation) {
            if (s.sid.equals(sid)) {
                result = s;
                break;
            }
        }

        return result;
    }

    /**
     * @param s
     *            临接表节点
     * @param sid
     *            边的终止车站ID
     * @return 邻接表的边
     */
    public EndStation getEndStation(StartStation s, String sid) {
        EndStation result = null;

        for (EndStation e : s.lstEndStation) {
            if (e.sid.equals(sid)) {
                result = e;
                break;
            }
        }

        return result;
    }

    /**
     * @param sid
     *            车站ID
     * @return 邻接表节点的索引
     */
    public int getStartStationIndex(String sid) {
        int result = ErrIndex;

        int i = 0;
        for (StartStation s : lstStartStation) {
            if (s.sid.equals(sid)) {
                result = i;
                break;
            }
            i++;
        }

        return result;
    }

    /**
     * @param ss
     *            邻接表的节点
     * @param sid
     *            邻接表节点边的终止车站ID
     * @return 邻接表节点边的索引
     */
    public int getEndStationIndex(StartStation ss, String sid) {
        int result = ErrIndex;

        int i = 0;
        for (EndStation e : ss.lstEndStation) {
            if (e.sid.equals(sid)) {
                result = i;
                break;
            }
            i++;
        }

        return result;
    }

    /**
     * @param startSid
     *            邻接表边的起始车站ID
     * @param endSid
     *            邻接表边的终止车站ID
     * @return 邻接表边的权值
     */
    public int getMeters(String startSid, String endSid) {
        int result = MaxMeters;

        if (!startSid.equals(endSid)) {
            StartStation s = getStartStation(startSid);
            if (s != null) {
                for (EndStation e : s.lstEndStation) {
                    if (e.sid.equals(endSid)) {
                        result = e.meters;
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * @param start
     *            邻接表边的起始车站的索引
     * @param end
     *            邻接表边的终止车站的索引
     * @return 邻接表边的权值
     */
    public int getMeters(int start, int end) {
        int result = MaxMeters;

        if (start != end) {
            String sid = lstStartStation.get(end).sid;
            for (EndStation e : lstStartStation.get(start).lstEndStation) {
                if (e.sid.equals(sid)) {
                    result = e.meters;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * @param startSid
     *            邻接表边的起始车站ID
     * @param endSid
     *            邻接表边的终止车站ID
     * @return 边所在的线路ID
     */
    public String getLineID(String startSid, String endSid) {
        String result = "";

        StartStation s = getStartStation(startSid);
        if (s != null) {
            for (EndStation e : s.lstEndStation) {
                if (e.lid.equals(endSid)) {
                    result = e.sid;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * @param start
     *            邻接表边的起始车站的索引
     * @param end
     *            邻接表边的终止车站的索引
     * @return 边所在的线路ID
     */
    public String getLineID(int start, int end) {
        String result = "";

        String sid = lstStartStation.get(end).sid;
        for (EndStation e : lstStartStation.get(start).lstEndStation) {
            if (e.sid.equals(sid)) {
                result = e.lid;
                break;
            }
        }

        return result;
    }

    /**
     * @param startSid
     *            检索起始车站ID(图)
     * @param endSid
     *            检索终止车站ID(图)
     * @return 换乘路径
     */
    private List<GraphSubPath> dijkstra(String startSid, String endSid) {
        Logger.d(TAG, "dijkstra");
        List<GraphSubPath> lstSubPath = new ArrayList<GraphSubPath>();
        int iStart = getStartStationIndex(startSid);
        int iEnd = getStartStationIndex(endSid);

        if (iStart != ErrIndex && iEnd != ErrIndex) {
            int size = lstStartStation.size();
            int[] meters = new int[size];
            int[] previous = new int[size];
            int[] path = new int[size];
            boolean[] isVisited = new boolean[size];

            for (int i = 0; i < size; i++) {
                meters[i] = getMeters(iStart, i);
                previous[i] = iStart;
                path[i] = -1;
                isVisited[i] = false;
            }

            previous[iStart] = -1;
            isVisited[iStart] = true;

            int next = 0;
            int min = 0;
            int tmp = 0;
            while (!isVisited[iEnd]) {
                // 寻找当前最小的路径
                min = MaxMeters;
                for (int i = 0; i < size; i++) {
                    if (!isVisited[i] && meters[i] < min) {
                        min = meters[i];
                        next = i;
                    }
                }
                // 标记已获取到最短路径
                isVisited[next] = true;
                // 修正当前最短路径和前驱顶点
                for (int i = 0; i < size; i++) {
                    tmp = getMeters(next, i);
                    tmp = (tmp == MaxMeters ? MaxMeters : (tmp + min));
                    if (!isVisited[i] && tmp < meters[i]) {
                        meters[i] = tmp;
                        previous[i] = next;
                    }
                }
            }

            // 前驱节点为起始节点且不可到达
            if (!(previous[iEnd] == iStart && meters[iEnd] == MaxMeters)) {
                // 生成路径
                size = 0;
                while (iEnd != iStart) {
                    path[size++] = iEnd;
                    iEnd = previous[iEnd];
                }
                path[size++] = iStart;

                // 格式化路径
                for (int i = size - 1; i > 0; i--) {
                    GraphSubPath sp = new GraphSubPath();
                    sp.startSid = lstStartStation.get(path[i]).sid;
                    sp.endSid = lstStartStation.get(path[i - 1]).sid;
                    sp.meters = getMeters(path[i], path[i - 1]);
                    sp.lid = getLineID(path[i], path[i - 1]);
                    lstSubPath.add(sp);
                }
            }
        }

        Logger.d(TAG, lstSubPath.toString());
        return lstSubPath;
    }

    /**
     * @param startSid
     *            检索起始车站ID(图)
     * @param endSid
     *            检索终止车站ID(图)
     * @return 换乘简要信息
     */
    public TransPathSummary[] getTransPathSummary(String startSid, String endSid) {
        Logger.d(TAG, "getTransPathSummary");
        TransPathSummary[] arrTps = null;

        List<GraphSubPath> lstSubPath = dijkstra(startSid, endSid);
        if (lstSubPath.size() > 0) {

            int size = 0;
            String preLid = "";
            for (GraphSubPath sp : lstSubPath) {
                if (!sp.lid.equals(preLid)) {
                    size++;
                    preLid = sp.lid;
                }
            }

            int i = -1;
            preLid = "";
            arrTps = new TransPathSummary[size];
            for (GraphSubPath sp : lstSubPath) {
                if (!sp.lid.equals(preLid)) {
                    arrTps[++i] = new TransPathSummary();
                    arrTps[i].startSid = sp.startSid;
                    arrTps[i].endSid = sp.endSid;
                    arrTps[i].lid = sp.lid;
                    arrTps[i].meters = sp.meters;
                    arrTps[i].lstSids = new ArrayList<String>();
                    preLid = sp.lid;
                } else {
                    arrTps[i].endSid = sp.endSid;
                    arrTps[i].meters += sp.meters;
                    arrTps[i].lstSids.add(sp.startSid);
                }
            }
        }

        Logger.d(TAG, arrTps.toString());
        return arrTps;
    }
}
