<template>
  <div class="px-3  pt-3 back">
    <v-card>
      <v-card-title>전체 작업 목록</v-card-title>
      <div class="px-2">
        <div class="w-100 flex-right">
          <v-select class="w-50 flex-grow-0" v-model="taskPerPage" :items="taskPerPageList"></v-select>
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
            class="mb-3"
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

      callCount: 0,
      taskList: [],
      taskPerPageList: function(){
        let temp = [];
        for(let i = 5; i<=30; i+=5){
          temp.push({text: `${i}개 씩 보기`, value: i});
        }
        return temp;
      }()
    }
  },

  components: {
    taskCard
  },

  computed: {
    pageLength() {
      return Math.ceil(this.taskList.length / this.taskPerPage);
    },

    pagenationDisable(){
      if(this.pageLength <= 0){
        return true;
      }

      return false;
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
      let copyedTaskList = this.taskList.slice();
      return copyedTaskList.sort(function(task1, task2){
        let date1 = new Date(task1.dueDate);
        let date2 = new Date(task2.dueDate);
        return date1.getTime() - date2.getTime();
      })
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
</script>

<style scoped>
.w-50 {
  width: 50%;
}

.w-100 {
  width: 100%;
}

.flex-right {
  display: flex;
  flex-flow: row nowrap;
  justify-content: flex-end;
}
</style>