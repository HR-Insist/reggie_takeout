import Vue from "vue";

import {
  Dialog,
  Form,
  FormItem,
  Container,
  Header,
  Main,
  Aside,
  Scrollbar,
  Menu,
  Submenu,
  MenuItem,
  Message,
  Input,
  Button,
  Table,
  TableColumn,
  Pagination,
  RadioGroup,
  Radio,
  MessageBox,
  Select,
  Option,
  Upload,
  Image,
  CheckboxGroup,
  Checkbox,
  CheckboxButton,
  InputNumber,
  Popover,
} from "element-ui";

Vue.use(Dialog);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Container);
Vue.use(Header);
Vue.use(Main);
Vue.use(Aside);
Vue.use(Scrollbar);
Vue.use(Menu);
Vue.use(Submenu);
Vue.use(MenuItem);
Vue.use(Input);
Vue.use(Button);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Pagination);
Vue.use(RadioGroup);
Vue.use(Radio);
Vue.use(Select);
Vue.use(Option);
Vue.use(Upload);
Vue.use(Image);
Vue.use(Checkbox);
Vue.use(CheckboxButton);
Vue.use(CheckboxGroup);
Vue.use(InputNumber);
Vue.use(Popover);

Vue.prototype.$message = Message;
Vue.prototype.$messagebox = MessageBox;
