package caiji.stock.stockList;

import caiji.db.Stocks;
import caiji.util.LinkListGrabUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * 生产者
 * 每隔1小时抓取一次待采集链接
 * 作为单例模式，交由spring容器管理
 */
@Controller
public class CaijiStockList {

    //采集所有股票
    private static final String URL = "https://xueqiu.com/service/v5/stock/screener/quote/list?page=1&size=9000&order=desc&orderby=percent&order_by=percent&market=CN&type=sh_sz&_=1549867001889";

    @Autowired
    @Qualifier("stocks")
    Stocks stocks;

    public void start(){
        runAGrab();
    }

    public void runAGrab(){

        String stockListJsonString = LinkListGrabUtil.grabJsonString(URL);

        JSONObject jsonObject = JSONObject.fromObject(stockListJsonString);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray stockList = data.getJSONArray("list");

        if(stockList.size()>0) {
            for (int i = 0; i < stockList.size(); i++) {
                JSONObject job = stockList.getJSONObject(i);//把每一个对象转成json对象
                stocks.setFieldStockCode(job.getString("symbol"));
                stocks.setFieldStockName(job.getString("name"));
                stocks.save();
            }
        }
    }


}
