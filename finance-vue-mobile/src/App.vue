<template>
  <div id="app">
    <!--<div class="content-div">-->
      <!--<router-view/>-->
    <!--</div>-->
    <!--<footer-bar></footer-bar>-->
    <article-audit></article-audit>
  </div>
</template>

<script>
import Footer from './components/FooterBar.vue'
import ArticleAudit from './pages/tool/ArticleAudit'
export default {
  name: 'app',
  components: {
    'footer-bar': Footer,
    'articleAudit': ArticleAudit
  },
  watch: {
    '$route': function(to, from) {
      let footerExcludes = ['main', 'tool', 'my', 'home']
      if (footerExcludes.indexOf(to.name) < 0) {
        this.$store.commit('SELECT_FOOTER_TAB')
      } else {
        this.$store.commit('SELECT_FOOTER_TAB', to.name)
      }
    }
  }
}
</script>

<style scoped>
.content-div {
  margin-bottom: 55px;
}
</style>
