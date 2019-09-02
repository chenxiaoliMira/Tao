<template>
  <div id="loadmore">
    <mt-header fixed title="实时快讯">
      <!--<router-link to="/tool" slot="left">-->
        <!--<mt-button icon="back">返回</mt-button>-->
      <!--</router-link>-->
    </mt-header>
    <div class="content">
      <mt-loadmore style="min-height: 100%;"
        :top-method="loadTop"
                   :bottom-all-loaded="bottomAllLoaded"
                   :auto-fill="false"
                   @bottom-status-change="handleBottomChange"
                   @top-status-change="handleTopChange"
                   :bottom-method="loadBottom" ref="loadmore">

        <div slot="top" class="mint-loadmore-top">
          <span v-show="topStatus === 'loading'">
            数据加载中<i class="fa fa-spinner fa-pulse"></i>
          </span>
          <span v-show="topStatus === 'drop'">我在加载数据</span>
          <span v-show="topStatus === 'pull'">下拉我就更新给你看</span>
        </div>
        <div slot="bottom" class="mint-loadmore-bottom" >
          <span v-show="bottomStatus === 'drop'">释放更新</span>
          <span v-show="bottomStatus === 'pull'">上拉加载更多</span>
          <span v-show="bottomStatus === 'loading'">
            数据加载中<i class="fa fa-spinner fa-pulse"></i>
          </span>
        </div>
        <mt-cell-swipe
          @click.native="clickMe($event, index)"
          v-for="(item,index) in list"
          :title=item.fieldTitle
          value=1
          :key="index">
        </mt-cell-swipe>
      </mt-loadmore>
    </div>
  </div>
</template>
<style lang="scss">
  .content {
    margin-top: 40px;
    height: auto;
    .mint-cell-wrapper{
      border-bottom: 1px solid #eaeaea;
    }
  }
</style>
<script>

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
      clickMe(event, index){
        // console.info('click me')
        // Toast.toString("11")


        // to="{name:'ArticleDetail',params:{articleId:item.fieldCommonId}}"
        console.info(index)
        console.info(this.list[index].fieldCommonId)
        // alert(index)
        // this.$router.push('/tool/ArticleDetail')

        this.$router.push({
          name: 'ArticleDetail',
          params: {
            articleId: this.list[index].fieldCommonId
          }
        })
      },
      loadTop(){
        let that = this;
        // for (let i = 0; i < 10; i++) {
        //   this.list.unshift('unshift' + i)
        // }
        setTimeout(function () {
          that.$refs.loadmore.onTopLoaded();
        }, 1000)
        this.getDataList()
      },
      loadBottom(){
        // for (let i = 0; i < 2; i++) {
        //   this.list.push('push' + i)
        // }
        // if (this.list.length > 100) {
        //   this.bottomAllLoaded = true;
        // }
        this.$refs.loadmore.onBottomLoaded();
      },
      handleTopChange(status){
        this.topStatus = status;
      },
      handleBottomChange(status) {
        this.bottomStatus = status;
      },
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/article/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize
          })
        }).then(({data}) => {
          this.$refs.loadmore.onTopLoaded();
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
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      }
    },
    mounted(){
      this.getDataList()
      // for (let i = 0; i < 100; i++) {
      //   this.list.push(i)
      // }
    },
    activated () {
      // this.getDataList()
    }
  }
</script>
