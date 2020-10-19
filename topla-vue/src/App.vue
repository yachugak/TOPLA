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
      <todo-view></todo-view>
    </v-main>
  </v-app>
</template>

<script>
import todoView from './views/todo.vue';

export default {
  name: 'App',

  components: {
    todoView,
  },

  data: () => ({
    //
  }),
};
</script>
