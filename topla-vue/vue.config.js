module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],

  pwa: {
    // configure the workbox plugin
    // workboxPluginMode: 'InjectManifest',
    // workboxOptions: {
    //   swSrc: 'src/firebase-messaging-sw.js',
    //   // swDest: 'service-worker.js',
    // }
    name: "TOPLA",
    themeColor: "#F8BBD0",
    msTileColor: "#F8BBD0"
  },

  chainWebpack: config => {
    config
        .plugin('html')
        .tap(args => {
          args[0].title = 'TOPLA'
          return args
        })
  }
}