const { defineConfig } = require("@vue/cli-service");
const port = 9528; // dev port

module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: "/",
  outputDir: "dist",
  assetsDir: "static",
  devServer: {
    port: port,
  },
  configureWebpack: {
    devtool: "source-map",
  },
});
