<template>
  <div class="sidebar-container">
    <div class="logo">
      <img
        src="../assets/images/login-logo.png"
        alt=""
        style="width: 117px; height: 32px"
      />
    </div>

    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="defAct"
        :unique-opened="false"
        :collapse-transition="false"
        background-color="#343744"
        text-color="#bfcbd9"
        active-text-color="#f4f4f5"
      >
        <div v-for="item in menuList" :key="item.id">
          <el-menu-item :index="item.id" @click="menuHandle(item)">
            <i class="iconfont" :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </el-menu-item>
        </div>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import Router from "@/router";
import { mapMutations } from "vuex";
export default {
  name: "SideBar",
  data() {
    return {
      defAct: "2",
      menuActived: "2",
      menuList: [
        {
          id: "2",
          name: "员工管理",
          url: "/member",
          icon: "icon-member",
        },
        {
          id: "3",
          name: "分类管理",
          url: "/category",
          icon: "icon-category",
        },
        {
          id: "4",
          name: "菜品管理",
          url: "/food/",
          icon: "icon-food",
        },
        {
          id: "5",
          name: "套餐管理",
          url: "/combo/",
          icon: "icon-combo",
        },
        {
          id: "6",
          name: "订单明细",
          url: "/order/",
          icon: "icon-order",
        },
      ],
    };
  },
  mounted() {
    Router.push("/member");
  },
  methods: {
    ...mapMutations(["setTitle"]),
    menuHandle(item) {
      this.loading = true;
      this.menuActived = item.id;
      // this.setTitle(item.name);
      Router.push(item.url);
    },
  },
};
</script>

<style lang="scss" scoped></style>
