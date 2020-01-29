package cn.choleece.base.leetcode.code1185;

/**
 * @author choleece
 * @Description: Given a date, return the corresponding day of the week for that date.
 *
 * The input is given as three integers representing the day, month and year respectively.
 * @Date 2019-09-15 22:07
 **/
public class Solution1185 {

    /**
     * Thursday of Jan 1st 1970
     */
    static final Integer DAY_OFFSET = 3;

    static final Integer FROM_YEAR = 1970;

    private static String[] WEEKDAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public static Integer[] MONTH_DAYS = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static String dayOfTheWeek(int day, int month, int year) {

        int days = day + DAY_OFFSET;

        for (int i = FROM_YEAR; i < year; i++) {
            days += isLeapYear(i) ? 366 : 365;
        }

        for (int i = 1; i < month ; i++) {
            days += MONTH_DAYS[i];
        }

        if (month > 2 && isLeapYear(year)) {
            days += 1;
        }
        return WEEKDAYS[days % 7];
    }

    /**
     * 闰年对2月有28天
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0) {
            return year % 4 == 0;
        } else {
            return year % 4 == 0;
        }
    }

    public static void main(String[] args) {
        System.out.println(dayOfTheWeek(29, 2, 2016));
    }
}
