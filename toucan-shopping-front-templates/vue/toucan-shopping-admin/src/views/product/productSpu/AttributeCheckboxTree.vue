<template>
  <div class="attr-tree">
    <div v-for="node in nodes" :key="node.id" class="attr-key" :style="{ marginLeft: depth * 20 + 'px' }">
      <div class="attr-key-name">{{ node.attributeName }}</div>
      <div v-if="node.values && node.values.length" class="attr-values">
        <el-checkbox
          v-for="v in node.values"
          :key="v.id"
          :model-value="selectedValueIds.includes(String(v.id))"
          @change="checked => $emit('toggle', node, v, checked)"
        >{{ v.attributeValue }}</el-checkbox>
      </div>
      <AttributeCheckboxTree
        v-if="node.children && node.children.length"
        :nodes="node.children"
        :selected-value-ids="selectedValueIds"
        :depth="depth + 1"
        @toggle="(n, v, c) => $emit('toggle', n, v, c)"
      />
    </div>
  </div>
</template>

<script setup>
defineProps({
  nodes: { type: Array, default: () => [] },
  selectedValueIds: { type: Array, default: () => [] },
  depth: { type: Number, default: 0 }
})
defineEmits(['toggle'])
</script>

<style lang="scss" scoped>
.attr-tree {
  .attr-key {
    margin: 8px 0;
    .attr-key-name { font-weight: 600; color: $text-primary; margin-bottom: 6px; }
    .attr-values { display: flex; flex-wrap: wrap; gap: 4px 16px; }
  }
}
</style>
