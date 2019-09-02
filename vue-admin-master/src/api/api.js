import axios from 'axios';

let base = '';
let base2 = 'http://192.168.1.125:8090/api';

export const requestLogin = params => { return axios.post(`${base}/login`, params).then(res => res.data); };

export const getUserList = params => { return axios.get(`${base}/user/list`, { params: params }); };

export const getUserListPage = params => { return axios.get(`${base}/user/listpage`, { params: params }); };

export const removeUser = params => { return axios.get(`${base}/user/remove`, { params: params }); };

export const batchRemoveUser = params => { return axios.get(`${base}/user/batchremove`, { params: params }); };

export const editUser = params => { return axios.get(`${base}/user/edit`, { params: params }); };

export const addUser = params => { return axios.get(`${base}/user/add`, { params: params }); };

export const getToAuditArticleListPage = params => { return axios.get(`${base2}/article/list`, { params: params })};

export const getStockBKList = params => { return axios.get(`${base2}/stockBK/list`, { params: params })};

export const auditFinanceArticle = params => { return axios.post(`${base2}/article/auditFinanceArticle`, params).then(res => res.data); };