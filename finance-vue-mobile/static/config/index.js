/**
 * 开发环境
 */
;(function () {
  window.SITE_CONFIG = {};

  // api接口请求地址
  // window.SITE_CONFIG['baseUrl'] = 'http://localhost:8090/api'; //本地调试
  // window.SITE_CONFIG['baseUrl'] = 'http://120.27.18.138:8090/api';
  window.SITE_CONFIG['baseUrl'] = 'http://192.168.1.125:8090/api/'; //aws发布

  // cdn地址 = 域名 + 版本号
  window.SITE_CONFIG['domain'] = './'; // 域名
  window.SITE_CONFIG['version'] = ''; // 版本号(年月日时分)
  window.SITE_CONFIG['cdnUrl'] = window.SITE_CONFIG.domain + window.SITE_CONFIG.version;
})();
