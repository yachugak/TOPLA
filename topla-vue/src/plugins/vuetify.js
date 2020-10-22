import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import ko from 'vuetify/src/locale/ko.ts';

Vue.use(Vuetify);

export default new Vuetify({
    lang: {
        locales: { ko },
        current: 'ko',
    }
});

