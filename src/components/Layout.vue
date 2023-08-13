<template>
  <el-container>
    <el-aside width="200px">
      <side-bar />
    </el-aside>
    <el-container>
      <el-header>
        <nav-bar />
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import SideBar from "./SideBar.vue";
import NavBar from "./NavBar.vue";
export default {
  name: "LayoutComponent",
  components: {
    SideBar,
    NavBar,
  },
};
</script>

<style lang="css" scoped></style>
