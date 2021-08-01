<template>
  <div id="app">

    <el-container>
      <el-header>
        <h1 style="text-align: center">Hello ~ 这是Head</h1>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-row class="tac">
            <el-col :span="24">
              <h5>默认颜色</h5>
              <el-menu
                      default-active="2"
                      class="el-menu-vertical-demo"
                      @open="handleOpen"
                      @close="handleClose">
                <el-submenu index="1">
                  <template slot="title">
                    <i class="el-icon-location"></i>
                    <span>系统管理</span>
                  </template>
                  <el-menu-item-group>
                  <#list tableList as table >
                    <el-menu-item index="1-${table_index + 1}"><router-link to="/${table.tableNameHump}">${table.tableNameBigHump}</router-link></el-menu-item>
                  </#list>
                  </el-menu-item-group>
                </el-submenu>
                <el-menu-item index="2">
                  <i class="el-icon-menu"></i>
                  <span slot="title">导航二</span>
                </el-menu-item>
                <el-menu-item index="3">
                  <i class="el-icon-setting"></i>
                  <span slot="title">导航三</span>
                </el-menu-item>

                <el-menu-item index="4">
                  <i class="el-icon-menu"></i>
                  <span slot="title">导航四</span>
                </el-menu-item>

                <el-menu-item index="5">
                  <i class="el-icon-menu"></i>
                  <span slot="title">导航五</span>
                </el-menu-item>

              </el-menu>
            </el-col>
          </el-row>
        </el-aside>
        <el-container>
          <el-main>
            <router-view/>
          </el-main>
          <el-footer>
            <h1 style="text-align: center">END ~ 这是Foot</h1>
          </el-footer>
        </el-container>
      </el-container>
    </el-container>

  </div>
</template>

<script>
  export default {
    methods: {
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      }
    }
  }
</script>

<style>
  #nav {
    padding: 30px;
  }

  #nav a {
    font-weight: bold;
    color: #2c3e50;
  }

  #nav a.router-link-exact-active {
    color: #42b983;
  }
</style>