import request from "@/utils/request";

// 获取员工列表
export function getMemberList(params) {
  return request({
    url: "/employee",
    method: "get",
    params,
  });
}

// 修改---启用禁用接口
export function enableOrDisableEmployee(params) {
  return request({
    url: "/employee",
    method: "patch",
    data: { ...params },
  });
}

// 新增---添加员工
export function addEmployee(params) {
  return request({
    url: "/employee",
    method: "post",
    data: params,
  });
}

// 修改---添加员工
export function editEmployee(params) {
  return request({
    url: "/employee",
    method: "patch",
    data: { ...params },
  });
}

// 修改页面反查详情接口
export function queryEmployeeById(id) {
  return request({
    url: `/employee/${id}`,
    method: "get",
  });
}

// 查询当前登录的员工
export function getLoginUser() {
  return request({
    url: "/employee/getLoginUser",
    method: "get",
  });
}
