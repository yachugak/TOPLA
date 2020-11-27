<template>
  <v-form>
    <v-container fluid>
      <v-form ref="form">
        <v-row>
          <v-col cols="12">
            <v-text-field
                label="작업 이름"
                outlined v-model="value.title"
                :rules="rules.title"
            ></v-text-field>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="3" class="leftCenter">
            중요도
            <v-icon>mdi-alert-circle-check</v-icon>
          </v-col>
          <v-col cols="9" class="leftCenter">
            <v-rating length="3" :color="bgColorByPriority[value.priority-1]" v-model="value.priority"></v-rating>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="3" class="leftCenter">
            마감일
            <v-icon>mdi-calendar-clock</v-icon>
          </v-col>
          <v-col cols="9" class="leftCenter">
            <v-menu
                ref="menu"
                v-model="isShowDatePicker"
                :close-on-content-click="false"
                :return-value.sync="value.dueDate"
                transition="scale-transition"
                offset-y
                min-width="290px"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                    v-model="value.dueDate"
                    label="마감일"
                    readonly
                    v-bind="attrs"
                    v-on="on"
                    :rules="rules.dueDate"
                ></v-text-field>
              </template>
              <v-date-picker
                  v-model="value.dueDate"
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
                    @click="$refs.menu.save(value.dueDate)"
                >
                  확인
                </v-btn>
              </v-date-picker>
            </v-menu>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="3" class="leftCenter">
            예상 시간
            <v-icon>mdi-clock-check-outline</v-icon>
          </v-col>
          <v-col cols="9" class="leftCenter">
            <duration-selector
                v-model="value.estimatedTime"
                :rule="rules.estimateTime"
            >
            </duration-selector>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols="3" class="leftCenter">
            장소
            <v-icon>mdi-map-marker-outline</v-icon>
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
                  <v-btn icon @click="isShowPlaceDialog = false">
                    <v-icon>mdi-close</v-icon>
                  </v-btn>
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
              {{ displayLocation }}
            </div>
          </v-col>
        </v-row>
      </v-form>
    </v-container>
    <kakao-map v-show="false" ref="map" :is-load-gps="false"></kakao-map>
  </v-form>
</template>

<script>
import durationSelector from "@/components/durationSelector";
import placeSelector from "@/components/placeSelector";
import gpsString from "@/plugins/gpsString";
import kakaoMap from "@/components/kakaoMap";
import VuetifyJetValidator from "vuetify-jet-validator";

export default {
  name: "taskInfoForm",

  data() {
    const validator = new VuetifyJetValidator();
    return {
      isShowDatePicker: false,
      isShowDueDateButton: true,
      isShowPlaceDialog: false,

      tempLocation: null,
      addr: "주소 불러오는 중",

      rules: {
        title: [
          validator.rules.required("작업 이름을 설정해 주세요."),
        ],

        priority: [],

        dueDate: [
          validator.rules.required("마감일을 설정해 주세요."),
        ],
        estimateTime: [
          validator.rules.required("예상 소요시간을 설정해 주세요."),
        ],
        location: [],
      },

      bgColorByPriority: [
        "amber lighten-3",
        "amber darken-2",
        "amber darken-4",
      ]
    }
  },

  components: {
    kakaoMap,
    durationSelector,
    placeSelector
  },

  created() {
    if (this.value === null || this.value === undefined) {
      this.$emit("input", getDefaultObject());
    }
  },

  props: {
    value: {
      type: Object,
      default: getDefaultObject()
    }
  },

  computed: {
    displayDueDate() {
      if (this.dueDate === null) {
        return "마감일 없음";
      }

      return this.dueDate;
    },

    displayLocation() {
      if (this.value.location === null) {
        return "장소 미지정";
      }
      if (gpsString.isGpsString(this.value.location)) {
        let latLng = gpsString.parse(this.value.location);
        this.getLoadAddress(latLng.lat, latLng.lng);
        return this.addr;
      } else {
        return this.value.location
      }
    }
  },

  methods: {
    throwEvent() {
      // let res = {
      //   title: this.title,
      //   dueDate: this.dueDate,
      //   priority: this.priority,
      //   estimatedTime: this.estimatedTime,
      //   location: this.location
      // };
      // this.$emit("input", res);
    },

    onConfirmLocation() {
      if (this.tempLocation.type === "single") {
        this.value.location = this.tempLocation.code;
      } else {
        this.value.location = this.tempLocation.keyword;
      }
    },

    async getLoadAddress(lat, lng) {
      let addr = null
      while (addr === null) {
        try {
          addr = await this.$refs.map.geoToAddress(lat, lng);
        } catch (e) {
          console.info(`${lat}, ${lng}의 주소 변환 시도 실패, 3초후 재시도`);
          await wait(300);
        }
      }

      this.addr = addr;
    },

    formValue() {
      return this.$refs.form.validate();
    },
  }
}

function getDefaultObject() {
  return {
    dueDate: null,
    estimatedTime: 0,
    location: null,
    priority: 1,
    title: ""
  }
}

function wait(time) {
  return new Promise(function (res) {
    setTimeout(function () {
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

.w-100 {
  width: 100%;
}
</style>