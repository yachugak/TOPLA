import Vue from 'vue';
import vuetify from '@/plugins/vuetify'
import VuetifyDialog from 'vuetify-dialog'
import 'vuetify-dialog/dist/vuetify-dialog.css'

Vue.use(VuetifyDialog, {
    context: {
        vuetify,
    },
    error: {
        actions: {
            true: {
                text: "이런..."
            }
        }
    },

    confirm: {
        actions: {
            true: {
                text: "예"
            },
            false: {
                text: "아니오"
            }
        }
    }
})