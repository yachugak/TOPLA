<template>
  <v-card :color="bgColor">
    <div id="flexBox">
      <div id="leftSide" class="large-checkbox">
        <v-checkbox v-model="isDone" :disabled="isCallDoing"></v-checkbox>
      </div>
      <div id="rightSide">
        <v-card-title>{{title}}</v-card-title>
        <v-card-text>
          <v-icon>mdi-clock-check-outline</v-icon>
        </v-card-text>
      </div>
    </div>
    <v-expand-transition>
      <v-progress-linear indeterminate height="10" v-show="isCallDoing"></v-progress-linear>
    </v-expand-transition>
  </v-card>
</template>

<script>
export default {
  name: "taskCard",
  data() {
    return {
      bgColorByPriority: [
        "amber lighten-3",
        "amber darken-2",
        "amber darken-4",
      ],

      doneColor: "brown lighten-3",
      isDone: null,
      isCallDoing: false //현재 뭔가 요청이 진행중인가?
    }
  },
  
  props: {
    title: {
      type: String,
      default: "제목 없는 작업"
    },

    priority: {
      type: Number,
      default: 1
    },

    progress: {
      type: Number,
      default: 0
    },

    uid: {
      type: Number,
      default: null
    }
  },

  computed: {
    bgColor(){
      if(this.progress >= 100){
        return this.doneColor;
      }

      //아직 완료되지 않은 작업이면 중요도로 색상 표시
      return this.bgColorByPriority[this.priority-1];
    }
  },

  created() {
    if(this.progress >= 100){
      this.isDone = true;
    }
    else{
      this.isDone = false;
    }
  },

  watch: {
    isDone(newValue, oldValue){
      if(oldValue === null){
        //첫 초기화 과정으로 인한 값 변경이므로 무시
        return;
      }
      if(newValue === true){
        this.callUpdateProgress(100);
      }
      else {
        this.callUpdateProgress(0);
      }
    }
  },

  methods: {
    async callUpdateProgress(newProgress){
      if(this.uid === null){
        return;
      }
      if(this.isCallDoing){
        return;
      }

      this.isCallDoing = true;
      await this.$axios.put(`/task/${this.uid}/finish`, {
        progress: newProgress
      })
      this.isCallDoing = false;
      this.$emit("update");
    }
  }
}
</script>

<style scoped>
#flexBox{
  display: flex;
  flex-flow: row nowrap;
  justify-content: flex-start;
  align-content: stretch;
}
#leftSide {
  flex: 0 0 50px;
  display: flex;
  flex-flow: row nowrap;
  justify-content: center;
  align-items: center;
}

.large-checkbox >>> i {
  font-size: 40px;
}
</style>