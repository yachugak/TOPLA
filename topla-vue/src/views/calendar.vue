<template>
  <div>
    <v-sheet height="600">
      <v-toolbar
          flat
      >
        <v-btn
            outlined
            class="mr-4"
            color="grey darken-2"
            @click="setToday"
        >
          Today
        </v-btn>

        <v-btn
            fab
            text
            small
            color="grey darken-2"
            @click="prev"
        >
          <v-icon small>
            mdi-chevron-left
          </v-icon>
        </v-btn>

        <v-btn
            fab
            text
            small
            color="grey darken-2"
            @click="next"
        >
          <v-icon small>
            mdi-chevron-right
          </v-icon>
        </v-btn>

        <v-toolbar-title v-if="$refs.calendar">
          {{ $refs.calendar.title }}
        </v-toolbar-title>

      </v-toolbar>
      <v-btn color="primary" block
             @click="toggleTaskViewMode()"
      >
        작업을 {{taskViewMode === "dueDate" ? "마감일로" : "하는 날로"}} 보는 중
      </v-btn>

      <v-calendar
          ref="calendar"
          v-model="value"
          color="primary"
          :weekdays="weekday"
          :type="type"
          :events="tasks"
          :event-color="getEventColor"
          @change="getEvents"
          @click:date="viewDay"
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
    dueTasks:[],
    doTasks:[],
    colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'grey darken-1'],
    taskViewMode: "dueDate",
  }),
  methods: {
    setToday(){
      this.value=''
    },
    prev () {
      this.$refs.calendar.prev()
    },
    next () {
      this.$refs.calendar.next()
    },

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

      this.dueTasks = dueTasks
      this.doTasks=doTasks

      if(this.taskViewMode === "dueDate"){
        this.tasks=dueTasks
      }
      else if(this.taskViewMode === "doDate"){
        this.tasks=doTasks
      }

    },
    getEventColor (event) {
      return event.color
    },
    toggleTaskViewMode(){
      if(this.taskViewMode === "dueDate"){
        this.taskViewMode = "doDate";
        this.tasks=this.doTasks
      }
      else if(this.taskViewMode === "doDate"){
        this.taskViewMode = "dueDate";
        this.tasks=this.dueTasks
      }
      else{
        throw new Error(`알 수 없는 taskViewMode: ${this.taskViewMode}`);
      }
    },
    async viewDay(){
      await this.$router.push({
        name : 'todolist mode',
        params:{
          date : this.value,
          viewMode : this.taskViewMode
        }})
    },
  },
}
</script>

<style scoped>

</style>