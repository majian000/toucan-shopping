<template>
  <!-- 有子菜单：显示 el-sub-menu 带展开箭头 -->
  <el-sub-menu v-if="hasChildren" :index="item.path || item.label">
    <template #title>
      <el-icon v-if="iconComp"><component :is="iconComp" /></el-icon>
      <span>{{ item.label }}</span>
    </template>
    <SideMenuItem
      v-for="(child, idx) in item.children"
      :key="child.path || child.label || idx"
      :item="child"
      @select="onSelect"
    />
  </el-sub-menu>

  <!-- 叶子菜单：直接导航 -->
  <el-menu-item v-else :index="item.path" @click="onSelect(item.path)">
    <el-icon v-if="iconComp"><component :is="iconComp" /></el-icon>
    <template #title>{{ item.label }}</template>
  </el-menu-item>
</template>

<script setup>
import { computed } from 'vue'
import * as Icons from '@element-plus/icons-vue'

const props = defineProps({
  item: { type: Object, required: true }
})

const emit = defineEmits(['select'])

const hasChildren = computed(() => {
  return props.item.children && props.item.children.length > 0
})

const iconComp = computed(() => {
  const name = props.item.icon
  return name ? Icons[name] : null
})

function onSelect(path) {
  if (path) emit('select', path)
}
</script>
