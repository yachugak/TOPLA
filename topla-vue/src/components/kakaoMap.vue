<template>
  <vue-daum-map
      :app-key="appKey"
      :center.sync="center"
      :level.sync="level"
      :map-type-id="mapTypeId"
      :libraries="libraries"
      ref="map"
      @load="onMapLoaded"
      @click="onMapClicked"
      class="ttt"
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
    },

    isLoadGps: {
      type: Boolean,
      default: true
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
      // let map = this.$refs.map;
      // console.log(map);
      // map.style.width = this.mapWidth;
      // map.style.height = this.mapHeight;
    },

    onMapLoaded(map){
      this.map = map;

      map.addControl(new window.kakao.maps.ZoomControl(), window.kakao.maps.ControlPosition.RIGHT);

      this.serviceLinking();
    },

    async drawMarker(lat, lng){
      this.marker.setPosition(latLng(lat, lng));

      let address = await this.geoToAddress(lat, lng);

      this.$emit("newMarkerPosition", {
        lat: lat,
        lng: lng,
        address: address
      });
    },

    serviceLinking() {
      //검색 서비스 연결
      this.place = new window.kakao.maps.services.Places();
      this.place.setMap(this.map);

      //그리기 서비스 연결
      this. marker = new window.kakao.maps.Marker({
        position: this.map.getCenter()
      });
      this.marker.setMap(this.map);

      //현위치 검색 걸어 놓기
      //현재 위치를 못 받아 오면 그냥 제주도 본사를 가리치고, 받아오면 그때 마커와 화면을 옮긴다. 해당 코드는 watch 기술되어 있음
      if(this.isLoadGps){
        this.getDevicePosition();
      }
    },

    async search(keyword){
      let position= new Object()
      position.location=await this.getDevicePosition()
      let res = await keywordSearchPromise(this.place, keyword, position);

      return res

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
      let lat = mouseEvent[0].latLng.getLat();
      let lng = mouseEvent[0].latLng.getLng();
      this.drawMarker(lat, lng);
    },

    async getDevicePosition(){
      let position=null
      try{
        position = await getLocationPromise();
        this.devicePosition = {
          lat: position.latitude,
          lng: position.longitude
        };
      }catch(e){
        console.error(e);
      }

      return latLng(position.latitude,position.longitude)
    },

    async geoToAddress(lat, lng){
      let addr = await geoToAddr(lat, lng);

      //도로명 주소가 있으면 도로명 주소 우선 반환
      if(addr.roadAddress !== null){
        return addr.roadAddress;
      }

      //없으면 지번 주소
      return addr.address;
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

function geoToAddr(lat, lng){
  return new Promise(function(res, rej){
    let geocoder = new window.kakao.maps.services.Geocoder();
    geocoder.coord2Address(lng, lat, function(result, status){
      if(status !== window.kakao.maps.services.Status.OK){
        rej("주소 변환 에러");
        return;
      }
      let r = result[0];
      res({
        roadAddress: r.road_address ? r.road_address.address_name : null,
        address: r.address.address_name
      });
    })
  })
}
</script>

<style scoped>
.ttt{
  width: 300px;
  height: 300px;
}
</style>