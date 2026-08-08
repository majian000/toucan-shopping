import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from './store'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css'
import './styles/global.scss'
import vPermission from './directives/permission'

const app = createApp(App)
app.use(router)
app.use(pinia)
app.use(ElementPlus, { size: 'default', locale: zhCn })
app.directive('permission', vPermission)
app.mount('#app')
