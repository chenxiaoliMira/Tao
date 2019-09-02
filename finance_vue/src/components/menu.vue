<template>
    <Menu mode="horizontal" :theme="theme1" active-name="3-4" @on-select="hs">
        <MenuItem name="1">
            <Icon type="ios-paper" />
            年度
        </MenuItem>
        <MenuItem name="2">
            <Icon type="ios-people" />
          我的
        </MenuItem>
    </Menu>
</template>
<script>
export default {
  data () {
    return {
      theme1: 'light'
    }
  },
  methods: {
    hs (n) {
      console.log(n)
    }
  }
}
</script>
