<template>
  <vue-daum-map
      :app-key="appKey"
      :center.sync="center"
      :level.sync="level"
      :map-type-id="mapTypeId"
      :libraries="libraries"
      id="kakaoMapComponent"
      @load="onMapLoaded"
      @click="onMapClicked"
  ></vue-daum-map>
</template>

<script>
import vueDaumMap from "vue-daum-map";

export default {
  name: "kakaoMap",

  components: {
    vueDaumMap
  },

  data() {
    return {
      appKey: "e5facc4c32f508bce12acfe0382f40fc",
      center: {lat: 33.450701, lng: 126.570667},
      level: 3,
      mapTypeId: vueDaumMap.MapTypeId.NORMAL,
      libraries: ["services", "drawing"],
      map: null,
      place: null,
      marker: null,

      devicePosition: null
    }
  },

  props: {
    mapWidth: {
      type: String,
      default: "500px"
    },

    mapHeight: {
      type: String,
      default: "500px"
    }
  },

  mounted() {
    this.updateSize();
  },

  watch: {
    mapWidth(){
      this.updateSize();
    },

    mapHeight() {
      this.updateSize();
    },

    devicePosition(newValue){
      //기기의 현재 위치가 검색되면 화면을 그쪽으로 보내고 마커도 거기로 설정한다.
      this.center = newValue;
      this.drawMarker(newValue.lat, newValue.lng);
    }
  },

  methods: {
    updateSize(){
      let map = document.getElementById("kakaoMapComponent");
      map.style.width = this.mapWidth;
      map.style.height = this.mapHeight;
    },

    onMapLoaded(map){
      this.map = map;

      map.addControl(new window.kakao.maps.ZoomControl(), window.kakao.maps.ControlPosition.RIGHT);

      this.serviceLinking();
    },

    drawMarker(lat, lng){
      this.marker.setPosition(latLng(lat, lng));
    },

    serviceLinking() {
      //검색 서비스 연결
      this.place = new window.kakao.maps.services.Places();
      this.place.setMap(this.map);
      this.search("넥슨");

      //그리기 서비스 연결
      this. marker = new window.kakao.maps.Marker({
        position: this.map.getCenter()
      });
      this.marker.setMap(this.map);

      //현위치 검색 걸어 놓기
      //현재 위치를 못 받아 오면 그냥 제주도 본사를 가리치고, 받아오면 그때 마커와 화면을 옮긴다. 해당 코드는 watch 기술되어 있음
      this.getDevicePosition();
    },

    async search(keyword){
      let res = await keywordSearchPromise(this.place, keyword);
      return res;
    },

    setCenterLatLng(lat, lng){
      this.center = {
        lat: lat,
        lng: lng
      }
    },

    setCenterXY(x, y){
      this.setCenterLatLng(y, x);
    },

    onMapClicked(mouseEvent){
      this.marker.setPosition(mouseEvent[0].latLng);
    },

    async getDevicePosition(){
      try{
        let position = await getLocationPromise();
        this.devicePosition = {
          lat: position.latitude,
          lng: position.longitude
        };
      }catch(e){
        console.error(e);
      }
    }
  }
}

//카카오 맵 api의 keyword search 메소드를 promise로 감싼 함수
function keywordSearchPromise(place, keyword, options){
  return new Promise(function(res, rej){
    place.keywordSearch(keyword, function(result, status){
      if(status === "ERROR"){
        rej("kakaomap keyword search fail");
      }

      res(result);
    }, options)
  })
}

function getLocation(res, rej){
  if (navigator.geolocation) { // GPS를 지원하면
    navigator.geolocation.getCurrentPosition(function(position) {
      res(position.coords);
    }, function(error) {
      rej(error);
    }, {
      enableHighAccuracy: false,
      maximumAge: 0,
      timeout: Infinity
    });
  } else {
    rej("이 브라우저는 GPS를 지원하지 않습니다.");
  }
}

function getLocationPromise(){
  return new Promise(function(res, rej){
    getLocation(res, rej);
  })
}

function latLng(lat, lng){
  return new window.kakao.maps.LatLng(lat, lng)
}
</script>

<style scoped>
</style>