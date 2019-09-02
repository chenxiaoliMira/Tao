package caiji.stock.bkList;

import caiji.db.Stocks;
import caiji.db.StocksBK;
import caiji.util.LinkListGrabUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 生产者
 * 每隔1小时抓取一次待采集链接
 * 作为单例模式，交由spring容器管理
 */
@Controller
public class CaijiBKList {

    //采集所有板块
    private static final String URL = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?cb=jQuery112404571511928816555_1551083336133&type=CT&token=4f1862fc3b5e77c150a2b985b12db0fd&sty=FPGBKI&js=%28%7Bdata%3A%5B%28x%29%5D%2CrecordsFiltered%3A%28tot%29%7D%29&cmd=C._BKHY&st=%28ChangePercent%29&sr=-1&p=1&ps=100&_=1551083336134";

    //采集每个板块下的所有股票
    private static String URL_BK_STOCKLIST = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?cb=jQuery1124055283032345073_1551083275525&type=CT&token=4f1862fc3b5e77c150a2b985b12db0fd&sty=DCFFPBFMS&st=(BalFlowMain)&sr=-1&cmd=C.%s1&p=1&ps=200&_=1551083275583";

    @Autowired
    @Qualifier("stocks")
    Stocks stocks;

    @Autowired
    @Qualifier("stocksBK")
    StocksBK stocksBK;

    public void start(){
        runAGrab();
    }

    public void runAGrab(){

        String bkListJsonString = LinkListGrabUtil.grabJsonString(URL);
        bkListJsonString = bkListJsonString.substring("jQuery112404571511928816555_1551083336133\\(".length()-1, bkListJsonString.length()-1);

        JSONObject jsonObject = JSONObject.fromObject(bkListJsonString);
        JSONArray bkList = jsonObject.getJSONArray("data");

        List<Stocks> stocksList = stocks.findLatestNRecord(4000);

        if(bkList.size()>0) {
            for (int i = 0; i < bkList.size(); i++) {
                String bkInfo = bkList.getString(i);
                System.out.println(bkInfo);
//                setStockBK(bkInfo.split(",")[1], bkInfo.split(",")[2], stocksList);

                stocksBK.setFieldStockBKCode(bkInfo.split(",")[1]);
                stocksBK.setFieldStockBKName(bkInfo.split(",")[2]);
                stocksBK.setFieldStockBKType(StocksBK.STOCK_BK_TYPE_HY);
                stocksBK.save();
            }
        }
    }

    public void setStockBK(String bkCode, String bkName, List<Stocks> stocksList){
        String stockListJsonString = LinkListGrabUtil.grabJsonString(String.format(URL_BK_STOCKLIST, bkCode));
        stockListJsonString = stockListJsonString.substring("jQuery1124055283032345073_1551083275525\\(".length()-1, stockListJsonString.length()-1);

        //该板块下的所有股票
        JSONArray jsonArray = JSONArray.fromObject(stockListJsonString);

        if(jsonArray.size()>0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                String stockInfo = jsonArray.getString(i);
//                System.out.println(stockInfo);
                for (Stocks stock : stocksList){
                    if (stock.getFieldStockCode().contains(stockInfo.split(",")[1])){
//                        if (stock.getFieldBKCodes() == null || stock.getFieldBKCodes().equalsIgnoreCase("")){
                            stock.setFieldBKNames(bkName);
                            stock.setFieldBKCodes(bkCode);
//                        }else{
//                            stock.setFieldBKNames(stock.getFieldBKNames()+","+bkName);
//                            stock.setFieldBKCodes(stock.getFieldBKCodes()+","+bkCode);
//                        }
                        stock.updateNonNullValues();
                    }
                }
            }
        }
    }

}
