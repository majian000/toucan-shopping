<!--
表单属性设置面板组件
-->
<template>
  <div class="form-properties"> 
    <div class="properties-body">
      <el-form label-position="left" size="mini"  >

        <el-form-item  :label="t('ngform.properties.page_title')">
          <el-input v-model="config.pageTitle" ></el-input>
        </el-form-item>
        <el-divider></el-divider>

        <slot name="form-extend-properties"></slot>
      
        <el-form-item  :label="t('ngform.properties.tip')"> 
          {{t('ngform.properties.result_tip')}}
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import LocalMixin from '../../locale/mixin.js' 
export default {
  mixins: [LocalMixin],
  name: "form-properties", 
  props: {
    config: {
      type: Object,
      required: true
    } 
  },
  methods: {

  }
};
</script> 
<style>
.form-properties .properties-body {
  padding: 10px;
}

</style>
<!-- <style lang="scss">
.form-properties {
    .properties-body {
        padding: 10px;
    }
}
</style> -->