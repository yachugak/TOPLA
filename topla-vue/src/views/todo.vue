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

      <v-row>
        <v-col cols="12">
          <v-btn color="primary" block
                 @click="toggleTaskViewMode()"
          >
            작업을 {{taskViewMode === "dueDate" ? "마감일로" : "하는 날로"}} 보는 중
          </v-btn>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="12">
          <schedule-alert-box ref="alertBox"></schedule-alert-box>
        </v-col>
      </v-row>

      <v-row v-if="taskViewMode === 'doDate'">
        <v-progress-linear
            :buffer-value="(todayAllocationTime/todayPresetTime)*100"
            :value="(todayFinishTime/todayPresetTime)*100"
            stream height="10" color="info"></v-progress-linear>
        오늘 프리셋: {{todayPresetTime/60}}시간<br>
        할당 시간: {{todayAllocationTime/60}}시간<br>
        한 시간: {{todayFinishTime/60}}시간<br>
      </v-row>
    </v-container>

    <div class="py-4 secondary" :class="{taskContainerSizeSm: isSm, taskContainerSizeMd: !isSm }">
      <div class="mb-2">
        <span id="dayText" class="pl-2">{{selectedDate.getMonth()+1}}월 {{selectedDate.getDate()}}일 {{getDayName(selectedDate.getDay())}}요일</span>
        <span id="taskCountText" class="pr-2">{{displayTaskList.length}}개의 작업</span>
      </div>
      <task-card class="mx-2 mb-4" v-for="task in displayTaskList" :key="taskViewMode === 'dueDate' ? task.uid : task.planUid"
                 :title="task.title"
                 :priority="task.priority"
                 :uid="task.uid"
                 :progress="taskViewMode === 'dueDate' ? task.progress : task.planProgress"
                 :estimated-time="taskViewMode === 'dueDate' ? task.estimatedTime : task.doTime"
                 :due-date="task.dueDate"
                 :location="task.location"
                 :plan-uid="taskViewMode === 'dueDate' ? -1 : task.planUid"
                 @update="getTaskList()"
                 @click="onTaskClicked(task.uid)"
      ></task-card>
    </div>

    <v-btn
        color="primary"
        elevation="2"
        fab
        large
        id="addNewTaskbutton"
        @click="isShowNewTaskdialog = true; taskCreatedMode = true"
    ><v-icon>mdi-plus-circle-outline</v-icon></v-btn>

    <!--테스트 추가 창-->
    <v-dialog
        v-model="isShowNewTaskdialog"
        persistent
        max-width="500"
    >
      <v-card v-if="isShowNewTaskdialog">
        <v-card-title v-if="taskCreatedMode">새로운 작업 추가</v-card-title>
        <v-card-title v-else>작업 정보 보기 / 수정</v-card-title>
        <task-info-form
            v-model="newTaskFormData"
            ref="infoForm"
        ></task-info-form>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="error"
              @click="deleteRequest(updateTargetTask.uid)"
              :loading="isCalling>0"
          >
            <v-icon>mdi-trash-can-outline</v-icon>
          </v-btn>
          <v-btn
              color="secondary"
              @click="isShowNewTaskdialog = false"
              :loading="isCalling>0"
          >
            <v-icon>mdi-undo</v-icon>
          </v-btn>
          <v-btn
              color="primary"
              @click="onAddNewTaskButtonClicked()"
              :loading="isCalling>0"
          >
            <span v-if="taskCreatedMode">
              <v-icon>mdi-note-plus-outline</v-icon>
            </span>
            <span v-else>
              <v-icon>mdi-pencil-box-outline</v-icon>
            </span>
          </v-btn>
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
      taskCreatedMode: true,
      newTaskFormData: {
        title: "",
        dueDate: null,
        estimatedTime: 0,
        priority: 1,
        location: null
      },
      updateTargetTask: null,
      isCalling: 0, //현재 통신 진행중인지 나타내는 변수, 1 이상이면 통신 진행중이라는 뜻
      taskViewMode: "dueDate",//현재 작업의 보기 모드, dueDate와 doDate가 있음.
      schedulePreset: [0,0,0,0,0,0,0]
    }
  },

  components: {
    taskInfoForm,
    taskCard,
    scheduleAlertBox
  },

  watch: {
    isShowNewTaskdialog(newVal){
      if(newVal === false){
        this.formClear();
      }
    }
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
        location: this.newTaskFormData.location
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
        this.isCalling--
        this.isShowNewTaskdialog = false;
      }

      catch(e){
        console.error(e.response.data)
      }
    },

    toggleTaskViewMode(){
      if(this.taskViewMode === "dueDate"){
        this.taskViewMode = "doDate";
      }
      else if(this.taskViewMode === "doDate"){
        this.taskViewMode = "dueDate";
      }
      else{
        throw new Error(`알 수 없는 taskViewMode: ${this.taskViewMode}`);
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

    onTaskClicked(taskUid){
      let task = this.taskList.find(item => item.uid === taskUid);
      this.updateTargetTask = task;
      this.isShowNewTaskdialog = true;
      this.taskCreatedMode = false;
      this.newTaskFormData = {
        dueDate: task.dueDate,
        estimatedTime: task.estimatedTime,
        location: task.location,
        priority: task.priority,
        title: task.title
      }
    },

    formClear(){
      this.newTaskFormData = {
        dueDate: null,
        title: "",
        priority: 1,
        location: null,
        estimatedTime: 0
      }
    },

    async deleteRequest(){
      this.isCalling += 1;
      try {
        await this.$axios.delete(`/task/${this.updateTargetTask.uid}`);
        await this.getTaskList();
        this.isShowNewTaskdialog = false;
      }catch(e){
        alert(e.response.message);
      }
      finally {
        this.isCalling -= 1;
      }
    }
  },

  created() {
    this.getTaskList();
    this.getSchedulePreset();

    if(this.$route.params.date !==undefined && this.$route.params.viewMode!==undefined){
      this.selectedDate=new Date(this.$route.params.date)
      this.taskViewMode=this.$route.params.viewMode
    }
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
  position: fixed;
  bottom: 30px;
  right: 30px;
}
</style>
