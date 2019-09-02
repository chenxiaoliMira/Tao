package caiji.finance.sinaBlogStock;

import Constants.ArticleCategory;
import caiji.db.Article;
import caiji.db.TargetGrabLink;
import caiji.util.LinkListGrabUtil;
import com.ituotu.tao_core.cache.TaoCache;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 采集单个股票新闻
 */
public class SingleSinaStockBlogCaijiTask {

    public static final String TITLE_REGEXP = "<h1 [\\S]+>([\\s\\S]+)</h1>";
    public static final String CONTENT_REGEXP = "<div class=\"articalContent\"[a-z0-9\"_= ]+>([\\s\\S]+)<b>免责声明：博主所发内容不构成买卖股票依据。股市有风险，入市需谨慎";
    public static final String THEME_IMG_REGEXP = "real_src =\"([\\S]+)\"";
//    public static final String IMG_PATH_REPLACE = "<img[\\s\\S]+?real_src=\"([\\s\\S]+?)\"[\\s\\S]+?>";
    public static final String IMG_PATH_REPLACE = "<img[0-9a-zA-Z\\.:/=_ \"]+?real_src=\"([\\s\\S]+?)\"[\\s\\S]+?>";

    public void start(final TargetGrabLink  targetGrabLink){
        TaoCache.caijiTaskThreadPool.execute(new Runnable() {
            public void run() {
                grabArticle(targetGrabLink);
            }
        });
    }

    public Article grabArticle(TargetGrabLink targetGrabLink){
        try {
            String html = LinkListGrabUtil.grabHTML(targetGrabLink.getFieldTargetLink());
            if (html == null){
                return null;
            }

            Article article = (Article) TaoCache.appContext.getBean("article");


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

                content = content.replaceAll(IMG_PATH_REPLACE, "<img src=\"$1\">");
                article.setFieldContent_medium(content);
            }else{
                throw new Exception("content not matched");
            }

            Pattern themeImagesPattern = Pattern.compile(THEME_IMG_REGEXP);
            matcher = themeImagesPattern.matcher(article.getFieldContent_medium());
            StringBuilder stringBuilder = new StringBuilder();
            while (matcher.find()) {
//                stringBuilder.append("http:"+matcher.group(1));
                stringBuilder.append(matcher.group(1));
                stringBuilder.append(",");
            }
            if (stringBuilder.length()>0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            article.setFieldThemeImages_medium(stringBuilder.toString());

            article.setFieldOverview("");
            article.setFieldValidType(7);
            article.setFieldReviewState(Article.REVIEW_STATE_UNREVIEWED);
            article.setFieldCategory(ArticleCategory.FINANCE_NEWS);
            article.setFieldSecondaryCategory(ArticleCategory.FINANCE_NEWS_STOCK_BLOG);
            article.setFieldCreateTime(new Date());
            article.setFieldSource(targetGrabLink.getFieldTargetLink());

            article.save();

            targetGrabLink.setFieldGrabState(1);
            targetGrabLink.updateNonNullValues();
            return article;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
