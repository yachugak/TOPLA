<template>
  <v-card :color="bgColorByPriority[priority-1]">
    <v-card-title>{{title}}</v-card-title>
    <v-card-text>
      <v-icon>mdi-clock-check-outline</v-icon>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: "taskCard",
  data() {
    return {
      bgColorByPriority: [
        "amber lighten-3",
        "amber darken-2",
        "amber darken-4",
      ],
    }
  },
  
  props: {
    title: {
      type: String,
      default: "제목 없는 작업"
    },

    priority: {
      type: Number,
      default: 1
    }
  }
}
</script>

<style scoped>

</style>