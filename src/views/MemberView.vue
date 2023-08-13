<template>
  <div class="dashboard-container" id="member-app">
    <div class="container">
      <div class="tableBar">
        <el-input
          v-model="input"
          placeholder="请输入员工姓名"
          style="width: 250px"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <i
            slot="prefix"
            class="el-input__icon el-icon-search"
            style="cursor: pointer"
            @click="handleQuery"
          ></i>
        </el-input>
        <el-button type="primary" @click="addMemberHandle(null)">
          + 添加员工
        </el-button>
      </div>
      <el-table :data="tableData" stripe class="tableBox">
        <el-table-column prop="name" label="员工姓名"></el-table-column>
        <el-table-column prop="username" label="账号"></el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column label="账号状态">
          <template slot-scope="scope">
            {{ String(scope.row.status) === "0" ? "已禁用" : "正常" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              class="blueBug"
              @click="addMemberHandle(scope.row)"
              :class="{ notAdmin: user !== 'admin' }"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              class="delBut non"
              @click="statusHandle(scope.row)"
              v-if="user === 'admin'"
            >
              {{ scope.row.status == "1" ? "禁用" : "启用" }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pageList"
        :page-sizes="[5, 10, 15, 20, 30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="counts"
        :current-page.sync="page"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      ></el-pagination>
    </div>
    <el-dialog
      title="添加员工"
      :visible.sync="dialogVisible"
      width="450px"
      center
    >
      <el-form
        ref="ruleForm"
        :model="ruleForm"
        :rules="rules"
        :inline="false"
        label-width="90px"
        class="demo-ruleForm"
      >
        <el-form-item label="账号:" prop="username">
          <el-input
            v-model="ruleForm.username"
            placeholder="请输入账号"
            maxlength="20"
          />
        </el-form-item>
        <el-form-item label="员工姓名:" prop="name">
          <el-input
            v-model="ruleForm.name"
            placeholder="请输入员工姓名"
            maxlength="20"
          />
        </el-form-item>

        <el-form-item label="手机号:" prop="phone">
          <el-input
            v-model="ruleForm.phone"
            placeholder="请输入手机号"
            maxlength="20"
          />
        </el-form-item>
        <el-form-item label="性别:" prop="sex">
          <el-radio-group v-model="ruleForm.sex">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="身份证号:" prop="idNumber">
          <el-input
            v-model="ruleForm.idNumber"
            placeholder="请输入身份证号"
            maxlength="20"
          />
        </el-form-item>
        <div class="subBox address">
          <el-form-item>
            <el-button @click="dialogVisible = false"> 取消 </el-button>
            <el-button type="primary" @click="submitForm('ruleForm', false)">
              保存
            </el-button>
            <el-button
              v-if="actionType == 'add'"
              type="primary"
              class="continue"
              @click="submitForm('ruleForm', true)"
            >
              保存并继续添加
            </el-button>
          </el-form-item>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {
  getMemberList,
  addEmployee,
  editEmployee,
  getLoginUser,
  enableOrDisableEmployee,
} from "@/api/employee";
import {
  checkUserName,
  checkName,
  checkPhone,
  validID,
} from "@/utils/validate";
export default {
  name: "MemberView",
  data() {
    return {
      input: "",
      counts: 0,
      page: 1,
      pageSize: 10,
      tableData: [],
      dialogVisible: false,
      actionType: "",
      ruleForm: {
        name: "贺锐",
        phone: "18284897975",
        sex: "男",
        idNumber: "511527199910144017",
        username: "HeRui",
        id: "",
      },
      user: "",
      id: "",
    };
  },
  computed: {
    rules() {
      return {
        //账号
        username: [
          { required: true, validator: checkUserName, trigger: "blur" },
        ],
        //姓名
        name: [{ required: true, validator: checkName, trigger: "blur" }],
        phone: [{ required: true, validator: checkPhone, trigger: "blur" }],
        idNumber: [{ required: true, validator: validID, trigger: "blur" }],
      };
    },
  },
  created() {
    getLoginUser().then((res) => {
      this.user = res.data.data;
    });
    this.init();
  },
  methods: {
    init() {
      getMemberList({
        page: this.page,
        pageSize: this.pageSize,
        name: this.input ? this.input : undefined,
      })
        .then((res) => {
          if (res.data.code === 1) {
            // console.log(res.data);
            this.tableData = res.data.data.records || [];
            this.counts = res.data.data.total;
          }
        })
        .catch((e) => {
          this.$message.error("请求出错了：" + e);
        });
    },
    handleQuery() {
      this.page = 1;
      this.init();
    },
    addMemberHandle(employee) {
      this.dialogVisible = true;
      if (!employee) {
        this.actionType = "add";
      } else {
        this.ruleForm.id = employee.id;
        this.ruleForm.name = employee.name;
        this.ruleForm.phone = employee.phone;
        this.ruleForm.sex = employee.sex === "1" ? "男" : "女";
        this.ruleForm.idNumber = employee.idNumber;
        this.ruleForm.username = employee.username;
      }
    },
    statusHandle(row) {
      this.$messagebox
        .confirm("确认调整该账号的状态?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        })
        .then(() => {
          enableOrDisableEmployee({
            id: row.id,
            status: !row.status ? 1 : 0,
          })
            .then((res) => {
              console.log("enableOrDisableEmployee", res);
              if (res.data.code === 1) {
                this.$message.success("账号状态更改成功！");
                this.init();
              }
            })
            .catch(() => {
              this.$message.error("账号状态更改失败!");
            });
        })
        .catch(() => {});
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.page = val;
      this.init();
    },
    submitForm(formName, st) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.actionType === "add") {
            const params = {
              ...this.ruleForm,
              sex: this.ruleForm.sex === "女" ? "0" : "1",
            };
            addEmployee(params)
              .then((res) => {
                // console.log(res);
                if (res.data.code === 1) {
                  this.$message.success("员工添加成功！");
                  if (!st) {
                    this.dialogVisible = false;
                    this.init();
                  }
                  this.ruleForm = {
                    username: "",
                    name: "",
                    phone: "",
                    sex: "男",
                    idNumber: "",
                  };
                } else {
                  this.$message.error(res.data.msg || "操作失败");
                }
              })
              .catch((err) => {
                this.$message.error("请求出错了：" + err);
              });
          } else {
            const params = {
              ...this.ruleForm,
              sex: this.ruleForm.sex === "女" ? "0" : "1",
            };
            // console.log(params);
            editEmployee(params)
              .then((res) => {
                if (res.data.code === 1) {
                  this.$message.success("员工信息修改成功！");
                  this.dialogVisible = false;
                  this.init();
                } else {
                  this.$message.error(res.data.msg || "操作失败");
                }
              })
              .catch((err) => {
                this.$message.error("请求出错了：" + err);
              });
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped></style>
