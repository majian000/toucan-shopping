<template>
  <div class="rich-editor">
    <Toolbar
      class="rich-editor__toolbar"
      :editor="editorRef"
      :default-config="toolbarConfig"
      :mode="mode"
    />
    <Editor
      class="rich-editor__body"
      :style="{ height: height + 'px' }"
      v-model="valueHtml"
      :default-config="editorConfig"
      :mode="mode"
      @onCreated="handleCreated"
    />
  </div>
</template>

<script setup>
import '@wangeditor/editor/dist/css/style.css'
import { onBeforeUnmount, ref, shallowRef, watch } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { uploadArticleImg } from '@/api/content/article'

const props = defineProps({
  modelValue: { type: String, default: '' },
  height: { type: Number, default: 400 },
  placeholder: { type: String, default: '请输入内容...' }
})

const emit = defineEmits(['update:modelValue'])

const editorRef = shallowRef()
const mode = 'default'
const valueHtml = ref(props.modelValue)

const toolbarConfig = {}

const editorConfig = {
  placeholder: props.placeholder,
  MENU_CONF: {
    uploadImage: {
      // 禁用 base64，强制走服务端上传，返回图片地址
      base64LimitSize: 0,
      customUpload(file, insertFn) {
        uploadArticleImg(file)
          .then(url => insertFn(url, file.name, url))
          .catch(err => console.error('图片上传失败', err))
      }
    }
  }
}

function handleCreated(editor) {
  editorRef.value = editor
}

// 外部内容变化时同步（编辑回显）
watch(
  () => props.modelValue,
  (val) => {
    if (val !== valueHtml.value) valueHtml.value = val
  }
)

// 编辑内容变化时回传
watch(valueHtml, (val) => emit('update:modelValue', val))

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
</script>

<style lang="scss" scoped>
.rich-editor {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  .rich-editor__toolbar {
    border-bottom: 1px solid #dcdfe6;
  }
  .rich-editor__body {
    overflow-y: auto;
    :deep(.w-e-text-container) {
      background-color: #fff;
    }
  }
}
</style>
