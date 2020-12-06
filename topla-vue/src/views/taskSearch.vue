<template>
  <div class="back pt-3 px-2 fullHeight">
    <v-card class="mb-3">
      <v-card-title>작업 검색기</v-card-title>
      <v-card-subtitle>이름으로 작업을 검색할 수 있습니다.</v-card-subtitle>
      <div class="px-3">
        <v-form ref="form">
          <v-text-field label="작업 이름" v-model="searchKeyword" hint="공백으로 and, |로 or"
                        :rules="rules"
          ></v-text-field>
        </v-form>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="onSearchButtonClicked()">검색</v-btn>
        </v-card-actions>
      </div>
    </v-card>
    <v-card class="pb-2">
      <v-card-title>검색 결과</v-card-title>
      <v-card-subtitle>총 {{searchResultCount}}개의 작업이 검색되었습니다.</v-card-subtitle>
      <div class="px-2">
        <task-card
            class="mb-2"
            v-for="task in searchedTaskList"
            :key="task.uid"
            :title="task.title"
            :priority="task.priority"
            :progress="task.progress"
            :estimated-time="task.estimatedTime"
            :location="task.location"
            @click="onTaskCardClicked(task.dueDate)"
            :due-date="task.dueDate"
            :reminding-time="task.remindingTiming"
            :uid="task.uid"
            @update="getTaskList()"
        >
        </task-card>
      </div>
    </v-card>
  </div>
</template>

<script>
import matcher from "keywords-match";
import taskCard from "@/components/taskCard";
import vuetifyJetValidator from "vuetify-jet-validator"

export default {
  name: "taskSearch",

  components: {
    taskCard
  },

  data() {
    const validator = new vuetifyJetValidator();
    return {
      taskList: [],
      callCount: 0,

      searchKeyword: "",
      confirmSearchKeyword: "",

      rules: [validator.rules.required("공백으로 검색할 수 없습니다.")]
    }
  },

  computed: {
    titleCollection(){
      return this.taskList.map(function(task){
        return task.title;
      })
    },

    searchedTaskList(){
      const expression = this.confirmSearchKeyword;
      if(expression === ""){
        return [];
      }
      const matchResult = matcher(expression, this.titleCollection);
      return this.taskList.filter(function(task){
        return matchResult.find(function (keyword){
          return keyword === task.title;
        }) !== undefined;
      });
    },

    searchResultCount(){
      return this.searchedTaskList.length;
    }
  },

  created() {
    this.getTaskList();
  },

  methods: {
    async getTaskList(){
      console.log("옹냐");
      this.callCount++;

      try{
        let res = await this.$axios.get("/task/list");
        this.taskList = res.data;
      }
      catch(e){
        if(e.response !== undefined){
          this.$dialog.error({
            title: "작업 목록 불러오기 실패",
            message: e.response.data.message
          });
        }
        else {
          this.$dialog.error({
            title: "작업 목록 불러오기 실패",
            message: "알 수 없는 서버 에러, TOPLA 관리자에게 문의하십시오."
          });
        }
      }
      finally {
        this.callCount--;
      }
    },

    onSearchButtonClicked(){
      if(this.$refs.form.validate() === false){
        return;
      }
      this.confirmSearchKeyword = this.searchKeyword;
    },

    async onTaskCardClicked(dueDate){
      try{
        await this.$router.push({
          name: 'todolist mode',
          params: {
            date: dueDate,
            viewMode: "dueDate"
          }
        });
      }
      catch(e){
        this.$dialog.error({
          title:"이동 실패",
          message:"알 수 없는 원인으로 todo 보기 페이지로 이동에 실패하였습니다. 콘솔 로그를 확인하세요."
        });
      }
    }
  }
}
</script>

<style scoped>
.fullHeight {
  min-height: 93vh;
}
</style>
