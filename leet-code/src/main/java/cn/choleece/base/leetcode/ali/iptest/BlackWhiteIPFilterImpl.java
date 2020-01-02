package cn.choleece.base.leetcode.ali.iptest;

import java.util.regex.Pattern;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-12-30 21:53
 **/
public abstract class BlackWhiteIPFilterImpl implements IpList {

    private byte[][] WHITE_INDEX_IP = new byte[4][255];

    private final Pattern IP_PATTERN = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");

    /**
     * 具体可以有不同的ip涞源
     */
    public void loadWhiteIPList() {
        String whiteIps = "10.50.7.24,10.507.25";

        String[] ips = whiteIps.split(",");

        for (String ip : ips) {
            String[] ipIndexArray = ip.split(".");

            for (int j = 0; j < ipIndexArray.length; j++) {
                WHITE_INDEX_IP[j][Integer.valueOf(ipIndexArray[j])] = 1;
            }
        }
    }

    @Override
    public boolean isInList(String ip) {
        if (ip == null && "".equals(ip) && !IP_PATTERN.matcher(ip).matches()) {
            return false;
        }

        String[] ipIndexArray = ip.split(".");

        for (int i = 0; i < ipIndexArray.length; i++) {
            if (WHITE_INDEX_IP[i][Integer.valueOf(ipIndexArray[i])] == 1) {
                continue;
            }

            return false;
        }
        return true;
    }
}
