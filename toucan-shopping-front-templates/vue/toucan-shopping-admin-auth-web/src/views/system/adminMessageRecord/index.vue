<template>
  <div class="msg-record-management">
    <h2 class="page-title">{{ route.meta.title }}</h2>
    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="关键词"><el-input v-model="searchForm.keyword" placeholder="消息标题" clearable style="width:220px" /></el-form-item>
        <el-form-item label="接收人">
          <div class="tag-input" @click="transferVisible=true" style="min-width:260px;min-height:32px;border:1px solid #dcdfe6;border-radius:4px;padding:2px 8px;cursor:pointer;display:flex;align-items:center;flex-wrap:wrap;gap:4px">
            <el-tag v-for="id in searchForm.adminIds.slice(0,3)" :key="id" closable size="small" @close="removeAdminId(id)" style="margin:2px">{{ getAdminLabel(id) }}</el-tag>
            <span v-if="searchForm.adminIds.length>3" style="color:#909399;font-size:12px">...等{{ searchForm.adminIds.length }}人</span>
            <span v-if="searchForm.adminIds.length===0" style="color:#c0c4cc;font-size:13px">点击选择接收人</span>
          </div>
        </el-form-item>
        <el-form-item label="阅读状态"><el-select v-model="searchForm.isRead" placeholder="请选择" clearable style="width:120px"><el-option label="未读" :value="0" /><el-option label="已读" :value="1" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button><el-button :icon="Refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="list" border stripe v-loading="loading" style="width:100%">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="messageTitle" label="消息标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="messageTypeName" label="消息类型" width="120" />
        <el-table-column label="推送范围" width="110" align="center"><template #default="{ row }">{{ targetLabel(row.targetType) }}</template></el-table-column>
        <el-table-column prop="adminUsername" label="接收人" width="130" show-overflow-tooltip />
        <el-table-column label="阅读状态" width="80" align="center">
          <template #default="{ row }"><el-tag :type="row.isRead===1?'success':'danger'" size="small">{{ row.isRead===1?'已读':'未读' }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="readDate" label="阅读时间" width="170" />
        <el-table-column prop="createDate" label="发送时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }"><el-button type="primary" link size="small" :icon="View" @click="handleView(row)">详情</el-button><el-button type="danger" link size="small" :icon="Delete" v-permission="'system:adminMessageRecord:delete'" @click="handleDelete(row)">删除</el-button></template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrapper"><el-pagination v-model:current-page="pag.page" v-model:page-size="pag.size" :page-sizes="[10,20,50,100]" :total="total" layout="total, sizes, prev, pager, next, jumper" background /></div>
    </el-card>
    <!-- 选择接收人穿梭弹窗 -->
    <el-dialog v-model="transferVisible" title="选择接收人" width="800px" destroy-on-close>
      <div class="transfer-box">
        <div class="transfer-left">
          <div class="transfer-title">已选 ({{ searchForm.adminIds.length }})</div>
          <div class="transfer-list">
            <div v-for="id in searchForm.adminIds" :key="id" class="transfer-item">
              <span>{{ getAdminLabel(id) }}</span>
              <el-button type="danger" link size="small" :icon="Delete" @click="removeAdminId(id)"></el-button>
            </div>
            <el-empty v-if="searchForm.adminIds.length===0" description="暂未选择" :image-size="40" />
          </div>
        </div>
        <div class="transfer-right">
          <div class="transfer-title">管理员列表</div>
          <el-input v-model="adminSearchKey" placeholder="搜索账号" clearable style="margin-bottom:8px" @input="onAdminSearch" />
          <el-table :data="adminTableData" border stripe height="320" @row-click="addAdminId" highlight-current-row style="cursor:pointer">
            <el-table-column type="index" width="50" />
            <el-table-column prop="username" label="账号" width="130" />
            <el-table-column prop="nickName" label="昵称" width="120" />
            <el-table-column prop="adminId" label="管理员ID" show-overflow-tooltip />
          </el-table>
          <el-pagination v-model:current-page="adminPage" :page-size="15" :total="adminTotal" layout="prev, next" small @current-change="loadAdminTable" style="margin-top:8px;justify-content:center" />
        </div>
      </div>
      <template #footer><el-button type="primary" @click="transferVisible=false">确定</el-button></template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="记录详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="消息标题" :span="2">{{ detail.messageTitle }}</el-descriptions-item>
        <el-descriptions-item label="消息类型">{{ detail.messageTypeName }}</el-descriptions-item>
        <el-descriptions-item label="推送范围">{{ targetLabel(detail.targetType) }}</el-descriptions-item>
        <el-descriptions-item label="接收人">{{ detail.adminUsername || detail.adminId }}</el-descriptions-item>
        <el-descriptions-item label="阅读状态"><el-tag :type="detail.isRead===1?'success':'danger'" size="small">{{ detail.isRead===1?'已读':'未读' }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="阅读时间">{{ detail.readDate || '--' }}</el-descriptions-item>
        <el-descriptions-item label="发送时间">{{ detail.createDate }}</el-descriptions-item>
        <el-descriptions-item label="消息内容" :span="2"><div class="content-preview" v-html="detail.messageContent || '--'"></div></el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="detailVisible=false">关闭</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref,reactive,watch,onMounted } from 'vue'; import { useRoute } from 'vue-router'; import { ElMessage,ElMessageBox } from 'element-plus'; import { Search,Refresh,Delete,View } from '@element-plus/icons-vue'; import { listMessageRecord,delMessageRecord } from '@/api/system/adminMessageRecord'; import { listAdmin } from '@/api/system/admin';
const route=useRoute(); const list=ref([]); const total=ref(0); const loading=ref(false); const pag=reactive({page:1,size:10}); const searchForm=reactive({keyword:'',adminIds:[],isRead:''});
const transferVisible=ref(false); const adminTableData=ref([]); const adminTotal=ref(0); const adminPage=ref(1); const adminSearchKey=ref(''); const adminLabelMap=ref({});
function getAdminLabel(id){return adminLabelMap.value[id]||id}
function removeAdminId(id){searchForm.adminIds=searchForm.adminIds.filter(i=>i!==id)}
function addAdminId(row){if(!searchForm.adminIds.includes(row.adminId)){searchForm.adminIds.push(row.adminId);adminLabelMap.value[row.adminId]=`${row.username}(${row.nickName||''})`}}
async function loadAdminTable(){try{const r=await listAdmin({page:adminPage.value,size:15,username:adminSearchKey.value||undefined});const data=r.data.list||r.data.rows||[];adminTableData.value=data;adminTotal.value=r.data.total||0;data.forEach(u=>{if(u.adminId)adminLabelMap.value[u.adminId]=`${u.username}(${u.nickName||''})`})}catch{}}
function onAdminSearch(){adminPage.value=1;loadAdminTable()}
// 打开弹窗时预加载
watch(transferVisible,v=>{if(v){adminSearchKey.value='';adminPage.value=1;loadAdminTable()}})
function targetLabel(t){const m={ALL:'全员',USER:'指定用户',POST:'指定岗位',ORG:'指定机构'};return m[t]||t}
async function fetchData(){loading.value=true;try{const p={page:pag.page,size:pag.size,keyword:searchForm.keyword||undefined,adminIds:searchForm.adminIds.length?searchForm.adminIds:undefined,isRead:searchForm.isRead!==''?searchForm.isRead:undefined};const r=await listMessageRecord(p);list.value=r.data.rows||r.data.list||[];total.value=r.data.total||0}finally{loading.value=false}}
onMounted(()=>fetchData()); function handleSearch(){pag.page=1;fetchData()} function handleReset(){searchForm.keyword='';searchForm.adminIds=[];searchForm.isRead='';pag.page=1;fetchData()}
watch(()=>pag.page,fetchData); watch(()=>pag.size,()=>{pag.page=1;fetchData()});
const detailVisible=ref(false); const detail=ref({});
function handleView(row){detail.value=row;detailVisible.value=true}
function handleDelete(row){ElMessageBox.confirm(`确认删除该条记录?`,'删除确认',{confirmButtonText:'确定',cancelButtonText:'取消',type:'warning'}).then(async()=>{try{await delMessageRecord(row.id);ElMessage.success('删除成功');fetchData()}catch{}}).catch(()=>{})}
</script>
<style lang="scss" scoped>
.msg-record-management{.search-card{margin-bottom:$gap-md;:deep(.el-card__body){padding:16px 20px 0}}.table-card{.pagination-wrapper{margin-top:$gap-md;display:flex;justify-content:flex-end}}:deep(.el-table)th{background-color:#f5f7fa;color:$text-primary;font-weight:600}}
.content-preview{min-height:120px;max-height:500px;overflow-y:auto;line-height:2;font-size:15px;word-break:break-word;padding:8px 0;:deep(img){max-width:100%}:deep(h3){margin:8px 0 4px;font-size:16px}:deep(ul),:deep(ol){padding-left:20px}:deep(li){margin:2px 0}:deep(p){margin:4px 0}}
.transfer-box{display:flex;gap:16px;.transfer-left,.transfer-right{flex:1;border:1px solid #ebeef5;border-radius:4px;padding:12px;.transfer-title{font-weight:600;margin-bottom:8px;font-size:14px}.transfer-list{max-height:350px;overflow-y:auto;.transfer-item{display:flex;justify-content:space-between;align-items:center;padding:6px 8px;border-bottom:1px solid #f2f3f5;font-size:13px;&:hover{background:#f5f7fa}}}}}
.tag-input:hover{border-color:#409eff}
</style>
