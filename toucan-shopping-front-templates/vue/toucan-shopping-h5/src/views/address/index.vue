<template>
  <div class="address page-scroll safe-top">
    <div class="address-head">
      <h2 class="address-title">收货地址</h2>
      <button class="add-btn" @click="openForm()">新增</button>
    </div>

    <!-- 地址列表 -->
    <div v-if="list.length" class="address-list">
      <div v-for="item in list" :key="item.id" class="address-card">
        <div class="address-line1">
          <span class="addr-name">{{ item.name }}</span>
          <span class="addr-phone">{{ item.phone }}</span>
          <span v-if="item.defaultStatus == 1" class="addr-default">默认</span>
        </div>
        <div class="addr-full">{{ item.provinceName }}{{ item.cityName }}{{ item.areaName }} {{ item.address }}</div>
        <div class="addr-actions">
          <button class="mini-btn" @click="openForm(item)">编辑</button>
          <button v-if="item.defaultStatus != 1" class="mini-btn" @click="handleSetDefault(item)">设为默认</button>
          <button class="mini-btn danger" @click="handleDelete(item)">删除</button>
        </div>
      </div>
    </div>
    <div v-else-if="!loading" class="address-empty">暂无收货地址</div>
    <div v-if="loading" class="address-loading">加载中...</div>

    <!-- 新增/编辑表单 -->
    <div v-if="showForm" class="form-mask">
      <div class="form-card">
        <div class="form-head">
          <span>{{ form.id ? '编辑地址' : '新增地址' }}</span>
          <span class="form-close" @click="showForm = false">✕</span>
        </div>
        <div class="form-body">
          <input v-model.trim="form.name" placeholder="收货人" class="field" />
          <input v-model.trim="form.phone" placeholder="手机号" class="field" />
          <div class="field-row">
            <input v-model.trim="form.provinceName" placeholder="省" class="field half" />
            <input v-model.trim="form.provinceCode" placeholder="省编码" class="field half" />
          </div>
          <div class="field-row">
            <input v-model.trim="form.cityName" placeholder="市" class="field half" />
            <input v-model.trim="form.cityCode" placeholder="市编码" class="field half" />
          </div>
          <div class="field-row">
            <input v-model.trim="form.areaName" placeholder="区/县" class="field half" />
            <input v-model.trim="form.areaCode" placeholder="区编码" class="field half" />
          </div>
          <textarea v-model.trim="form.address" placeholder="详细地址" class="field textarea"></textarea>
          <button class="submit-btn" :disabled="saving" @click="handleSave">
            {{ saving ? '保存中...' : '保存' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { addressList, saveAddress, deleteAddress, setDefaultAddress } from '@/api/address';
import { toast, toastSuccess } from '@/utils/toast';
import { confirm } from '@/utils/toast';

defineOptions({ name: 'Address' });

const list = ref([]);
const loading = ref(true);
const showForm = ref(false);
const saving = ref(false);
const form = ref({});

async function load() {
  try {
    const res = await addressList({ page: 1, limit: 100 });
    const data = (res && res.data) || {};
    list.value = data.list || [];
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    loading.value = false;
  }
}

onMounted(load);

function openForm(item) {
  form.value = item ? { ...item } : {};
  showForm.value = true;
}

async function handleSave() {
  const f = form.value;
  if (!f.name) { toast('请输入收货人', 'warning'); return; }
  if (!f.phone) { toast('请输入手机号', 'warning'); return; }
  if (!f.address) { toast('请输入详细地址', 'warning'); return; }
  if (!f.provinceName || !f.areaName) { toast('请填写省/市区', 'warning'); return; }

  saving.value = true;
  try {
    await saveAddress(f);
    toastSuccess('保存成功');
    showForm.value = false;
    load();
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  } finally {
    saving.value = false;
  }
}

async function handleDelete(item) {
  confirm({ title: '提示', message: '确定删除该地址吗？', danger: true }).then(async ok => {
    if (!ok) return;
    try {
      await deleteAddress({ id: item.id });
      toastSuccess('删除成功');
      load();
    } catch (e) {
      // 错误提示已在 request.js 统一处理
    }
  });
}

async function handleSetDefault(item) {
  try {
    await setDefaultAddress({ id: item.id });
    toastSuccess('设置成功');
    load();
  } catch (e) {
    // 错误提示已在 request.js 统一处理
  }
}
</script>

<style lang="scss" scoped>
.address {
  padding: $gap-md;

  .address-head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $gap-md;

    .address-title {
      margin: 0;
      font-size: 18px;
      color: $text-primary;
    }
    .add-btn {
      border: none;
      background: $primary;
      color: #fff;
      border-radius: $radius-sm;
      padding: 6px 16px;
      font-size: 14px;
      cursor: pointer;
    }
  }

  .address-list {
    .address-card {
      padding: $gap-md;
      margin-bottom: $gap-sm;
      background: $bg-card;
      border-radius: $radius-md;

      .address-line1 {
        display: flex;
        align-items: center;
        gap: $gap-sm;

        .addr-name {
          font-size: 15px;
          font-weight: 600;
          color: $text-primary;
        }
        .addr-phone {
          font-size: 14px;
          color: $text-regular;
        }
        .addr-default {
          font-size: 11px;
          color: #fff;
          background: $primary;
          border-radius: 3px;
          padding: 1px 6px;
        }
      }
      .addr-full {
        margin-top: 6px;
        font-size: 14px;
        color: $text-regular;
        line-height: 1.5;
      }
      .addr-actions {
        margin-top: $gap-sm;
        display: flex;
        gap: $gap-md;

        .mini-btn {
          border: none;
          background: none;
          color: $primary;
          font-size: 13px;
          cursor: pointer;
          padding: 0;

          &.danger {
            color: $danger;
          }
        }
      }
    }
  }

  .address-empty,
  .address-loading {
    text-align: center;
    color: $text-secondary;
    padding: $gap-lg 0;
  }

  .form-mask {
    position: fixed;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.45);
    z-index: 1000;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 8vw;

    .form-card {
      width: 100%;
      max-width: 360px;
      background: #fff;
      border-radius: 12px;
      padding: 16px;

      .form-head {
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 16px;
        font-weight: 600;
        color: $text-primary;
        margin-bottom: 14px;

        .form-close {
          cursor: pointer;
          color: $text-secondary;
        }
      }

      .form-body {
        .field {
          width: 100%;
          height: 42px;
          margin-bottom: 12px;
          padding: 0 12px;
          border: 1px solid $border-color;
          border-radius: $radius-sm;
          font-size: 14px;
          outline: none;

          &:focus {
            border-color: $primary;
          }
        }
        .field-row {
          display: flex;
          gap: $gap-sm;

          .field.half {
            flex: 1;
          }
        }
        .textarea {
          height: 64px;
          padding: 10px 12px;
          resize: none;
        }
        .submit-btn {
          width: 100%;
          height: 42px;
          border: none;
          border-radius: $radius-sm;
          background: $primary;
          color: #fff;
          font-size: 15px;
          cursor: pointer;

          &:disabled {
            opacity: 0.6;
          }
        }
      }
    }
  }
}
</style>
