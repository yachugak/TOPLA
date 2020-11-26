<template>
  <v-row>
    <v-col
        cols="12"
        sm="6"
    >
      <v-menu
          v-model="menu2"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-btn
              outlined
              class="mr-4"
              color="grey darken-2"
              v-model="date"
              label="Picker in menu"
              v-bind="attrs"
              v-on="on"
          >
            {{date}}
          </v-btn>
        </template>
        <v-date-picker
            v-model="date"
            type="month"
            @input="menu2 = false"
        ></v-date-picker>

      </v-menu>
    </v-col>
  </v-row>
</template>

<script>
export default {
  data: () => ({
    date: new Date().toISOString().substr(0, 7),
    menu: false,
    menu2: false,
  }),
}
</script>

<style scoped>

</style>