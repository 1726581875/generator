 <template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 基础表格
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="el-icon-refresh-right" class="handle-del mr10"
                           :loading="isLoading" @click="reloadList">刷新</el-button>
                <el-button type="danger" icon="el-icon-delete" class="handle-del mr10" @click="deleteSelected"
                        :disabled="buttonStatus.deleteMultipleButtonDisabled">批量删除</el-button>

                <el-select v-model="selectValue" placeholder="查询条件" class="handle-select mr10">
                    <#list fieldList as field >
                        <#if field.javaType!='Date'>
                        <el-option key="${field_index}" label="${field.nameCn}" value="${field.nameHump}"></el-option>
                        </#if>
                    </#list>
                </el-select>
                <!--  查找输入框 -->
                <el-input v-model="inputVale" :placeholder="selectValue? '请输入' + nameMap[selectValue]  : '请输入查询内容'"
                        class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" :disabled="buttonStatus.searchButtonDisabled"
                        @click="handleSearch">搜索</el-button>

                <el-button type="primary" icon="el-icon-plus" class="handle-add mr10" @click="handleAdd">新增</el-button>
            </div>
            <!-- ============   表格table begin  =================-->
            <el-table :data="${entityNameLow}List" border class="table" ref="multipleTable" header-cell-class-name="table-header"
                    :default-sort="{prop: 'date', order: 'descending'}" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"/>
                <#list fieldList as field>
                    <#if field.keyType=='PRI'>
                        <el-table-column prop="${field.nameHump}" label="${field.nameCn}" width="55" align="center"/>
                    </#if>
                    <#if field.keyType!='PRI'>
                        <el-table-column prop="${field.nameHump}" label="${field.nameCn}"/>
                    </#if>
                </#list>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.<#noparse>$</#noparse>index, scope.row)">编辑</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.<#noparse>$</#noparse>index, scope.row.id)">删除
                        </el-button>
                    </template>
                </el-table-column>

            </el-table>
            <!-- =========表格table  end============ -->

            <!-- 分页组件 -->
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="queryParam.pageIndex"
                        :page-size="queryParam.pageSize" :page-count="pageCount" @current-change="handlePageChange">
                </el-pagination>
            </div>
        </div>

        <!-- 【修改/插入】 弹出框   -->
        <el-dialog :title="dialogTitle" :visible.sync="editVisible" width="40%">
            <el-form ref="${entityNameLow}" :model="${entityNameLow}" label-width="70px">
                <#list fieldList as field>
                    <#if field.keyType!='PRI' && field.nameHump != 'createTime' && field.nameHump != 'updateTime'>
                        <el-form-item label="${field.nameCn}">
                            <el-input v-model="${entityNameLow}.${field.nameHump}"></el-input>
                        </el-form-item>
                    </#if>
                </#list>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" :disabled="buttonStatus.saveButtonDisabled" @click="save">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
  export default {
    name: '${entityNameLow}',
    data() {
      return {
        /* 分页查询条件 */
        queryParam: {
        <#list fieldList as field>
        <#if field.javaType!='Date'>
        ${field.nameHump}: null,
        </#if>
        </#list>
          pageIndex: 1,
          pageSize: 10
        },
        //分页查询结果
        ${entityNameLow}List: [],
        pageCount: 1,
        //多选，选择的元素
        multipleSelection: [],
        /* 控制弹出框 */
        editVisible: false,
        dialogTitle: '',
        ${entityNameLow}: {},
        //刷新 转动
        isLoading: false,
        /*搜索框，选择框*/
        inputVale: '',
        selectValue: 'id',
        //下拉选择框映射
        nameMap: {
        <#list fieldList as field >
        ${field.nameHump}:'${field.nameCn}',
        </#list>
      },
      /* 控制按钮状态 */
      buttonStatus: {
        deleteMultipleButtonDisabled: false,
         searchButtonDisabled: false,
         saveButtonDisabled: false
      },

    };
    },
    created() {
      this.list();
    },
    methods: {

      //初始化查询条件
      resetQueryParam() {
        this.queryParam = {};
        this.queryParam.pageIndex = 1;
        this.queryParam.pageSize = 10;
      },
      /**
       * 刷新
       * 说明：点击刷新按钮触发
       * 1、图标转动isLoading = true
       * 2、${entityNameLow}List置空
       * 3、初始化查询条件
       * 4、延迟一秒，发ajax获取列表信息 （为了用户体验）
       */
      reloadList() {
        let _this = this;
        _this.isLoading = true;
        _this.${entityNameLow}List = [];
        _this.resetQueryParam();
        // 延迟一秒执行
        setTimeout(function () {
          _this.list();
          _this.isLoading = false;
        }, 1000)

      },
      /**
       * 分页获取数据
       * 1、根据目前的查询条件queryParam查询
       * 2、ajax请求分页接口获取数据
       */
      list() {
        this.<#noparse>$</#noparse>axios.get('http://localhost:9002/${entityNameLow}/list', {
          params: this.queryParam
        }).then(res => {
          let result = res.data;
          if (result.success) {
            this.${entityNameLow}List = result.data.content;
            this.pageCount = result.data.pageCount;
          }
        }).catch(err => {
          this.<#noparse>$</#noparse>message.error('发生系统内部错误');
          console.error("error = " + err)
        });
      },

      /**
       * 说明：点击查询按钮触发，输入框的查询
       * 1、防刷控制
       * 2、输入框参数校验
       * 3、初始化查询条件
       * 4、设置查询条件
       * 5、调用查询方法
       */
      handleSearch() {
        // 1、防刷，控制按钮一秒内只能点击一次
        this.buttonStatus.searchButtonDisabled = true;
        setTimeout(() => this.buttonStatus.searchButtonDisabled = false, 1000);

        // 2、输入框参数校验
        //获取输入的值，并去掉字符串两边空格
        let inputValue = this.inputVale.replace(/^\s+|\s+<#noparse>$</#noparse>/g, "");
        // 如果没有输入值，不需要发ajax
        if (!inputValue) {
          this.<#noparse>$</#noparse>message.warning("你还没有输入任何内容!");
          return;
        }

        // 自己定义，参数校验
        // ...
        this.resetQueryParam();
        this.<#noparse>$</#noparse>set(this.queryParam, this.selectValue, inputValue);
        this.list();
      },

      /**
       *  点击删除按钮触发
       */
      handleDelete(index, id) {
        // 弹框，二次确认删除
        this.<#noparse>$</#noparse>confirm('确定要删除吗？', '提示', {
          type: 'warning'
        }).then(() => { // 点击确认删除
          this.deleteById(id);
        }).catch(() => {// 点击取消
          console.log("已取消");
        });
      },

      /**
       * 根据id删除
       */
      deleteById(id){
          this.<#noparse>$</#noparse>axios.post("http://localhost:9002/${entityNameLow}/batch/delete", [id])
          .then(res => {
            res.data.success ? this.<#noparse>$</#noparse>message.success('删除成功') : this.<#noparse>$</#noparse>message.error('删除失败，请刷新后重新试试');
            this.list();
          }).catch(err => {
          this.<#noparse>$</#noparse>message.error('删除操作发生系统内部错误');
          console.error("error = " + err)
        })
      },

      /**
       *  多选时触发
       */
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },

      /**
       * 说明：点击批量删除按钮时触发
       *  批量删除,删除已经选择的
       * 1、获取到要删除的${entityNameLow}Id数组
       * 2、弹出框，提示信息，二次确认
       * 3、发ajax，批量删除
       */
      deleteSelected() {
        // 防刷
        this.buttonStatus.searchButtonDisabled = true;
        setTimeout(() => this.buttonStatus.searchButtonDisabled = false, 1000);
        //参数检测
        if (this.multipleSelection.length == 0) {
          this.<#noparse>$</#noparse>message.warning("你还没有选择任何元素！");
          return;
        }

        // 1、获取到要删除的${entityNameLow} id数组
        const ${entityNameLow}IdList = [];
        // 名字 list
        let del${entityName}Names = [];
        this.multipleSelection.forEach((${entityNameLow}, index) => {
          // 拿到要删除的${entityNameLow}Id
          ${entityNameLow}IdList.push(${entityNameLow}.id);
          // 取前五个名字
          if (index <= 5) {
            del${entityName}Names.push(${entityNameLow}.name);
          }
        });
        let delCount = ${entityNameLow}IdList.length;
        // 批量删除的名字拼接 String
        let delMsgStr = delCount <= 5 ? del${entityName}Names.toString() : del${entityName}Names.toString() + '...';

        //2、弹出框，提示信息,二次确认
        this.<#noparse>$</#noparse>confirm(`确定要批量删除 [<span style="color: red"><#noparse>$</#noparse>{delMsgStr}</span>] 一共
         <span style="color: red"><#noparse>$</#noparse>{${entityNameLow}IdList.length}</span> 列吗?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          dangerouslyUseHTMLString: true
        }).then(() => {//当点击确认

          //3、发送批量删除请求
          this.<#noparse>$</#noparse>axios.post("http://localhost:9002/${entityNameLow}/batch/delete", ${entityNameLow}IdList)
            .then(res => {
              res.data.success ? this.<#noparse>$</#noparse>message.success(`删除了 <#noparse>$</#noparse>{delMsgStr}`) : this.<#noparse>$</#noparse>message.error('批量删除失败，请刷新后重新试试');
              //延迟一秒执行
              setTimeout(() => this.list(), 1000);
            })
            .catch(err => {
              this.<#noparse>$</#noparse>message.error('批量删除操作发生错误');
              console.error("error = " + err)
            })

        }).catch(() => {//当点击取消
          console.log('批量删除已取消');
        });

        this.multipleSelection = [];
      },

      /**
       * 点击编辑按钮触发，展示编辑框
       */
      handleEdit(index, row) {
        this.dialogTitle = '修改';
        //为对象分配一个新地址，改变也不影响原来的值
        let new${entityName} = JSON.parse(JSON.stringify(row));
        this.${entityNameLow} = new${entityName};
        // 展示编辑框
        this.editVisible = true;
      },

      /**
       *  点击新增，展示新增框
       */
      handleAdd() {
        this.dialogTitle = '新增';
        this.${entityNameLow} = {};
        this.editVisible = true;
      },

      /**
       * 编辑框、新增框点击保存（新增/修改）
       * 1、防刷控制
       * 2、参数校验
       * 3、向后台发ajax请求
       * 4、重新查询数据(刷新)
       */
      save() {
        //1、防刷控制
        this.buttonStatus.saveButtonDisabled = true;
        setTimeout(() => this.buttonStatus.saveButtonDisabled = false, 1000);

        //2、参数校验
        // ...

        this.editVisible = false;
        //3、发请求
        this.<#noparse>$</#noparse>axios.post('http://localhost:9002/${entityNameLow}/save', this.${entityNameLow})
          .then(res => {
            if (res.data.success) {
              this.<#noparse>$</#noparse>message.success('保存成功');
              //4、重新加载数据
              this.list();
            } else {
              this.<#noparse>$</#noparse>message.success('保存失败，请重新试试');
            }
          }).catch(err => {
          this.<#noparse>$</#noparse>message.error('保存操作发生系统内部错误');
          console.error("error = " + err)
        })

      },
      // 分页导航
      handlePageChange(val) {
        console.log("val = " + val)
        this.<#noparse>$</#noparse>set(this.queryParam, 'pageIndex', val);
        this.list();
      }
    }
  };
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }

    .table {
        width: 100%;
        font-size: 14px;
    }

    .red {
        color: #ff0000;
    }

    .mr10 {
        margin-right: 10px;
    }

    .table-td-thumb {
        display: block;
        margin: auto;
        width: 40px;
        height: 40px;
    }

    .pagination {
        margin: 20px 0;
        text-align: right;
    }
</style>