package caiji.db;

import caiji.util.SimHash;
import com.ituotu.tao_core.db.BaseDBObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 文章
 */
@Repository
@Scope("prototype")
public class Article extends BaseDBObject {

    public static final int REVIEW_STATE_UNREVIEWED = 0;
    public static final int REVIEW_STATE_PASSED = 1;
    public static final int REVIEW_STATE_UNPASSED = -1;
    public static final int REVIEW_STATE_SECRET = 2;

    @Setter
    @Getter
    private String fieldTitle;
    //文章概述
    @Setter
    @Getter
    private String fieldOverview;
    //文章主题图片,多张用逗号分割
    @Setter
    @Getter
    private String fieldThemeImages_medium;
    //文章视频
    @Setter
    @Getter
    private String fieldVedio;
    @Setter
    @Getter
    private String fieldContent_medium;
    @Setter
    @Getter
    private String fieldAuthor;
    //时效类型： -1：有效期已过；1-当天有效、n-天有效
    @Setter
    @Getter
    private int fieldValidType;
    //最长句子1 simhash
    @Setter
    @Getter
    private String fieldContent1Simhash;
    //最长句子2 simhash
    @Setter
    @Getter
    private String fieldContent2Simhash;
    //最长句子3 simhash
    @Setter
    @Getter
    private String fieldContent3Simhash;
    //标题simhash
    @Setter
    @Getter
    private String fieldTitleSimhash;
    //文章其他类别
    @Setter
    @Getter
    private String fieldOtherCategory;
    //情感分析: 正面、负面
    @Setter
    @Getter
    private String fieldEmotion;
    //关键字
    @Setter
    @Getter
    private String fieldKeywords;
    //文章标签
    @Setter
    @Getter
    private String fieldTags;
    //文章扩展属性：
    //{"bk":xxx}
    @Setter
    @Getter
    private String fieldArticleProperty_medium;
    /**
     * 文章基础类别:
     *  搞笑：图文段子
     *  财经新闻：股票咨讯
     */
    @Setter
    @Getter
    private String fieldCategory;
    //文章二级基础类别，如新闻/体育新闻
    @Setter
    @Getter
    private String fieldSecondaryCategory;
    @Setter
    @Getter
    private String fieldSimhash;
    //审核状态：1：通过,-1：不通过,0:未审核
    @Setter
    @Getter
    private int fieldReviewState;
    //举报次数
    @Setter
    @Getter
    private int fieldTipOffCount;
    //可见状态：可见/隐藏/删除
    @Setter
    @Getter
    private int fieldVisibleState;
    //文章创建时间
    @Setter
    @Getter
    private Date fieldCreateTime;
    //文章更新时间
    @Setter
    @Getter
    private Date fieldUpdateTime;
    //访问次数
    @Setter
    @Getter
    private int fieldViewAccount;
    //评论数
    @Setter
    @Getter
    private int fieldCommentAccount;
    //收藏数
    @Setter
    @Getter
    private int fieldFavorAccount;
    //点赞数
    @Setter
    @Getter
    private int fieldThumbUpAccount;
    //点踩数
    @Setter
    @Getter
    private int fieldThumbDownAccount;
    //分享数
    @Setter
    @Getter
    private int fieldShareAccount;
    //后台推荐指数
    @Setter
    @Getter
    private int fieldRecommandIndex;
    //评论开关状态
    @Setter
    @Getter
    private int fieldCommentStatus;
    //文章来源
    @Setter
    @Getter
    private String fieldSource;

    /**
     * 获取最近添加的N条记录的simhash值, 用来判断文章是否重复
     * @param n
     * @return
     */
    public List<Article> findLatestNRecordWithSameCategory(int n){
        String condition = String.format("fieldCategory='%s' and fieldSecondaryCategory='%s'", fieldCategory,fieldSecondaryCategory);
        return dbObject.findTopNResult("fieldSimhash,fieldCommonId,fieldTitleSimhash,fieldContent1Simhash,fieldContent2Simhash,fieldContent3Simhash,fieldTitle, fieldSource ",n,"fieldCreateTime","desc",condition);
    }

    /**
     * 获取最近添加的N条记录的标题、图片、浏览数量等, 用来展示文章列表
     * @param pageNumber 第一页pageNumber为1
     * @return
     */
    public List<Article> findLatestNRecordWithSameCategoryForPageList(int pageSize, int pageNumber){
        String condition = String.format("fieldCategory='%s' and fieldSecondaryCategory='%s'", fieldCategory,fieldSecondaryCategory);
        return dbObject.findTopNResult("fieldCommonId,fieldTitle,fieldThemeImages_medium,fieldViewAccount,fieldCommentAccount,fieldThumbUpAccount",pageSize,"fieldCreateTime","desc",condition,(pageNumber-1)*pageSize);
    }

    public boolean isArticleExist(Article article){
        List<Article> articleList = article.findLatestNRecordWithSameCategory(500);

        SimHash titleSimHash = simhashStringToSimhashObject(this.fieldTitleSimhash);
        SimHash contentSimHash = simhashStringToSimhashObject(fieldSimhash);

        for (Article article1 : articleList){
            SimHash contentSimHash2 = simhashStringToSimhashObject(article1.getFieldSimhash());
            SimHash titleSimhash2 = simhashStringToSimhashObject(article1.getFieldTitleSimhash());
//            if (contentSimHash.hammingDistance(contentSimHash2)<6 || titleSimHash.hammingDistance(titleSimhash2)<3){
            if (titleSimHash.hammingDistance(titleSimhash2)<3){
                System.out.println("contentSimHash: "+contentSimHash.getStrSimHash().toString()+" contentSimHash2: "+contentSimHash2.getStrSimHash().toString());
                System.out.println("contentSimHash.hammingDistance(contentSimHash2): " + contentSimHash.hammingDistance(contentSimHash2));
                System.out.println("titleSimHash: "+titleSimHash.getStrSimHash().toString()+" titleSimHash2: "+titleSimhash2.getStrSimHash().toString());
                System.out.println("titleSimHash.hammingDistance(titleSimhash2): " + titleSimHash.hammingDistance(titleSimhash2));
                System.out.println(this.fieldTitle + " ==> " + article1.getFieldTitle());
                System.out.println(this.fieldSource);
                System.out.println(article1.getFieldSource());
                return true;
            }
        }
        return false;
    }

    /**
     * 根据内容字段，计算simhash字符串
     * @return
     */
    private String getSimhashString(String str){
        return new SimHash(str).getStrSimHash().toString();
    }

    /**
     * simhash字符串转simhash对象
     * @param simhashString
     * @return
     */
    private SimHash simhashStringToSimhashObject(String simhashString){
        return new SimHash(new BigInteger(simhashString));
    }

    @Override
    public void save() {

        this.fieldSimhash = getSimhashString(getFieldContent_medium());
        this.fieldTitleSimhash = getSimhashString(getFieldTitle());
//        this.fieldArticleProperty_medium;

        if (isArticleExist(this)){
            return;
        }
        super.save();
    }
}
