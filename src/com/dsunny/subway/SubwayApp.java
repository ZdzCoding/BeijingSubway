package com.dsunny.subway;

/**
 * @author m 全局变量
 * 
 */
public class SubwayApp {

    private static SubwayApp mApp;

    private SubwayApp() {
    }

    /**
     * @return App实例
     */
    public static SubwayApp getInstance() {
        if (mApp != null) {
            mApp = new SubwayApp();
        }
        return mApp;
    }
}
