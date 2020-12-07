  <template>
    <v-card elevation="5" rounded="xl">
      <div id="flexBox">
        <div id="leftSide" class="large-checkbox">
          <v-checkbox v-model="isDone" :disabled="callCount>0"></v-checkbox>
        </div>
        <div id="rightSide" @click="onCardClicked($event)">
          <v-card-title>
            {{title}}
            <v-icon v-if="remindingTime !== null">mdi-bell</v-icon>
            <v-spacer></v-spacer>
            <v-icon v-if="priority>=1" :color="bgColor">mdi-star</v-icon>
            <v-icon v-if="priority>=2" :color="bgColor">mdi-star</v-icon>
            <v-icon v-if="priority>=3" :color="bgColor">mdi-star</v-icon>
          </v-card-title>
          <v-card-text>
            <v-container fluid>
              <v-row no-gutters>
                <v-col cols="6">
                  <v-icon>mdi-clock-check-outline</v-icon>
                  {{displayEstimatedTime}}
                </v-col>
                <v-col cols="6">
                  <v-icon>mdi-calendar-clock</v-icon>
                  {{displayDueDate}}
                </v-col>
              </v-row>
              <v-row no-gutters>
                <v-col cols="6">
                  <v-icon>mdi-map-marker-outline</v-icon>
                  <span v-if="displayLocation !==null">
                    {{displayLocation}}
                    <v-btn v-if="lat !== null" small icon @click="goToKakaoMapSite()">
                      <v-icon>mdi-map-search-outline</v-icon>
                    </v-btn>
                  </span>
                  <span v-else>
                    <v-progress-circular color="primary" :size="20" indeterminate></v-progress-circular>
                  </span>
                </v-col>
                <v-col cols="6">
                  <div id="progressContentBox">
                    <v-icon id="progressIcon">mdi-progress-clock</v-icon>
                    <v-progress-linear :value="percentProgress"></v-progress-linear>
                  </div>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
        </div>
      </div>
      <v-expand-transition>
        <v-progress-linear indeterminate height="10" v-show="callCount>0"></v-progress-linear>
      </v-expand-transition>
      <kakao-map ref="map" v-show="false" :is-load-gps="false"></kakao-map>

    <v-dialog
            v-model="isDialogShow"
            persistent
            max-width="500"
    >
      <v-card>
        <v-card-title>작업 정보 수정</v-card-title>
        <task-info-form
                v-model="taskFormData"
                ref="infoForm"
        ></task-info-form>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" :loading="callCount>0" @click="onTaskDeleteButtonclicked()">삭제 </v-btn>
          <v-btn color="secondary" @click="isDialogShow = false" :loading="callCount>0">뒤로 </v-btn>
          <v-btn color="primary" @click="onTaskUpdateButtonClicked()" :loading="callCount>0"> 수정 </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
</template>

<script>
import gpsString from "@/plugins/gpsString";
import kakaoMap from "@/components/kakaoMap";
import taskInfoForm from "./taskInfoForm";
import errorDialog from "../plugins/errorDialog";

export default {
  name: "taskCard",

  components: {
    kakaoMap,
    taskInfoForm
  },

  data() {
    return {
      bgColorByPriority: [
        "amber lighten-3",
        "amber darken-2",
        "amber darken-4",
      ],

      doneColor: "brown lighten-3",
      isDone: null,
      addr: null,
      distance:null,
      lat: null,
      lng: null,

      taskFormData: {
        title: "",
        dueDate: null,
        estimatedTime: 0,
        priority: 1,
        location: null,
        remindingTime: null
      },
      callCount: 0,
      isDialogShow: false,
      destroyedFlag: false
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
    },

    estimatedTime: {
      type: Number,
      default: 0
    },

    dueDate: {
      type: String,
      default: null
    },

    location: {
      type: String,
      default: null
    },

    planUid: {
      type: Number,
      default: -1
    },

    remindingTime: {
      type: String,
      default: null
    }
  },

  destroyed() {
    this.destroyedFlag = true;
  },

  computed: {
    bgColor(){
      if(this.progress >= this.estimatedTime){
        return this.doneColor;
      }

      //아직 완료되지 않은 작업이면 중요도로 색상 표시
      return this.bgColorByPriority[this.priority-1];
    },

    displayEstimatedTime(){
      let hour = Number.parseInt(this.estimatedTime / 60); //몫 구하기
      let min = this.estimatedTime % 60; //나머지 구하기

      return `${hour}시간 ${min}분`;
    },

    displayDueDate(){
      if(this.dueDate === null){
        return "마감일 없음"
      }

      return this.dueDate;
    },
    displayLocation(){
      if(this.location === null){
        return "장소 미지정";
      }

      if(gpsString.isGpsString(this.location)){
        let latLng = gpsString.parse(this.location);
        this.loadAddr(latLng.lat, latLng.lng);
        this.loadDistance(latLng)

        if(this.addr===null || this.distance ===null)
          return null
        return `${this.addr} (${this.distance}m)`
      }

      else{
        let keyword=this.location
        this.calculateDistanceByKeyword(keyword)

        if(this.addr===null || this.distance ===null)
          return null

        return `${this.addr} (${this.distance}m)`
      }
    },

    percentProgress(){
      return (this.progress/this.estimatedTime)*100
    }
  },

  created() {
    this.isDone = this.progress >= this.estimatedTime;
  },

  watch: {
    isDone(newValue, oldValue){
      if(oldValue === null){
        //첫 초기화 과정으로 인한 값 변경이므로 무시
        return;
      }
      if(newValue === true){
        this.callUpdateProgress(this.estimatedTime);
      }
      else {
        this.callUpdateProgress(-1);
      }
    }
  },

  methods: {
    async callUpdateProgress(newProgress){
      if(this.uid === null){
        return;
      }
      if(this.callCount > 0){
        return;
      }


      this.callCount++;
      try {
        if(this.planUid === -1){
          //task 끝내기 모드
          await this.$axios.put(`/task/${this.uid}/finish`, {
            progress: newProgress
          })
        }
        else {
          // paln id가 있으면 plan을 표시중이라는 것으로  plan 완료 처리
          console.log(`plan/${this.planUid}/finish`);
          await this.$axios.put(`/plan/${this.planUid}/finish`, {
            progress: newProgress
          })
        }
        this.$emit("update");
      }
      catch(e){
        console.error(e.response.data);
      }
      finally {
        this.callCount--;
      }
    },

    onCardClicked(){
      this.taskFormData = {
        title: this.title,
        dueDate: this.dueDate,
        estimatedTime: this.estimatedTime,
        priority: this.priority,
        location: this.location,
        remindingTime: this.remindingTime
      }
      this.isDialogShow = true;
    },

    async loadAddr(lat, lng){
      let addr = null
      while(addr === null){
        if(this.destroyedFlag){
          console.log("인스턴스 파괴됨, 주소 변환 종료");
          return;
        }
        try {
          addr = await this.$refs.map.geoToAddress(lat, lng);
          this.lat = lat;
          this.lng = lng;
        }
        catch(e){
          console.info(`${lat}, ${lng}의 주소 변환 시도 실패, 1초후 재시도`);
          await wait(1000);
        }
      }

      this.addr = addr;
    },

    async loadDistance(latLng){
      let devicePostion
      let polyline = null

      while(polyline ===null){
        if(this.destroyedFlag){
          console.log("인스턴스 파괴됨, 거리 획득 시도 종료");
          return;
        }
        try{

          devicePostion=await this.$refs.map.getDevicePosition()
          polyline = new window.kakao.maps.Polyline({
            path:[
              devicePostion,
              new window.kakao.maps.LatLng(latLng.lat, latLng.lng),
            ]
          })
        }
        catch (e) {
          // console.log(e)
          console.info(`거리 획득 실패, 1초후 재시도`);
          await wait(1000)
        }
      }

      let distance=parseInt(polyline.getLength());
      this.distance=distance
    },

    async calculateDistanceByKeyword(keyword){
      let searchList=null
      while(searchList === null){
        if(this.destroyedFlag){
          console.log("인스턴스 파괴됨, 거리 획득 시도 종료");
          return;
        }
        try {
          searchList = await this.$refs.map.search(keyword);
        }
        catch(e){
          // console.log(e)
          await wait(1000);
        }
      }
      let destination = searchList[0]

      this.addr=destination.place_name;
      let destinationLatLng={
        lat:destination.y,
        lng:destination.x
      }
      this.lat = destinationLatLng.lat;
      this.lng = destinationLatLng.lng;
      await this.loadDistance(destinationLatLng)
    },

    goToKakaoMapSite(){
      window.open(`https://map.kakao.com/link/to/${this.displayLocation},${this.lat},${this.lng}`);
    },

    async onTaskUpdateButtonClicked(){
      let validationResultFlag = this.$refs.infoForm.formValue()
      if(validationResultFlag === false){
        return;
      }

      let requestBody = {
        title: this.taskFormData.title,
        priority: this.taskFormData.priority,
        dueDate: this.taskFormData.dueDate,
        estimatedTime: this.taskFormData.estimatedTime,
        location: this.taskFormData.location,
        remindingTiming: this.taskFormData.remindingTime
      }

      try{
        this.callCount++;
        await this.$axios.put(`/task/${this.uid}`, requestBody);

        this.$emit("update");
        this.isDialogShow = false;
      }

      catch(e){
          errorDialog(this, "수정 실패", e);
      }
      finally {
          this.callCount--;
      }
    },

    async onTaskDeleteButtonclicked(){
      let confrimResult = await this.confrimDelete();
      if(confrimResult === false){
        return;
      }

      this.callCount++;
      try{
        await this.$axios.delete(`/task/${this.uid}`);
      }
      catch(e){
        errorDialog(this, "작업 삭제 실패", e);
      }
      finally {
        this.callCount--;
      }

      this.$dialog.notify.success(`[${this.title}] 작업을 삭제하였습니다.`);
      this.$emit("update");
    },

    async confrimDelete(){
      this.callCount++;
      let res = await this.$dialog.error({
        title:"정말로 삭제하시겠습니까?",
        text: "이 작업은 되돌릴 수 없습니다.",
        actions: {
          true: {
            text: "삭제합니다.",
            color: "error"
          },
          false: {
            text: "아니오",
            color: "success"
          }
        }
      })
      if(res === undefined){
        res = false;
      }
      this.callCount--;
      return res;
    }
  }
}

function wait(time){
  return new Promise(function (res){
    setTimeout(function(){
      res();
    }, time);
  });
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

#rightSide{
  width: 100%;
}

#progressContentBox {
  display: flex;
  flex-flow: row nowrap;
  justify-content: flex-start;
  align-content: stretch;
  align-items: center;
}

#progressIcon {
  flex: 0 0 10px;
}

.large-checkbox >>> i {
  font-size: 40px;
}
</style>