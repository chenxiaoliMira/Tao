package caiji.util;

import java.util.HashMap;
import java.util.Map;

public class HTTPUtil {

    public static Map<String,String> Split(String urlparam){
        Map<String,String> map = new HashMap<String, String>();
        String[] param =  urlparam.split("&");
        for(String keyvalue:param){
            String[] pair = keyvalue.split("=");
            if(pair.length==2){
                map.put(pair[0], pair[1]);
            }else{
                map.put(pair[0], "");
            }
        }
        return map;
    }

}
