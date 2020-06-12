package cn.choleece.base.niuke;

import java.util.*;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-06-11 08:08
 **/
public class CountStr {

    public static void countStr(String str) {

        char[] chars = str.toCharArray();

        Map<Character, Integer> map = new TreeMap<Character, Integer>(
                new Comparator<Character>() {
                    @Override
                    public int compare(Character c1,Character c2){
                        return c1.compareTo(c2);
                    }
                }
        );

        for(char ch : chars) {
            if (Character.isLetter(ch) || Character.isDigit(ch) || Character.isSpaceChar(ch)) {
                int count = map.getOrDefault(ch, 0);
                map.put(ch, count + 1);
            }
        }

        ArrayList<Map.Entry<Character, Integer>> list = new ArrayList<Map.Entry<Character, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        StringBuffer sb = new StringBuffer();
        for(Map.Entry<Character, Integer> entry : list) {
            sb.append(entry.getKey());
        }

        System.out.print(sb.toString());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()) {
            String str = scanner.nextLine();
            countStr(str);
        }
    }
}
