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
import com.dsunny.subway.db.LineDao;
import com.dsunny.subway.db.StationDao;
import com.dsunny.subway.db.TStationDao;
import com.dsunny.subway.util.Logger;
import com.dsunny.subway.util.Utils;

/**
 * @author m 路径处理
 * 
 */
public class TransPath {
    public static final String TAG = "TransPath";

    private static final int MaxResultCount = 3;
    private static final int MaxMeters = Integer.MAX_VALUE;

    private LineDao mLDao;
    private StationDao mSDao;
    private TStationDao mTSDao;
    private SubwayGraph mSg;

    public TransPath() {
        mLDao = new LineDao();
        mSDao = new StationDao();
        mTSDao = new TStationDao();
        initSubwayGraph();
    }

    /**
     * 初始化抽象地铁图
     */
    private void initSubwayGraph() {
        // 取得所有路线换乘信息，可优化
        List<TStation> lstTStation = mTSDao.getAllTStations();
        mSg = new SubwayGraph(lstTStation);
        mSg.printGraph();
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
     * @param startSid
     *            起始车站ID(图)
     * @param endSid
     *            终止车站ID(图)
     * @return 换乘简要信息
     */
    private TransPathSummary[] getFirstPathSummary(String startSid, String endSid) {
        Logger.d(TAG, "getFirstPathSummary");
        List<String> lstStartAdjSids = null;
        List<String> lstEndAdjSids = null;

        if (!mTSDao.isTransOrTailStation(startSid)) {
            lstStartAdjSids = mLDao.getAdjacentSids(startSid);
        }
        if (!mTSDao.isTransOrTailStation(endSid)) {
            lstEndAdjSids = mLDao.getAdjacentSids(endSid);
        }

        if (lstStartAdjSids != null && lstEndAdjSids != null
                && lstStartAdjSids.toString().equals(lstEndAdjSids.toString())) {
            addNewStartStations(lstStartAdjSids, startSid, endSid);
        } else {
            addNewStartStation(lstStartAdjSids, startSid);
            addNewStartStation(lstEndAdjSids, endSid);
        }

        return mSg.getTransPathSummary(startSid, endSid);
    }

    /**
     * @param lstAdjSids
     *            车站ID的相邻换乘车站ID
     * @param sid
     *            车站ID
     */
    private void addNewStartStation(List<String> lstAdjSids, String sid) {
        Logger.d(TAG, "addNewStartStation");
        if (lstAdjSids != null) {
            int meters = 0;
            String lid = Utils.getLID(sid);
            String preGraphSid = mSDao.getGraphSidById(lstAdjSids.get(0));
            String nxtGraphSid = mSDao.getGraphSidById(lstAdjSids.get(1));

            mSg.delDoubleEdges(preGraphSid, nxtGraphSid);

            meters = mLDao.getAdjacentSMeters(lstAdjSids.get(0), sid);
            mSg.addDoubleEdges(preGraphSid, sid, lid, meters);
            meters = mLDao.getAdjacentSMeters(sid, lstAdjSids.get(1));
            mSg.addDoubleEdges(sid, nxtGraphSid, lid, meters);

            Logger.d(TAG, preGraphSid + "-" + sid);
            Logger.d(TAG, sid + "-" + nxtGraphSid);
        }
    }

    /**
     * @param lstAdjSids
     *            车站ID的相邻换乘车站ID
     * @param sid1
     *            车站ID
     * @param sid2
     *            车站ID
     */
    private void addNewStartStations(List<String> lstAdjSids, String sid1, String sid2) {
        Logger.d(TAG, "addNewStartStations");
        if (lstAdjSids != null) {
            int meters = 0;
            String lid = Utils.getLID(sid1);
            String preGraphSid = mSDao.getGraphSidById(lstAdjSids.get(0));
            String nxtGraphSid = mSDao.getGraphSidById(lstAdjSids.get(1));

            mSg.delDoubleEdges(preGraphSid, nxtGraphSid);

            meters = mLDao.getAdjacentSMeters(lstAdjSids.get(0), sid1);
            mSg.addDoubleEdges(preGraphSid, sid1, lid, meters);
            meters = mLDao.getAdjacentSMeters(sid1, sid2);
            mSg.addDoubleEdges(sid1, sid2, lid, meters);
            meters = mLDao.getAdjacentSMeters(sid2, lstAdjSids.get(1));
            mSg.addDoubleEdges(sid2, nxtGraphSid, lid, meters);

            Logger.d(TAG, preGraphSid + "-" + sid1);
            Logger.d(TAG, sid1 + "-" + sid2);
            Logger.d(TAG, sid2 + "-" + nxtGraphSid);
        }
    }

    /**
     * @param startSName
     *            起始车站名
     * @param endSName
     *            终止车站名
     * @return 换乘详细信息
     */
    public SearchResult getTransPath(String startSName, String endSName) {
        Logger.d(TAG, "getTransPath");
        SearchResult result = null;

        int code = checkStation(startSName, endSName);
        if (code != Message.CHECK_OK) {
            result = new SearchResult();
            result.startSName = startSName;
            result.endSName = endSName;
            result.code = code;
            result.lstTpd = new ArrayList<TransPathDetail>();
        } else {
            String lid = mSDao.getCommonLidByName(startSName, endSName);
            if (TextUtils.isEmpty(lid) || mLDao.isLineHasLoop(lid)) {
                result = getAllTransPaths(startSName, endSName);
            } else {
                result = getSingleTransPath(lid, startSName, endSName);
            }
        }

        return result;
    }

    /**
     * @param startSName
     *            起始车站名
     * @param endSName
     *            终止车站名
     * @return 同一路线换乘详细信息
     */
    public SearchResult getSingleTransPath(String lid, String startSName, String endSName) {
        Logger.d(TAG, "getSingleTransPath");
        SearchResult result = new SearchResult();
        result.startSName = startSName;
        result.endSName = endSName;
        result.code = Message.CHECK_OK;
        result.lstTpd = new ArrayList<TransPathDetail>();

        String startSid = mSDao.getGraphSidByName(startSName);
        String endSid = mSDao.getGraphSidByName(endSName);

        TransPathSummary[] arrTps = getFirstPathSummary(startSid, endSid);
        if (arrTps != null) {
            TransPathDetail tpd = getTransPathDetail(arrTps);
            result.lstTpd.add(tpd);
        }

        result.count = result.lstTpd.size();
        return result;
    }

    /**
     * @param startSName
     *            起始车站名
     * @param endSName
     *            终止车站名
     * @return 所有换乘详细信息
     */
    public SearchResult getAllTransPaths(String startSName, String endSName) {
        Logger.d(TAG, "getAllTransPaths");
        SearchResult result = new SearchResult();
        result.startSName = startSName;
        result.endSName = endSName;
        result.code = Message.CHECK_OK;
        result.lstTpd = new ArrayList<TransPathDetail>();

        String startSid = mSDao.getGraphSidByName(startSName);
        String endSid = mSDao.getGraphSidByName(endSName);
        TransPathSummary[] arrTps = getFirstPathSummary(startSid, endSid);

        int resultCount = 0;
        while (arrTps != null && resultCount < MaxResultCount) {
            TransPathDetail tpd = getTransPathDetail(arrTps);
            result.lstTpd.add(tpd);
            resultCount++;

            int index = 0;
            int minMeters = MaxMeters;
            for (int i = 0; i < arrTps.length; i++) {
                if (arrTps[i].meters < minMeters) {
                    minMeters = arrTps[i].meters;
                    index = i;
                }
            }

            int size = arrTps[index].lstSids.size();
            if (size > 0) {
                mSg.delDoubleEdges(arrTps[index].startSid, arrTps[index].lstSids.get(0));
                for (int i = 0; i < size - 1; i++) {
                    mSg.delDoubleEdges(arrTps[index].lstSids.get(i),
                            arrTps[index].lstSids.get(i + 1));
                }
                mSg.delDoubleEdges(arrTps[index].lstSids.get(size - 1), arrTps[index].endSid);
            } else {
                mSg.delDoubleEdges(arrTps[index].startSid, arrTps[index].endSid);
            }

            arrTps = mSg.getTransPathSummary(startSid, endSid);
        }

        result.count = result.lstTpd.size();
        return result;
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

            String startSid = mSDao.getLineSID(arrTps[i].lid, tsp.startSName);
            String endSid = mSDao.getLineSID(arrTps[i].lid, tsp.endSName);
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

            meters += arrTps[i].meters;
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

        resultTpd.stations = stations + transTimes - 1;
        resultTpd.meters = meters;
        resultTpd.minutes = minutes;
        resultTpd.price = price;
        resultTpd.lstTransSubPath = lstTransSubPath;

        return resultTpd;
    }
}
