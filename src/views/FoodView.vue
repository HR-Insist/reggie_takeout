<template>
  <div class="dashboard-container" id="food-app">
    <div class="container">
      <div class="tableBar">
        <el-input
          v-model="input"
          placeholder="请输入菜品名称"
          style="width: 250px"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <i
            slot="prefix"
            class="el-input__icon el-icon-search"
            style="cursor: pointer"
            @click="initList"
          ></i>
        </el-input>
        <div class="tableLab">
          <span class="span-btn delBut non" @click="deleteHandle()"
            >批量删除</span
          >
          <span class="span-btn blueBug non" @click="statusHandleBatch(1)"
            >批量启售</span
          >
          <span
            style="border: none"
            class="span-btn delBut non"
            @click="statusHandleBatch(0)"
            >批量停售</span
          >
          <el-button type="primary" @click="addFoodtype()">
            + 新建菜品
          </el-button>
        </div>
      </div>
      <el-table
        ref="multipleTable"
        :data="tableData"
        stripe
        class="tableBox"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column prop="name" label="菜品名称"></el-table-column>
        <el-table-column prop="image" label="图片" align="center">
          <template slot-scope="{ row }">
            <el-image
              style="width: auto; height: 40px; border: none; cursor: pointer"
              :src="getImage(row.image)"
              :preview-src-list="[getImage(row.image)]"
            >
              <div slot="error" class="image-slot">
                <img
                  src="../assets/images/noImg.png"
                  style="width: auto; height: 40px; border: none"
                />
              </div>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="菜品分类"></el-table-column>
        <el-table-column label="售价">
          <template slot-scope="scope">
            <span style="margin-right: 10px"
              >￥{{ scope.row.price / 100 }}</span
            >
          </template>
        </el-table-column>
        <el-table-column label="售卖状态">
          <template slot-scope="scope">
            <span style="margin-right: 10px">{{
              scope.row.status == "0" ? "停售" : "启售"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="最后操作时间">
        </el-table-column>
        <el-table-column label="操作" width="160" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              class="blueBug"
              @click="editdFoodtype(scope.row.id)"
            >
              修改
            </el-button>
            <el-button
              type="text"
              size="small"
              class="blueBug"
              @click="statusHandle(scope.row.id, scope.row.status == 0 ? 1 : 0)"
            >
              {{ scope.row.status == 0 ? "启售" : "停售" }}
            </el-button>
            <el-button
              type="text"
              size="small"
              class="delBut non"
              @click="deleteHandle(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pageList"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="counts"
        @size-change="handleSizeChange"
        :current-page.sync="page"
        @current-change="handleCurrentChange"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import {
  getDishPage,
  deleteDish,
  dishStatusByStatus,
  // getDishImage,
} from "@/api/food";
import { downloadImageUrl } from "@/utils/common";
import { mapMutations } from "vuex";

export default {
  data() {
    return {
      input: "",
      counts: 0,
      page: 1,
      pageSize: 10,
      tableData: [],
      dishState: "",
      checkList: [],
    };
  },
  created() {
    this.initList();
  },
  methods: {
    ...mapMutations(["setDishId"]),
    initList() {
      const params = {
        page: this.page,
        pageSize: this.pageSize,
        name: this.input ? this.input : undefined,
      };
      getDishPage(params)
        .then((res) => {
          if (res.data.code === 1) {
            this.tableData = res.data.data.records || [];
            this.counts = res.data.data.total;
          }
        })
        .catch((err) => {
          this.$message.error("请求出错了：" + err);
        });
    },

    getImage(image) {
      return downloadImageUrl + "?name=" + image;
    },
    handleQuery() {
      this.page = 1;
      this.initList();
    },
    // 添加
    addFoodtype() {
      this.$router.push("/addfood");
    },
    //修改
    editdFoodtype(id) {
      this.$router.push({ path: `/editfood/${id}` });
    },

    // 删除
    async deleteHandle(id) {
      if (id == null) {
        if (this.checkList.length === 0) {
          return this.$message.error("请选择删除对象");
        }
      } else {
        this.toggleSelection();
        this.checkList = [];
        this.checkList.push(id);
      }
      await this.$messagebox
        .confirm("确认删除菜品, 是否继续?", "确定删除", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
        })
        .then(async () => {
          // console.log(this.checkList.join(","));
          await deleteDish(this.checkList.join(","))
            .then((res) => {
              if (res.data.code === 1) {
                this.$message.success("删除成功！");
                this.handleQuery();
              } else {
                this.$message.error(res.data.msg || "操作失败");
              }
            })
            .catch((err) => {
              this.$message.error("请求出错了：" + err);
            });
        })
        .catch(() => {});
    },
    //状态更改
    statusHandle(ids, status) {
      this.$messagebox
        .confirm("确认更改该菜品状态?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        .then(() => {
          // 起售停售---批量起售停售接口
          this.toggleSelection();
          dishStatusByStatus({
            ids: ids,
            status: status,
          })
            .then((res) => {
              if (res.data.code === 1) {
                this.$message.success("菜品状态已经更改成功！");
                this.handleQuery();
              } else {
                this.$message.error(res.data.msg || "操作失败");
              }
            })
            .catch((err) => {
              this.$message.error("请求出错了：" + err);
            });
        })
        .catch(() => {});
    },
    // 批量更改状态
    statusHandleBatch(status) {
      if (this.checkList.length === 0) {
        this.$message.error("批量操作，请先勾选操作菜品！");
        return false;
      }
      this.statusHandle(this.checkList.join(","), status);
    },
    // 全部操作
    handleSelectionChange(val) {
      let checkArr = [];
      val.forEach((n) => {
        checkArr.push(n.id);
      });
      this.checkList = checkArr;
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },

    handleSizeChange(val) {
      this.pageSize = val;
      this.initList();
    },
    handleCurrentChange(val) {
      this.page = val;
      this.initList();
    },
  },
};
</script>

<style lang="scss" scoped></style>
