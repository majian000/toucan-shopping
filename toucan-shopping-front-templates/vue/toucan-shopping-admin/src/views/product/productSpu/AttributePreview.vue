<template>
  <div class="attr-preview">
    <div
      v-for="node in nodes"
      :key="node.attributeKeyId"
      class="attr-preview-item"
      :style="{ marginLeft: depth * 20 + 'px' }"
    >
      <div class="attr-preview-row">
        <span class="attr-preview-name">{{ node.attributeName }}：</span>
        <span class="attr-preview-value">{{ node.values.join('；') }}</span>
      </div>
      <AttributePreview
        v-if="node.children && node.children.length"
        :nodes="node.children"
        :depth="depth + 1"
      />
    </div>
  </div>
</template>

<script setup>
defineProps({
  nodes: { type: Array, default: () => [] },
  depth: { type: Number, default: 0 }
})
</script>

<style lang="scss" scoped>
.attr-preview {
  .attr-preview-item {
    margin: 4px 0;
    .attr-preview-row {
      line-height: 24px;
      font-size: 13px;
      .attr-preview-name { color: $text-secondary; }
      .attr-preview-value { color: $text-primary; }
    }
  }
}
</style>
