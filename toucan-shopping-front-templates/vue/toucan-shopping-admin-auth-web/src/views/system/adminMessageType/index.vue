<template>
  <div class="msg-type-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="类型名称"><el-input v-model="searchForm.name" placeholder="请输入" clearable style="width:200px" /></el-form-item>
        <el-form-item><el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button><el-button :icon="Refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="table-card">
      <div class="toolbar"><el-button type="primary" :icon="Plus" v-permission="'system:adminMessageCategory:add'" @click="handleAdd">新增类型</el-button></div>
      <el-table :data="list" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="name" label="类型名称" width="150" />
        <el-table-column prop="code" label="编码" width="150" />
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }"><el-button type="primary" link size="small" :icon="Edit" v-permission="'system:adminMessageCategory:edit'" @click="handleEdit(row)">编辑</el-button><el-button type="danger" link size="small" :icon="Delete" v-permission="'system:adminMessageCategory:delete'" @click="handleDelete(row)">删除</el-button></template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrapper"><el-pagination v-model:current-page="pag.page" v-model:page-size="pag.size" :page-sizes="[10,20,50,100]" :total="total" layout="total, sizes, prev, pager, next, jumper" background /></div>
    </el-card>
    <el-dialog v-model="dlg" :title="isEdit?'编辑类型':'新增类型'" width="480px" destroy-on-close>
      <el-form ref="fRef" :model="f" :rules="fr" label-width="100px">
        <el-form-item label="类型名称" prop="name"><el-input v-model="f.name" maxlength="50" /></el-form-item>
        <el-form-item label="编码" prop="code"><el-input v-model="f.code" maxlength="50" /></el-form-item>
        <el-form-item label="备注"><el-input v-model="f.remark" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dlg=false">取消</el-button><el-button type="primary" @click="handleSubmit" :loading="saving">确定</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref,reactive,computed,watch,onMounted } from 'vue'; import { useRoute } from 'vue-router'; import { ElMessage,ElMessageBox } from 'element-plus'; import { Plus,Search,Refresh,Delete,Edit } from '@element-plus/icons-vue'; import { listAdminMessageType,addAdminMessageType,updateAdminMessageType,delAdminMessageType } from '@/api/system/adminMessageType';
const route=useRoute(); const list=ref([]); const total=ref(0); const loading=ref(false); const pag=reactive({page:1,size:10}); const searchForm=reactive({name:''});
async function fetchData(){loading.value=true;try{const p={page:pag.page,size:pag.size,name:searchForm.name||undefined};const r=await listAdminMessageType(p);list.value=r.data.rows||r.data.list||[];total.value=r.data.total||0}finally{loading.value=false}}
onMounted(()=>fetchData()); function handleSearch(){pag.page=1;fetchData()} function handleReset(){searchForm.name='';pag.page=1;fetchData()}
watch(()=>pag.page,fetchData); watch(()=>pag.size,()=>{pag.page=1;fetchData()});
const dlg=ref(false); const isEdit=ref(false); const editId=ref(null); const saving=ref(false); const fRef=ref(null); const f=reactive({name:'',code:'',remark:''}); const fr={name:[{required:true,message:'请输入类型名称',trigger:'blur'}],code:[{required:true,message:'请输入编码',trigger:'blur'}]};
function reset(){f.name='';f.code='';f.remark=''}
function handleAdd(){isEdit.value=false;editId.value=null;reset();dlg.value=true}
function handleEdit(row){isEdit.value=true;editId.value=row.id;f.name=row.name;f.code=row.code;f.remark=row.remark;dlg.value=true}
async function handleSubmit(){const v=await fRef.value.validate().catch(()=>false);if(!v)return;saving.value=true;try{isEdit.value?await updateAdminMessageType({id:editId.value,name:f.name,code:f.code,remark:f.remark}):await addAdminMessageType({name:f.name,code:f.code,remark:f.remark});ElMessage.success(isEdit.value?'编辑成功':'新增成功');dlg.value=false;reset();fetchData()}finally{saving.value=false}}
function handleDelete(row){ElMessageBox.confirm(`确认删除"${row.name}"?`,'删除确认',{confirmButtonText:'确定',cancelButtonText:'取消',type:'warning'}).then(async()=>{try{await delAdminMessageType(row.id);ElMessage.success('删除成功');fetchData()}catch{}}).catch(()=>{})}
</script>
<style lang="scss" scoped>
.msg-type-management{.search-card{margin-bottom:$gap-md;:deep(.el-card__body){padding:16px 20px 0}}.table-card{.toolbar{margin-bottom:$gap-md}.pagination-wrapper{margin-top:$gap-md;display:flex;justify-content:flex-end}}:deep(.el-table)th{background-color:#f5f7fa;color:$text-primary;font-weight:600}}
</style>
