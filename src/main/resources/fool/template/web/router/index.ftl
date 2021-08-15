import Vue from 'vue'
import VueRouter from 'vue-router'
<#list tableList as table >
import ${table.tableNameBigHump} from '../views/${table.tableNameBigHump}'
</#list>
Vue.use(VueRouter)

const routes = [
<#list tableList as table>
  {
  path: '/${table.tableNameHump}',
  name: '${table.tableNameBigHump}',
  component: ${table.tableNameBigHump}
  },
</#list>
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
