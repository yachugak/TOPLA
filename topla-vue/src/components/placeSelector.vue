<template>
  <div>
    <div>장소 유형</div>
    <v-radio-group row v-model="placeType">
      <v-radio label="단일 지점" :value="'single'"></v-radio>
      <v-radio label="다중 지점" :value="'multi'"></v-radio>
    </v-radio-group>

    <div v-show="placeType==='single'">
      <kakao-map map-width="300px" map-height="300px" @newMarkerPosition="selectedPoint = $event" class="mx-auto"></kakao-map>
      <div>
        선택된 위치<br>
        위도: {{selectedPoint.lat}}<br>
        경도: {{selectedPoint.lng}}<br>
        주소: {{selectedPoint.address}}
      </div>
    </div>
    <div v-show="placeType==='multi'">
      <v-text-field label="키워드" hint="지도에서 해당 장소를 나타낼 수 있는 키워드를 입력하십시오."
                    placeholder="편의점, 마트, 약국 등등"
                    v-model="placeKeyword"
      >
      </v-text-field>
    </div>

  </div>
</template>

<script>
import kakaoMap from "@/components/kakaoMap";

export default {
  name: "placeSelector",

  components: {
    kakaoMap
  },

  data() {
    return {
      placeType: "single",
      selectedPoint: {lat:0, lng: 0},
      placeKeyword: ""
    }
  },

  computed: {
    dbString() {
      if(this.placeType === "single"){
        return {
          type: "single",
          address: this.selectedPoint.address,
          code: `!@GPS!@${this.selectedPoint.lat}!@${this.selectedPoint.lng}`
        }
      }else{
        return {
          type: "multi",
          keyword: this.placeKeyword
        }
      }
    }
  },

  watch: {
    dbString(){
      this.throwEvent();
    }
  },

  methods: {
    throwEvent(){
      this.$emit("input", this.dbString)
    }
  }
}
</script>

<style scoped>

</style>