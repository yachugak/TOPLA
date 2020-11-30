<template>
  <div>
    <v-card>
      <v-card-title>전체 작업 목록</v-card-title>
      <div>
        <task-card
            v-for="task in taskList"
            :key="task.uid"
        >
        </task-card>
        <v-pagination
            :length="pageLength"
            :disabled="pagenationDisable"
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
      taskPerPage: 1,
      nowPage: 1,

      callCount: 0,
      taskList: []
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

</style>