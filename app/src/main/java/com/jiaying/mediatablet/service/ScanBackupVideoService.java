package com.jiaying.mediatablet.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jiaying.mediatablet.entity.VideoEntity;
import com.jiaying.mediatablet.net.serveraddress.VideoServer;
import com.jiaying.mediatablet.net.softfan.FtpSenderFile;
import com.jiaying.mediatablet.net.softfan.SoftFanFTPException;
import com.jiaying.mediatablet.thread.SendVideoThread;
import com.jiaying.mediatablet.utils.FileScan;
import com.jiaying.mediatablet.utils.MyLog;
import com.jiaying.mediatablet.utils.SelfFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 作者：lenovo on 2016/5/29 17:16
 * 邮箱：353510746@qq.com
 * 功能：扫描本地未上传到服务器的本地视频，定时扫描传送到服务器
 */
public class ScanBackupVideoService extends Service {
    private static final String TAG = "ScanBackupVideoService";
    private Handler mHandler = null;
    private HandlerThread mHandlerThread = null;
    private DealBackupVideoRunnable mDealBackupVideoRunnable = null;

    //立即扫描
    private static final int MSG_SCAN_NOW = 1001;
    //等5分钟扫描
    private static final int MSG_SCAN_DELAY = 1002;
    private static final int TIME_5_MINUTE = 5 * 60 * 1000;

    //传送结果
    private String resultStr;

    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.e(TAG, "开始启动 ScanBackupVideoService");
        stopHandlerThread();
        mHandlerThread = new HandlerThread("scanbackthread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_SCAN_DELAY:
                        mHandler.post(mDealBackupVideoRunnable);
                        break;
                    case MSG_SCAN_NOW:
                        mHandler.post(mDealBackupVideoRunnable);
                        break;

                    default:
                        break;
                }

            }
        };
        mDealBackupVideoRunnable = new DealBackupVideoRunnable();
        mHandler.post(mDealBackupVideoRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        MyLog.e(TAG, "销毁 ScanBackupVideoService");
        super.onDestroy();
        Intent it = new Intent(this, ScanBackupVideoService.class);
        startService(it);
    }

    private void stopHandlerThread() {
        if (mHandler != null && mDealBackupVideoRunnable != null) {
            mHandler.removeCallbacks(mDealBackupVideoRunnable);
            mHandler = null;
            mDealBackupVideoRunnable = null;
        }
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
    }


    private class DealBackupVideoRunnable implements Runnable {

        @Override
        public void run() {
            //扫描back目录是否有视频文件
            List<VideoEntity> backupFileList = FileScan.getLocalVideoList(FileScan.VIDEO_PATH_BACKUP);
            if (backupFileList == null || backupFileList.isEmpty()) {
                //等5分钟再次发送扫描请求
                MyLog.e(TAG, "backup目录下不存在视频文件");
                mHandler.sendEmptyMessageDelayed(MSG_SCAN_DELAY, TIME_5_MINUTE);
            } else {
                //向服务器传送文件
                MyLog.e(TAG, "backup目录下存在视频文件：" + backupFileList.get(0).getPlay_url());
                upLoadVideo(backupFileList.get(0).getPlay_url(), SelfFile.generateRemoteVideoName());
            }
        }
    }

    private void upLoadVideo(String lPath, String rPath) {
        long start = System.currentTimeMillis();

        File file = new File(lPath);
        while (true) {
            boolean b = file.exists();
            if (b) {
                Log.e(TAG, "视频文件存在");
                break;
            } else {
                if ((System.currentTimeMillis() - start) < 2 * 60 * 1000) {
                    Log.e(TAG, "视频文件不存在,因为时间问题");
                    mHandler.sendEmptyMessageDelayed(MSG_SCAN_DELAY, TIME_5_MINUTE);
                    return;
                }
            }
            synchronized (this) {
                try {
                    this.wait(500);
                } catch (InterruptedException e) {
                }
            }
        }

        MyLog.e(TAG, ",开始向服务器发送back中的视频文件");
        FtpSenderFile sender = new FtpSenderFile(VideoServer.getInstance().getIp(), VideoServer.getInstance().getPort());
        try {
            resultStr = sender.send(lPath, rPath);
        } catch (SoftFanFTPException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (Exception e) {
        } finally {
            Log.e(TAG, "传送视频 finally");
        }

        long end = System.currentTimeMillis();
        Log.e(TAG, "传送视频耗时" + (end - start));


        if ("传送成功".equals(resultStr)) {
            Log.e(TAG, resultStr);

            // success and delete the video file.
            SelfFile.delFile(lPath);
            mHandler.sendEmptyMessage(MSG_SCAN_NOW);

        } else {
            Log.e(TAG, "传送失败");
            // save the video if failure.
            File srcFile = SelfFile.createNewFile(SelfFile.generateLocalVideoName());
            File destFile = SelfFile.createNewFile(SelfFile.generateLocalBackupVideoName());
            try {
                SelfFile.copyFile(srcFile, destFile);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //这里考虑到无法连接ftp的时候传输失败，避免反复传输造成浪费
                mHandler.sendEmptyMessageDelayed(MSG_SCAN_DELAY,TIME_5_MINUTE);
            }
        }
    }
}
