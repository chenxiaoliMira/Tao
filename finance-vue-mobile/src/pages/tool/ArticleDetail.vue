<template>
  <div id="loadmore">
    <mt-header fixed :title="list.fieldTitle">
      <router-link to="/tool/loadmore" slot="left">
        <mt-button icon="back">返回</mt-button>
      </router-link>
    </mt-header>
    <div id="content" class="content" v-html="list.fieldContent_medium">
      {{list.fieldContent_medium}}
    </div>
  </div>
</template>
<style lang="scss">
  .content {
    margin-top: 40px;
    margin-left: 20px;
    margin-right: 20px;
    height: auto;
    overflow:hidden;
    .mint-cell-wrapper{
      border-bottom: 1px solid #eaeaea;
    }
  }
  .content img{
    max-width: 100%;
  }
</style>
<script>
  import {Toast} from 'mint-ui'

  export default {
    data(){
      return {
        bottomAllLoaded: false,
        topStatus: '',
        bottomStatus:'',
        list: []
      }
    },
    methods: {
      release(val){
        console.info('release:' + val)
      },
      delete1(val){
        console.info('delete:' + val)
      },
      clickMe(){
        console.info('click me')
        Toast.toString("11")
      },
      loadTop(){
        let that = this;
        for (let i = 0; i < 10; i++) {
          this.list.unshift('unshift' + i)
        }
        setTimeout(function () {
          that.$refs.loadmore.onTopLoaded();
        }, 1000)
      },
      loadBottom(){
        for (let i = 0; i < 2; i++) {
          this.list.push('push' + i)
        }
        if (this.list.length > 100) {
          this.bottomAllLoaded = true;
        }
        this.$refs.loadmore.onBottomLoaded();
      },
      handleTopChange(status){
        this.topStatus = status;
      },
      handleBottomChange(status) {
        this.bottomStatus = status;
      },
      // 获取数据列表
      getArticle () {
        this.dataListLoading = true
        var articleIdParam = this.$route.params.articleId
        if (articleIdParam === undefined){
          articleIdParam = this.$route.query.articleId
          // articleIdParam = getParam("articleId")
        }
        this.$http({
          url: this.$http.adornUrl('/article/detail'),
          method: 'get',
          params: this.$http.adornParams({
            'articleId': articleIdParam,
            'limit': this.pageSize
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.list = data.data
          } else {
            this.list = []
          }
          this.dataListLoading = false
        })
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getArticle()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getArticle()
      }
    },
    mounted(){
      this.getArticle()
      // for (let i = 0; i < 100; i++) {
      //   this.list.push(i)
      // }
    },
    activated () {
      // this.getArticle()
    }
  }
</script>
