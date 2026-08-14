<template>
  <div class="order-management">
    <h2 class="page-title">订单列表</h2>

    <el-card shadow="never" class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="主订单编号">
          <el-input v-model="searchForm.mainOrderNo" placeholder="请输入主订单编号" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单编号" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="店铺ID">
          <el-input v-model="searchForm.shopId" placeholder="请输入店铺ID" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="支付状态">
          <el-select v-model="searchForm.payStatus" placeholder="全部" clearable style="width:130px">
            <el-option label="未支付" :value="0" />
            <el-option label="已支付" :value="1" />
            <el-option label="取消支付" :value="4" />
            <el-option label="已退款" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="下单时间">
          <el-date-picker
            v-model="searchForm.startCreateDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="开始时间" style="width:200px"
          />
        </el-form-item>
        <el-form-item label="至">
          <el-date-picker
            v-model="searchForm.endCreateDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="结束时间" style="width:200px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="table-card">
      <el-tabs v-model="activeTradeStatus" @tab-change="handleTabChange">
        <el-tab-pane v-for="tab in tradeTabs" :key="tab.value" :label="tab.label" :name="String(tab.value)" />
      </el-tabs>

      <el-table ref="tableRef" :data="tableData" border stripe v-loading="loading" row-key="id">
        <el-table-column prop="id" label="主键" width="180" show-overflow-tooltip />
        <el-table-column prop="mainOrderNo" label="主订单编号" width="220" show-overflow-tooltip />
        <el-table-column prop="orderNo" label="订单编号" width="220" show-overflow-tooltip />
        <el-table-column prop="userId" label="用户ID" width="160" show-overflow-tooltip />
        <el-table-column prop="shopId" label="店铺ID" width="160" show-overflow-tooltip />
        <el-table-column prop="payMethod" label="支付方式" width="100" align="center">
          <template #default="{ row }">{{ payMethodText(row.payMethod) }}</template>
        </el-table-column>
        <el-table-column prop="payType" label="支付渠道" width="100" align="center">
          <template #default="{ row }">{{ payTypeText(row.payType) }}</template>
        </el-table-column>
        <el-table-column prop="payStatus" label="支付状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="payStatusTag(row.payStatus)" size="small">{{ payStatusText(row.payStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="tradeStatus" label="交易状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="tradeStatusTag(row.tradeStatus)" size="small">{{ tradeStatusText(row.tradeStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderAmount" label="订单金额" width="110" align="right" />
        <el-table-column prop="payAmount" label="付款金额" width="110" align="right" />
        <el-table-column prop="freightAmount" label="运费金额" width="100" align="right" />
        <el-table-column prop="outerTradeNo" label="交易流水号" width="180" show-overflow-tooltip />
        <el-table-column prop="srcType" label="购买渠道" width="90" align="center">
          <template #default="{ row }">{{ srcTypeText(row.srcType) }}</template>
        </el-table-column>
        <el-table-column prop="payDate" label="支付时间" width="170" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="cancelDate" label="取消时间" width="170" />
        <el-table-column label="操作" width="280" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="View" v-permission="'toucan:order:item:list'" @click="handleView(row)">查看</el-button>
            <el-button type="primary" link size="small" :icon="Edit" v-permission="'toucan:order:update'" @click="handleEdit(row)">修改</el-button>
            <el-button type="warning" link size="small" :icon="Goods" v-permission="'toucan:order:list:item:updatesFromOrderList'" @click="handleModifyItems(row)">修改订单项</el-button>
            <el-button type="info" link size="small" :icon="Document" v-permission="'toucan:order:orderLogList'" @click="handleOrderLog(row)">订单日志</el-button>
            <el-button
              type="danger" link size="small" :icon="CircleClose"
              v-permission="'toucan:order:cancel'"
              :disabled="row.tradeStatus === 2" @click="handleCancel(row)"
            >取消</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="pagination.page" v-model:page-size="pagination.limit"
          :page-sizes="[15, 30, 100, 200]" layout="total, sizes, prev, pager, next"
          :total="pagination.total" @size-change="handleSearch" @current-change="loadTableData"
        />
      </div>
    </el-card>

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="1000px" :close-on-click-modal="false" destroy-on-close>
      <div v-loading="detailLoading">
        <template v-if="detailRow">
          <el-descriptions title="订单信息" :column="3" border>
            <el-descriptions-item label="主订单编号">{{ detailRow.mainOrderNo }}</el-descriptions-item>
            <el-descriptions-item label="订单编号">{{ detailRow.orderNo }}</el-descriptions-item>
            <el-descriptions-item label="用户ID">{{ detailRow.userId }}</el-descriptions-item>
            <el-descriptions-item label="店铺ID">{{ detailRow.shopId }}</el-descriptions-item>
            <el-descriptions-item label="支付方式">{{ payMethodText(detailRow.payMethod) }}</el-descriptions-item>
            <el-descriptions-item label="支付渠道">{{ payTypeText(detailRow.payType) }}</el-descriptions-item>
            <el-descriptions-item label="支付状态">
              <el-tag :type="payStatusTag(detailRow.payStatus)" size="small">{{ payStatusText(detailRow.payStatus) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="交易状态">
              <el-tag :type="tradeStatusTag(detailRow.tradeStatus)" size="small">{{ tradeStatusText(detailRow.tradeStatus) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="购买渠道">{{ srcTypeText(detailRow.srcType) }}</el-descriptions-item>
            <el-descriptions-item label="订单金额">{{ detailRow.orderAmount }}</el-descriptions-item>
            <el-descriptions-item label="付款金额">{{ detailRow.payAmount }}</el-descriptions-item>
            <el-descriptions-item label="运费金额">{{ detailRow.freightAmount }}</el-descriptions-item>
            <el-descriptions-item label="红包金额">{{ detailRow.redPackageAmount }}</el-descriptions-item>
            <el-descriptions-item label="优惠券金额">{{ detailRow.couponAmount }}</el-descriptions-item>
            <el-descriptions-item label="交易流水号">{{ detailRow.outerTradeNo }}</el-descriptions-item>
            <el-descriptions-item label="支付时间">{{ detailRow.payDate }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ detailRow.createDate }}</el-descriptions-item>
            <el-descriptions-item label="取消时间">{{ detailRow.cancelDate }}</el-descriptions-item>
            <el-descriptions-item label="取消备注" :span="2">{{ detailRow.cancelRemark }}</el-descriptions-item>
            <el-descriptions-item label="订单备注" :span="3">{{ detailRow.remark }}</el-descriptions-item>
          </el-descriptions>

          <el-descriptions title="收货人信息" :column="3" border style="margin-top: 16px">
            <el-descriptions-item label="收货人">{{ detailRow.orderConsigneeAddress?.name }}</el-descriptions-item>
            <el-descriptions-item label="收货人电话">{{ detailRow.orderConsigneeAddress?.phone }}</el-descriptions-item>
            <el-descriptions-item label="省份">{{ detailRow.orderConsigneeAddress?.provinceName }}</el-descriptions-item>
            <el-descriptions-item label="地市">{{ detailRow.orderConsigneeAddress?.cityName }}</el-descriptions-item>
            <el-descriptions-item label="区县">{{ detailRow.orderConsigneeAddress?.areaName }}</el-descriptions-item>
            <el-descriptions-item label="收货地址" :span="3">{{ detailRow.orderConsigneeAddress?.address }}</el-descriptions-item>
          </el-descriptions>

          <div class="sub-table-title">订单项列表</div>
          <el-table :data="detailItems" border stripe size="small" v-loading="detailItemLoading">
            <el-table-column prop="skuId" label="商品SKUID" width="160" show-overflow-tooltip />
            <el-table-column prop="productSkuName" label="商品名称" min-width="200" show-overflow-tooltip />
            <el-table-column label="商品预览" width="120" align="center">
              <template #default="{ row }">
                <el-image
                  v-if="row.httpProductPreviewPath"
                  :src="row.httpProductPreviewPath"
                  :preview-src-list="[row.httpProductPreviewPath]"
                  preview-teleported fit="cover"
                  style="width:50px;height:50px;cursor:zoom-in"
                />
              </template>
            </el-table-column>
            <el-table-column prop="deliveryStatus" label="配送状态" width="100" align="center">
              <template #default="{ row }">{{ deliveryStatusText(row.deliveryStatus) }}</template>
            </el-table-column>
            <el-table-column prop="sellerStatus" label="备货状态" width="100" align="center">
              <template #default="{ row }">{{ sellerStatusText(row.sellerStatus) }}</template>
            </el-table-column>
            <el-table-column prop="buyerStatus" label="买家状态" width="100" align="center">
              <template #default="{ row }">{{ buyerStatusText(row.buyerStatus) }}</template>
            </el-table-column>
            <el-table-column prop="productPrice" label="商品单价" width="100" align="right" />
            <el-table-column prop="productNum" label="购买数量" width="90" align="center" />
            <el-table-column prop="orderItemAmount" label="总金额" width="110" align="right" />
            <el-table-column prop="deliveryReceiveTime" label="收货时间" width="170" />
          </el-table>
          <div class="pagination-wrap">
            <el-pagination
              v-model:current-page="detailItemPage" v-model:page-size="detailItemLimit"
              :page-sizes="[15, 30, 100]" layout="total, sizes, prev, pager, next"
              :total="detailItemTotal" @size-change="loadDetailItems" @current-change="loadDetailItems"
            />
          </div>
        </template>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 修改订单弹窗 -->
    <el-dialog v-model="editVisible" title="修改订单" width="760px" :close-on-click-modal="false" destroy-on-close>
      <el-form ref="editFormRef" :model="editForm" label-width="130px" v-loading="editLoading">
        <el-divider content-position="left">订单信息</el-divider>
        <el-form-item label="主订单编号"><el-input :model-value="editForm.mainOrderNo" disabled /></el-form-item>
        <el-form-item label="订单编号"><el-input :model-value="editForm.orderNo" disabled /></el-form-item>
        <el-form-item label="运费金额"><el-input v-model="editForm.freightAmount" /></el-form-item>
        <el-form-item label="红包金额"><el-input v-model="editForm.redPackageAmount" /></el-form-item>
        <el-form-item label="优惠券金额"><el-input v-model="editForm.couponAmount" /></el-form-item>
        <el-form-item label="订单金额(不算折扣)"><el-input v-model="editForm.totalAmount" /></el-form-item>
        <el-form-item label="订单金额(折扣算完)"><el-input v-model="editForm.orderAmount" /></el-form-item>
        <el-form-item label="付款金额"><el-input v-model="editForm.payAmount" /></el-form-item>
        <el-form-item label="交易流水号"><el-input v-model="editForm.outerTradeNo" /></el-form-item>
        <el-form-item label="支付状态">
          <el-select v-model="editForm.payStatus" style="width:100%">
            <el-option label="未支付" :value="0" />
            <el-option label="已支付" :value="1" />
            <el-option label="取消支付" :value="4" />
            <el-option label="已退款" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易状态">
          <el-select v-model="editForm.tradeStatus" style="width:100%">
            <el-option label="待付款" :value="0" />
            <el-option label="待发货" :value="4" />
            <el-option label="待收货" :value="1" />
            <el-option label="已取消" :value="2" />
            <el-option label="已完成" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="editForm.payMethod" style="width:100%">
            <el-option label="线上支付" :value="1" />
            <el-option label="线下支付" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付渠道">
          <el-select v-model="editForm.payType" style="width:100%">
            <el-option label="未确定" :value="-1" />
            <el-option label="微信" :value="0" />
            <el-option label="支付宝" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="支付时间">
          <el-date-picker v-model="editForm.payDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择支付时间" style="width:100%" />
        </el-form-item>
        <el-form-item label="订单备注"><el-input v-model="editForm.remark" type="textarea" :rows="2" /></el-form-item>

        <el-divider content-position="left">收货人信息</el-divider>
        <el-form-item label="收货人"><el-input v-model="editForm.orderConsigneeAddress.name" /></el-form-item>
        <el-form-item label="收货人电话"><el-input v-model="editForm.orderConsigneeAddress.phone" /></el-form-item>
        <el-form-item label="省份">
          <el-select v-model="editForm.orderConsigneeAddress.provinceCode" style="width:100%" @change="onProvinceChange">
            <el-option v-for="p in provinceOptions" :key="p.code" :label="p.label" :value="p.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="地市">
          <el-select v-model="editForm.orderConsigneeAddress.cityCode" style="width:100%" @change="onCityChange">
            <el-option v-for="c in cityOptions" :key="c.code" :label="c.label" :value="c.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="区县">
          <el-select v-model="editForm.orderConsigneeAddress.areaCode" style="width:100%" @change="onAreaChange">
            <el-option v-for="a in areaOptions" :key="a.code" :label="a.label" :value="a.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="收货地址"><el-input v-model="editForm.orderConsigneeAddress.address" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEditSubmit" :loading="editSubmitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改订单项弹窗 -->
    <el-dialog v-model="itemsVisible" :title="`订单号:${itemsOrderNo}——订单项列表`" width="1100px" :close-on-click-modal="false" destroy-on-close>
      <el-table :data="itemsList" border stripe size="small" v-loading="itemsLoading" row-key="id">
        <el-table-column prop="skuId" label="商品SKUID" width="160" show-overflow-tooltip />
        <el-table-column prop="productSkuName" label="商品名称" min-width="180" show-overflow-tooltip />
        <el-table-column label="商品预览" width="100" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.httpProductPreviewPath"
              :src="row.httpProductPreviewPath"
              :preview-src-list="[row.httpProductPreviewPath]"
              preview-teleported fit="cover"
              style="width:40px;height:40px;cursor:zoom-in"
            />
          </template>
        </el-table-column>
        <el-table-column label="配送状态" width="130" align="center">
          <template #default="{ row }">
            <el-select v-model="row.deliveryStatus" size="small">
              <el-option label="未收货" :value="0" />
              <el-option label="送货中" :value="1" />
              <el-option label="已收货" :value="2" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="卖家备货状态" width="140" align="center">
          <template #default="{ row }">
            <el-select v-model="row.sellerStatus" size="small">
              <el-option label="备货中" :value="0" />
              <el-option label="备货完成" :value="1" />
              <el-option label="缺货" :value="2" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="买家状态" width="130" align="center">
          <template #default="{ row }">
            <el-select v-model="row.buyerStatus" size="small">
              <el-option label="待收货" :value="0" />
              <el-option label="已收货" :value="1" />
              <el-option label="换货" :value="2" />
              <el-option label="退货" :value="3" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column prop="productPrice" label="商品单价" width="100" align="right" />
        <el-table-column prop="productNum" label="购买数量" width="90" align="center" />
        <el-table-column prop="orderItemAmount" label="总金额" width="110" align="right" />
        <el-table-column label="收货时间" width="190">
          <template #default="{ row }">
            <el-date-picker v-model="row.deliveryReceiveTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" size="small" style="width:100%" />
          </template>
        </el-table-column>
        <el-table-column label="配送完成时间" width="190">
          <template #default="{ row }">
            <el-date-picker v-model="row.deliveryFinishTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" size="small" style="width:100%" />
          </template>
        </el-table-column>
        <el-table-column label="卖家完成时间" width="190">
          <template #default="{ row }">
            <el-date-picker v-model="row.sellerFinishTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" size="small" style="width:100%" />
          </template>
        </el-table-column>
        <el-table-column label="买家完成时间" width="190">
          <template #default="{ row }">
            <el-date-picker v-model="row.buyerFinishTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择" size="small" style="width:100%" />
          </template>
        </el-table-column>
        <el-table-column prop="freightTemplateId" label="运费模板ID" width="130" show-overflow-tooltip />
      </el-table>
      <template #footer>
        <el-button @click="itemsVisible = false">取消</el-button>
        <el-button type="primary" @click="handleItemsSubmit" :loading="itemsSubmitLoading">确认修改</el-button>
      </template>
    </el-dialog>

    <!-- 订单日志弹窗 -->
    <el-dialog v-model="logVisible" :title="`订单号:${logOrderNo}——日志列表`" width="1000px" :close-on-click-modal="false" destroy-on-close>
      <el-table :data="logList" border stripe size="small" v-loading="logLoading">
        <el-table-column prop="orderNo" label="订单编号" width="240" show-overflow-tooltip />
        <el-table-column prop="batchId" label="操作批次ID" width="200" show-overflow-tooltip />
        <el-table-column prop="operateUserId" label="操作人ID" width="160" show-overflow-tooltip />
        <el-table-column prop="operateUserName" label="操作人" width="120" />
        <el-table-column prop="operateUserType" label="操作人类型" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="row.operateUserType === 1 ? 'warning' : 'info'" size="small">{{ operateUserTypeText(row.operateUserType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="typeName" label="操作类型" width="140" />
        <el-table-column prop="createDate" label="创建时间" width="170" />
        <el-table-column prop="remark" label="操作备注" min-width="160" show-overflow-tooltip />
        <el-table-column prop="dataBody" label="变更数据" min-width="200" show-overflow-tooltip />
      </el-table>
      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="logPage" v-model:page-size="logLimit"
          :page-sizes="[15, 30, 100]" layout="total, sizes, prev, pager, next"
          :total="logTotal" @size-change="loadOrderLog" @current-change="loadOrderLog"
        />
      </div>
      <template #footer>
        <el-button @click="logVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, RefreshRight, View, Edit, Goods, Document, CircleClose } from '@element-plus/icons-vue'
import {
  listOrder, updateOrder, cancelOrder, listOrderItem, listOrderItemAll,
  updatesOrderItems, listOrderLog, listAreaByParentCode
} from '@/api/order/order'

// ===================== 常量映射 =====================
const tradeTabs = [
  { value: -1, label: '全部' },
  { value: 0, label: '待付款' },
  { value: 4, label: '待发货' },
  { value: 1, label: '待收货' },
  { value: 2, label: '已取消' },
  { value: 3, label: '已完成' }
]

function tradeStatusText(v) {
  const map = { 0: '待付款', 4: '待发货', 1: '待收货', 2: '已取消', 3: '已完成' }
  return map[v] != null ? map[v] : v
}
function tradeStatusTag(v) {
  const map = { 0: 'info', 4: 'warning', 1: 'primary', 2: 'danger', 3: 'success' }
  return map[v] || 'info'
}
function payStatusText(v) {
  const map = { 0: '未支付', 1: '已支付', 4: '取消支付', 5: '已退款' }
  return map[v] != null ? map[v] : v
}
function payStatusTag(v) {
  const map = { 0: 'info', 1: 'success', 4: 'danger', 5: 'warning' }
  return map[v] || 'info'
}
function payMethodText(v) {
  const map = { 1: '线上支付', 2: '线下支付' }
  return map[v] != null ? map[v] : v
}
function payTypeText(v) {
  if (v === -1 || v === '-1') return '未确定'
  const map = { 0: '微信', 1: '支付宝' }
  return map[v] != null ? map[v] : v
}
function srcTypeText(v) {
  const map = { 1: 'PC', 2: 'APP' }
  return map[v] != null ? map[v] : v
}
function deliveryStatusText(v) {
  const map = { 0: '未收货', 1: '送货中', 2: '已收货' }
  return map[v] != null ? map[v] : v
}
function sellerStatusText(v) {
  const map = { 0: '备货中', 1: '备货完成', 2: '缺货' }
  return map[v] != null ? map[v] : v
}
function buyerStatusText(v) {
  const map = { 0: '待收货', 1: '已收货', 2: '换货', 3: '退货' }
  return map[v] != null ? map[v] : v
}
function operateUserTypeText(v) {
  return v === 1 ? '管理员' : v === 2 ? '普通用户' : v
}

// ===================== 列表 =====================
const searchForm = reactive({ mainOrderNo: '', orderNo: '', userId: '', shopId: '', payStatus: '', startCreateDate: '', endCreateDate: '' })
const activeTradeStatus = ref('-1')
const tableData = ref([])
const loading = ref(false)
const pagination = reactive({ page: 1, limit: 15, total: 0 })

function handleSearch() { pagination.page = 1; loadTableData() }
function handleTabChange() { handleSearch() }
function handleReset() {
  searchForm.mainOrderNo = ''; searchForm.orderNo = ''; searchForm.userId = ''
  searchForm.shopId = ''; searchForm.payStatus = ''
  searchForm.startCreateDate = ''; searchForm.endCreateDate = ''
  activeTradeStatus.value = '-1'
  handleSearch()
}

function buildSearchParams() {
  const p = {
    ...searchForm,
    tradeStatus: Number(activeTradeStatus.value),
    page: pagination.page,
    limit: pagination.limit
  }
  Object.keys(p).forEach(k => { if (p[k] === '' || p[k] === undefined || p[k] === null) delete p[k] })
  return p
}

async function loadTableData() {
  loading.value = true
  try {
    const res = await listOrder(buildSearchParams())
    tableData.value = res.data || []
    pagination.total = res.count || 0
  } catch { } finally { loading.value = false }
}

// ===================== 查看详情 =====================
const detailVisible = ref(false)
const detailLoading = ref(false)
const detailRow = ref(null)
const detailItems = ref([])
const detailItemLoading = ref(false)
const detailItemPage = ref(1)
const detailItemLimit = ref(15)
const detailItemTotal = ref(0)

async function loadDetailItems() {
  detailItemLoading.value = true
  try {
    const res = await listOrderItem({ orderId: detailRow.value.id, page: detailItemPage.value, limit: detailItemLimit.value })
    detailItems.value = res.data || []
    detailItemTotal.value = res.count || 0
  } catch { } finally { detailItemLoading.value = false }
}

function handleView(row) {
  detailVisible.value = true
  detailRow.value = row
  detailItemPage.value = 1
  loadDetailItems()
}

// ===================== 修改订单 =====================
const editVisible = ref(false)
const editLoading = ref(false)
const editSubmitLoading = ref(false)
const editFormRef = ref(null)
const editForm = reactive({
  id: null, mainOrderNo: '', orderNo: '',
  freightAmount: '', redPackageAmount: '', couponAmount: '', totalAmount: '', orderAmount: '', payAmount: '',
  outerTradeNo: '', payStatus: null, tradeStatus: null, payMethod: null, payType: null,
  payDate: '', remark: '',
  orderConsigneeAddress: { name: '', phone: '', provinceCode: '', provinceName: '', cityCode: '', cityName: '', areaCode: '', areaName: '', address: '' }
})

const provinceOptions = ref([])
const cityOptions = ref([])
const areaOptions = ref([])

function areaLabelForProvince(a) { return a.isMunicipality === 1 || a.isMunicipality === '1' ? (a.city || a.province) : a.province }

async function loadProvinces() {
  const res = await listAreaByParentCode({ parentCode: '-1' })
  if (res.code === 1 && res.data) {
    provinceOptions.value = (res.data || []).map(a => ({ code: a.code, label: areaLabelForProvince(a), isMunicipality: a.isMunicipality }))
  }
}

async function onProvinceChange(code) {
  editForm.orderConsigneeAddress.provinceCode = code
  editForm.orderConsigneeAddress.cityCode = ''
  editForm.orderConsigneeAddress.cityName = ''
  editForm.orderConsigneeAddress.areaCode = ''
  editForm.orderConsigneeAddress.areaName = ''
  areaOptions.value = []
  const prov = provinceOptions.value.find(p => p.code === code)
  editForm.orderConsigneeAddress.provinceName = prov ? prov.label : ''
  if (prov && (prov.isMunicipality === 1 || prov.isMunicipality === '1')) {
    cityOptions.value = [{ code: prov.code, label: prov.label }]
    editForm.orderConsigneeAddress.cityCode = prov.code
    editForm.orderConsigneeAddress.cityName = prov.label
    await onCityChange(prov.code)
  } else {
    cityOptions.value = []
    const res = await listAreaByParentCode({ parentCode: code })
    if (res.code === 1 && res.data) cityOptions.value = (res.data || []).map(c => ({ code: c.code, label: c.city }))
  }
}

async function onCityChange(code) {
  editForm.orderConsigneeAddress.cityCode = code
  const city = cityOptions.value.find(c => c.code === code)
  editForm.orderConsigneeAddress.cityName = city ? city.label : ''
  editForm.orderConsigneeAddress.areaCode = ''
  editForm.orderConsigneeAddress.areaName = ''
  areaOptions.value = []
  const res = await listAreaByParentCode({ parentCode: code })
  if (res.code === 1 && res.data) areaOptions.value = (res.data || []).map(a => ({ code: a.code, label: a.area }))
}

function onAreaChange(code) {
  editForm.orderConsigneeAddress.areaCode = code
  const area = areaOptions.value.find(a => a.code === code)
  editForm.orderConsigneeAddress.areaName = area ? area.label : ''
}

async function handleEdit(row) {
  editVisible.value = true
  editLoading.value = true
  const addr = row.orderConsigneeAddress || {}
  Object.assign(editForm, {
    id: row.id, mainOrderNo: row.mainOrderNo || '', orderNo: row.orderNo || '',
    freightAmount: row.freightAmount != null ? String(row.freightAmount) : '',
    redPackageAmount: row.redPackageAmount != null ? String(row.redPackageAmount) : '',
    couponAmount: row.couponAmount != null ? String(row.couponAmount) : '',
    totalAmount: row.totalAmount != null ? String(row.totalAmount) : '',
    orderAmount: row.orderAmount != null ? String(row.orderAmount) : '',
    payAmount: row.payAmount != null ? String(row.payAmount) : '',
    outerTradeNo: row.outerTradeNo || '',
    payStatus: row.payStatus, tradeStatus: row.tradeStatus,
    payMethod: row.payMethod, payType: row.payType,
    payDate: row.payDate || '', remark: row.remark || ''
  })
  Object.assign(editForm.orderConsigneeAddress, {
    name: addr.name || '', phone: addr.phone || '',
    provinceCode: addr.provinceCode || '', provinceName: addr.provinceName || '',
    cityCode: addr.cityCode || '', cityName: addr.cityName || '',
    areaCode: addr.areaCode || '', areaName: addr.areaName || '',
    address: addr.address || ''
  })
  cityOptions.value = []
  areaOptions.value = []
  try {
    await loadProvinces()
    const pCode = editForm.orderConsigneeAddress.provinceCode
    if (pCode) {
      const prov = provinceOptions.value.find(p => p.code === pCode)
      if (prov && (prov.isMunicipality === 1 || prov.isMunicipality === '1')) {
        cityOptions.value = [{ code: prov.code, label: prov.label }]
      } else if (pCode) {
        const res = await listAreaByParentCode({ parentCode: pCode })
        if (res.code === 1 && res.data) cityOptions.value = (res.data || []).map(c => ({ code: c.code, label: c.city }))
      }
      const cCode = editForm.orderConsigneeAddress.cityCode
      if (cCode) {
        const res = await listAreaByParentCode({ parentCode: cCode })
        if (res.code === 1 && res.data) areaOptions.value = (res.data || []).map(a => ({ code: a.code, label: a.area }))
      }
    }
  } catch { } finally { editLoading.value = false }
}

async function handleEditSubmit() {
  editSubmitLoading.value = true
  try {
    const res = await updateOrder({ ...editForm, orderConsigneeAddress: { ...editForm.orderConsigneeAddress } })
    if (res.code === 1) {
      ElMessage.success(res.msg || '修改成功')
      editVisible.value = false
      loadTableData()
    } else {
      ElMessage.error(res.msg || '修改失败')
    }
  } catch { } finally { editSubmitLoading.value = false }
}

// ===================== 取消订单 =====================
function handleCancel(row) {
  ElMessageBox.prompt('请输入取消原因', '取消订单', {
    confirmButtonText: '确认取消', cancelButtonText: '取消',
    inputType: 'textarea', inputPlaceholder: '请输入取消原因', inputValidator: (v) => v && v.trim() ? true : '请输入取消原因'
  }).then(async ({ value }) => {
    try {
      const res = await cancelOrder({ orderNo: row.orderNo, cancelRemark: value })
      if (res.code === 1) { ElMessage.success(res.msg || '取消成功'); loadTableData() }
      else ElMessage.error(res.msg || '取消失败')
    } catch { }
  }).catch(() => {})
}

// ===================== 修改订单项 =====================
const itemsVisible = ref(false)
const itemsLoading = ref(false)
const itemsSubmitLoading = ref(false)
const itemsList = ref([])
const itemsOrderNo = ref('')

async function handleModifyItems(row) {
  itemsVisible.value = true
  itemsOrderNo.value = row.orderNo || ''
  itemsLoading.value = true
  try {
    const res = await listOrderItemAll({ orderId: row.id })
    itemsList.value = res.data || []
  } catch { } finally { itemsLoading.value = false }
}

async function handleItemsSubmit() {
  if (!itemsList.value.length) { ElMessage.warning('没有可修改的订单项'); return }
  itemsSubmitLoading.value = true
  try {
    const res = await updatesOrderItems(itemsList.value)
    if (res.code === 1) { ElMessage.success(res.msg || '修改成功'); itemsVisible.value = false; loadTableData() }
    else ElMessage.error(res.msg || '修改失败')
  } catch { } finally { itemsSubmitLoading.value = false }
}

// ===================== 订单日志 =====================
const logVisible = ref(false)
const logLoading = ref(false)
const logList = ref([])
const logOrderNo = ref('')
const logPage = ref(1)
const logLimit = ref(15)
const logTotal = ref(0)

async function loadOrderLog() {
  logLoading.value = true
  try {
    const res = await listOrderLog({ orderNo: logOrderNo.value, page: logPage.value, limit: logLimit.value })
    logList.value = res.data || []
    logTotal.value = res.count || 0
  } catch { } finally { logLoading.value = false }
}

function handleOrderLog(row) {
  logVisible.value = true
  logOrderNo.value = row.orderNo || ''
  logPage.value = 1
  loadOrderLog()
}

loadTableData()
</script>

<style lang="scss" scoped>
.order-management {
  .search-card { margin-bottom: $gap-md; :deep(.el-card__body) { padding-bottom: 0; } }
  .table-card {
    .toolbar { margin-bottom: $gap-md; display: flex; gap: 8px; }
    .pagination-wrap { margin-top: $gap-md; display: flex; justify-content: flex-end; }
  }
  .sub-table-title { margin: 16px 0 8px; font-weight: 600; color: $text-primary; }
  :deep(.el-table) { th { background-color: #f5f7fa; color: $text-primary; font-weight: 600; } }
  :deep(.el-descriptions__table) { width: 100%; }
  :deep(.el-descriptions__label) { word-break: keep-all; }
  :deep(.el-descriptions__content) { word-break: break-all; overflow-wrap: anywhere; min-width: 0; }
}
</style>
