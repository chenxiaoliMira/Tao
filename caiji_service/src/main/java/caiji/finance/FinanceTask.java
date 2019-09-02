package caiji.finance;

import caiji.finance.sinaBlogStock.StockBlogTask;
import caiji.finance.sinaStockNews.StockNewsTask;
import caiji.heibaimanhua.duanzi.DuanZiTask;

/**
 * 1.采集新浪财经-》股票咨讯版块
 */
public class FinanceTask {
    public static void start(){
        StockNewsTask.start();
        StockBlogTask.start();
    }
}
