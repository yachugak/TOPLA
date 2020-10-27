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

      <v-row>
        <v-col cols="3" class="leftCenter">
          장소 <v-icon>mdi-map-marker-outline</v-icon>
        </v-col>
        <v-col cols="9" class="leftCenter verticalStack flex-column">
          <v-dialog
              v-model="isShowPlaceDialog"
              fullscreen
              hide-overlay
              transition="dialog-bottom-transition"
          >
            <template v-slot:activator="{on, attrs}">
              <v-btn v-bind="attrs" v-on="on" class="d-block">
                <span v-if="displayLocation===null">
                  장소 지정되지 않음
                </span>
                  <span v-else>
                  장소 다시 설정
                </span>
              </v-btn>
            </template>
            <v-card>
              <v-toolbar color="primary" dark>
                <v-btn icon @click="isShowPlaceDialog = false"><v-icon>mdi-close</v-icon></v-btn>
                <v-toolbar-title>장소 선택</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-toolbar-items>
                  <v-btn @click="isShowPlaceDialog=false; onConfirmLocation()" text>
                    확인
                  </v-btn>
                </v-toolbar-items>
              </v-toolbar>
              <div class="pa-2">
                <place-selector @input="tempLocation = $event"></place-selector>
              </div>
            </v-card>
          </v-dialog>
          <br>
          <div>
            {{displayLocation}}
          </div>
        </v-col>
      </v-row>
    </v-container>
  </v-form>
</template>

<script>
import durationSelector from "@/components/durationSelector";
import placeSelector from "@/components/placeSelector";

export default {
  name: "taskInfoForm",

  data() {
    return {
      isShowDatePicker: false,
      isShowDueDateButton: true,
      isShowPlaceDialog: false,

      dueDate: null,
      title: "",
      priority: 1,
      estimatedTime: 0,
      location: null,

      tempLocation: null,
      displayLocation: null,

      bgColorByPriority: [
        "amber lighten-3",
        "amber darken-2",
        "amber darken-4",
      ]
    }
  },

  components: {
    durationSelector,
    placeSelector
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
    },

    location(){
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
        estimatedTime: this.estimatedTime,
        location: this.location
      };
      this.$emit("input", res);
    },

    onConfirmLocation(){
      if(this.tempLocation.type==="single"){
        this.location = this.tempLocation.code;
        this.displayLocation = this.tempLocation.address;
      }
      else{
        this.location = this.tempLocation.keyword;
        this.displayLocation = this.tempLocation.keyword;
      }
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

.w-100 {
  width: 100%;
}
</style>