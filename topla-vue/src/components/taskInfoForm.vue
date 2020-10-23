<template>
  <v-form>
    <v-container fluid>
      <v-row>
        <v-col cols="12">
          <v-text-field label="작업 이름" outlined v-model="title"></v-text-field>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="3" class="leftCenter">
          중요도<v-icon>mdi-alert-circle-check</v-icon>
        </v-col>
        <v-col cols="9" class="leftCenter">
          <v-rating length="3" :color="bgColorByPriority[priority-1]" v-model="priority"></v-rating>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="3" class="leftCenter">
          마감일<v-icon>mdi-calendar-clock</v-icon>
        </v-col>
        <v-col cols="9" class="leftCenter">
          <v-expand-transition>
            <v-btn color="primary" @click="onDueDateButtonClicked()" v-show="isShowDueDateButton">{{displayDueDate}}</v-btn>
          </v-expand-transition>
          <v-expand-transition>
            <v-date-picker v-show="isShowDatePicker" show-current color="primary" v-model="dueDate"></v-date-picker>
          </v-expand-transition>
        </v-col>
      </v-row>
    </v-container>
  </v-form>
</template>

<script>
export default {
  name: "taskInfoForm",

  data() {
    return {
      isShowDatePicker: false,
      isShowDueDateButton: true,

      dueDate: null,
      title: "",
      priority: 1,

      bgColorByPriority: [
        "amber lighten-3",
        "amber darken-2",
        "amber darken-4",
      ],
    }
  },

  watch: {
    dueDate(){
      this.isShowDatePicker = false;
      this.delayedDueDateButtonShow();
      this.throwEvent();
    },

    title(){
      this.throwEvent();
    },

    priority(){
      this.throwEvent();
    }
  },

  computed: {
    displayDueDate(){
      if(this.dueDate === null){
        return "마감일 없음";
      }

      return this.dueDate;
    }
  },

  methods: {
    throwEvent(){
      let res = {
        title: this.title,
        dueDate: this.dueDate,
        priority: this.priority
      };
      this.$emit("input", res);
    },

    onDueDateButtonClicked(){
      this.isShowDueDateButton = false;
      this.delayedDatePickerShow();
    },

    async delayedDueDateButtonShow(){
      await wait(300);
      this.isShowDueDateButton = true;
    },

    async delayedDatePickerShow(){
      await wait(300);
      this.isShowDatePicker = true;
    }
  }
}

let wait = function(time){
  return new Promise(function(res){
    setTimeout(function(){
      res();
    }, time);
  });
}
</script>

<style scoped>
.leftCenter {
  display: flex;
  justify-content: left;
  align-items: center;
}
</style>