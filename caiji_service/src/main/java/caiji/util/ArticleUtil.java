package caiji.util;

import caiji.db.Article;
import caiji.db.Stocks;

import java.net.URLEncoder;

public class ArticleUtil {
    public static String getPostContentToXueQiu(Article article, Stocks stock){
        String postContent = String.format(" $%s(%s)$ %s",stock.getFieldStockName(),stock.getFieldStockCode(),article.getFieldContent_medium());
//        try {
//            postContent = URLEncoder.encode(postContent,"UTF-8");
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        return postContent;
    }
}
