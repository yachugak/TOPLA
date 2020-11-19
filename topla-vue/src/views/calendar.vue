<template>
  <div>
    <v-sheet height="600">
      <v-calendar
          ref="calendar"
          v-model="value"
          :weekdays="weekday"
          :type="type"
          :events="events"
          :event-overlap-mode="mode"
          :event-overlap-threshold="30"
          :event-color="getEventColor"
          @change="getEvents"
      ></v-calendar>
    </v-sheet>
  </div>
</template>

<script>
export default {
  data: () => ({
    type: 'month',
    mode: 'stack',
    weekday: [0, 1, 2, 3, 4, 5, 6],
    value: '',
    events: [],
    colors: ['blue', 'indigo', 'deep-purple', 'cyan', 'green', 'orange', 'grey darken-1'],
  }),
  methods: {
    async getEvents () {

      let events=[]
      let task = await this.$axios.get("/task/list")

      for (let i = 0;i<task.data.length;i++)
      {
        events.push({
          name: task.data[i].title,
          start: new Date(`${task.data[i].dueDate}`),
          end: new Date(`${task.data[i].dueDate}`),
          color: this.colors[3],
          timed: false,
        })
      }
      console.log(events)

      this.events = events
    },
    getEventColor (event) {
      return event.color
    },
  },
}
</script>

<style scoped>

</style>