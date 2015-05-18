package com.dsunny.subway.db;

/**
 * @author m 查询TRANSFER表
 * 
 */
public class TransferDao {
    public static final String TAG = "TransferDao";

    private DataBase db;

    public TransferDao() {
        db = DataBase.getInstance();
    }
}
