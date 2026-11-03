import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import globalPlugin from './plugins/global';

// 全局样式（变量由 vite.config.js 的 additionalData 注入到每个 sass 文件）
import './styles/index.scss';

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(globalPlugin);

app.mount('#app');
