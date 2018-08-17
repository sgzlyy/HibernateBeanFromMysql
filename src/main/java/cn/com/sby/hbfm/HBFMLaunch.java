package cn.com.sby.hbfm;

/**
 * 系统的入口<br>
 * 调试的时候，请设置系统参数-Dtest.SystemStartForder来设置系统的启动路径，系统会根据这个启动路径寻找相关信息，
 * 比如resources目录等 <br>
 * 比如-Dtest.SystemStartForder=D:\codes\workspacex\nddd0.2
 * Dtest.SystemStartForder=D:\javaproject_ceshi\hbfm
 * 
 *
 * @date 2017年4月27日
 * @version 1.0
 */
public class HBFMLaunch {

    public static void main(String[] args) {

        // 开始服务
        HBFMApplication.getInstance().startApp();
    }
}
