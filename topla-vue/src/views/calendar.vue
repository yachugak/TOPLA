<template>
  <div>
    <v-sheet height="600" class="back">
      <v-row >
        <v-toolbar
            flat
            class="back"
        >
          <v-btn
              text
              small
              color="grey darken-2"
              @click="prev"
          >
            <v-icon small>
              mdi-chevron-left
            </v-icon>
          </v-btn>

          <v-menu
              v-model="menu2"
              :close-on-content-click="false"
              :nudge-right="40"
              transition="scale-transition"
              offset-y
              min-width="290px"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                  outlined
                  class="mr-4"
                  color="grey darken-2"
                  v-model="date"
                  label="Picker in menu"
                  v-bind="attrs"
                  v-on="on"
              >
                {{ date }}
              </v-btn>
            </template>
            <v-date-picker
                v-model="date"
                type="month"
                @input="setMonth"
                :locale="'ko'"
            ></v-date-picker>
          </v-menu>

          <v-btn
              text
              small
              color="grey darken-2"
              @click="next"
          >
            <v-icon small>
              mdi-chevron-right
            </v-icon>
          </v-btn>

          <v-spacer></v-spacer>
          <v-btn
              outlined
              class="mr-4"
              color="grey darken-2"
              @click="setToday"
          >
            Today
          </v-btn>

        </v-toolbar>
      </v-row>
      <v-row>
        <v-spacer></v-spacer>
        <v-btn color="primary"
               class="mb-1"
               @click="toggleTaskViewMode()"
        >
          작업을 {{ taskViewMode === "dueDate" ? "마감일로" : "하는 날로" }} 보는 중
        </v-btn>
        <v-spacer></v-spacer>
      </v-row>

      <v-calendar
          ref="calendar"
          v-model="value"
          color="primary"
          :weekdays="weekday"
          :type="type"
          :events="tasks"
          :locale="'ko'"
          :event-color="getEventColor"
          @change="getEvents"
          @click:date="viewDay"
          :day-format="dateFormat"
          :show-month-on-first="false"
          :dark="dark"
      ></v-calendar>
    </v-sheet>
  </div>
</template>

<script>
import errorDialog from "@/plugins/errorDialog";

export default {
  data: () => ({
    type: 'month',
    weekday: [0, 1, 2, 3, 4, 5, 6],
    value: '',
    tasks: [],
    dueTasks: [],
    doTasks: [],
    colors: ["#FFE082", "#FFA000", "#FF6F00",],
    taskViewMode: "dueDate",
    date: new Date().toISOString().substr(0, 7),
    menu2: false,
    more:true,
    theme:null,
    dark:false

  }),

  created(){
    this.theme=window.localStorage.getItem("theme")*1

    if(this.theme===1){
      this.dark=true
    }
    else{
      this.dark=false
    }

  },
  methods: {
    setToday() {
      this.value = ''
      this.date = new Date().toISOString().substr(0, 7)
    },
    async prev() {
      await this.$refs.calendar.prev()
      let monthDate=this.$refs.calendar.title
      if(monthDate[1]==="월"){
        this.date=monthDate.substr(3,7)+"-0"+monthDate[0]
      }
      else{
        this.date=monthDate.substr(4,8)+"-"+monthDate.substring(0,2)
      }
    },

    async next() {
      await this.$refs.calendar.next()
      let monthDate=this.$refs.calendar.title
      if(monthDate[1]==="월"){
        this.date=monthDate.substr(3,7)+"-0"+monthDate[0]
      }
      else{
        this.date=monthDate.substr(4,8)+"-"+monthDate.substring(0,2)
      }
    },

    async getEvents() {
      let dueTasks = []
      let doTasks = []
      let task
      try{
        task = await this.$axios.get("/task/list")
      }
      catch(e){
        errorDialog(this,"받아오기 실패",e)
      }

      console.log(task.data)
      for (let i = 0; i < task.data.length; i++) {
        dueTasks.push({
          name: task.data[i].title,
          start: new Date(`${task.data[i].dueDate}`),
          end: new Date(`${task.data[i].dueDate}`),
          color: this.colors[task.data[i].priority-1],
          timed: false,
        })
      }

      for (let i = 0; i < task.data.length; i++) {
        for (let j = 0; j < task.data[i].planList.length; j++) {
          doTasks.push({
            name: task.data[i].title,
            start: new Date(`${task.data[i].planList[j].doDate}`),
            end: new Date(`${task.data[i].planList[j].doDate}`),
            color: this.colors[task.data[i].priority-1],
            timed: false,
          })
        }
      }

      this.dueTasks = dueTasks
      this.doTasks = doTasks

      if (this.taskViewMode === "dueDate") {
        this.tasks = dueTasks
      } else if (this.taskViewMode === "doDate") {
        this.tasks = doTasks
      }

    },
    getEventColor(event) {
      return event.color
    },
    toggleTaskViewMode() {
      if (this.taskViewMode === "dueDate") {
        this.taskViewMode = "doDate";
        this.tasks = this.doTasks
      } else if (this.taskViewMode === "doDate") {
        this.taskViewMode = "dueDate";
        this.tasks = this.dueTasks
      } else {
        throw new Error(`알 수 없는 taskViewMode: ${this.taskViewMode}`);
      }
    },
    async viewDay() {
      await this.$router.push({
        name: 'todolist mode',
        params: {
          date: this.value,
          viewMode: this.taskViewMode
        }
      })
    },

    setMonth() {
      let now = this.date
      let preNow = this.$refs.calendar.title
      let nowNum = now.substring(0, 4) * 12 + (now.substring(5, 7) * 1)
      let preNowNum = 0
      if (preNow[1] === "월")
        preNowNum = preNow[0] * 1 + preNow.substring(3, 7) * 12
      else
        preNowNum = preNow.substring(0, 2) * 1 + preNow.substring(4, 8) * 12

      this.$refs.calendar.move(nowNum - preNowNum)
      this.menu2 = false
    },

    dateFormat(dateObject){
      return dateObject.day;
    },
  },
}
</script>

<style scoped>
</style>