<template lang="html">
  <v-card>
    <v-card-title>작업 목록</v-card-title>
    <v-card v-for="task in taskList" :key="task.uid">
      <v-card-title>{{task.title}}</v-card-title>
      <v-card-text>마감일: {{task.dueDate}}</v-card-text>
      <v-card-text>중요도: {{task.priority}}</v-card-text>
    </v-card>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      taskList: []
    }
  },

  created() {
    this.getTaskList();
  },

  methods: {
    async getTaskList(){
      let res = await this.$axios.get("/task/list");
      this.taskList = res.data;
    }
  }
}
</script>

<style lang="css" scoped>
</style>
