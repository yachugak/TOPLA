<template lang="html">
  <div>
    <v-container fluid class="secondary">
      <v-row no-gutters>
        <v-col cols="1" v-if="$vuetify.breakpoint.mdAndUp">
          <v-btn class="arrowButton primary darken-1" @click="onArrowButtonSelected('left')" tile block><v-icon>mdi-chevron-left</v-icon></v-btn>
        </v-col>
        <v-col cols="12" md="10">
          <div id="dateSelector">
            <v-btn class="dateButton primary"
                   :class="{'darken-4': i===3, 'darken-1': i!==3, sunday: dateSelectorButtonDisplayList.text[(i-1)*2+1]==='일', saturday: dateSelectorButtonDisplayList.text[(i-1)*2+1]==='토' }"
                   v-for="i in 5" :key="i" @click="onDateSelectorButtonSelected(i-1)" tile>
              {{dateSelectorButtonDisplayList.text[(i-1)*2]}} <br>
              {{dateSelectorButtonDisplayList.text[(i-1)*2+1]}}
            </v-btn>
          </div>
        </v-col>
        <v-col cols="1" v-if="$vuetify.breakpoint.mdAndUp">
          <v-btn class="arrowButton primary darken-1" @click="onArrowButtonSelected('right')" tile block><v-icon>mdi-chevron-right</v-icon></v-btn>
        </v-col>
      </v-row>
      <v-row v-if="$vuetify.breakpoint.smAndDown" no-gutters>
        <v-col cols="6">
          <v-btn class="arrowButton primary darken-1" @click="onArrowButtonSelected('left')" tile block><v-icon>mdi-chevron-left</v-icon></v-btn>
        </v-col>
        <v-col cols="6">
          <v-btn class="arrowButton primary darken-1" @click="onArrowButtonSelected('right')" tile block><v-icon>mdi-chevron-right</v-icon></v-btn>
        </v-col>
      </v-row>
    </v-container>

    <div class="py-4 secondary" :class="{taskContainerSizeSm: isSm, taskContainerSizeMd: !isSm }">
      <div class="mb-2">
        <span id="dayText" class="pl-2">{{selectedDate.getMonth()+1}}월 {{selectedDate.getDate()}}일 {{getDayName(selectedDate.getDay())}}요일</span>
        <span id="taskCountText" class="pr-2">{{displayTaskList.length}}개의 작업</span>
      </div>
      <task-card class="mx-2 mb-4" v-for="task in displayTaskList" :key="task.uid"
                 :title="task.title"
                 :priority="task.priority"
                 :uid="task.uid"
                 :progress="task.progress"
                 @update="getTaskList()"
      ></task-card>
    </div>

    <v-btn
        color="primary"
        elevation="2"
        fab
        large
        id="addNewTaskbutton"
        @click="isShowNewTaskdialog = true"
    ><v-icon>mdi-plus-circle-outline</v-icon></v-btn>

    <!--테스트 추가 창-->
    <v-dialog
        v-model="isShowNewTaskdialog"
        persistent
        max-width="500"
    >
      <v-card>
        <v-card-title>새로운 작업 추가</v-card-title>
        <task-info-form v-model="newTaskFormData"></task-info-form>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="error"
              @click="isShowNewTaskdialog = false"
          >
            취소
          </v-btn>
          <v-btn
              color="primary"
              @click="onAddNewTaskButtonClicked()"
          >
            작업 추가
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import taskInfoForm from "@/components/taskInfoForm";
import taskCard from "@/components/taskCard";
export default {
  data() {
    return {
      selectedDateButton: 3,
      selectedDate: new Date(),
      taskList: [],
      isShowNewTaskdialog: false,
      newTaskFormData: null
    }
  },

  components: {
    taskInfoForm,
    taskCard
  },

  computed: {
    dateSelectorButtonDisplayList(){
      let today = this.selectedDate;
      let textList = [];
      let dateList = [];
      for(let i = -2; i <= 2; i++){
        let todayCopy = new Date(today);
        todayCopy.setDate(todayCopy.getDate()+i);
        textList.push(todayCopy.getDate());
        textList.push(this.getDayName(todayCopy.getDay()));
        dateList.push(todayCopy);
      }

      return {
        text: textList,
        date: dateList
      };
    },

    isSm(){
      return this.$vuetify.breakpoint.smAndDown;
    },

    displayTaskList(){
      let vueInstance = this;
      return this.taskList.filter(function(task){
        let taskDate = new Date(task.dueDate);
        return vueInstance.isSameDay(vueInstance.selectedDate, taskDate);
      })
    }
  },

  methods: {
    async getTaskList() {
      let res = await this.$axios.get("/task/list");
      this.taskList = res.data;
    },

    async onDateSelectorButtonSelected(selectedButtonIndex) {
      this.selectedDate = this.dateSelectorButtonDisplayList.date[selectedButtonIndex];
    },

    async onArrowButtonSelected(direction){
      let todayCopy = new Date(this.selectedDate);

      if(direction === "left"){
        todayCopy.setDate(todayCopy.getDate()-5);
      }
      else{
        todayCopy.setDate(todayCopy.getDate()+5);
      }

      this.selectedDate = todayCopy;
    },

    isSameDay(date1, date2) {
      let flag1 = date1.getFullYear() === date2.getFullYear();
      let flag2 = date1.getMonth() === date2.getMonth();
      let flag3 = date1.getDate() === date2.getDate();

      return flag1 && flag2 && flag3;
    },

    getDayName(dayNumber){
      let week = ['일', '월', '화', '수', '목', '금', '토'];
      return week[dayNumber];
    },

    async onAddNewTaskButtonClicked(){
      console.log("a");
      try{
        let res = await this.$axios.post("/task", {
          title: this.newTaskFormData.title,
          priority: 1,
          progress: 0,
          dueDate: this.newTaskFormData.dueDate,
          estimatedTime: 60
        });
        console.log("b");
        alert(res.data);
      }catch(e){
        console.log("c");
        console.log(e);
      }
    }
  },

  created() {
    this.getTaskList();
  }
}
</script>

<style lang="css" scoped>
.w-100 {
  width: 100%;
}

.dateButton {
  width: 20%;
}

.arrowButton {
  width: 10%;
}

.taskContainerSizeSm {
  min-height: calc(100vh - 56px - 96px);
}

.taskContainerSizeMd {
  min-height: calc(100vh - 64px - 60px);
}

#dayText {
  display: inline-block;
  width: calc((100vw - (100vw - 100%)) / 2);
  color: white;
}

#taskCountText {
  display: inline-block;
  width: calc((100vw - (100vw - 100%)) / 2);
  text-align: right;
  color: white;
}

.sunday {
  color: red !important;
}

.saturday{
  color: deepskyblue !important;
}

#addNewTaskbutton {
  position: absolute;
  bottom: 30px;
  right: 30px;
}
</style>
