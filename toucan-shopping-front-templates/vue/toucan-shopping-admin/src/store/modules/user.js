import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref({})

  function setUserInfo(data) {
    userInfo.value = data
  }

  function clearUser() {
    userInfo.value = {}
  }

  return { userInfo, setUserInfo, clearUser }
})
