package com.zhao.myreader.common;

/**
 * Created by zhao on 2016/10/20.
 */

public class URLCONST {

    // 命名空间
    public static String nameSpace_tianlai = "https://www.23txt.com/";

    public static  String nameSpace_biquge = "https://www.52bqg.net/";

    public static String nameSpace_system = "https://10.10.123.31:8080/jeecg";

    public static boolean isRSA = false;

    // 搜索小说
    public static String method_buxiu_search = "https://www.23txt.com/search.php";

    public static String method_bqg_search = nameSpace_biquge + "/modules/article/search.php";
//    searchkey=%C1%E9%D3%F2

    // 获取最新版本号
    public static String method_getCurAppVersion = nameSpace_system + "/mReaderController.do?getCurAppVersion";

    // app下载
    public static String method_downloadApp = nameSpace_system + "/upload/app/MissZzzReader.apk";

}

