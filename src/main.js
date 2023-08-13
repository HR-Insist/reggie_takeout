import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

import "@/utils/elementUI";
import "./assets/style/index.css";
import "./assets/style/common.css";
import "./assets/style/icon/iconfont.css";
import "./assets/style/page.css";
import "./assets/style/login.css";

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
