<template lang="html">
  <div>
    <v-container fluid class="back">
      <v-row no-gutters>
        <v-col cols="12">
          <v-btn class="fullDateButton" color="primary" @click="onYearMonthButtonClicked()">
            {{selectedDate.getFullYear()}}년 {{selectedDate.getMonth()+1}}월
          </v-btn>
        </v-col>
        <v-col cols="1" v-if="$vuetify.breakpoint.mdAndUp">
          <v-btn class="arrowButton sec" color="primary" @click="onArrowButtonSelected('left')" tile block><v-icon>mdi-chevron-left</v-icon></v-btn>
        </v-col>
        <v-col cols="12" md="10">
          <div id="dateSelector">
            <v-btn class="dateButton primary"
                   :class="{'darken-1': i===3, '': i!==3, sunday: dateSelectorButtonDisplayList.text[(i-1)*2+1]==='일', saturday: dateSelectorButtonDisplayList.text[(i-1)*2+1]==='토' }"
                   v-for="i in 5" :key="i" @click="onDateSelectorButtonSelected(i-1)" tile>
              {{dateSelectorButtonDisplayList.text[(i-1)*2]}} <br>
              {{dateSelectorButtonDisplayList.text[(i-1)*2+1]}}
            </v-btn>
          </div>
        </v-col>
        <v-col cols="1" v-if="$vuetify.breakpoint.mdAndUp">
          <v-btn class="arrowButton primary" @click="onArrowButtonSelected('right')" tile block><v-icon>mdi-chevron-right</v-icon></v-btn>
        </v-col>
      </v-row>
      <v-row v-if="$vuetify.breakpoint.smAndDown" no-gutters>
        <v-col cols="6">
          <v-btn class="arrowButton primary" @click="onArrowButtonSelected('left')" tile block><v-icon>mdi-chevron-left</v-icon></v-btn>
        </v-col>
        <v-col cols="6">
          <v-btn class="arrowButton primary text--primary" @click="onArrowButtonSelected('right')" tile block><v-icon>mdi-chevron-right</v-icon></v-btn>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="12">
          <schedule-alert-box ref="alertBox"></schedule-alert-box>
        </v-col>
      </v-row>

      <v-row no-gutters>
        <v-col cols="12" md="4">
          <div class="flex-center">
            <v-select label="보기 기준" :items="taskViewModelSelectItem" v-model="taskViewMode"></v-select>
          </div>
        </v-col>
      </v-row>


      <v-row v-if="taskViewMode === 'doDate'" class="back">
        <v-progress-linear
            v-if="todayAllocationTime > 0"
            :value="(todayFinishTime/todayAllocationTime)*100"
            stream height="20" color="primary"
        >
          <template v-slot>
            {{((todayFinishTime/todayAllocationTime)*100).toFixed(0)}}%
          </template>
        </v-progress-linear>
        <v-progress-linear
            v-else
            :buffer-value="0"
            stream height="20" color="primary"
        >
          <template v-slot>
          </template>
        </v-progress-linear>
      </v-row>
    </v-container>

    <v-divider></v-divider>

    <div class="py-4 back" :class="{taskContainerSizeSm: isSm, taskContainerSizeMd: !isSm }">
      <task-card class="mx-2 mb-4" v-for="task in displayTaskList" :key="taskViewMode === 'dueDate' ? task.uid : task.planUid"
                 :title="task.title"
                 :priority="task.priority"
                 :uid="task.uid"
                 :progress="taskViewMode === 'dueDate' ? task.progress : task.planProgress"
                 :estimated-time="taskViewMode === 'dueDate' ? task.estimatedTime : task.doTime"
                 :due-date="task.dueDate"
                 :location="task.location"
                 :plan-uid="taskViewMode === 'dueDate' ? -1 : task.planUid"
                 :reminding-time="task.remindingTiming"
                 :full-task-info="findTask(task.uid)"
                 @update="getTaskList()"
      ></task-card>
    </div>

    <v-btn
        color="secondary"
        elevation="2"
        fab
        large
        id="addNewTaskbutton"
        @click="isShowNewTaskdialog = true; taskCreatedMode = true"
    ><v-icon>mdi-plus-circle-outline</v-icon></v-btn>

    <!--태스크 추가 창-->
    <v-dialog
        v-model="isShowNewTaskdialog"
        persistent
        max-width="500"
    >
      <v-card v-if="isShowNewTaskdialog">
        <v-card-title>새로운 작업 추가</v-card-title>
        <task-info-form
            v-model="newTaskFormData"
            ref="infoForm"
        ></task-info-form>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="secondary" @click="isShowNewTaskdialog = false" :loading="isCalling>0">뒤로</v-btn>
          <v-btn color="primary" @click="onAddNewTaskButtonClicked()" :loading="isCalling>0">추가</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import taskInfoForm from "@/components/taskInfoForm";
import taskCard from "@/components/taskCard";
import scheduleAlertBox from "@/components/scheduleAlertBox";

export default {
  data() {
    return {
      selectedDateButton: 3,
      selectedDate: new Date(),
      taskList: [],
      isShowNewTaskdialog: false,
      newTaskFormData: {
        title: "",
        dueDate: null,
        estimatedTime: 30,
        priority: 1,
        location: null,
        remindingTime: null
      },
      updateTargetTask: null,
      isCalling: 0, //현재 통신 진행중인지 나타내는 변수, 1 이상이면 통신 진행중이라는 뜻
      taskViewMode: "dueDate",//현재 작업의 보기 모드, dueDate와 doDate가 있음.
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
      schedulePreset: [0,0,0,0,0,0,0]
    }
  },

  components: {
    taskInfoForm,
    taskCard,
    scheduleAlertBox
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
      if(this.taskViewMode === "dueDate"){
        return this.getTaskListWithTodayIsDueDate(this.taskList, this.selectedDate);
      }
      else if(this.taskViewMode === "doDate"){
        return this.getTaskListWithTodayIsDoDate(this.taskList, this.selectedDate);
      }

      throw new Error(`알 수 없는 taskViewMode: ${this.taskViewMode}`);
    },

    todayPresetTime(){
      let day = this.selectedDate.getDay();
      let time = this.schedulePreset[day];
      return time
    },

    todayAllocationTime(){
      let timeSum = 0;
      for(let task of this.displayTaskList){
        timeSum += task.doTime
      }
      return timeSum;
    },

    todayFinishTime(){
      let timeSum = 0;
      let doneTaskList = this.displayTaskList.filter((t)=>t.planProgress===t.doTime);
      for(let task of doneTaskList) {
        timeSum += task.doTime;
      }
      return timeSum;
    }
  },

  watch: {
    isShowNewTaskdialog(newVal){
      if(newVal === true){
        this.clearForm();
      }
    }
  },

  methods: {
    async getTaskList() {
      this.isCalling++;
      let res = await this.$axios.get("/task/list");
      this.taskList = res.data;
      this.$refs.alertBox.getTotalLoss();
      this.isCalling--;
    },

    async getSchedulePreset(){
      this.isCalling++;
      let res = await this.$axios.get("/preset");
      this.schedulePreset = res.data.schedulePreset;
      this.isCalling--;
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

      let validationResultFlag = this.$refs.infoForm.formValue()
      if(validationResultFlag === false){
        return;
      }

      let requestBody = {
        title: this.newTaskFormData.title,
        priority: this.newTaskFormData.priority,
        dueDate: this.newTaskFormData.dueDate,
        estimatedTime: this.newTaskFormData.estimatedTime,
        location: this.newTaskFormData.location,
        remindingTiming: this.newTaskFormData.remindingTime
      }

      try{
        this.isCalling++;
        if(this.taskCreatedMode){
          await this.$axios.post("/task", requestBody);
        }
        else{
          await this.$axios.put(`/task/${this.updateTargetTask.uid}`, requestBody);
        }

        await this.getTaskList();
        this.isShowNewTaskdialog = false;
      }

      catch(e){
        this.$dialog.error({
          title: "등록 실패",
          text: e.response.data.message
        });

      }
      finally {
        this.isCalling--
      }
    },

    getTaskListWithTodayIsDueDate(taskList, today){
      let vueInstance = this;
      return taskList.filter(function(task){
        let taskDate = new Date(task.dueDate);
        return vueInstance.isSameDay(today, taskDate);
      })
    },

    getTaskListWithTodayIsDoDate(taskList, today){
      let todayIsDoDatePlan = [];

      let taskCount = taskList.length
      for(let i = 0; i<taskCount; i++){
        let planList = taskList[i].planList;
        for(let plan of planList){
          let planDoDate = new Date(plan.doDate);
          if(this.isSameDay(today, planDoDate)){
            todayIsDoDatePlan.push({
              index: i,
              doTime: plan.doTime,
              order: planList.indexOf(plan)+1,
              planUid: plan.planUid,
              planProgress: plan.progress
            });
          }
        }
      }

      let dispalyTaskList = [];
      for(let item of todayIsDoDatePlan){
        let tempTask = JSON.parse(JSON.stringify(taskList[item.index]));//객체 깊은 복사
        let totalPlanCount = tempTask.planList.length;
        if(totalPlanCount > 1){
          tempTask.title = `${tempTask.title} ( ${item.order} / ${totalPlanCount} )`;
        }
        tempTask.planProgress = item.planProgress;
        tempTask.doTime = item.doTime;
        tempTask.planUid = item.planUid;
        dispalyTaskList.push(tempTask);
      }

      return dispalyTaskList;
    },

    clearForm(){
      this.newTaskFormData = {
        title: "",
        dueDate: null,
        estimatedTime: 30,
        priority: 1,
        location: null,
        remindingTime: null
      }
    },

    onYearMonthButtonClicked(){
      this.$router.push({
        name: 'calendar mode',
        params: {
          date: this.selectedDate,
          viewMode: this.taskViewMode
        }
      })
    },

    findTask(taskUid){
      return this.taskList.find(function(task){
        return task.uid === taskUid;
      })
    },

    async showGuideBookMsg(){
      let res = undefined;

      if(this.$store.state.isSeenGuideBook === true){
        return;
      }

      while(res===undefined){
        res = await this.$dialog.info({
          title: "TOPLA에 오신 것을 환영합니다!",
          text: "TOPLA를 선택해 주셔서 감사합니다. 사용 전에 앞서 미리 안내서를 읽어볼 수도 있습니다. 안내서를 읽어 보시겠습니까?",
          actions: {
            false: {
              text: "아니오"
            },
            true: {
              text: "예"
            }
          }
        });

        if(res === true){
          this.$router.push("/guide");
          break;
        }
        else if(res === false){
          break;
        }
      }

      console.log("??");
      this.$store.commit("setGuideBookState", true);
      window.localStorage.setItem("isSeenGuideBook", true);
    }

  },

  created() {
    this.getTaskList();
    this.getSchedulePreset();

    if(this.$route.params.date !==undefined && this.$route.params.viewMode!==undefined){
      this.selectedDate=new Date(this.$route.params.date)
      this.taskViewMode=this.$route.params.viewMode
    }

    this.showGuideBookMsg();
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
}

#taskCountText {
  display: inline-block;
  width: calc((100vw - (100vw - 100%)) / 2);
  text-align: right;
}

.sunday {
  color: #EF9A9A !important;
}

.saturday{
  color: #90CAF9 !important;
}

#addNewTaskbutton {
  position: fixed;
  bottom: 30px;
  right: 30px;
}

.flex-center {
  display: flex;
  flex-flow: row nowrap;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.fullDateButton {
  width: 100%;
}
</style>
