package xueqiuAPI.xueqiuWorkflow;

import Constants.HTTPConstants;
import caiji.db.Article;
import caiji.db.Stocks;
import caiji.db.XueqiuUsers;
import com.ituotu.tao_core.cache.TaoCache;
import xueqiuAPI.XueQiuAPI;

import java.util.List;

public class XueQiuPostArticle {
    public String postAnArticle(Article article){

        XueqiuUsers xueqiuUser = getRandomXueQiuUser();

        XueQiuAPI xueQiuAPI = new XueQiuAPI();

        Stocks stocks = getStockByArticle(article);

        xueQiuAPI.callPostToStock(xueqiuUser,article,stocks);

        return HTTPConstants.SUCCESS;
    }

    public XueqiuUsers getRandomXueQiuUser(){
        XueqiuUsers xueqiuUsers = (XueqiuUsers) TaoCache.appContext.getBean("xueqiuUsers");
        List<XueqiuUsers> xueqiuUsersList = xueqiuUsers.findLatestNRecord(10000);
        System.out.println(xueqiuUsersList.size());
        return null;
    }

    public Stocks getStockByArticle(Article article){
        return null;
    }
}
