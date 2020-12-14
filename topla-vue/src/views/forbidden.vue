<template>
  <div style="height: 96vh;" class="error text-center pt-5">
    <span class="text-h5">이 페이지에 접속할 권한이 없습니다.</span><br>
    <v-btn color="primary" @click="$router.push('/todolist')">메인 페이지로 돌아가기</v-btn>
  </div>
</template>

<script>
export default {
name: "forbidden"
}
</script>

<style scoped>

</style>