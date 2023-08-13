import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    user: "",
    navBarTitle: "",
    dishId: "",
  },
  getters: {
    getUser(state) {
      return state.user;
    },
  },
  mutations: {
    setUser(state, name) {
      state.user = name;
    },
    setTitle(state, title) {
      state.navBarTitle = title;
    },
    setDishId(state, id) {
      state.dishId = id;
    },
  },
  actions: {},
  modules: {},
});
