<template>
  <v-app>
    <v-app-bar
      app
      color="primary"
      dark
    >
      <div class="d-flex align-center">
        TOPLA
      </div>
    </v-app-bar>

    <v-main>
      <test-view></test-view>
    </v-main>
  </v-app>
</template>

<script>
import testView from './views/test.vue';

export default {
  name: 'App',

  components: {
    testView,
  },

  data: () => ({
    //
  }),
};
</script>
