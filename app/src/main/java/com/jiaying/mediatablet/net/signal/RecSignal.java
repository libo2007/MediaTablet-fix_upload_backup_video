package com.jiaying.mediatablet.net.signal;

/**
 * Created by hipil on 2016/4/2.
 */
public enum RecSignal {

    // The signals received from the plasma.
    TIMESTAMP,
    CONFIRM,
    RECORDDONORVIDEO,
    RECORDNURSEVIDEO,
    RECORDOVER,
    AUTHPASS,
    SERAUTHRES,
    ZXDCAUTHRES,
    AUTHRESOK,
    AUTHRESTIMEOUT,
    REAUTHPASS,
    CANCLEAUTHPASS,
    COMPRESSINON,
    PUNCTURE,
    START,
    AUTOTRANFUSIONSTART,
    AUTOTRANFUSIONEND,
    PLASMAWEIGHT,
    PIPELOW,
    PIPENORMAL,
    PAUSED,
    END,

    //
    LOWPOWER,
    CHECKSTART,
    CHECKOVER,
    AVAILABLERES,
    WAITING,
    STARTPUNTUREVIDEO,
    STARTCOLLECTIONVIDEO,


    //
    SETTINGS,
    RESTART,


    // 这个是视频列表
    TOVIDEOLIST,
    //视频分类列表
    TOVIDEOCATEGORY,

    // Switch between the tabs

    TOSURF,
    TOSUGGEST,
    TOAPPOINT,

    //between activity and fragment
    VIDEOTOMAIN,
    CLICKSUGGESTION,
    CLICKEVALUATION,
    CLICKAPPOINTMENT,
    SAVEAPPOINTMENT,
    SAVESUGGESTION,
    SAVEEVALUATION,
    AUTH,
    STARTVIDEO,

    //back button
    BACKTOVIDEOLIST,
    BACKTOADVICE,
    BACKTOAPPOINTMENT,

    //
    NOTHING,

    //The three physical keys
    POWEROFF,
    RECENT,
    HOME

}
