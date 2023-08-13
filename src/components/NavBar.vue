<template>
  <div class="navbar">
    <div class="head-lable">
      <span v-if="goBackFlag" class="goBack" @click="goBack()"
        ><img src="../assets/images/icons/btn_back@2x.png" alt="" /> 返回</span
      >
      <span>{{ headTitle }}</span>
    </div>
    <div class="right-menu">
      <div class="avatar-wrapper">{{ username }}</div>
      <img
        src="../assets/images/icons/btn_close@2x.png"
        class="outLogin"
        alt="退出"
        @click="dialogVisible = true"
      />
    </div>
    <el-dialog
      title="确认退出"
      :visible.sync="dialogVisible"
      width="300px"
      center
    >
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="logout">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { logoutApi } from "@/api/login";
import { getLoginUser } from "@/api/employee";
import Router from "@/router";
export default {
  name: "NavBar",
  data() {
    return {
      goBackFlag: false,
      dialogVisible: false,
      headTitle: "",
      username: "",
    };
  },
  mounted() {
    this.headTitle = this.$route.meta.title;
    getLoginUser().then((res) => {
      this.username = res.data.data;
    });
  },
  methods: {
    logout() {
      logoutApi().then((res) => {
        console.log(res);
        if (res.data.code === 1) {
          localStorage.removeItem("reggie_token");
          Router.replace("/login");
        }
      });
    },
    goBack() {
      this.$router.go(-1);
    },
  },
  watch: {
    $route(to) {
      this.headTitle = to.meta.title;
      this.goBackFlag = to.meta.isBack;
    },
  },
};
</script>

<style lang="scss" scoped></style>
