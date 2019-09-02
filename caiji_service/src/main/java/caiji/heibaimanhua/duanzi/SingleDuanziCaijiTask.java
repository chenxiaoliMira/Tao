package caiji.heibaimanhua.duanzi;

import Constants.ArticleCategory;
import caiji.util.LinkListGrabUtil;
import caiji.util.SimHash;
import com.ituotu.tao_core.cache.TaoCache;
import caiji.db.Article;
import caiji.db.TargetGrabLink;
import org.jsoup.Jsoup;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 采集单个段子
 */
public class SingleDuanziCaijiTask {

    public static final String TITLE_REGEXP = "<h1 class=\"title\">([\\s\\S]+)</h1>";
    public static final String CONTENT_REGEXP = "<div class=\"post-images-item\">([\\s\\S]+)<div class=\"next-prev-posts clearfix\">";
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
            article.setFieldCategory(ArticleCategory.GAOXIAO);
            article.setFieldSecondaryCategory(ArticleCategory.GAOXIAO_PIC_WORD);
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
                stringBuilder.append(matcher.group(1));
                stringBuilder.append(",");
            }
            if (stringBuilder.length()>0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            article.setFieldThemeImages_medium(stringBuilder.toString());

            article.setFieldOverview("");
            article.setFieldValidType(100);
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
