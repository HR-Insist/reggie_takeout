const Layout = () => import("@/components/Layout");
const Member = () => import("@/views/MemberView");
const Login = () => import("@/views/LoginView");
const Category = () => import("@/views/CategoryView");
const Combo = () => import("@/views/ComboView");
const Food = () => import("@/views/FoodView");
const Order = () => import("@/views/OrderView");
const AddFood = () => import("@/views/AddFoodView");
const AddCombo = () => import("@/views/AddComboView");

export default {
  mode: "history",
  routes: [
    {
      path: "/login",
      component: Login,
    },
    {
      path: "/",
      name: "LayoutComponent",
      component: Layout,
      redirect: "/member",
      children: [
        {
          path: "member",
          name: "Member",
          component: Member,
          meta: {
            keepAlive: true,
            scrollTop: 0,
            title: "员工管理",
          },
        },
        {
          path: "category",
          name: "Category",
          component: Category,
          meta: {
            keepAlive: true,
            scrollTop: 0,
            title: "分类管理",
          },
        },
        {
          path: "food",
          name: "Food",
          component: Food,
          meta: {
            keepAlive: true,
            scrollTop: 0,
            title: "菜品管理",
          },
        },
        {
          path: "addfood",
          component: AddFood,
          meta: {
            keepAlive: true,
            scrollTop: 0,
            title: "新增菜品",
            isBack: true,
          },
        },
        {
          path: "editfood/:id",
          component: AddFood,
          meta: {
            keepAlive: true,
            scrollTop: 0,
            title: "编辑菜品",
            isBack: true,
          },
        },
        {
          path: "combo",
          name: "Combo",
          component: Combo,
          meta: {
            keepAlive: true,
            scrollTop: 0,
            title: "套餐管理",
          },
        },
        {
          path: "addcombo",
          component: AddCombo,
          meta: {
            keepAlive: true,
            scrollTop: 0,
            title: "新增套餐",
            isBack: true,
          },
        },
        {
          path: "editcombo/:id",
          component: AddCombo,
          meta: {
            keepAlive: true,
            scrollTop: 0,
            title: "编辑套餐",
            isBack: true,
          },
        },
        {
          path: "order",
          name: "Order",
          component: Order,
          meta: {
            keepAlive: true,
            scrollTop: 0,
            title: "订单明细",
          },
        },
      ],
    },
  ],
};
