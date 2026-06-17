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
import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.constant.AdminRoleCacheElasticSearchConstant;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleCacheVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("esAdminRoleService")
public class AdminRoleElasticSearchServiceImpl implements AdminRoleCacheService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void save(AdminRoleCacheVO esVO) throws Exception {
        elasticsearchClient.index(i -> i
                .index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX)
                .id(String.valueOf(esVO.getId()))
                .document(esVO)
                .refresh(Refresh.True));
    }

    @Override
    public void update(AdminRoleCacheVO esVO) throws Exception {
        Map<String, Object> doc = new LinkedHashMap<>();
        doc.put("id", esVO.getId());
        doc.put("adminId", esVO.getAdminId());
        doc.put("roleId", esVO.getRoleId());
        doc.put("appCode", esVO.getAppCode());
        doc.put("createAdminId", esVO.getCreateAdminId());
        doc.put("deleteStatus", esVO.getDeleteStatus());
        if (esVO.getCreateDate() != null) {
            doc.put("createDate", esVO.getCreateDate().getTime());
        }
        elasticsearchClient.update(u -> u
                        .index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX)
                        .id(String.valueOf(esVO.getId()))
                        .doc(doc)
                        .refresh(Refresh.True),
                Map.class);
    }

    @Override
    public boolean existsIndex() {
        try {
            BooleanResponse exists = elasticsearchClient.indices()
                    .exists(e -> e.index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX));
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
                    .index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Override
    public List<AdminRoleCacheVO> queryById(Long id) throws Exception {
        List<AdminRoleCacheVO> result = new ArrayList<>();
        SearchResponse<AdminRoleCacheVO> response = elasticsearchClient.search(s -> s
                        .index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX)
                        .query(q -> q.term(t -> t.field("_id").value(id)))
                        .size(10),
                AdminRoleCacheVO.class);
        for (Hit<AdminRoleCacheVO> hit : response.hits().hits()) {
            if (hit.source() != null) {
                result.add(hit.source());
            }
        }
        return result;
    }

    @Override
    public List<AdminRoleCacheVO> queryByEntity(AdminRoleCacheVO query) throws Exception {
        List<AdminRoleCacheVO> result = new ArrayList<>();
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
        if (query.getId() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("_id").value(query.getId()))));
        }
        if (query.getAdminId() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("adminId").value(query.getAdminId()))));
        }
        if (query.getRoleId() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("roleId").value(query.getRoleId()))));
        }
        if (query.getAppCode() != null) {
            boolBuilder.must(Query.of(q -> q.term(t -> t.field("appCode").value(query.getAppCode()))));
        }
        Query boolQuery = boolBuilder.build()._toQuery();
        int total = queryCount(boolQuery).intValue();

        SearchResponse<AdminRoleCacheVO> response = elasticsearchClient.search(s -> s
                        .index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX)
                        .query(boolQuery)
                        .size(total > 0 ? total : 10),
                AdminRoleCacheVO.class);
        for (Hit<AdminRoleCacheVO> hit : response.hits().hits()) {
            if (hit.source() != null) {
                result.add(hit.source());
            }
        }
        return result;
    }

    @Override
    public boolean deleteById(String id) throws Exception {
        DeleteResponse response = elasticsearchClient.delete(d -> d
                .index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX)
                .id(id)
                .refresh(Refresh.True));
        return "deleted".equals(response.result().jsonValue());
    }

    @Override
    public boolean deleteIndex() throws Exception {
        return elasticsearchClient.indices()
                .delete(d -> d.index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX))
                .acknowledged();
    }

    @Override
    public boolean deleteByAdminIdAndAppCodes(String adminId, String appCode, List<String> deleteFaildIdList) throws Exception {
        BoolQuery.Builder boolBuilder = new BoolQuery.Builder();
        boolBuilder.must(Query.of(q -> q.term(t -> t.field("adminId").value(adminId))));
        boolBuilder.must(Query.of(q -> q.term(t -> t.field("appCode").value(appCode))));
        Query query = boolBuilder.build()._toQuery();
        int total = queryCount(query).intValue();

        SearchResponse<AdminRoleCacheVO> response = elasticsearchClient.search(s -> s
                        .index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX)
                        .query(query)
                        .size(total > 0 ? total : 10),
                AdminRoleCacheVO.class);

        for (Hit<AdminRoleCacheVO> hit : response.hits().hits()) {
            DeleteResponse deleteResponse = elasticsearchClient.delete(d -> d
                    .index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX)
                    .id(hit.id())
                    .refresh(Refresh.True));
            if (!"deleted".equals(deleteResponse.result().jsonValue())) {
                deleteFaildIdList.add(hit.id());
            }
        }
        return CollectionUtils.isEmpty(deleteFaildIdList);
    }

    @Override
    public void saves(AdminRoleCacheVO[] adminRoleCacheVOS) throws Exception {
        for (AdminRoleCacheVO vo : adminRoleCacheVOS) {
            save(vo);
        }
    }

    public Long queryCount(Query query) throws Exception {
        CountResponse response = elasticsearchClient.count(c -> c
                .index(AdminRoleCacheElasticSearchConstant.ADMIN_ROLE_INDEX).query(query));
        return response.count();
    }
}
