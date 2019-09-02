package caiji.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    public static ArrayList<String> removeRepeatRecord(ArrayList<String> urlList) {
        ArrayList<String> newList = new ArrayList<String>();
        boolean exist;
        for (String s : urlList) {
            exist = false;
            for (String s2 : newList) {
                if (s.equalsIgnoreCase(s2)) {
                    exist = true;
                    break;
                }
            }
            if (exist == false) {
                newList.add(s);
            }
        }
        return newList;
    }
}
