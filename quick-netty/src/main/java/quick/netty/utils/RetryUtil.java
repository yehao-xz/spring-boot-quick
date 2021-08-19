package quick.netty.utils;

/**
 * 重试工具类
 *
 * @author yehao
 * @date 2021/8/4
 */
public class RetryUtil {

    // 重试时间
    private static final int[] retryTime = new int[]{1, 2, 5, 10, 20, 30};

    /**
     * 获取下次重试时间 单位：分钟
     *
     * @param retries
     * @return
     */
    public static int getNextTime(int retries) {
        if (retries >= 6) {
            retries = 6;
        }
        return retryTime[retries - 1];
    }
}

