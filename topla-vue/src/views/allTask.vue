<template>
  <div class="px-3  pt-3 back">
    <v-card>
      <v-card-title>전체 작업 목록</v-card-title>
      <div class="px-2">
        <div class="w-100 flexBox">
          <v-select class="flexItem" v-model="sortMethod" :items="sortMethodList"></v-select>
          <v-select class="flexItem" v-model="taskPerPage" :items="taskPerPageList"></v-select>
        </div>
        <div>
          <v-checkbox label="완료한 작업 안 보기" v-model="isHideDoneTask"></v-checkbox>
        </div>
        <task-card
            v-for="task in pageTaskList"
            :key="task.uid"
            :uid="task.uid"
            :title="task.title"
            :location="task.location"
            :priority="task.priority"
            :estimated-time="task.estimatedTime"
            :due-date="task.dueDate"
            :progress="task.progress"
            :reminding-time="task.remindingTiming"
            class="mb-3"
            @update="getTaskList()"
        >
        </task-card>
        <v-pagination
            :length="pageLength"
            :disabled="pagenationDisable"
            v-model="nowPage"
        >
        </v-pagination>
      </div>
    </v-card>
  </div>
</template>

<script>
import taskCard from "@/components/taskCard";
import errorDialog from "@/plugins/errorDialog";

export default {
  name: "allTask",

  data() {
    return {
      taskPerPage: 5,
      nowPage: 1,
      searchKeyword: "",


      callCount: 0,
      taskList: [],
      taskPerPageList: function(){
        let temp = [];
        for(let i = 5; i<=30; i+=5){
          temp.push({text: `${i}개 씩 보기`, value: i});
        }
        return temp;
      }(),
      sortMethodList: [
        {text: "마감일 가까운 순", value:"dueDateIn"},
        {text: "마감일 먼 순", value:"dueDateDe"},
        {text: "중요도 높은 순", value:"priorityDe"},
        {text: "중요도 낮은 순", value:"priorityIn"},
        {text: "예상 시간 긴 순", value:"estimatedTimeDe"},
        {text: "예상 시간 짧은 순", value:"estimatedTimeIn"},
      ],
      sortMethod: "dueDateIn",
      isHideDoneTask: true
    }
  },

  components: {
    taskCard
  },

  computed: {
    pageLength() {
      return Math.ceil(this.hidedTaskList.length / this.taskPerPage);
    },

    pagenationDisable(){
      if(this.pageLength <= 0){
        return true;
      }

      return false;
    },

    hidedTaskList(){
      if(this.isHideDoneTask === false){
        return this.taskList;
      }

      return this.taskList.filter(function(task){
          return task.finishDate === null || task.finishDate === undefined
      });
    },

    pageTaskList(){
      let resultList = [];

      let taskCount = this.sortedTaskList.length;

      let indexStart = this.taskPerPage*(this.nowPage-1);
      let indexEnd = this.taskPerPage*this.nowPage -1;


      for(let i = indexStart; i<=indexEnd; i++){
        if(i >= taskCount){
          break;
        }

        resultList.push(this.sortedTaskList[i]);
      }

      return resultList;
    },

    sortedTaskList(){
      let copyedTaskList = this.hidedTaskList.slice();
      return copyedTaskList.sort(this.sortFunction)
    },

    sortFunction(){
      switch (this.sortMethod) {
        case "dueDateIn":
          return dueDateSort(true);
        case "dueDateDe":
            return dueDateSort(false);

        case "priorityIn":
          return prioritySort(true);
        case "priorityDe":
          return prioritySort(false);

        case "estimatedTimeIn":
          return estimateTimeSort(true);
        case "estimatedTimeDe":
          return estimateTimeSort(false);
      }

      throw new Error(`알 수 없는 정렬 방법:" ${this.sortMethod}`);
    }
  },

  watch: {
    pageLength(newVal){
      if(this.nowPage > newVal){
        this.nowPage = newVal;
      }
    }
  },

  methods: {
    async getTaskList() {
      this.callCount++;
      try{
        let res = await this.$axios.get("/task/list");
        this.taskList = res.data;
      }
      catch(e){
        errorDialog(this, "작업 목록 받기 실패", e);
      }
      finally {
        this.callCount--;
      }
    }
  },

  created() {
    this.getTaskList();
  }
}

function dueDateSort(isIncrease){
    return function(task1, task2){
      let date1 = new Date(task1.dueDate);
      let date2 = new Date(task2.dueDate);
      if(isIncrease){
        return date1.getTime() - date2.getTime();
      }
      else{
        return date2.getTime() - date1.getTime();
      }
    }
}

function prioritySort(isIncrease){
    return function(task1, task2){
      let priority1 = task1.priority;
      let priority2 = task2.priority;
      let sub = priority1 - priority2;
      if(isIncrease){
        return sub;
      }
      return -sub;
    }
}

function estimateTimeSort(isIncrease){
  return function(task1, task2){
    let estimatedTime1 = task1.estimatedTime;
    let estimatedTime2 = task2.estimatedTime;
    let sub = estimatedTime1 - estimatedTime2;
    if(isIncrease){
      return sub;
    }
    return -sub;
  }
}
</script>

<style scoped>
.w-50 {
  width: 50%;
}

.w-100 {
  width: 100%;
}

.flexBox{
  display: flex;
  flex-flow: row nowrap;
  justify-content: space-around;
}

.flexItem {
  width: 45%;
  flex-grow: 0;
}
</style>