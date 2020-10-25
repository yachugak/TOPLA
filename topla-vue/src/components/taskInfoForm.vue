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
          <v-menu
              ref="menu"
              v-model="isShowDatePicker"
              :close-on-content-click="false"
              :return-value.sync="dueDate"
              transition="scale-transition"
              offset-y
              min-width="290px"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                  v-model="dueDate"
                  label="마감일"
                  readonly
                  v-bind="attrs"
                  v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
                v-model="dueDate"
                no-title
                scrollable
            >
              <v-spacer></v-spacer>
              <v-btn
                  text
                  color="error"
                  @click="isShowDatePicker = false"
              >
                취소
              </v-btn>
              <v-btn
                  text
                  color="primary"
                  @click="$refs.menu.save(dueDate)"
              >
                확인
              </v-btn>
            </v-date-picker>
          </v-menu>
        </v-col>
      </v-row>

      <v-row>
        <v-col cols="3" class="leftCenter">
          예상 시간 <v-icon>mdi-clock-check-outline</v-icon>
        </v-col>
        <v-col cols="9" class="leftCenter">
          <duration-selector v-model="estimatedTime"></duration-selector>
        </v-col>
      </v-row>
    </v-container>
  </v-form>
</template>

<script>
import durationSelector from "@/components/durationSelector";

export default {
  name: "taskInfoForm",

  data() {
    return {
      isShowDatePicker: false,
      isShowDueDateButton: true,

      dueDate: null,
      title: "",
      priority: 1,
      estimatedTime: 0,

      bgColorByPriority: [
        "amber lighten-3",
        "amber darken-2",
        "amber darken-4",
      ]
    }
  },

  components: {
    durationSelector
  },

  created() {
    this.throwEvent();
  },

  watch: {
    dueDate(){
      this.throwEvent();
    },

    title(){
      this.throwEvent();
    },

    priority(){
      this.throwEvent();
    },

    estimatedTime(){
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
        priority: this.priority,
        estimatedTime: this.estimatedTime
      };
      this.$emit("input", res);
    }
  }
}
</script>

<style scoped>
.leftCenter {
  display: flex;
  justify-content: left;
  align-items: center;
}
</style>