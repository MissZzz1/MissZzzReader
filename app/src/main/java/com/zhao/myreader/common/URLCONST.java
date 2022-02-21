package com.zhao.myreader.common;

/**
 * Created by zhao on 2016/10/20.
 */

public class URLCONST {

    // 命名空间
    public static String nameSpace_tianlai = "https://www.hkxsw.cc";

    public static  String nameSpace_biquge = "https://www.xxbqg.com";

    public static  String nameSpace_dingdian = "https://www.ddxs.cc";





    public static String nameSpace_system = "https://10.10.123.31:8080/jeecg";

    public static boolean isRSA = false;

    // 搜索小说
    public static String method_tl_search = nameSpace_tianlai + "/search.php";

    public static String method_bqg_search = nameSpace_biquge + "/modules/article/search.php";
//    searchkey=%C1%E9%D3%F2

    public static String method_dd_search = nameSpace_dingdian + "/modules/article/search.php";

    public static String method_dd_rank = nameSpace_dingdian + "/paihangbang/";

    public static String method_tl_rank = nameSpace_tianlai + "/rank/";

    // 获取最新版本号
    public static String method_getCurAppVersion = nameSpace_system + "/mReaderController.do?getCurAppVersion";

    // app下载
    public static String method_downloadApp = nameSpace_system + "/upload/app/MissZzzReader.apk";

}

