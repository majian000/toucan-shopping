package com.toucan.shopping.modules.admin.auth.cache.es.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.CountResponse;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.toucan.shopping.modules.admin.auth.cache.service.FunctionCacheService;
import com.toucan.shopping.modules.admin.auth.constant.FunctionCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.vo.FunctionCacheVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("esFunctionService")
public class FunctionElasticSearchServiceImpl implements FunctionCacheService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void save(FunctionCacheVO esVO) throws Exception {
        elasticsearchClient.index(i -> i
                .index(FunctionCacheElasticSearchConstant.FUNCTION_INDEX)
                .id(String.valueOf(esVO.getId()))
                .document(esVO)
                .refresh(Refresh.True));
    }

    @Override
    public boolean deleteIndex() throws Exception {
        return elasticsearchClient.indices()
                .delete(d -> d.index(FunctionCacheElasticSearchConstant.FUNCTION_INDEX))
                .acknowledged();
    }

    @Override
    public void update(FunctionCacheVO esVO) throws Exception {
        Map<String, Object> doc = new LinkedHashMap<>();
        doc.put("id", esVO.getId());
        doc.put("functionId", esVO.getFunctionId());
        doc.put("name", esVO.getName());
        doc.put("url", esVO.getUrl());
        doc.put("permission", esVO.getPermission());
        doc.put("type", esVO.getType());
        doc.put("functionText", esVO.getFunctionText());
        doc.put("pid", esVO.getPid());
        doc.put("enableStatus", esVO.getEnableStatus());
        doc.put("icon", esVO.getIcon());
        doc.put("remark", esVO.getRemark());
        doc.put("functionSort", esVO.getFunctionSort());
        doc.put("appCode", esVO.getAppCode());
        doc.put("createAdminId", esVO.getCreateAdminId());
        doc.put("deleteStatus", esVO.getDeleteStatus());
        if (esVO.getCreateDate() != null) {
            doc.put("createDate", esVO.getCreateDate().getTime());
        }
        elasticsearchClient.update(u -> u
                        .index(FunctionCacheElasticSearchConstant.FUNCTION_INDEX)
                        .id(String.valueOf(esVO.getId()))
                        .doc(doc)
                        .refresh(Refresh.True),
                Map.class);
    }

    @Override
    public boolean existsIndex() {
        try {
            BooleanResponse exists = elasticsearchClient.indices()
                    .exists(e -> e.index(FunctionCacheElasticSearchConstant.FUNCTION_INDEX));
            return exists.value();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void createIndex() {
        try {
            elasticsearchClient.indices().create(c -> c
                    .index(FunctionCacheElasticSearchConstant.FUNCTION_INDEX));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    public List<FunctionCacheVO> queryById(Long id) throws Exception {
        List<FunctionCacheVO> result = new ArrayList<>();
        SearchResponse<FunctionCacheVO> response = elasticsearchClient.search(s -> s
                        .index(FunctionCacheElasticSearchConstant.FUNCTION_INDEX)
                        .query(q -> q.term(t -> t.field("_id").value(id)))
                        .size(10),
                FunctionCacheVO.class);
        for (Hit<FunctionCacheVO> hit : response.hits().hits()) {
            if (hit.source() != null) {
                result.add(hit.source());
            }
        }
        return result;
    }

    @Override
    public List<FunctionCacheVO> queryByEntity(FunctionCacheVO query) throws Exception {
        List<FunctionCacheVO> result = new ArrayList<>();
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
        if (query.getId() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("_id").value(query.getId()))));
        }
        if (query.getFunctionId() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("functionId").value(query.getFunctionId()))));
        }
        if (query.getUrl() != null) {
            boolBuilder.must(Query.of(q -> q.matchPhrasePrefix(m -> m.field("url").query(query.getUrl()))));
        }
        if (query.getEnableStatus() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("enableStatus").value(query.getEnableStatus()))));
        }
        if (query.getPid() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("pid").value(query.getPid()))));
        }
        if (query.getAppCode() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("appCode").value(query.getAppCode()))));
        }
        Query boolQuery = boolBuilder.build()._toQuery();
        int total = queryCount(boolQuery).intValue();

        SearchResponse<FunctionCacheVO> response = elasticsearchClient.search(s -> s
                        .index(FunctionCacheElasticSearchConstant.FUNCTION_INDEX)
                        .query(boolQuery)
                        .size(total > 0 ? total : 10),
                FunctionCacheVO.class);
        for (Hit<FunctionCacheVO> hit : response.hits().hits()) {
            if (hit.source() != null) {
                result.add(hit.source());
            }
        }
        return result;
    }

    @Override
    public boolean deleteById(String id) throws Exception {
        DeleteResponse response = elasticsearchClient.delete(d -> d
                .index(FunctionCacheElasticSearchConstant.FUNCTION_INDEX)
                .id(id)
                .refresh(Refresh.True));
        return "deleted".equals(response.result().jsonValue());
    }

    public Long queryCount(Query query) throws Exception {
        CountResponse response = elasticsearchClient.count(c -> c
                .index(FunctionCacheElasticSearchConstant.FUNCTION_INDEX).query(query));
        return response.count();
    }
}
