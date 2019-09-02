package com.ituotu.caiji_web_springboot.controller;

import Constants.ArticleCategory;
import caiji.db.Article;
import com.ituotu.caiji_web_springboot.common.R;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private Article article;
    private Pattern pattern = Pattern.compile("[0-9]*");

    @RequestMapping("/list")
    public R list(){
        article.setFieldCategory(ArticleCategory.FINANCE_NEWS);
        article.setFieldSecondaryCategory(ArticleCategory.FINANCE_NEWS_STOCK_NEWS);
        List articleList = article.findLatestNRecordWithSameCategoryForPageList(50,1);
        return R.ok().put("data",articleList);
    }

    @RequestMapping("/detail")
    public R detail(@RequestParam String articleId){
        article.setFieldCommonId(articleId);
//        article.setFieldCategory(ArticleCategory.FINANCE_NEWS);
//        article.setFieldSecondaryCategory(ArticleCategory.FINANCE_NEWS_STOCK_NEWS);
        Article articleResult = (Article) article.findObjectByCommonId(articleId);
        return R.ok().put("data",articleResult);
    }

    @RequestMapping(value = "/auditFinanceArticle", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public R auditFinanceArticle(@RequestBody String json){
//        article.setFieldCommonId(articleId);
//        Article articleResult = (Article) article.findObjectByCommonId(articleId);
//        return R.ok().put("data",articleResult);

        JSONArray jsonArray = JSONArray.fromObject(json);
        LinkedList<String> auditList = new LinkedList<>();
        for (int i=0;i<jsonArray.size();i++){
            auditList.add(jsonArray.getString(i));
        }

        Collections.sort(auditList,new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                String str1=(String) o1;
                String str2=(String) o2;
                if (str1.compareToIgnoreCase(str2)<0){
                    return -1;
                }
                return 1;
            }});

        String lastestCommonId = null;
        Article tempArticle = null;
        for(String s:auditList){
            String arr[] = s.split(":");
            if (lastestCommonId == null){
                tempArticle = (Article)this.article.findObjectByCommonId(arr[0]);
                lastestCommonId = arr[0];
            }

            if (!arr[0].equalsIgnoreCase(lastestCommonId)){
                tempArticle.setFieldReviewState(Article.REVIEW_STATE_PASSED);
                tempArticle.updateNonNullValues();
                lastestCommonId = arr[0];
                tempArticle = (Article)this.article.findObjectByCommonId(lastestCommonId);
                tempArticle = setArticleProperty(tempArticle, arr[1]);
            }else{
                tempArticle = setArticleProperty(tempArticle, arr[1]);
            }
        }
        return R.ok();
    }

    private Article setArticleProperty(Article article, String propertyValue){
        if (propertyValue.equalsIgnoreCase("all")) {
            article.setFieldArticleProperty_medium("bk:all");
        }else if(this.isNumeric(propertyValue)){
            article.setFieldValidType(Integer.parseInt(propertyValue));
        }else {
            if (article.getFieldArticleProperty_medium() == null || article.getFieldArticleProperty_medium().length() == 0){
                article.setFieldArticleProperty_medium("bk:"+propertyValue);
            }else{
                article.setFieldArticleProperty_medium(article.getFieldArticleProperty_medium()+"/"+propertyValue);
            }
        }

        return article;
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
