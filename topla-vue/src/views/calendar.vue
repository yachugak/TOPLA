<template>
  <div>
    <v-sheet height="600">
      <v-btn color="primary" block
             @click="toggleTaskViewMode()"
      >
        작업을 {{taskViewMode === "dueDate" ? "마감일로" : "하는 날로"}} 보는 중
      </v-btn>

      <v-calendar
          ref="calendar"
          v-model="value"
          :weekdays="weekday"
          :type="type"
          :events="tasks"
          :event-overlap-threshold="30"
          :event-color="getEventColor"
          @change="getEvents"
      ></v-calendar>
    </v-sheet>
  </div>
</template>

<script>
export default {
  data: () => ({
    type: 'month',
    weekday: [0, 1, 2, 3, 4, 5, 6],
    value: '',
    tasks: [],
    duetasks:[],
    dotasks:[],
    colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'grey darken-1'],
    taskViewMode: "dueDate",
  }),
  methods: {
    async getEvents () {
      let dueTasks=[]
      let doTasks=[]
      let task = await this.$axios.get("/task/list")

      for (let i = 0;i<task.data.length;i++)
      {
        dueTasks.push({
          name: task.data[i].title,
          start: new Date(`${task.data[i].dueDate}`),
          end: new Date(`${task.data[i].dueDate}`),
          color: this.colors[3],
          timed: false,
        })
      }
      this.tasks=dueTasks
      this.duetasks = dueTasks

      for (let i = 0;i<task.data.length;i++)
      {
        for (let j=0;j<task.data[i].planList.length;j++)
        {
          doTasks.push({
            name: task.data[i].title,
            start: new Date(`${task.data[i].planList[j].doDate}`),
            end: new Date(`${task.data[i].planList[j].doDate}`),
            color: this.colors[3],
            timed: false,
          })
        }
      }

      this.dotasks=doTasks

    },
    getEventColor (event) {
      return event.color
    },
    async toggleTaskViewMode(){
      if(this.taskViewMode === "dueDate"){
        this.taskViewMode = "doDate";
        this.tasks=this.dotasks
      }
      else if(this.taskViewMode === "doDate"){
        this.taskViewMode = "dueDate";
        this.tasks=this.duetasks
      }
      else{
        throw new Error(`알 수 없는 taskViewMode: ${this.taskViewMode}`);
      }
    },
  },
}
</script>

<style scoped>

</style>