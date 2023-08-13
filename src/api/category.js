import request from "@/utils/request";

// 查询列表接口
export function getCategoryPage(params) {
  return request({
    url: "/category/page",
    method: "get",
    params,
  });
}

// 根据type获取菜品分类列表
export const getCategoryList = (params) => {
  return request({
    url: "/category/list",
    method: "get",
    params,
  });
};

// 编辑页面反查详情接口
export function queryCategoryById(id) {
  return request({
    url: `/category/${id}`,
    method: "get",
  });
}

// 删除当前列的接口
export function deleCategory(id) {
  return request({
    url: "/category/" + id,
    method: "delete",
  });
}

// 修改接口
export function editCategory(params) {
  return request({
    url: "/category",
    method: "patch",
    data: params,
  });
}

// 新增接口
export function addCategory(params) {
  return request({
    url: "/category",
    method: "post",
    data: params,
  });
}
