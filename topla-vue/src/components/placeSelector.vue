<template>
  <v-card style="width: 400px" class="mx-auto" :elevation="3">
    <v-card-title>
      장소 유형
      <v-spacer></v-spacer>
      <v-btn icon @click="isShowHelpDialog=true"><v-icon>mdi-help-circle</v-icon></v-btn>
    </v-card-title>

    <v-tabs grow v-model="selectedTab">
      <v-tab>단일 지점</v-tab>
      <v-tab>다중 지점</v-tab>
    </v-tabs>
    <v-divider></v-divider>
    <v-tabs-items v-model="selectedTab" touchless>
      <v-tab-item>
        <div class="px-2 pb-3">
          <v-text-field label="검색" v-model="searchKeyword" append-icon="mdi-magnify" @click:append="onSearchButtonClicked()"></v-text-field>
          <kakao-map map-width="300px" map-height="300px" @newMarkerPosition="selectedPoint = $event" class="mx-auto" :isload-gps="true" ref="map"></kakao-map>
          <div class="text-center">지도에서 직접 고르거나 장소를 검색하세요.</div>
        </div>
      </v-tab-item>
      <v-tab-item>
        <div class="px-2 pb-3">
          <v-text-field label="키워드" hint="지도에서 해당 장소를 나타낼 수 있는 키워드를 입력하십시오."
                        placeholder="편의점, 마트, 약국 등등"
                        v-model="placeKeyword"
          >
          </v-text-field>
        </div>
      </v-tab-item>
    </v-tabs-items>

    <v-dialog v-model="isShowSearchDialog" scrollable>
      <v-card>
        <v-card-title>장소 검색 결과</v-card-title>
        <div class="text-center" style="height: 300px" v-if="searchResult===null">
          <v-progress-circular indeterminate color="primary"></v-progress-circular><br>
          <span>장소를 검색하고 있습니다...</span>
        </div>
        <v-card-text style="height: 300px" v-else-if="searchResult.length>0">
          <v-list two-line>
            <v-list-item v-for="place in searchResult" :key="place.id" @click="onSearchItemClicked(place)">
              <v-list-item-content>
                <v-list-item-title>{{place.place_name}}</v-list-item-title>
                <v-list-item-subtitle>{{ (place.road_address_name === "" || place.road_address_name === null || place.road_address_name === undefined) ? place.address_name : place.road_address_name}}</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-card-text>
        <div style="height: 300px" class="text-center" v-else>
          검색 결과가 없습니다.
        </div>
        <v-card-actions>
          <v-spacer> </v-spacer>
          <v-btn color="secondary" @click="isShowSearchDialog=false">닫기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="isShowHelpDialog">
      <v-card>
        <v-card-title>단일 지점과 다중 지점</v-card-title>
        <v-card-text>
          단일 지점과 다중 지점은 아래와 같은 차이가 있습니다.<br>
          <br>
          <span class="text-h6">단일 지점</span><br>
          단일 지점은 지도 상에서 고유의 장소를 말합니다. 단일 지점을 장소 필드로 가지는 작업은 딱 이 장소에서만 행해질 수 있습니다.<br>
          이러한 작업의 예시로 "학교에 서류 제출하기" 등이 있습니다.
          <br>
          <span class="text-h6">다중 지점</span><br>
          다중 지점은 작업을 행할 수 있는 장소가 여러 곳임을 나타냅니다. 해당 장소의 특징을 나타낼 수 있는 키워드로 정의됩니다.<br>
          예를 들어 "버스카드 충전"이라는 작업의 경우 전국 어느 편의점에서나 가능합니다. 이 작업의 장소 필드를 다중 지점 키워드로 "편의점"을 등록해 놓으면 TOPLA는 가장 가까운 편의점으로 안내해 줍니다.
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="isShowHelpDialog=false">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-card>
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
      selectedPoint: {lat:0, lng: 0, address:null},
      placeKeyword: "",
      isShowSearchDialog: false,
      isShowHelpDialog: false,
      searchKeyword: "",
      searchResult: null,
      selectedTab: 0
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
    },

    placeType(){
      if(this.selectedTab === 0){
        return "single";
      }

      return "multi";
    }
  },

  watch: {
    dbString(){
      this.throwEvent();
    },

    "selectedPoint.address"(newVal){
      this.searchKeyword = newVal;
    }
  },

  methods: {
    throwEvent(){
      this.$emit("input", this.dbString)
    },

    onSearchButtonClicked(){
      this.isShowSearchDialog = true;
      this.placeSearch(this.searchKeyword);
    },

    async placeSearch(keyword){
      this.searchResult = null;
      let result = await this.$refs.map.search(keyword);
      if(result === "ERROR"){
        this.searchResult = [];
        return;
      }
      console.log(result);
      this.searchResult = result;
    },

    onSearchItemClicked(place){
      let address;
      if(place.road_address_name === "" || place.road_address_name === null || place.road_address_name === undefined){
        address = place.address_name;
      }else {
        address = place.road_address_name;
      }
      this.selectedPoint = {
        lat: place.y,
        lng: place.x,
        address: address
      }
      this.$refs.map.setCenterLatLng(place.y, place.x);
      this.$refs.map.drawMarker(place.y, place.x);
      this.isShowSearchDialog = false;
    }
  }
}
</script>

<style scoped>
</style>