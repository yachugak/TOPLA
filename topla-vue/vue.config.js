module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],

  pwa: {
    // configure the workbox plugin
    workboxPluginMode: 'InjectManifest',
    workboxOptions: {
      swSrc: 'src/sw.js',
      swDest: 'service-worker.js',
    }
  }
}