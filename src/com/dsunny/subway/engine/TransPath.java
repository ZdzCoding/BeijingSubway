package com.dsunny.subway.engine;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.dsunny.subway.bean.SearchResult;
import com.dsunny.subway.bean.TStation;
import com.dsunny.subway.bean.TransPathDetail;
import com.dsunny.subway.bean.TransPathSummary;
import com.dsunny.subway.bean.TransSubPath;
import com.dsunny.subway.constant.Message;
import com.dsunny.subway.constant.SubwayConst;
import com.dsunny.subway.db.LineDao;
import com.dsunny.subway.db.StationDao;
import com.dsunny.subway.db.TStationDao;
import com.dsunny.subway.db.TransferDao;
import com.dsunny.subway.util.Logger;
import com.dsunny.subway.util.Utils;

/**
 * @author m 路径处理
 * 
 */
public class TransPath {
    public static final String TAG = "TransPath";

    private static final String SID_1408 = "1408";
    private static final String SID_1425 = "1425";

    private LineDao mLDao;
    private StationDao mSDao;
    private TransferDao mTDao;
    private TStationDao mTSDao;

    public TransPath() {
        mLDao = new LineDao();
        mSDao = new StationDao();
        mTDao = new TransferDao();
        mTSDao = new TStationDao();
    }

    /**
     * @param startSName
     *            起始车站名
     * @param endSName
     *            终止车站名
     * @return 所有换乘信息
     */
    public SearchResult getTransPaths(String startSName, String endSName) {
        Logger.d(TAG, "getTransPaths");
        SearchResult result = null;

        int code = checkStation(startSName, endSName);
        if (code != Message.CHECK_OK) {
            result = new SearchResult();
            result.startSName = startSName;
            result.endSName = endSName;
            result.code = code;
            result.lstTpd = new ArrayList<TransPathDetail>();
        } else {
            List<String> lstStartSids = mSDao.getAllSidsByName(startSName);
            List<String> lstEndSids = mSDao.getAllSidsByName(endSName);
            List<String[]> lstTransLids = getAllTransLids(lstStartSids, lstEndSids);
            result = getAllTransPaths(lstStartSids.get(0), lstEndSids.get(0), lstTransLids);
        }

        return result;
    }

    /**
     * @param startSName
     *            起始车站名
     * @param endSName
     *            终止车站名
     * @return 错误ID
     */
    private int checkStation(String startSName, String endSName) {
        Logger.d(TAG, "checkStation");
        int code = Message.CHECK_OK;

        if (TextUtils.isEmpty(startSName.trim())) {
            code = Message.ERR_START_EMPTY;
        } else if (TextUtils.isEmpty(endSName.trim())) {
            code = Message.ERR_END_EMPTY;
        } else if (startSName.equals(endSName)) {
            code = Message.ERR_SAME_STATION;
        } else if (!mSDao.isStationExist(startSName)) {
            code = Message.ERR_START_NOT_EXIST;
        } else if (!mSDao.isStationExist(endSName)) {
            code = Message.ERR_END_NOT_EXIST;
        }

        return code;
    }

    /**
     * @param lstStartSids
     *            起点车站ID
     * @param lstEndSids
     *            终点车站ID
     * @return 换乘线路
     */
    private List<String[]> getAllTransLids(List<String> lstStartSids, List<String> lstEndSids) {
        Logger.d(TAG, "getAllTransLids");
        List<String[]> lstResult = null;

        List<String> lstStartLids = new ArrayList<String>();
        for (String s : lstStartSids) {
            lstStartLids.add(Utils.getLID(s));
        }

        List<String> lstEndLids = new ArrayList<String>();
        for (String s : lstEndSids) {
            lstEndLids.add(Utils.getLID(s));
        }

        List<String> lstCommonLids = new ArrayList<String>(lstStartLids);
        lstCommonLids.retainAll(lstEndLids);

        switch (lstCommonLids.size()) {
        case 0:
            // 不在同一线路
            LineGraph lg = new LineGraph();
            lstResult = lg.getAllTransLids(lstStartLids, lstEndLids);
            break;
        case 1:
            // 在同一线路
            String sCmnLid = lstCommonLids.get(0);
            String startSid = null;
            String endSid = null;
            if (sCmnLid.equals(SubwayConst.Line_14)) {
                startSid = mSDao.getLineSIDByName(SubwayConst.Line_14, lstStartSids.get(0));
                endSid = mSDao.getLineSIDByName(SubwayConst.Line_14, lstEndSids.get(0));
                if ((startSid.compareTo(SID_1408) < 0 && endSid.compareTo(SID_1408) < 0)
                        || (endSid.compareTo(SID_1425) > 0 && endSid.compareTo(SID_1425) > 0)) {
                    lstResult = new ArrayList<String[]>();
                    lstResult.add(new String[] { sCmnLid });
                } else {
                    LineGraph lg2 = new LineGraph();
                    lstResult = lg2.getAllTransLids(SubwayConst.Line_14a, SubwayConst.Line_14b);
                }
            } else if (sCmnLid.equals(SubwayConst.Line_99)) {
                startSid = mSDao.getLineSIDByName(SubwayConst.Line_99, lstStartSids.get(0));
                endSid = mSDao.getLineSIDByName(SubwayConst.Line_99, lstEndSids.get(0));
                if ((startSid.equals("9902") && endSid.equals("9903"))
                        || (startSid.equals("9903") && endSid.equals("9902"))) {
                    lstResult = new ArrayList<String[]>();
                    lstResult.add(new String[] { sCmnLid });
                    lstResult.add(new String[] { SubwayConst.Line_02, SubwayConst.Line_13,
                            SubwayConst.Line_10 });
                }
            } else {
                lstResult = new ArrayList<String[]>();
                lstResult.add(new String[] { sCmnLid });
                if (mLDao.isLineHasLoop(sCmnLid)) {
                    List<String> lstLids = mTDao.getAcrossLids(sCmnLid);
                    for (String lid : lstLids) {
                        lstResult.add(new String[] { sCmnLid, lid });
                    }
                }
            }
            break;
        case 2:
            // 特殊(四惠-四惠东)
            lstResult = new ArrayList<String[]>();
            lstResult.add(new String[] { lstCommonLids.get(0) });
            lstResult.add(new String[] { lstCommonLids.get(1) });
            break;
        default:
            break;
        }

        return lstResult;
    }

    /**
     * @param startSid
     *            起始车站ID(图)
     * @param endSid
     *            终止车站ID(图)
     * @param lstTransLids
     *            换乘线路
     * @return 所有换乘信息
     */
    private SearchResult getAllTransPaths(String startSid, String endSid,
            List<String[]> lstTransLids) {
        Logger.d(TAG, "getAllTransPaths");
        SearchResult result = new SearchResult();
        result.startSName = mSDao.getSName(startSid);
        result.endSName = mSDao.getSName(endSid);
        result.code = Message.CHECK_OK;
        result.lstTpd = new ArrayList<TransPathDetail>();

        for (String[] arr : lstTransLids) {
            SubwayGraph sg = null;
            if (arr.length == 1) {
                sg = new SubwayGraph(mTSDao.getLineTStations(arr[0]));
            } else {
                if (arr[0].equals(SubwayConst.Line_99)) {
                    arr[0] = "";
                    sg = new SubwayGraph(mTSDao.getLinesTStations(arr));
                    List<TStation> lst1 = mTSDao.getLineTStations(SubwayConst.Line_99);
                    TStation ts = lst1.get(0);
                    sg.addDoubleEdges(ts.StartSID, ts.EndSID, ts.LID, ts.Meters);
                    lst1.remove(0);
                    for (TStation t : lst1) {
                        sg.addEdge(t.StartSID, t.EndSID, t.LID, t.Meters);
                    }
                } else if (arr[0].equals(SubwayConst.Line_94)) {
                    arr[0] = "";
                    arr[1] = "";
                    sg = new SubwayGraph(mTSDao.getLinesTStations(arr));
                    List<TStation> lst1 = mTSDao.getLineTStations(SubwayConst.Line_01);
                    for (TStation t : lst1) {
                        sg.addDoubleEdges(t.StartSID, t.EndSID, t.LID, t.Meters);
                    }
                    List<TStation> lst2 = mTSDao.getLineTStations(SubwayConst.Line_94);
                    lst2.remove(0);
                    for (TStation t : lst2) {
                        sg.addDoubleEdges(t.StartSID, t.EndSID, t.LID, t.Meters);
                    }
                } else if (arr[arr.length - 1].equals(SubwayConst.Line_99)) {
                    arr[arr.length - 1] = "";
                    sg = new SubwayGraph(mTSDao.getLinesTStations(arr));
                    List<TStation> lst1 = mTSDao.getLineTStations(SubwayConst.Line_99);
                    TStation ts = lst1.get(0);
                    sg.addDoubleEdges(ts.StartSID, ts.EndSID, ts.LID, ts.Meters);
                    lst1.remove(0);
                    for (TStation t : lst1) {
                        sg.addEdge(t.StartSID, t.EndSID, t.LID, t.Meters);
                    }
                } else if (arr[arr.length - 1].equals(SubwayConst.Line_94)) {
                    arr[arr.length - 1] = "";
                    arr[arr.length - 2] = "";
                    sg = new SubwayGraph(mTSDao.getLinesTStations(arr));
                    List<TStation> lst1 = mTSDao.getLineTStations(SubwayConst.Line_01);
                    lst1.remove(lst1.size() - 1);
                    for (TStation t : lst1) {
                        sg.addDoubleEdges(t.StartSID, t.EndSID, t.LID, t.Meters);
                    }
                    List<TStation> lst2 = mTSDao.getLineTStations(SubwayConst.Line_94);
                    for (TStation t : lst2) {
                        sg.addDoubleEdges(t.StartSID, t.EndSID, t.LID, t.Meters);
                    }
                } else {
                    sg = new SubwayGraph(mTSDao.getLinesTStations(arr));
                }
            }
            sg.printGraph();
            TransPathSummary[] tps = getTransPathSummary(sg, startSid, endSid);
            TransPathDetail tpd = getTransPathDetail(tps);
            addTransPathDetail(result, tpd);
        }
        return result;
    }

    /**
     * @param sg
     *            抽象地铁图
     * @param startSid
     *            检索起始车站ID(图)
     * @param endSid
     *            检索终止车站ID(图)
     * @return 换乘简要信息
     */
    private TransPathSummary[] getTransPathSummary(SubwayGraph sg, String startSid, String endSid) {
        Logger.d(TAG, "getTransPathSummary");
        List<String> lstStartAdjSids = null;
        List<String> lstEndAdjSids = null;

        if (!mTSDao.isTransOrTailStation(startSid)) {
            lstStartAdjSids = mLDao.getAdjacentSids(startSid);
        }
        if (!mTSDao.isTransOrTailStation(endSid)) {
            lstEndAdjSids = mLDao.getAdjacentSids(endSid);
        }

        int meters = 0;
        String lid = null;
        String preGraphSid = null;
        String nxtGraphSid = null;
        if (lstStartAdjSids != null && lstEndAdjSids != null
                && lstStartAdjSids.toString().equals(lstEndAdjSids.toString())) {
            lid = Utils.getLID(startSid);
            preGraphSid = mSDao.getGraphSidById(lstStartAdjSids.get(0));
            nxtGraphSid = mSDao.getGraphSidById(lstStartAdjSids.get(1));

            sg.delDoubleEdges(preGraphSid, nxtGraphSid);

            meters = mLDao.getAdjacentSMeters(lstStartAdjSids.get(0), startSid);
            sg.addDoubleEdges(preGraphSid, startSid, lid, meters);
            meters = mLDao.getAdjacentSMeters(startSid, endSid);
            sg.addDoubleEdges(startSid, endSid, lid, meters);
            meters = mLDao.getAdjacentSMeters(endSid, lstStartAdjSids.get(1));
            sg.addDoubleEdges(endSid, nxtGraphSid, lid, meters);
        } else {
            if (lstStartAdjSids != null) {
                lid = Utils.getLID(startSid);
                preGraphSid = mSDao.getGraphSidById(lstStartAdjSids.get(0));
                nxtGraphSid = mSDao.getGraphSidById(lstStartAdjSids.get(1));

                sg.delDoubleEdges(preGraphSid, nxtGraphSid);

                meters = mLDao.getAdjacentSMeters(lstStartAdjSids.get(0), startSid);
                sg.addDoubleEdges(preGraphSid, startSid, lid, meters);
                meters = mLDao.getAdjacentSMeters(startSid, lstStartAdjSids.get(1));
                sg.addDoubleEdges(startSid, nxtGraphSid, lid, meters);
            }
            if (lstEndAdjSids != null) {
                lid = Utils.getLID(endSid);
                preGraphSid = mSDao.getGraphSidById(lstEndAdjSids.get(0));
                nxtGraphSid = mSDao.getGraphSidById(lstEndAdjSids.get(1));

                sg.delDoubleEdges(preGraphSid, nxtGraphSid);

                meters = mLDao.getAdjacentSMeters(lstEndAdjSids.get(0), endSid);
                sg.addDoubleEdges(preGraphSid, endSid, lid, meters);
                meters = mLDao.getAdjacentSMeters(endSid, lstEndAdjSids.get(1));
                sg.addDoubleEdges(endSid, nxtGraphSid, lid, meters);
            }
        }

        return sg.getTransPathSummary(startSid, endSid);
    }

    /**
     * @param arrTps
     * @return 换乘详细信息
     */
    private TransPathDetail getTransPathDetail(TransPathSummary[] arrTps) {
        Logger.d(TAG, "getTransPathDetail");
        TransPathDetail resultTpd = new TransPathDetail();

        int meters = 0;
        int stations = 0;
        int transTimes = arrTps.length;
        List<TransSubPath> lstTransSubPath = new ArrayList<TransSubPath>();
        for (int i = 0; i < transTimes; i++) {
            TransSubPath tsp = new TransSubPath();
            tsp.startSName = mSDao.getSName(arrTps[i].startSid);
            tsp.endSName = mSDao.getSName(arrTps[i].endSid);
            tsp.lineName = mLDao.getLineName(arrTps[i].lid);

            String startSid = mSDao.getLineSIDByName(arrTps[i].lid, tsp.startSName);
            String endSid = mSDao.getLineSIDByName(arrTps[i].lid, tsp.endSName);
            if (mLDao.isLoopLine(arrTps[i].lid)) {
                String sName = arrTps[i].lstSids.size() > 0 ? mSDao.getSName(arrTps[i].lstSids
                        .get(0)) : null;
                tsp.lstSNames = mSDao.getSNamesContain(startSid, endSid, sName);
                String sDirection = tsp.lstSNames.size() > 0 ? tsp.lstSNames.get(0) : tsp.endSName;
                tsp.direction = sDirection;
            } else {
                tsp.lstSNames = mSDao.getSNamesBetween(startSid, endSid);
                tsp.direction = mSDao.getTransDirection(startSid, endSid);
            }

            if (!arrTps[i].lid.equals(SubwayConst.Line_99)) {
                meters += arrTps[i].meters;
            }
            stations += tsp.lstSNames.size();
            lstTransSubPath.add(tsp);
        }

        int price = 0;
        int minutes = meters / 500 + 1;
        if (meters <= 6000) {
            price = 3;
        } else if (meters <= 12000) {
            price = 4;
        } else if (meters <= 22000) {
            price = 5;
        } else if (meters <= 32000) {
            price = 6;
        } else {
            price = 7 + (meters - 32000) / 20000;
        }
        if (arrTps[0].lid.equals(SubwayConst.Line_99)
                || arrTps[arrTps.length - 1].lid.equals(SubwayConst.Line_99)) {
            price += 25;
        }
        resultTpd.stations = stations + transTimes - 1;
        resultTpd.meters = meters;
        resultTpd.minutes = minutes;
        resultTpd.price = price;
        resultTpd.lstTransSubPath = lstTransSubPath;

        return resultTpd;
    }

    /**
     * @param sr
     *            所有换乘信息
     * @param tpd
     *            换乘详细信息
     */
    private void addTransPathDetail(SearchResult sr, TransPathDetail tpd) {
        Logger.d(TAG, "addTransPathDetail");
        boolean isExist = false;
        for (TransPathDetail t : sr.lstTpd) {
            if (t.toString().equals(tpd.toString())) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            sr.lstTpd.add(tpd);
            sr.count = sr.lstTpd.size();
        }
    }

}
