package util;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by gaotong1 on 2015/12/14.
 */
public class Config {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat sdfRead = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat sdfReadFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public  static boolean isWin = false;
    static {
        String os = System.getProperty("os.name");
        isWin = os.toLowerCase().startsWith("win");

    }
    
}
