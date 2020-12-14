<template>
  <div style="min-height: 95vh;" class="back pa-3">
    <v-card max-width="800" class="mx-auto">
      <v-card-title>TOPLA 사용 안내서</v-card-title>
      <v-card-subtitle>TOPLA 사용 방법을 배울 수 있는 자습서입니다.</v-card-subtitle>

      <v-tabs
          grow v-model="selectedTab"
          next-icon="mdi-arrow-right-bold-box-outline"
          prev-icon="mdi-arrow-left-bold-box-outline"
          show-arrows
      >
        <v-tab>일간 작업 보기</v-tab>
        <v-tab>월간 작업 보기</v-tab>
        <v-tab>작업 생성하기</v-tab>
        <v-tab>일정</v-tab>
        <v-tab>스케줄 프리셋</v-tab>
      </v-tabs>

      <v-tabs-items v-model="selectedTab">
        <v-tab-item>
          <div class="pa-2">
            <h1>일간 작업 보기</h1>
            <v-img :src="imgSrc.todo01" :height="imgHeight" contain alt="todo01"></v-img>
            <p>
              일간 작업 보기 페이지에서는 일별로 마감되거나, 사용자의 <a @click="selectPage(4)">스케줄 프리셋</a>에 따라 자동 배분된 작업을 확인할 수 있습니다.<br>
              또한, 새로운 작업을 추가하거나 완료 표시를 할 수 있습니다.
            </p>
            <p>
              <strong>(1) 날짜 선택:</strong> 클릭하여 해당 일의 작업을 볼 수 있습니다. <br>
              <strong>(2) 날짜 이동:</strong> 선택된 날짜로부터 5일만큼 앞으로 또는 뒤로 이동합니다.<br>
              <strong>(3) 보기 기준:</strong> 작업들을 마감일을 기준으로 표시할 지, TOPLA의 알고리즘이 자동 분배한
              <a @click="selectPage(4)">하는날</a>을 기준으로 표시할 지를 설정합니다.<br>
              <strong>(4) 작업 완료:</strong> 클릭하여 해당 작업을 완료하였음을 기록합니다.<br>
              <strong>(5) 작업 조회:</strong> 해당 작업의 제목, 중요도, 소요시간, 마감일, 장소, 진행도 등을 표시합니다. 작업을 클릭하여 수정 및 삭제할 수 있습니다.<br>
              <strong>(6) 작업 추가:</strong> <a @click="selectPage(2)">새로운 작업을 등록</a>합니다.<br>
            </p>
          </div>
        </v-tab-item>

        <v-tab-item>
          <div class="pa-2">
            <h1>월간 작업 보기</h1>
            <v-img :src="imgSrc.calendar01" :height="imgHeight" contain alt="calender01"></v-img>
            <p>
              월간 작업 보기 페이지에서는 등록한 작업을 달력형식으로 한번에 볼 수 있습니다.<br>
              다양한 작업들을 마감일 혹은 하는날을 기준으로 볼 수 있습니다.
            </p>
            <p>
              <strong>(1) 날짜 이동:</strong> 직접 날짜를 입력하거나 좌우 화살표로 다른 달로 이동합니다.<br>
              <strong>(2) 오늘로 이동:</strong> 오늘 날짜로 이동합니다.<br>
              <strong>(3) 보기 기준:</strong> 작업들을 마감일을 기준으로 표시할 지, 자동계산된 하는날을 기준으로 표시할 지를 설정합니다.<br>
              <strong>(4) 해당일로 이동:</strong> 클릭한 날짜에 해당하는
              <a @click="selectPage(0)">일간 작업 보기</a>페이지로 이동합니다. <br>
            </p>
          </div>
        </v-tab-item>

        <v-tab-item>
          <div class="pa-2">
            <h1>새로운 작업 등록하기</h1>
            <v-img :src="imgSrc.newTask" :height="imgHeight" contain alt="newTask"></v-img>
            <p>
              새로운 작업을 등록하는 데에는 크게 중요도, 마감일, 예상시간이 필요합니다. <br>
              TOPLA는 작업의 정보와 유저가 등록한 <a @click="selectPage(4)">스케줄 프리셋</a>을 기반으로 작업들을 자동 분배해줍니다.
            </p>
            <p>
              <strong>(1) 중요도:</strong> 작업의 중요도를 설정합니다. <br>
              <ul>
                <li>중요도 1: 반드시 하지 않아도 문제없는 작업</li>
                <li>중요도 2: 50%이상은 완료되어야 하는 작업</li>
                <li>중요도 3: 100% 완료되어야 하는 작업을 뜻합니다.</li>
              </ul>
              * <a @click="selectPage(3)">TOPLA가 사용자의 일정을 자동으로 계산해주는 과정</a>에서 중요도가 낮은 작업은 축소되거나 포기될 수 있으며, 이는 유저에게 미리 경고됩니다.<br>
              <strong>(2) 장소 지정:</strong> 특정한 장소나 위치에서 완료할 수 있는 작업이라면, 그 장소를 등록합니다.<br>
              <strong>(3) 알림 설정:</strong> 알림이 필요한 작업에 대해 알림 시각을 설정합니다.<br>
            </p>
          </div>
        </v-tab-item>

        <v-tab-item>
          <div class="pa-2">
            <h1>일정 자동계산</h1>

            <h2>* 일정 계산 방식</h2>
            <p>
              TOPLA는 사용자가 등록한 <a @click="selectPage(4)">스케줄 프리셋</a>과, 작업의 마감일, 중요도를 기반으로 일정을 자동계산합니다.<br>
              이 정보들을 통해 각각의 작업을 어떤 날짜에 해야 할지 계산하므로, 작업을 등록할 때에 유의해주세요.<br>
              새로운 작업이 등록되거나, 스케줄 프리셋이 변경되어도 자동으로 일정을 재조정해주니 걱정하지마세요.
            </p>
            <br>

            <h2>* 자동계산된 일정의 확인</h2>
            <v-img :src="imgSrc.plan01" :height="imgHeight" contain alt="plan01"></v-img>
            <v-img :src="imgSrc.plan02" :height="imgHeight" contain alt="plan02"></v-img>
            <p>
              자동계산된 일정은 <a @click="selectPage(0)">일간 작업 보기</a> 페이지에서 보기 기준을 하는날 기준으로 선택하거나,<br>
              <a @click="selectPage(1)">월간 작업 보기</a> 페이지에서 보기 기준을 하는날 기준으로 변경하면 확인할 수 있습니다.<br>
            </p>
            <br>

            <h2>* 작업의 축소 및 포기</h2>
            <v-img :src="imgSrc.plan03" :height="imgHeight" contain alt="plan03"></v-img>
            <p>
              요일별 일정을 계산하는 과정에서 중요도가 낮거나, 제 시간에 완료가 불가능한 작업들은 축소되거나 포기될 수 있습니다.<br>
              TOPLA는 불가능한 일에 시간과 노력을 쏟기보다 가능성이 있는 일을 우선합니다.<br>
              이런 경우에는 경고 메시지를 통해 알려주니 놓칠 일이 없습니다.<br>
            </p>
            <br>

            <h2>* 작업의 분할과 진행도</h2>
            <v-img :src="imgSrc.plan04" :height="imgHeight" contain alt="plan04"></v-img>
            <v-img :src="imgSrc.plan05" :height="imgHeight" contain alt="plan05"></v-img>
            <p>
              시험 공부나 운동처럼 오랜 기간에 걸친 작업들은 하루안에 끝내기가 어렵죠.<br>
              TOPLA는 이렇게 큰 작업들을 작은 작업으로 분리해서 일정을 계산합니다.<br>
              그리고, 분리된 작업들의 전체적인 진행도도 관리할 수 있습니다.<br>
            </p>
            <br>
          </div>
        </v-tab-item>

        <v-tab-item>
          <div class="pa-2">
            <h1>스케줄 프리셋 설정하기</h1>
            <v-img :src="imgSrc.preset01" :height="imgHeight" contain alt="preset01"></v-img>
            <p>
              사용자가 설정한 요일별 작업 가능 시간을 스케줄 프리셋이라고 부릅니다.<br>
              TOPLA는 이 프리셋을 기반으로 일정을 <a @click="selectPage(3)">자동분배</a>해 줍니다.<br>
              다양한 프리셋을 저장해 두었다가 상황에 따라 바꿀 수 있습니다.
            </p>
            <p>
              <strong>(1) 프리셋 선택:</strong> 클릭하여 저장된 프리셋을 조회하고, 선택할 수 있습니다.<br>
              <strong>(2) 작업시간 조정:</strong> 드래그하여 각 요일별로 작업이 가능한 시간을 설정합니다.<br>
              <strong>(3) 프리셋 추가:</strong> 클릭하여 새로운 프리셋을 추가할 수 있습니다.<br>
            </p>
          </div>
        </v-tab-item>
      </v-tabs-items>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "userGuide",

  data() {
    return {
      selectedTab: 0,
      imgSrc: {
        todo01: require("@/assets/guide/todo01.png"),
        calendar01: require("@/assets/guide/calender01.png"),
        calendar02: require("@/assets/guide/calender02.png"),
        newTask: require("@/assets/guide/newTask.png"),
        plan01: require("@/assets/guide/plan01.jpg"),
        plan02: require("@/assets/guide/plan02.jpg"),
        plan03: require("@/assets/guide/plan03.jpg"),
        plan04: require("@/assets/guide/plan04.jpg"),
        plan05: require("@/assets/guide/plan05.jpg"),
        preset01: require("@/assets/guide/preset01.png"),
      },
      imgHeight: 500
    }
  },

  methods: {
    selectPage(pageNum){
      this.selectedTab = pageNum;
      window.scrollTo(0,0);
    }
  }
}
</script>

<style scoped>
h1 {
  text-align: center;
}
</style>