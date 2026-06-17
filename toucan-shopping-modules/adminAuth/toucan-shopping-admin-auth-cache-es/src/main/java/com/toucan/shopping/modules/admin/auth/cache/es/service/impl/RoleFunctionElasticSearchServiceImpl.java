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
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.constant.RoleFunctionCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionCacheVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service("esRoleFunctionService")
public class RoleFunctionElasticSearchServiceImpl implements RoleFunctionCacheService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void save(RoleFunctionCacheVO esVO) throws Exception {
        elasticsearchClient.index(i -> i
                .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX)
                .id(String.valueOf(esVO.getId()))
                .document(esVO)
                .refresh(Refresh.True));
    }

    @Override
    public void update(RoleFunctionCacheVO esVO) throws Exception {
        Map<String, Object> doc = new LinkedHashMap<>();
        doc.put("id", esVO.getId());
        doc.put("roleId", esVO.getRoleId());
        doc.put("functionId", esVO.getFunctionId());
        doc.put("appCode", esVO.getAppCode());
        doc.put("createAdminId", esVO.getCreateAdminId());
        doc.put("deleteStatus", esVO.getDeleteStatus());
        if (esVO.getCreateDate() != null) {
            doc.put("createDate", esVO.getCreateDate().getTime());
        }
        elasticsearchClient.update(u -> u
                        .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX)
                        .id(String.valueOf(esVO.getId()))
                        .doc(doc)
                        .refresh(Refresh.True),
                Map.class);
    }

    @Override
    public boolean existsIndex() {
        try {
            BooleanResponse exists = elasticsearchClient.indices()
                    .exists(e -> e.index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX));
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
                    .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteIndex() throws Exception {
        return elasticsearchClient.indices()
                .delete(d -> d.index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX))
                .acknowledged();
    }

    @Override
    public List<RoleFunctionCacheVO> queryById(Long id) throws Exception {
        List<RoleFunctionCacheVO> result = new ArrayList<>();
        SearchResponse<RoleFunctionCacheVO> response = elasticsearchClient.search(s -> s
                        .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX)
                        .query(q -> q.term(t -> t.field("_id").value(id)))
                        .size(10),
                RoleFunctionCacheVO.class);
        for (Hit<RoleFunctionCacheVO> hit : response.hits().hits()) {
            if (hit.source() != null) {
                result.add(hit.source());
            }
        }
        return result;
    }

    @Override
    public List<RoleFunctionCacheVO> queryByEntity(RoleFunctionCacheVO query) throws Exception {
        List<RoleFunctionCacheVO> result = new ArrayList<>();
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
        if (query.getId() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("_id").value(query.getId()))));
        }
        if (query.getFunctionId() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("functionId").value(query.getFunctionId()))));
        }
        if (query.getRoleId() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("roleId").value(query.getRoleId()))));
        }
        if (query.getDeleteStatus() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("deleteStatus").value(query.getDeleteStatus()))));
        }
        if (query.getAppCode() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("appCode").value(query.getAppCode()))));
        }
        Query boolQuery = boolBuilder.build()._toQuery();
        int total = queryCount(boolQuery).intValue();

        SearchResponse<RoleFunctionCacheVO> response = elasticsearchClient.search(s -> s
                        .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX)
                        .query(boolQuery)
                        .size(total > 0 ? total : 10),
                RoleFunctionCacheVO.class);
        for (Hit<RoleFunctionCacheVO> hit : response.hits().hits()) {
            if (hit.source() != null) {
                result.add(hit.source());
            }
        }
        return result;
    }

    @Override
    public boolean deleteById(String id) throws Exception {
        DeleteResponse response = elasticsearchClient.delete(d -> d
                .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX)
                .id(id)
                .refresh(Refresh.True));
        return "deleted".equals(response.result().jsonValue());
    }

    @Override
    public boolean deleteByRoleId(String roleId, List<String> deleteFaildIdList) throws Exception {
        Query query = Query.of(q -> q.term(t -> t.field("roleId").value(roleId)));
        int total = queryCount(query).intValue();

        SearchResponse<RoleFunctionCacheVO> response = elasticsearchClient.search(s -> s
                        .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX)
                        .query(query)
                        .size(total > 0 ? total : 10),
                RoleFunctionCacheVO.class);

        for (Hit<RoleFunctionCacheVO> hit : response.hits().hits()) {
            logger.info(" 删除角色资源关联对象 {}", hit.source());
            DeleteResponse deleteResponse = elasticsearchClient.delete(d -> d
                    .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX)
                    .id(hit.id())
                    .refresh(Refresh.True));
            if (!"deleted".equals(deleteResponse.result().jsonValue())) {
                deleteFaildIdList.add(hit.id());
            }
        }
        return CollectionUtils.isEmpty(deleteFaildIdList);
    }

    @Override
    public void saves(RoleFunctionCacheVO[] roleFunctionCacheVOS) throws Exception {
        for (RoleFunctionCacheVO vo : roleFunctionCacheVOS) {
            save(vo);
        }
    }

    @Override
    public boolean deleteByFunctionId(String functionId, List<String> deleteFaildIdList) throws Exception {
        Query query = Query.of(q -> q.term(t -> t.field("functionId").value(functionId)));
        int total = queryCount(query).intValue();

        SearchResponse<RoleFunctionCacheVO> response = elasticsearchClient.search(s -> s
                        .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX)
                        .query(query)
                        .size(total > 0 ? total : 10),
                RoleFunctionCacheVO.class);

        for (Hit<RoleFunctionCacheVO> hit : response.hits().hits()) {
            DeleteResponse deleteResponse = elasticsearchClient.delete(d -> d
                    .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX)
                    .id(hit.id())
                    .refresh(Refresh.True));
            if (!"deleted".equals(deleteResponse.result().jsonValue())) {
                deleteFaildIdList.add(hit.id());
            }
        }
        return CollectionUtils.isEmpty(deleteFaildIdList);
    }

    public Long queryCount(Query query) throws Exception {
        CountResponse response = elasticsearchClient.count(c -> c
                .index(RoleFunctionCacheElasticSearchConstant.ROLE_FUNCTION_INDEX).query(query));
        return response.count();
    }
}
