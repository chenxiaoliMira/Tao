<template>
  <div>
    <!--<img src="../assets/logo.png">-->
    <h1>{{ msg }}</h1>
    <el-button @click.native="startHacking">Let's do it</el-button>
    <div class="block">
      <span class="demonstration">显示默认颜色</span>
      <span class="wrapper">
    <el-button type="success">成功按钮</el-button>
    <el-button type="warning">警告按钮</el-button>
    <el-button type="danger">危险按钮</el-button>
    <el-button type="info">信息按钮</el-button>
    </span>
    </div>
    <br/>
    <div class="block">
      <span class="demonstration">hover 显示颜色</span>
      <span class="wrapper">
    <el-button :plain="true" type="success">成功按钮</el-button>
    <el-button :plain="true" type="warning">警告按钮</el-button>
    <el-button :plain="true" type="danger">危险按钮</el-button>
    <el-button :plain="true" type="info">信息按钮</el-button>
    </span>
    </div>
  </div>
</template>
<script>
export default {
  name: 'ArticleList',
  data () {
    return {
      msg: 'Use Vue 2.0 Today!'
    }
  },

  methods: {
    startHacking () {
      this.$notify({
        title: 'It Works',
        message: 'We have laid the groundwork for you. Now it\'s your time to build something epic!',
        duration: 6000
      })
    }
  }
}
</script>

<style>
  body {
    font-family: Helvetica, sans-serif;
  }
</style>
