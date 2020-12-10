<template>
  <div>
    <v-sheet height="600">
      <v-container>
        <v-row no-gutters>
          <v-col cols="12">
            <v-toolbar
                flat
            >
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

              <v-spacer></v-spacer>
              <v-btn
                  outlined
                  color="grey darken-2"
                  @click="setToday"
              >
                Today
              </v-btn>

            </v-toolbar>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12" class="px-6">
            <v-select label="보기 기준" :items="taskViewModelSelectItem" v-model="taskViewMode"></v-select>
          </v-col>
        </v-row>
      </v-container>
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
    colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'grey darken-1'],
    taskViewMode: "dueDate",
    date: new Date().toISOString().substr(0, 7),
    menu2: false,
    taskViewModelSelectItem: [
      {
        text: "마감일 기준으로 보기",
        value: "dueDate"
      },
      {
        text: "하는 날 기준으로 보기",
        value: "doDate"
      }
    ],
  }),
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

      for (let i = 0; i < task.data.length; i++) {
        dueTasks.push({
          name: task.data[i].title,
          start: new Date(`${task.data[i].dueDate}`),
          end: new Date(`${task.data[i].dueDate}`),
          color: this.colors[3],
          timed: false,
        })
      }

      for (let i = 0; i < task.data.length; i++) {
        for (let j = 0; j < task.data[i].planList.length; j++) {
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
    }
  },

  mounted() {
    if(this.$route.params.date !==undefined && this.$route.params.viewMode!==undefined){
      let targetDateObject = this.$route.params.date;
      this.date = `${targetDateObject.getFullYear()}-${targetDateObject.getMonth()+1}`;
      this.taskViewMode=this.$route.params.viewMode;
      this.setMonth();
    }
  }
}
</script>

<style scoped>
</style>