package caiji.finance.sinaStockNews;

import Constants.ArticleCategory;
import caiji.db.Article;
import caiji.db.TargetGrabLink;
import caiji.util.LinkListGrabUtil;
import caiji.util.SimHash;
import com.ituotu.tao_core.cache.TaoCache;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 采集单个股票新闻
 */
public class SingleSinaStockNewsCaijiTask {

    public static final String TITLE_REGEXP = "<h1 class=\"main-title\">([\\s\\S]+)</h1>";
    public static final String CONTENT_REGEXP = "<!-- 行情图end -->([\\s\\S]+)<!-- 编辑姓名及工作代码-->";
    public static final String THEME_IMG_REGEXP = "<img src=\"([\\S]+)\" ";

    public void start(final TargetGrabLink  targetGrabLink){
        TaoCache.caijiTaskThreadPool.execute(new Runnable() {
            public void run() {
                grabArticle(targetGrabLink);
            }
        });
    }

    public void grabArticle(TargetGrabLink targetGrabLink){
        try {
            String html = LinkListGrabUtil.grabHTML(targetGrabLink.getFieldTargetLink());
            if (html == null){
                return ;
            }

            Article article = (Article) TaoCache.appContext.getBean("article");
            article.setFieldCategory(ArticleCategory.FINANCE_NEWS);
            article.setFieldSecondaryCategory(ArticleCategory.FINANCE_NEWS_STOCK_NEWS);
            article.setFieldCreateTime(new Date());

            Pattern titlePattern = Pattern.compile(TITLE_REGEXP);
            Matcher matcher = titlePattern.matcher(html);
            if (matcher.find()) {
                article.setFieldTitle(matcher.group(1));
            }

            Pattern contentPattern = Pattern.compile(CONTENT_REGEXP);
            matcher = contentPattern.matcher(html);
            if (matcher.find()) {
                String content = matcher.group(1);
                content = content.replaceAll("&amp;","&");
                article.setFieldContent_medium(matcher.group(1));
            }else{
                throw new Exception("content not matched");
            }

            Pattern themeImagesPattern = Pattern.compile(THEME_IMG_REGEXP);
            matcher = themeImagesPattern.matcher(article.getFieldContent_medium());
            StringBuilder stringBuilder = new StringBuilder();
            while (matcher.find()) {
                stringBuilder.append("http:"+matcher.group(1));
                stringBuilder.append(",");
            }
            if (stringBuilder.length()>0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            article.setFieldThemeImages_medium(stringBuilder.toString());

            article.setFieldOverview("");
            article.setFieldValidType(1);
            article.setFieldReviewState(Article.REVIEW_STATE_UNREVIEWED);
            article.setFieldSource(targetGrabLink.getFieldTargetLink());

            article.save();

            targetGrabLink.setFieldGrabState(1);
            targetGrabLink.updateNonNullValues();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
