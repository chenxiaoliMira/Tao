package nicknameLib;

import java.io.*;
import java.util.ArrayList;

public class NicknameUtil {

    private static ArrayList<String> nicknameLib;
    private static NicknameUtil nicknameUtil = new NicknameUtil();

    public static void main(String[] args) {
//        //单个scel文件转化
//        FileProcessing scel=new SougouScelFileProcessing();
////        scel.parseFile("/Users/zhengjunwang/Downloads/动漫名称大全.scel", "/Users/zhengjunwang/Downloads/动漫名称大全.txt", true);
//
//        //多个scel文件转化为一个txt (格式：拼音字母 词)
//        try {
//            scel.parseFiles("/Users/zhengjunwang/Downloads/words/", "/Users/zhengjunwang/Downloads/汇总.txt", false);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
////        //多个scel文件转化为多个txt文件
////        scel.setTargetDir("/Users/ST_iOS/Desktop/test/ciku/多对多");//转化后文件的存储位置
////        scel.parseFile("/Users/ST_iOS/Desktop/test/ciku",false);

        toArrayByFileReader1("/nicknameList.txt");

    }

    public static ArrayList<String> toArrayByFileReader1(String name) {
        // 使用ArrayList来存储每行读取到的字符串
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
//            FileReader fr = new FileReader(name);
//            FileReader fr = new FileReader(new File(new NicknameUtil().getClass().getResource("/nicknameList.txt").toURI()));

            InputStream is=nicknameUtil.getClass().getResourceAsStream("/nicknameList.txt");
            BufferedReader bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
//            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对ArrayList中存储的字符串进行处理
        return arrayList;
    }

    public static String randomANickName(){
        if (nicknameLib == null){
            nicknameLib = toArrayByFileReader1("/nicknameList.txt");
        }

        if (nicknameLib == null){
            return null;
        }

        int x=(int)(Math.random()*nicknameLib.size());
        x--;
        if (x<=0 && nicknameLib.size()<=0){
            return null;
        }

        return nicknameLib.get(x);
    }


}

