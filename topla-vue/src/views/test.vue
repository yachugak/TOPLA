<template lang="html">
  <div>
    <div id="dateSelector">
      <v-btn class="dateButton" @click="onArrowButtonSelected('left')"><v-icon>mdi-chevron-left</v-icon></v-btn>
      <v-btn class="dateButton" v-for="i in 5" :key="i" @click="onDateSelectorButtonSelected(i-1)">
        {{dateSelectorButtonDisplayList.text[(i-1)*2]}} <br>
        {{dateSelectorButtonDisplayList.text[(i-1)*2+1]}}
      </v-btn>
      <v-btn class="dateButton" @click="onArrowButtonSelected('right')"><v-icon>mdi-chevron-right</v-icon></v-btn>
    </div>
    <div class="deep-purple darken-3 py-4">
      <v-card class="mx-2 mb-4" v-for="task in taskList" :key="task.uid" :color="bgColorByPriority[task.priority-1]">
        <v-card-title>{{task.title}}</v-card-title>
        <v-card-text>
          <v-icon>mdi-clock-check-outline</v-icon>
        </v-card-text>
      </v-card>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectedDateButton: 3,
      selectedDate: new Date(),
      taskList: [],
      bgColorByPriority: [
          "amber lighten-3",
          "amber darken-2",
          "amber darken-4",
      ],
    }
  },

  computed: {
    dateSelectorButtonDisplayList(){
      let week = ['일', '월', '화', '수', '목', '금', '토'];
      let today = this.selectedDate;
      let textList = [];
      let dateList = [];
      for(let i = -2; i <= 2; i++){
        let todayCopy = new Date(today);
        todayCopy.setDate(todayCopy.getDate()+i);
        textList.push(todayCopy.getDate());
        textList.push(week[todayCopy.getDay()]);
        dateList.push(todayCopy);
      }

      return {
        text: textList,
        date: dateList
      };
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

#dateSelector {
  display: inline-flex;
  flex-direction: row;
  flex-wrap: nowrap;
  justify-content: space-between;
  width: 100%;
}
.dateButton {
  width: 14.28571429%;
}
</style>
