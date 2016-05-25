package com.dlut.jky.app1.utils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import com.sun.org.apache.xerces.internal.dom.RangeExceptionImpl;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by jiangkunyou on 15/11/19.
 */

public class SSHHelper {
    public static Connection connection = null;
    public static Session session = null;

    // 标记终端是否已经读完所有日志
    public static boolean flag = false;

    /**
     * 建立ssh连接
     */
    private static void toConnect(String hostName, String userName, String password){
        try {
            connection = new Connection(hostName);
            connection.connect();
            boolean isAuthenticated = connection.authenticateWithPassword(userName, password);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            session = connection.openSession();
        } catch (Exception e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
        }
    }

    /**
     * ssh连接参数传递
     */
    public static void init() {
        flag = false;
        String hostName = LinuxManager.getInstance().getProperty("hostname");
        String userName = LinuxManager.getInstance().getProperty("username");
        String password = LinuxManager.getInstance().getProperty("password");
        toConnect(hostName, userName, password);
    }

    /**
     * ssh连接函数，执行cmd命令
     */
    public static ArrayList<InputStream> ssh(String cmd) {

        ArrayList<InputStream> arrs = new ArrayList<InputStream>();
        try {
            SSHHelper.init();
            exec(cmd, arrs);
        } catch (Exception e) {
            Logger.getGlobal().info("");
            e.printStackTrace(System.err);
        } finally {
            /* Close this session */
            session.close();
            /* Close the connection */
            connection.close();
        }
        return arrs;
    }

    private static void exec(String cmd, ArrayList<InputStream> arrs) throws Exception {
        session.execCommand(cmd);
        /*
         * Advanced:
         * The following is a demo on how one can read from stdout and
         * stderr without having to use two parallel worker threads (i.e.,
         * we don't use the Streamgobblers here) and at the same time not
         * risking a deadlock (due to a filled SSH2 channel window, caused
         * by the stream which you are currently NOT reading from =).
         */

        /* Don't wrap these streams and don't let other threads work on
         * these streams while you work with Session.waitForCondition()!!!
         */
        InputStream stdout = session.getStdout();
        InputStream stderr = session.getStderr();

        arrs.add(stdout);
        arrs.add(stderr);

        writeLog(stdout, stderr);
    }

    private static void writeLog(InputStream stdout, InputStream stderr) throws Exception {
        byte[] buffer = new byte[8192];
        // 获取日志文件写入的地址
        String logPath = FileUploadManager.getInstance().getLogPath() + "/log";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logPath);
            while (true) {
                if ((stdout.available() == 0) && (stderr.available() == 0)) {
                /* Even though currently there is no data available, it may be that new data arrives
                 * and the session's underlying channel is closed before we call waitForCondition().
                 * This means that EOF and STDOUT_DATA (or STDERR_DATA, or both) may
                 * be set together.
                 */
                    int conditions = session.waitForCondition(ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA
                            | ChannelCondition.EOF, 100000);

                /* Wait no longer than 10 seconds (= 10000 milliseconds) */
                    if ((conditions & ChannelCondition.TIMEOUT) != 0) {
                    /* A timeout occured. */
                        throw new IOException("Timeout while waiting for data from peer.");
                    }

                /* Here we do not need to check separately for CLOSED, since CLOSED implies EOF */
                    if ((conditions & ChannelCondition.EOF) != 0) {
                    /* The remote side won't send us further data... */
                        if ((conditions & (ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA)) == 0) {
                        /* ... and we have consumed all data in the local arrival window. */
                            break;
                        }
                    }
                /* OK, either STDOUT_DATA or STDERR_DATA (or both) is set. */
                    // You can be paranoid and check that the library is not going nuts:
                    // if ((conditions & (ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA)) == 0)
                    //	throw new IllegalStateException("Unexpected condition result (" + conditions + ")");
                }
            /* If you below replace "while" with "if", then the way the output appears on the local
             * stdout and stder streams is more "balanced". Addtionally reducing the buffer size
             * will also improve the interleaving, but performance will slightly suffer.
             * OKOK, that all matters only if you get HUGE amounts of stdout and stderr data =)
             */
                while (stdout.available() > 0) {
                    int len = stdout.read(buffer);
                    if (len > 0) {
                        // this check is somewhat paranoid
                        System.out.write(buffer, 0, len);
                        fos.write(buffer, 0, len);
                    }
                }
                while (stderr.available() > 0) {
                    int len = stderr.read(buffer);
                    if (len > 0) {
                        System.err.write(buffer, 0, len);
                        fos.write(buffer, 0, len);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getGlobal().info("");
            e.printStackTrace();
            throw new Exception();
        } finally {
            // 设置已经完成日志的写操作
            flag = true;
            closeAll(fos, stdout, stderr);
        }

    }

    private static void closeAll(FileOutputStream fos, InputStream stdout, InputStream stderr){
        if(fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                Logger.getGlobal().info("");
                e.printStackTrace();
            }
        }
        if(stdout != null) {
            try {
                stdout.close();
            } catch (IOException e) {
                Logger.getGlobal().info("");
                e.printStackTrace();
            }
        }
        if(stderr != null) {
            try {
                stderr.close();
            } catch (IOException e) {
                Logger.getGlobal().info("");
                e.printStackTrace();
            }
        }
    }
}
