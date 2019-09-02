package com.ituotu.caiji_web_springboot.controller;

import Constants.ArticleCategory;
import caiji.db.Article;
import caiji.db.StocksBK;
import com.ituotu.caiji_web_springboot.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/stockBK")
@CrossOrigin
public class BKController {

    @Autowired
    private StocksBK stocksBK;

    @RequestMapping("/list")
    public R list(){
        List stockBKList = stocksBK.findLatestAllRecords(10000);
        return R.ok().put("data",stockBKList);
    }

}
