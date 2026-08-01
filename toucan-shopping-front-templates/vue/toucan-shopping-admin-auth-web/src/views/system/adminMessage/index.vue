<template>
    <div class="msg-management">
        <h2 class="page-title">{{ route.meta.title }}</h2>
        <el-card shadow="never" class="search-card">
            <el-form :model="searchForm" inline>
                <el-form-item label="标题">
                    <el-input v-model="searchForm.title" placeholder="请输入标题" clearable style="width:200px"/>
                </el-form-item>
                <el-form-item label="推送类型">
                    <el-select v-model="searchForm.targetType" placeholder="请选择" clearable style="width:140px">
                        <el-option label="全员" value="ALL"/>
                        <el-option label="指定用户" value="USER"/>
                        <el-option label="指定岗位" value="POST"/>
                        <el-option label="指定机构" value="ORG"/>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
                    <el-button :icon="Refresh" @click="handleReset">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>
        <el-card shadow="never" class="table-card">
            <div class="toolbar">
                <el-button type="primary" :icon="Plus" v-permission="'system:adminMessage:add'" @click="handleAdd">
                    新增消息
                </el-button>
            </div>
            <el-table :data="list" border stripe v-loading="loading" style="width:100%">
                <el-table-column type="index" label="序号" width="60" align="center"/>
                <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip/>
                <el-table-column prop="messageTypeName" label="消息类型" width="120"/>
                <el-table-column label="推送范围" width="110" align="center">
                    <template #default="{ row }">{{ targetLabel(row.targetType) }}</template>
                </el-table-column>
                <el-table-column prop="senderUsername" label="发送人" width="120"/>
                <el-table-column label="发送状态" width="90" align="center">
                    <template #default="{ row }">
                        <el-tag :type="row.sendStatus===1?'success':'info'" size="small">{{
                            row.sendStatus===1?'已发送':'未发送' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="createDate" label="创建时间" min-width="160"/>
                <el-table-column label="操作" width="200" fixed="right">
                    <template #default="{ row }">
                        <el-button type="success" link size="small" :icon="Promotion" :disabled="row.sendStatus===1"
                                   v-permission="'system:adminMessage:send'" :loading="sendingId===row.id" @click="handleSend(row)">发送
                        </el-button>
                        <el-button type="primary" link size="small" :icon="Edit" :disabled="row.sendStatus===1"
                                   v-permission="'system:adminMessage:edit'" @click="handleEdit(row)">编辑
                        </el-button>
                        <el-button type="info" link size="small" :icon="View" @click="handleView(row)">查看</el-button>
                        <el-button type="danger" link size="small" :icon="Delete"
                                   v-permission="'system:adminMessage:delete'" @click="handleDelete(row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination-wrapper">
                <el-pagination v-model:current-page="pag.page" v-model:page-size="pag.size" :page-sizes="[10,20,50,100]"
                               :total="total" layout="total, sizes, prev, pager, next, jumper" background/>
            </div>
        </el-card>

        <el-dialog v-model="dlg" :title="isEdit?'编辑消息':'新增消息'" width="700px" destroy-on-close>
            <el-form ref="fRef" :model="f" :rules="fr" label-width="100px">
                <el-form-item label="消息类型" prop="messageTypeId">
                    <el-select v-model="f.messageTypeId" placeholder="请选择" style="width:100%">
                        <el-option v-for="t in mtList" :key="t.id" :label="t.name" :value="t.id"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="标题" prop="title">
                    <el-input v-model="f.title" maxlength="100"/>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                    <div class="rich-editor">
                      <div class="editor-toolbar">
                        <button type="button" @click="execCmd('bold')" title="加粗"><b>B</b></button>
                        <button type="button" @click="execCmd('italic')" title="斜体"><i>I</i></button>
                        <button type="button" @click="execCmd('underline')" title="下划线"><u>U</u></button>
                        <button type="button" @click="execCmd('strikeThrough')" title="删除线"><s>S</s></button>
                        <span class="sep"></span>
                        <button type="button" @click="execCmd('insertUnorderedList')" title="无序列表">•≡</button>
                        <button type="button" @click="execCmd('insertOrderedList')" title="有序列表">1.</button>
                        <span class="sep"></span>
                        <button type="button" @click="execCmd('formatBlock','<h3>')" title="标题">H</button>
                        <button type="button" @click="execCmd('formatBlock','<p>')" title="段落">P</button>
                      </div>
                      <div class="editor-body" ref="editorRef" contenteditable="true" @input="onEditorInput" @paste="onEditorPaste" placeholder="请输入消息内容"></div>
                    </div>
                </el-form-item>
                <el-form-item label="跳转链接">
                    <el-input v-model="f.linkUri" placeholder="可选"/>
                </el-form-item>
                <el-form-item label="推送范围" prop="targetType">
                    <el-radio-group v-model="f.targetType" @change="onTargetChange">
                        <el-radio value="ALL">全员</el-radio>
                        <el-radio value="USER">指定用户</el-radio>
                        <el-radio value="POST">指定岗位</el-radio>
                        <el-radio value="ORG">指定机构</el-radio>
                    </el-radio-group>
                </el-form-item>
                <!-- USER: 穿梭框选择用户 -->
                <el-form-item v-if="f.targetType==='USER'" label="选择用户" prop="targetIds">
                    <div class="tag-input" @click="userTransferVisible=true" style="min-height:32px;border:1px solid #dcdfe6;border-radius:4px;padding:2px 8px;cursor:pointer;display:flex;align-items:center;flex-wrap:wrap;gap:4px">
                        <el-tag v-for="id in f.selectedUserIds.slice(0,3)" :key="id" closable size="small" @close="removeUser(id)" style="margin:2px">{{ getUserLabel(id) }}</el-tag>
                        <span v-if="f.selectedUserIds.length>3" style="color:#909399;font-size:12px">...等{{ f.selectedUserIds.length }}人</span>
                        <span v-if="f.selectedUserIds.length===0" style="color:#c0c4cc;font-size:13px">点击选择用户</span>
                    </div>
                </el-form-item>
                <!-- POST: 穿梭框选择岗位 -->
                <el-form-item v-if="f.targetType==='POST'" label="选择岗位" prop="targetIds">
                    <div class="tag-input" @click="postTransferVisible=true" style="min-height:32px;border:1px solid #dcdfe6;border-radius:4px;padding:2px 8px;cursor:pointer;display:flex;align-items:center;flex-wrap:wrap;gap:4px">
                        <el-tag v-for="id in f.selectedPostIds.slice(0,3)" :key="id" closable size="small" @close="removePost(id)" style="margin:2px">{{ getPostLabel(id) }}</el-tag>
                        <span v-if="f.selectedPostIds.length>3" style="color:#909399;font-size:12px">...等{{ f.selectedPostIds.length }}个</span>
                        <span v-if="f.selectedPostIds.length===0" style="color:#c0c4cc;font-size:13px">点击选择岗位</span>
                    </div>
                </el-form-item>
                <!-- ORG: 多选机构树 -->
                <el-form-item v-if="f.targetType==='ORG'" label="选择机构" prop="targetIds">
                    <el-select v-model="f.selectedOrgIds" multiple filterable placeholder="请选择机构" style="width:100%">
                        <el-option v-for="o in flatOrgOptions" :key="o.id" :label="o.label" :value="o.id"/>
                    </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <el-button @click="dlg=false">取消</el-button>
                <el-button type="primary" @click="handleSubmit" :loading="saving">保存</el-button>
            </template>
        </el-dialog>

        <!-- 岗位穿梭弹窗 -->
        <el-dialog v-model="postTransferVisible" title="选择岗位" width="650px" destroy-on-close>
          <div class="transfer-box">
            <div class="transfer-left"><div class="transfer-title">已选 ({{ f.selectedPostIds.length }})</div><div class="transfer-list"><div v-for="id in f.selectedPostIds" :key="id" class="transfer-item"><span>{{ getPostLabel(id) }}</span><el-button type="danger" link size="small" :icon="Delete" @click="removePost(id)"></el-button></div><el-empty v-if="f.selectedPostIds.length===0" description="暂未选择" :image-size="40" /></div></div>
            <div class="transfer-right"><div class="transfer-title">岗位列表</div><el-table :data="postOptions" border stripe height="350" highlight-current-row style="cursor:pointer" @row-click="addPost"><el-table-column type="index" width="50" /><el-table-column prop="name" label="岗位名称" /><el-table-column prop="postId" label="岗位ID" /></el-table></div>
          </div>
          <template #footer><el-button type="primary" @click="postTransferVisible=false">确定</el-button></template>
        </el-dialog>

        <!-- 用户穿梭弹窗 -->
        <el-dialog v-model="userTransferVisible" title="选择用户" width="800px" destroy-on-close>
          <div class="transfer-box">
            <div class="transfer-left"><div class="transfer-title">已选 ({{ f.selectedUserIds.length }})</div><div class="transfer-list"><div v-for="id in f.selectedUserIds" :key="id" class="transfer-item"><span>{{ getUserLabel(id) }}</span><el-button type="danger" link size="small" :icon="Delete" @click="removeUser(id)"></el-button></div><el-empty v-if="f.selectedUserIds.length===0" description="暂未选择" :image-size="40" /></div></div>
            <div class="transfer-right"><div class="transfer-title">管理员列表</div><el-input v-model="userSearchKey" placeholder="搜索账号" clearable style="margin-bottom:8px" @input="onUserSearch" /><el-table :data="userTableData" border stripe height="320" highlight-current-row style="cursor:pointer" @row-click="addUser"><el-table-column type="index" width="50" /><el-table-column prop="username" label="账号" width="130" /><el-table-column prop="nickName" label="昵称" width="120" /><el-table-column prop="adminId" label="管理员ID" show-overflow-tooltip /></el-table><el-pagination v-model:current-page="userPage" :page-size="15" :total="userTotal" layout="prev, next" small @current-change="loadUserTable" style="margin-top:8px;justify-content:center" /></div>
          </div>
          <template #footer><el-button type="primary" @click="userTransferVisible=false">确定</el-button></template>
        </el-dialog>

        <!-- 接收人列表弹窗 -->
        <el-dialog v-model="recipientVisible" :title="'接收人列表 ('+recipientList.length+'人)'" width="800px" destroy-on-close>
          <el-input v-model="recipientSearch" placeholder="搜索" clearable style="width:280px;margin-bottom:10px" />
          <el-table :data="filteredRecipients" border stripe height="400">
            <el-table-column type="index" label="序号" width="60" />
            <el-table-column v-if="recipientColType==='USER'" prop="username" label="登录账号" width="140" />
            <el-table-column prop="label" :label="recipientColLabel" show-overflow-tooltip />
          </el-table>
          <template #footer><el-button type="primary" @click="recipientVisible=false">关闭</el-button></template>
        </el-dialog>

        <!-- 查看详情弹窗 -->
        <el-dialog v-model="viewVisible" title="消息详情" width="650px" destroy-on-close>
            <el-descriptions :column="2" border v-if="viewDetail">
                <el-descriptions-item label="消息类型">{{ viewDetail.messageTypeName }}</el-descriptions-item>
                <el-descriptions-item label="发送状态">
                    <el-tag :type="viewDetail.sendStatus===1?'success':'info'" size="small">{{
                        viewDetail.sendStatus===1?'已发送':'未发送' }}
                    </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="标题" :span="2">{{ viewDetail.title }}</el-descriptions-item>
                <el-descriptions-item label="推送范围">{{ targetLabel(viewDetail.targetType) }}</el-descriptions-item>
                <el-descriptions-item label="推送目标" :span="2" v-if="viewDetail.targetType!=='ALL'">
                  <el-button type="primary" link size="small" @click="openRecipientDialog">{{ recipientBtnLabel }} ({{ viewTargetCount }})</el-button>
                </el-descriptions-item>
                <el-descriptions-item label="发送人">{{ viewDetail.senderUsername || '--' }}</el-descriptions-item>
                <el-descriptions-item label="跳转链接" :span="2">{{ viewDetail.linkUri || '--' }}</el-descriptions-item>
                <el-descriptions-item label="内容" :span="2">
                    <div class="content-preview" v-html="viewDetail.content || '--'"></div>
                </el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ viewDetail.createDate }}</el-descriptions-item>
            </el-descriptions>
            <template #footer>
                <el-button @click="viewVisible=false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>
<script setup>
    import {ref, reactive, computed, watch, onMounted, nextTick} from 'vue';
    import {useRoute} from 'vue-router';
    import {ElMessage, ElMessageBox} from 'element-plus';
    import {Plus, Search, Refresh, Delete, Edit, Promotion, View} from '@element-plus/icons-vue';
    import {
        listAdminMessage,
        addAdminMessage,
        updateAdminMessage,
        delAdminMessage,
        sendAdminMessage
    } from '@/api/system/adminMessage';
    import {listAllAdminMessageType} from '@/api/system/adminMessageType';
    import {listAdmin} from '@/api/system/admin';
    import {listPost} from '@/api/system/post';
    import {listOrgnazitionTree} from '@/api/system/orgnazition';

    const route = useRoute();
    const list = ref([]);
    const total = ref(0);
    const loading = ref(false);
    const pag = reactive({page: 1, size: 10});
    const searchForm = reactive({title: '', targetType: ''});
    const mtList = ref([]);

    async function loadTypes() {
        try {
            const r = await listAllAdminMessageType();
            mtList.value = r.data || []
        } catch {
        }
    }

    async function fetchData() {
        loading.value = true;
        try {
            const p = {
                page: pag.page,
                size: pag.size,
                title: searchForm.title || undefined,
                targetType: searchForm.targetType || undefined
            };
            const r = await listAdminMessage(p);
            list.value = r.data.rows || r.data.list || [];
            total.value = r.data.total || 0
        } finally {
            loading.value = false
        }
    }

    onMounted(() => {
        loadTypes();
        fetchData()
    });

    function handleSearch() {
        pag.page = 1;
        fetchData()
    }

    function handleReset() {
        searchForm.title = '';
        searchForm.targetType = '';
        pag.page = 1;
        fetchData()
    }

    watch(() => pag.page, fetchData);
    watch(() => pag.size, () => {
        pag.page = 1;
        fetchData()
    });

    function targetLabel(t) {
        const m = {ALL: '全员', USER: '指定用户', POST: '指定岗位', ORG: '指定机构'};
        return m[t] || t
    }

    // ===== 富文本编辑器 =====
    const editorRef = ref(null);

    function execCmd(cmd, val) {
        document.execCommand(cmd, false, val);
        editorRef.value?.focus()
    }

    function onEditorInput() {
        f.content = editorRef.value?.innerHTML || ''
    }

    function onEditorPaste(e) {
        e.preventDefault();
        const text = e.clipboardData.getData('text/plain');
        document.execCommand('insertText', false, text)
    }

    // ===== 用户穿梭框 =====
    const userTransferVisible = ref(false); const userTableData = ref([]); const userTotal = ref(0); const userPage = ref(1); const userSearchKey = ref(''); const userLabelMap = ref({});
    function getUserLabel(id){return userLabelMap.value[id]||id}
    function removeUser(id){f.selectedUserIds=f.selectedUserIds.filter(i=>i!==id)}
    function addUser(row){if(!f.selectedUserIds.includes(row.adminId)){f.selectedUserIds.push(row.adminId);userLabelMap.value[row.adminId]=`${row.username}(${row.nickName||''})`}}
    async function loadUserTable(){try{const r=await listAdmin({page:userPage.value,size:15,username:userSearchKey.value||undefined});const data=r.data.list||r.data.rows||[];userTableData.value=data;userTotal.value=r.data.total||0;data.forEach(u=>{if(u.adminId)userLabelMap.value[u.adminId]=`${u.username}(${u.nickName||''})`})}catch{}}
    function onUserSearch(){userPage.value=1;loadUserTable()}
    watch(userTransferVisible,v=>{if(v){userSearchKey.value='';userPage.value=1;loadUserTable()}})

    // ===== 岗位列表 =====
    const postOptions = ref([]); const postTransferVisible = ref(false); const postLabelMap = ref({});
    function getPostLabel(id){return postLabelMap.value[id]||id}
    function removePost(id){f.selectedPostIds=f.selectedPostIds.filter(i=>i!==id)}
    function addPost(row){if(!f.selectedPostIds.includes(row.postId)){f.selectedPostIds.push(row.postId);postLabelMap.value[row.postId]=row.name}}
    watch(postTransferVisible,v=>{if(v){postOptions.value.forEach(p=>{if(p.postId)postLabelMap.value[p.postId]=p.name})}})

    async function loadPosts() {
        try {
            const r = await listPost({page: 1, size: 200});
            postOptions.value = r.data.rows || r.data.list || [];
            postOptions.value.forEach(p => { if (p.postId) postLabelMap.value[p.postId] = p.name })
        } catch {
        }
    }

    // ===== 机构树（扁平化显示层级） =====
    const orgTreeData = ref([]);
    const flatOrgOptions = ref([]);

    function flattenTree(tree, prefix = '') {
        const result = [];
        tree.forEach(n => {
            result.push({id: String(n.id), label: prefix + n.name});
            if (n.children && n.children.length) result.push(...flattenTree(n.children, prefix + '　'))
        });
        return result;
    }

    async function loadOrgTree() {
        try {
            const r = await listOrgnazitionTree();
            orgTreeData.value = r.data || [];
            flatOrgOptions.value = flattenTree(orgTreeData.value)
        } catch {
        }
    }

    const dlg = ref(false);
    const isEdit = ref(false);
    const editId = ref(null);
    const saving = ref(false);
    const fRef = ref(null);
    const f = reactive({
        messageTypeId: null,
        title: '',
        content: '',
        linkUri: '',
        targetType: 'ALL',
        selectedUserIds: [],
        selectedPostIds: [],
        selectedOrgIds: []
    });
    const fr = {
        messageTypeId: [{required: true, message: '请选择消息类型', trigger: 'change'}],
        title: [{required: true, message: '请输入标题', trigger: 'blur'}],
        content: [{required: true, message: '请输入内容', trigger: 'blur'}],
        targetType: [{required: true, message: '请选择推送范围', trigger: 'change'}]
    };

    function getTargetIdsStr() {
        if (f.targetType === 'USER') return JSON.stringify(f.selectedUserIds);
        if (f.targetType === 'POST') return JSON.stringify(f.selectedPostIds);
        if (f.targetType === 'ORG') return JSON.stringify(f.selectedOrgIds);
        return null;
    }

    function setTargetIdsFromStr(ids, targetType) {
        if (!ids) return;
        try {
            const arr = JSON.parse(ids);
            if (targetType === 'USER') f.selectedUserIds = arr;
            if (targetType === 'POST') f.selectedPostIds = arr;
            if (targetType === 'ORG') f.selectedOrgIds = arr
        } catch {
        }
    }

    function onTargetChange() {
        f.selectedUserIds = [];
        f.selectedPostIds = [];
        f.selectedOrgIds = []
    }

    function reset() {
        f.messageTypeId = null;
        f.title = '';
        f.content = '';
        f.linkUri = '';
        f.targetType = 'ALL';
        f.selectedUserIds = [];
        if (editorRef.value) editorRef.value.innerHTML = ''
        f.selectedPostIds = [];
        f.selectedOrgIds = []
    }

    async function handleAdd() {
        isEdit.value = false;
        editId.value = null;
        reset();
        await Promise.all([loadTypes(), loadPosts(), loadOrgTree()]);
        dlg.value = true
    }

    async function handleEdit(row) {
        isEdit.value = true;
        editId.value = row.id;
        f.messageTypeId = row.messageTypeId;
        f.title = row.title;
        f.content = row.content || '';
        f.linkUri = row.linkUri || '';
        f.targetType = row.targetType || 'ALL';
        await Promise.all([loadTypes(), loadPosts(), loadOrgTree()]);
        setTargetIdsFromStr(row.targetIds, row.targetType); // 必须在 loadOrgTree 之后，确保树数据已加载
        dlg.value = true;
        await nextTick();
        if (editorRef.value) editorRef.value.innerHTML = row.content || ''
    }

    async function handleSubmit() {
        const v = await fRef.value.validate().catch(() => false);
        if (!v) return;
        saving.value = true;
        try {
            const d = {
                messageTypeId: f.messageTypeId,
                title: f.title,
                content: f.content,
                linkUri: f.linkUri,
                targetType: f.targetType,
                targetIds: getTargetIdsStr()
            };
            isEdit.value ? await updateAdminMessage({id: editId.value, ...d}) : await addAdminMessage(d);
            ElMessage.success(isEdit.value ? '编辑成功' : '新增成功');
            dlg.value = false;
            reset();
            fetchData()
        } finally {
            saving.value = false
        }
    }

    const sendingId = ref(null)
    async function handleSend(row) {
        ElMessageBox.confirm(`确认立即发送消息「${row.title}」?`, '发送确认', {
            confirmButtonText: '确认发送',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(async () => {
            try {
                sendingId.value = row.id
                await sendAdminMessage(row);
                ElMessage.success('发送成功');
                fetchData()
            } catch {
            } finally {
                sendingId.value = null
            }
        }).catch(() => {
        })
    }

    const viewVisible = ref(false);
    const viewDetail = ref(null);
    const viewTargetList = ref([]);
    const viewTargetCount = ref(0);
    const recipientVisible = ref(false);
    const recipientList = ref([]);
    const recipientSearch = ref('');
    const recipientColLabel = ref('接收人');
    const recipientColType = ref('USER');
    const recipientBtnLabel = ref('查看接收人');
    const filteredRecipients = computed(() => {
      if (!recipientSearch.value) return recipientList.value
      const kw = recipientSearch.value.toLowerCase()
      return recipientList.value.filter(r => r.label.toLowerCase().includes(kw))
    });

    function openRecipientDialog() {
      recipientList.value = viewTargetList.value; recipientSearch.value = ''; recipientVisible.value = true
    }

    async function handleView(row) {
        viewDetail.value = row; viewTargetList.value = []; viewTargetCount.value = 0; viewVisible.value = true
        if (row.targetType === 'ALL' || !row.targetIds) return
        try {
            const ids = JSON.parse(row.targetIds)
            let list = []
            if (row.targetType === 'USER') {
                recipientColLabel.value = '昵称'; recipientColType.value = 'USER'; recipientBtnLabel.value = '查看接收人'
                for (const id of ids) {
                    try {
                        const r = await listAdmin({ page: 1, size: 1, adminId: id })
                        const u = (r.data?.list || r.data?.rows || [])[0]
                        list.push({ username: u?.username || '', label: u?.nickName || '' })
                    } catch { list.push({ username: id, label: '' }) }
                }
            } else if (row.targetType === 'POST') {
                recipientColLabel.value = '岗位名称'; recipientColType.value = 'POST'; recipientBtnLabel.value = '查看接收岗位'; await loadPosts()
                list = ids.map(id => ({ label: postLabelMap.value[id] || id }))
            } else if (row.targetType === 'ORG') {
                recipientColLabel.value = '组织机构'; recipientColType.value = 'ORG'; recipientBtnLabel.value = '查看接收组织机构'; await loadOrgTree()
                list = ids.map(id => {
                    const o = flatOrgOptions.value.find(f => f.id === String(id))
                    return { label: o ? o.label : id }
                })
            }
            viewTargetList.value = list; viewTargetCount.value = list.length
        } catch {}
    }

    function handleDelete(row) {
        ElMessageBox.confirm(`确认删除"${row.title}"?`, '删除确认', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(async () => {
            try {
                await delAdminMessage(row.id);
                ElMessage.success('删除成功');
                fetchData()
            } catch {
            }
        }).catch(() => {
        })
    }
</script>
<style lang="scss" scoped>
    .msg-management {
        .search-card {
            margin-bottom: $gap-md;

            :deep(.el-card__body) {
                padding: 16px 20px 0
            }
        }

        .table-card {
            .toolbar {
                margin-bottom: $gap-md
            }

            .pagination-wrapper {
                margin-top: $gap-md;
                display: flex;
                justify-content: flex-end
            }
        }

        :deep(.el-table) th {
            background-color: #f5f7fa;
            color: $text-primary;
            font-weight: 600
        }
    }

    .rich-editor {
        border: 1px solid #dcdfe6;
        border-radius: 4px;
        width: 100%;
        .editor-toolbar {
            display: flex;
            gap: 2px;
            padding: 6px 8px;
            border-bottom: 1px solid #dcdfe6;
            background: #f5f7fa;
            button {
                width: 28px;
                height: 28px;
                border: 1px solid transparent;
                background: transparent;
                border-radius: 3px;
                cursor: pointer;
                font-size: 13px;
                color: #606266;
                display: flex;
                align-items: center;
                justify-content: center;
                &:hover { background: #e6e8eb; border-color: #c0c4cc; }
            }
            .sep {
                width: 1px;
                background: #dcdfe6;
                margin: 4px 4px;
            }
        }
        .editor-body {
            min-height: 180px;
            max-height: 400px;
            overflow-y: auto;
            padding: 10px 12px;
            font-size: 14px;
            line-height: 1.8;
            outline: none;
            &:empty:before {
                content: attr(placeholder);
                color: #c0c4cc;
            }
        }
    }

    .tag-input:hover { border-color: #409eff; }
    .transfer-box { display:flex;gap:16px;.transfer-left,.transfer-right{flex:1;border:1px solid #ebeef5;border-radius:4px;padding:12px;.transfer-title{font-weight:600;margin-bottom:8px;font-size:14px}.transfer-list{max-height:350px;overflow-y:auto;.transfer-item{display:flex;justify-content:space-between;align-items:center;padding:6px 8px;border-bottom:1px solid #f2f3f5;font-size:13px;&:hover{background:#f5f7fa}}}}}
    .content-preview {
        max-height: 400px;
        overflow-y: auto;
        line-height: 1.8;
        font-size: 14px;
        word-break: break-word;
        :deep(img) { max-width: 100%; }
        :deep(h3) { margin: 8px 0 4px; font-size: 16px; }
        :deep(ul), :deep(ol) { padding-left: 20px; }
        :deep(li) { margin: 2px 0; }
    }
</style>
