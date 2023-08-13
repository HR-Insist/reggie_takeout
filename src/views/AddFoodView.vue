<template>
  <div class="addBrand-container" id="food-add-app">
    <div class="container">
      <el-form
        ref="ruleForm"
        :model="ruleForm"
        :rules="rules"
        :inline="true"
        label-width="180px"
        class="demo-ruleForm"
      >
        <div>
          <el-form-item label="菜品名称:" prop="name">
            <el-input
              v-model="ruleForm.name"
              placeholder="请填写菜品名称"
              maxlength="20"
            />
          </el-form-item>
          <el-form-item label="菜品分类:" prop="categoryId">
            <el-select
              v-model="ruleForm.categoryId"
              placeholder="请选择菜品分类"
            >
              <el-option
                v-for="(item, index) in dishList"
                :key="index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="菜品价格:" prop="price">
            <el-input v-model="ruleForm.price" placeholder="请设置菜品价格" />
          </el-form-item>
        </div>
        <el-form-item label="口味做法配置:">
          <el-form-item>
            <div class="flavorBox">
              <span
                v-if="dishFlavors.length == 0"
                class="addBut"
                @click="addFlavore"
              >
                + 添加口味</span
              >
              <div v-if="dishFlavors.length != 0" class="flavor">
                <div class="title">
                  <span>口味名（3个字内）</span
                  ><span>口味标签（输入标签回车添加）</span>
                </div>
                <div class="cont">
                  <div
                    v-for="(item, index) in dishFlavors"
                    :key="index"
                    class="items"
                  >
                    <div class="itTit">
                      <div class="selectInput">
                        <div>
                          <el-input
                            v-model="item.name"
                            type="text"
                            style="width: 100%"
                            placeholder="请输入口味"
                            @focus="selectFlavor(true, index)"
                            @blur="outSelect(false, index)"
                            @input="inputHandle(index)"
                          />
                        </div>
                        <div v-show="item.showOption" class="flavorSelect">
                          <span
                            v-for="(it, ind) in dishFlavorsData"
                            :key="ind"
                            class="items"
                            @click="checkOption(it, ind, index)"
                            >{{ it.name }}</span
                          >
                          <span v-if="dishFlavorsData == []" class="none"
                            >无数据</span
                          >
                        </div>
                      </div>
                    </div>
                    <div class="labItems" style="display: flex">
                      <span v-for="(it, ind) in item.value" :key="ind"
                        >{{ it }}
                        <i @click="delFlavorLabel(index, ind)">X</i></span
                      >
                      <div
                        class="inputBox"
                        :style="inputStyle"
                        contenteditable="true"
                        @focus="flavorPosition(index)"
                        @keydown.enter="(val) => keyDownHandle(val, index)"
                      ></div>
                    </div>
                    <span class="delFlavor delBut non" @click="delFlavor(index)"
                      >删除</span
                    >
                  </div>
                </div>
                <div class="addBut" @click="addFlavore">添加口味</div>
              </div>
            </div>
          </el-form-item>
        </el-form-item>
        <div></div>
        <div>
          <el-form-item label="菜品图片:" prop="region" class="uploadImg">
            <el-upload
              class="avatar-uploader"
              :action="uploadImageUrl"
              :headers="uploadHeader"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeUpload"
              ref="upload"
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </div>
        <div class="address">
          <el-form-item label="菜品描述:" prop="region">
            <el-input
              v-model="ruleForm.description"
              type="textarea"
              :rows="3"
              placeholder="菜品描述，最长200字"
              maxlength="200"
            />
          </el-form-item>
        </div>
        <div class="subBox address">
          <el-form-item>
            <el-button @click="goBack()"> 取消 </el-button>
            <el-button type="primary" @click="submitForm('ruleForm')">
              保存
            </el-button>
            <el-button
              v-if="actionType == 'add'"
              type="primary"
              class="continue"
              @click="submitForm('ruleForm', 'goAnd')"
            >
              保存并继续添加菜品
            </el-button>
          </el-form-item>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { editDish, addDish, queryDishById } from "@/api/food";
import { getCategoryList } from "@/api/category";
import { getToken } from "@/utils/auth";
import { uploadImageUrl, downloadImageUrl } from "@/utils/common";
export default {
  data() {
    return {
      id: "",
      restKey: 0,
      textarea: "",
      value: "",
      imageUrl: "",
      actionType: "",
      dishList: [],
      dishFlavorsData: [],
      dishFlavors: [],
      vueRest: "1",
      index: 0,
      inputStyle: { flex: 1 },
      ruleForm: {
        name: "",
        id: "",
        price: "",
        code: "",
        image: "",
        description: "",
        dishFlavors: [],
        status: true,
        categoryId: "",
      },
      mak: false,
      uploadImageUrl: "",
      uploadHeader: {
        token: "",
      },
    };
  },
  computed: {
    rules() {
      return {
        name: [{ required: true, message: "请填写菜品名称", trigger: "blur" }],
        categoryId: [
          { required: true, message: "请选择菜品分类", trigger: "change" },
        ],
        price: [
          {
            required: true,
            // 'message': '请填写菜品价格',
            validator: (rules, value, callback) => {
              if (!value) {
                callback(new Error("请填写菜品价格"));
              } else {
                const reg = /^\d+(\.\d{0,2})?$/;
                if (reg.test(value)) {
                  if (value < 10000) {
                    callback();
                  } else {
                    callback(new Error("菜品价格小于10000"));
                  }
                } else {
                  callback(
                    new Error("菜品价格格式只能为数字,且最多保留两位小数")
                  );
                }
              }
            },
            trigger: "blur",
          },
        ],
      };
    },
  },
  created() {
    this.uploadImageUrl = uploadImageUrl;
    this.uploadHeader.token = getToken();

    this.getDishList(); // 获取菜品分类
    // 口味临时数据
    this.getFlavorListHand();
    this.id = this.requestUrlParam("id");
    this.actionType = this.id ? "edit" : "add";
    if (this.id) {
      this.getDishById();
    }
  },
  mounted() {},
  methods: {
    // 获取URL中的id
    requestUrlParam(id) {
      if (this.$route.params[id]) {
        console.log(this.$route.params[id]);
        return this.$route.params[id];
      }
    },
    // 通过ID获取dish信息
    getDishById() {
      queryDishById(this.id).then((res) => {
        if (res.data.code === 1) {
          this.ruleForm = { ...res.data.data };
          this.ruleForm.price = String(res.data.data.price / 100);
          this.ruleForm.status = res.data.data.status == "1";
          this.dishFlavors =
            res.data.data.flavors &&
            res.data.data.flavors.map((obj) => ({
              ...obj,
              value: JSON.parse(obj.value),
              showOption: false,
            }));
          // this.ruleForm.id = res.data.data.categoryId
          // this.imageUrl = res.data.data.image
          this.imageUrl = downloadImageUrl + "?name=" + res.data.data.image;
        } else {
          this.$message.error(res.data.msg || "操作失败");
        }
      });
    },
    // 按钮 - 添加口味
    addFlavore() {
      // JSON.parse(JSON.stringify(this.dishFlavorsData))
      this.dishFlavors.push({ name: "", value: [], showOption: false });
    },

    // 按钮 - 删除口味
    delFlavor(ind) {
      this.dishFlavors.splice(ind, 1);
    },

    // 按钮 - 删除口味标签
    delFlavorLabel(index, ind) {
      this.dishFlavors[index].value.splice(ind, 1);
    },

    //口味位置记录
    flavorPosition(index) {
      this.index = index;
    },

    // 添加口味标签
    keyDownHandle(val, index) {
      if (val.target.innerText.trim() != "") {
        this.dishFlavors[index].value.push(val.target.innerText);
        val.target.innerText = "";
      }
    },

    // 获取菜品分类
    getDishList() {
      getCategoryList({ type: 1 }).then((res) => {
        if (res.data.code === 1) {
          this.dishList = res.data.data;
        } else {
          this.$message.error(res.data.msg || "操作失败");
        }
      });
    },

    // 获取口味列表
    getFlavorListHand() {
      // flavor flavorData
      this.dishFlavorsData = [
        { name: "甜味", value: ["无糖", "少糖", "半糖", "多糖", "全糖"] },
        { name: "温度", value: ["热饮", "常温", "去冰", "少冰", "多冰"] },
        { name: "忌口", value: ["不要葱", "不要蒜", "不要香菜", "不要辣"] },
        { name: "辣度", value: ["不辣", "微辣", "中辣", "重辣"] },
      ];
    },

    selectFlavor(st, index) {
      const obj = JSON.parse(JSON.stringify(this.dishFlavors[index]));
      obj.showOption = st;
      this.dishFlavors.splice(index, 1, obj);
      // this.dishFlavors[index].showOption = st
    },

    outSelect(st, index) {
      const _this = this;
      setTimeout(() => {
        const obj = JSON.parse(JSON.stringify(_this.dishFlavors[index]));
        obj.showOption = st;
        _this.dishFlavors.splice(index, 1, obj);
      }, 200);
    },

    // inputHandle(val, index) {
    //   // this.selectFlavor(false,index)
    // },

    checkOption(val, ind, index) {
      this.selectHandle(val.name, index, ind);
      this.dishFlavors[index].name = val.name;
    },

    selectHandle(val, key, ind) {
      const arrDate = [...this.dishFlavors];
      arrDate[key] = JSON.parse(JSON.stringify(this.dishFlavorsData[ind]));
      this.dishFlavors = arrDate;
    },

    submitForm(formName, st) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let params = { ...this.ruleForm };
          // params.flavors = this.dishFlavors
          params.status = this.ruleForm ? 1 : 0;
          params.price *= 100;
          params.categoryId = this.ruleForm.categoryId;
          params.flavors = this.dishFlavors.map((obj) => ({
            ...obj,
            value: JSON.stringify(obj.value),
          }));
          delete params.dishFlavors;
          if (!this.imageUrl) {
            this.$message.error("请上传菜品图片");
            return;
          }
          if (this.actionType == "add") {
            delete params.id;
            addDish(params)
              .then((res) => {
                if (res.data.code === 1) {
                  this.$message.success("菜品添加成功！");
                  if (!st) {
                    this.goBack();
                  } else {
                    this.dishFlavors = [];
                    // this.dishFlavorsData = []
                    this.imageUrl = "";
                    this.ruleForm = {
                      name: "",
                      id: "",
                      price: "",
                      code: "",
                      image: "",
                      description: "",
                      dishFlavors: [],
                      status: true,
                      categoryId: "",
                    };
                  }
                } else {
                  this.$message.error(res.data.msg || "操作失败");
                }
              })
              .catch((err) => {
                this.$message.error("请求出错了：" + err);
              });
          } else {
            delete params.updateTime;
            editDish(params)
              .then((res) => {
                if (res.data.code === 1) {
                  this.$message.success("菜品修改成功！");
                  this.goBack();
                } else {
                  this.$message.error(res.data.msg || "操作失败");
                }
              })
              .catch((err) => {
                this.$message.error("请求出错了：" + err);
              });
          }
        } else {
          return false;
        }
      });
    },
    // 图片上传成功
    handleAvatarSuccess(response) {
      this.imageUrl = downloadImageUrl + "?name=" + response.data;
      this.ruleForm.image = response.data;
    },

    beforeUpload(file) {
      const suffix = file.name.split(".").pop();
      const size = file.size / 1024 / 1024 < 2;
      if (["png", "jpeg", "jpg"].indexOf(suffix) < 0) {
        this.$message.error("上传图片只支持 png、jpeg、jpg 格式！");
        this.$refs.upload.clearFiles();
        return false;
      }
      if (!size) {
        this.$message.error("上传文件大小不能超过 2MB!");
        return false;
      }
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style lang="scss" scoped></style>
