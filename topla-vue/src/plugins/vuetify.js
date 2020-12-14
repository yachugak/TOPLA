import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import ko from 'vuetify/src/locale/ko.ts';

Vue.use(Vuetify);

export default new Vuetify({
    lang: {
        locales: { ko },
        current: 'ko',
    },

    theme: {
        themes:{
            light: {
                primary: '#F8BBD0',
                secondary: '#ffcc80',
                accent: '#82B1FF',
                error: '#FF5252',
                info: '#2196F3',
                success: '#4CAF50',
                warning: '#FFC107',
                back: "#F5F5F6",
            }
        }
    }
});

