<template>
  <v-card elevation="5" rounded="xl">
    <div id="flexBox">
      <div id="leftSide" class="large-checkbox">
        <v-checkbox v-model="isDone" :disabled="isCallDoing"></v-checkbox>
      </div>
      <div id="rightSide" @click="onCardClicked($event)">
        <v-card-title>
          {{title}}
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
      <v-progress-linear indeterminate height="10" v-show="isCallDoing"></v-progress-linear>
    </v-expand-transition>
    <kakao-map ref="map" v-show="false" :is-load-gps="false"></kakao-map>
  </v-card>
</template>

<script>
import gpsString from "@/plugins/gpsString";
import kakaoMap from "@/components/kakaoMap";

export default {
  name: "taskCard",

  components: {
    kakaoMap
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
      isCallDoing: false, //현재 뭔가 요청이 진행중인가?,
      addr: null,
      distance:null,
      lat: null,
      lng: null
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
    }
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
    if(this.progress >= this.estimatedTime){
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
      if(this.isCallDoing){
        return;
      }


      this.isCallDoing = true;
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
        this.isCallDoing = false;
      }
    },

    onCardClicked(){
      this.$emit("click", this.uid);
    },

    async loadAddr(lat, lng){
      let addr = null
      while(addr === null){
        try {
          addr = await this.$refs.map.geoToAddress(lat, lng);
          this.lat = lat;
          this.lng = lng;
        }
        catch(e){
          console.log(e)
          console.info(`${lat}, ${lng}의 주소 변환 시도 실패, 1초후 재시도`);
          await wait(1000);
        }
      }

      this.addr = addr;
      // if(addrObject.roadAddress === undefined){
      //   this.addr = addrObject.roadAddress;
      // }
      // else{
      //   this.addr = addrObject.address;
      // }
    },

    async loadDistance(latLng){

      let devicePostion
      let polyline = null

      while(polyline ===null){
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
          console.log(e)
          console.info(`실패, 1초후 재시도`);
          await wait(1000)
        }
      }

      let distance=parseInt(polyline.getLength());
      this.distance=distance
    },

    async calculateDistanceByKeyword(keyword){
      let searchList=null
      while(searchList === null){
        try {
          searchList = await this.$refs.map.search(keyword);
        }
        catch(e){
          console.log(e)
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