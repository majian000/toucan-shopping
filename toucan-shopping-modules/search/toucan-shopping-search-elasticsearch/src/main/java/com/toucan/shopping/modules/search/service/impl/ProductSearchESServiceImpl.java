package com.toucan.shopping.modules.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.ChildScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.indices.GetIndicesSettingsResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.search.es.index.ProductIndex;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchAttributeVO;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service("productSearchESServiceImpl")
public class ProductSearchESServiceImpl implements ProductSearchService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void createIndex() {
        try {
            elasticsearchClient.indices().create(c -> c
                    .index(ProductIndex.PRODUCT_SKU_INDEX)
                    .mappings(m -> m
                            .properties("id", p -> p.long_(l -> l))
                            .properties("skuId", p -> p.long_(l -> l))
                            .properties("name", p -> p.text(t -> t.analyzer("ik_max_word").searchAnalyzer("ik_smart")))
                            .properties("price", p -> p.double_(d -> d))
                            .properties("brandName", p -> p.text(t -> t.analyzer("ik_max_word").searchAnalyzer("ik_smart")))
                            .properties("brandNameCN", p -> p.text(t -> t.analyzer("ik_max_word").searchAnalyzer("ik_smart")))
                            .properties("brandNameEN", p -> p.text(t -> t.analyzer("ik_max_word").searchAnalyzer("ik_smart")))
                            .properties("categoryName", p -> p.text(t -> t.analyzer("ik_max_word").searchAnalyzer("ik_smart")))
                            .properties("categoryIds", p -> p.text(t -> t))
                            .properties("shopCategoryIds", p -> p.text(t -> t))
                            .properties("attributes", p -> p.nested(n -> n
                                    .properties("name", np -> np.keyword(k -> k))
                                    .properties("value", np -> np.keyword(k -> k))))
                            .properties("searchAttributes", p -> p.nested(n -> n
                                    .properties("nameId", np -> np.long_(l -> l))
                                    .properties("name", np -> np.keyword(k -> k))
                                    .properties("valueId", np -> np.long_(l -> l))
                                    .properties("value", np -> np.keyword(k -> k))))
                            .properties("searchShopAttributes", p -> p.nested(n -> n
                                    .properties("nameId", np -> np.long_(l -> l))
                                    .properties("name", np -> np.keyword(k -> k))
                                    .properties("valueId", np -> np.long_(l -> l))
                                    .properties("value", np -> np.keyword(k -> k))))
                            .properties("brandId", p -> p.long_(l -> l))
                            .properties("shopId", p -> p.long_(l -> l))
                            .properties("shopCategoryId", p -> p.long_(l -> l))
                            .properties("newestRank", p -> p.double_(d -> d))
                            .properties("randk", p -> p.double_(d -> d))
                    )
            );
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    public PageInfo<ProductSearchResultVO> search(ProductSearchVO productSearchVO) throws Exception {
        List<ProductSearchResultVO> queryResult = new LinkedList<>();

        int from = productSearchVO.getPage() == 1 ? productSearchVO.getPage() - 1
                : ((productSearchVO.getPage() - 1) * productSearchVO.getSize());

        Query boolQuery = buildBoolQuery(productSearchVO);

        SearchRequest.Builder searchBuilder = new SearchRequest.Builder()
                .index(ProductIndex.PRODUCT_SKU_INDEX)
                .query(boolQuery)
                .from(from)
                .size(productSearchVO.getSize());

        // 默认排序,根据rank值最大在最前面
        if (StringUtils.isEmpty(productSearchVO.getPst()) && StringUtils.isEmpty(productSearchVO.getPdst())) {
            searchBuilder.sort(s -> s.field(f -> f.field("randk").order(SortOrder.Desc)));
        }
        // 价格排序
        if (StringUtils.isNotEmpty(productSearchVO.getPst())) {
            SortOrder order = "asc".equals(productSearchVO.getPst()) ? SortOrder.Asc : SortOrder.Desc;
            searchBuilder.sort(s -> s.field(f -> f.field("price").order(order)));
        }
        // 新品排序
        if (StringUtils.isNotEmpty(productSearchVO.getPdst())) {
            SortOrder order = "asc".equals(productSearchVO.getPdst()) ? SortOrder.Asc : SortOrder.Desc;
            searchBuilder.sort(s -> s.field(f -> f.field("newestRank").order(order)));
        }

        try {
            SearchResponse<ProductSearchResultVO> response = elasticsearchClient.search(
                    searchBuilder.build(), ProductSearchResultVO.class);
            for (Hit<ProductSearchResultVO> hit : response.hits().hits()) {
                if (hit.source() != null) {
                    queryResult.add(hit.source());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(queryResult);
        pageInfo.setPage(productSearchVO.getPage());
        pageInfo.setSize(productSearchVO.getSize());
        pageInfo.setTotal(queryCountByVO(productSearchVO));
        pageInfo.setMaxTotal(queryMaxResultWindowCount());
        pageInfo.setPageTotal(pageInfo.getTotal() % pageInfo.getSize() == 0
                ? (pageInfo.getTotal() / pageInfo.getSize())
                : ((pageInfo.getTotal() / pageInfo.getSize()) + 1));
        return pageInfo;
    }

    /**
     * 构造查询条件
     */
    private Query buildBoolQuery(ProductSearchVO productSearchVO) {
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

        // 关键词查询
        if (StringUtils.isNotEmpty(productSearchVO.getKeyword())) {
            boolBuilder.must(Query.of(q -> q.multiMatch(m -> m
                    .query(productSearchVO.getKeyword())
                    .fields("name", "brandName", "brandNameCN", "brandNameEN", "categoryName"))));
        }
        // 分类查询
        if (StringUtils.isNotEmpty(productSearchVO.getCid())) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("categoryIds").value(productSearchVO.getCid()))));
        }
        // 店铺分类查询
        if (StringUtils.isNotEmpty(productSearchVO.getScid())) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("shopCategoryIds").value(productSearchVO.getScid()))));
        }
        // 店铺查询
        if (StringUtils.isNotEmpty(productSearchVO.getSid())) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("shopId").value(productSearchVO.getSid()))));
        }
        // 商品名称查询
        if (StringUtils.isNotEmpty(productSearchVO.getProductName())) {
            boolBuilder.must(Query.of(q -> q.multiMatch(m -> m
                    .query(productSearchVO.getProductName()).fields("name"))));
        }
        // 品牌名称查询
        if (StringUtils.isNotEmpty(productSearchVO.getBn())) {
            boolBuilder.must(Query.of(q -> q.multiMatch(m -> m
                    .query(productSearchVO.getBn()).fields("brandName", "brandNameCN", "brandNameEN"))));
        }
        // 分类名称查询
        if (StringUtils.isNotEmpty(productSearchVO.getCategoryName())) {
            boolBuilder.must(Query.of(q -> q.multiMatch(m -> m
                    .query(productSearchVO.getCategoryName()).fields("categoryName"))));
        }
        // SKU ID查询
        if (StringUtils.isNotEmpty(productSearchVO.getSkuId())) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("skuId").value(productSearchVO.getSkuId()))));
        }
        // 品牌ID查询
        if (CollectionUtils.isNotEmpty(productSearchVO.getBrandIds())) {
            boolBuilder.must(Query.of(q -> q.terms(t -> t.field("brandId")
                    .terms(tv -> tv.value(productSearchVO.getBrandIds().stream()
                            .map(b -> FieldValue.of(b)).collect(Collectors.toList()))))));
        }
        if (StringUtils.isNotEmpty(productSearchVO.getBid())) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("brandId").value(productSearchVO.getBid()))));
        }
        // 属性查询
        if (CollectionUtils.isNotEmpty(productSearchVO.getSearchAttributes())) {
            BoolQuery.Builder nestedBool = new BoolQuery.Builder();
            for (ProductSearchAttributeVO attr : productSearchVO.getSearchAttributes()) {
                BoolQuery.Builder attrQuery = new BoolQuery.Builder();
                attrQuery.must(Query.of(q -> q.term(t -> t.field("searchAttributes.name").value(attr.getName()))));
                attrQuery.must(Query.of(q -> q.term(t -> t.field("searchAttributes.value").value(attr.getValue()))));
                nestedBool.filter(Query.of(q -> q.nested(n -> n
                        .path("searchAttributes").query(attrQuery.build()._toQuery())
                        .scoreMode(ChildScoreMode.None))));
            }
            boolBuilder.must(nestedBool.build()._toQuery());
        }
        // 店铺属性查询
        if (CollectionUtils.isNotEmpty(productSearchVO.getSearchShopAttributes())) {
            BoolQuery.Builder nestedBool = new BoolQuery.Builder();
            for (ProductSearchAttributeVO attr : productSearchVO.getSearchShopAttributes()) {
                BoolQuery.Builder attrQuery = new BoolQuery.Builder();
                attrQuery.must(Query.of(q -> q.term(t -> t.field("searchShopAttributes.name").value(attr.getName()))));
                attrQuery.must(Query.of(q -> q.term(t -> t.field("searchShopAttributes.value").value(attr.getValue()))));
                nestedBool.filter(Query.of(q -> q.nested(n -> n
                        .path("searchShopAttributes").query(attrQuery.build()._toQuery())
                        .scoreMode(ChildScoreMode.None))));
            }
            boolBuilder.must(nestedBool.build()._toQuery());
        }

        // 价格查询
        if (productSearchVO.getPsd() != null || productSearchVO.getPed() != null) {
            BoolQuery.Builder rangeBool = new BoolQuery.Builder();
            if (productSearchVO.getPsd() != null) {
                rangeBool.filter(Query.of(q -> q.range(r -> r.number(nr -> nr.field("price").gte(productSearchVO.getPsd())))));
            }
            if (productSearchVO.getPed() != null) {
                rangeBool.filter(Query.of(q -> q.range(r -> r.number(nr -> nr.field("price").lte(productSearchVO.getPed())))));
            }
            boolBuilder.must(rangeBool.build()._toQuery());
        }

        return boolBuilder.build()._toQuery();
    }

    @Override
    public Long queryCount(ProductSearchVO productSearchVO) throws Exception {
        return queryCountByVO(productSearchVO);
    }

    public Long queryCountByVO(ProductSearchVO productSearchVO) throws Exception {
        Query query = buildBoolQuery(productSearchVO);
        CountResponse response = elasticsearchClient.count(c -> c
                .index(ProductIndex.PRODUCT_SKU_INDEX).query(query));
        return response.count();
    }

    @Override
    public boolean existsIndex() {
        try {
            BooleanResponse exists = elasticsearchClient.indices()
                    .exists(e -> e.index(ProductIndex.PRODUCT_SKU_INDEX));
            return exists.value();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public List<ProductSearchResultVO> queryBySkuId(Long id) throws Exception {
        List<ProductSearchResultVO> result = new ArrayList<>();
        SearchResponse<ProductSearchResultVO> response = elasticsearchClient.search(s -> s
                        .index(ProductIndex.PRODUCT_SKU_INDEX)
                        .query(q -> q.term(t -> t.field("skuId").value(id)))
                        .size(10),
                ProductSearchResultVO.class);
        for (Hit<ProductSearchResultVO> hit : response.hits().hits()) {
            if (hit.source() != null) {
                result.add(hit.source());
            }
        }
        return result;
    }

    @Override
    public void setMaxResultWindow(Long maxCount) {
        try {
            elasticsearchClient.indices().putSettings(p -> p
                    .index(ProductIndex.PRODUCT_SKU_INDEX)
                    .settings(s -> s.maxResultWindow(maxCount.intValue())));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Long queryMaxResultWindowCount() throws IOException {
        try {
            GetIndicesSettingsResponse settingsResponse = elasticsearchClient.indices()
                    .getSettings(g -> g.index(ProductIndex.PRODUCT_SKU_INDEX));
            var indexSettings = settingsResponse.get(ProductIndex.PRODUCT_SKU_INDEX);
            if (indexSettings != null && indexSettings.settings() != null
                    && indexSettings.settings().index() != null
                    && indexSettings.settings().index().maxResultWindow() != null) {
                return Long.valueOf(indexSettings.settings().index().maxResultWindow());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ProductIndex.MAX_RESULT_WINDOW;
    }

    @Override
    public void save(ProductSearchResultVO productSearchResultVO) throws IOException {
        productSearchResultVO.setCreateDate(DateUtils.FORMATTER_SS.get().format(DateUtils.currentDate()));
        elasticsearchClient.index(i -> i
                .index(ProductIndex.PRODUCT_SKU_INDEX)
                .id(String.valueOf(productSearchResultVO.getSkuId()))
                .document(productSearchResultVO)
                .refresh(Refresh.True));
    }

    @Override
    public void update(ProductSearchResultVO productSearchResultVO) throws Exception {
        List<Long> deleteFaildList = new ArrayList<>();
        this.removeById(productSearchResultVO.getSkuId(), deleteFaildList);
        this.save(productSearchResultVO);
    }

    @Override
    public boolean removeById(Long id, List<Long> deleteFaildIdList) throws Exception {
        Query skuQuery = Query.of(q -> q.term(t -> t.field("skuId").value(id)));
        long total = elasticsearchClient.count(c -> c
                .index(ProductIndex.PRODUCT_SKU_INDEX).query(skuQuery)).count();

        SearchResponse<ProductSearchResultVO> response = elasticsearchClient.search(s -> s
                        .index(ProductIndex.PRODUCT_SKU_INDEX)
                        .query(skuQuery)
                        .size((int) total),
                ProductSearchResultVO.class);

        for (Hit<ProductSearchResultVO> hit : response.hits().hits()) {
            DeleteResponse deleteResponse = elasticsearchClient.delete(d -> d
                    .index(ProductIndex.PRODUCT_SKU_INDEX)
                    .id(hit.id())
                    .refresh(Refresh.True));
            if (!"deleted".equals(deleteResponse.result().jsonValue())) {
                deleteFaildIdList.add(Long.parseLong(hit.id()));
            }
        }
        return CollectionUtils.isEmpty(deleteFaildIdList);
    }

    @Override
    public void deleteIndex() throws Exception {
        elasticsearchClient.indices().delete(d -> d.index(ProductIndex.PRODUCT_SKU_INDEX));
    }
}
