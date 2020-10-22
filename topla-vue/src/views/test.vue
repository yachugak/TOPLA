<template>
  <div class="secondary">
    <task-info-form v-model="formData"></task-info-form>
  </div>
</template>

<script>
import taskInfoForm from "@/components/taskInfoForm";

export default {
  name: "test",

  components: {
    taskInfoForm
  },

  data() {
    return {
      formData: null
    }
  }
}
</script>

<style scoped>

</style>