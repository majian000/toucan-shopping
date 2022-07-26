package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ConsigneeAddressMapper {

    int insert(ConsigneeAddress entity);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    int deleteByIdAndUserMainIdAndAppCode(Long id, Long userMainId,String appCode);

    int setDefaultByIdAndUserMainIdAndAppCode(Long id, Long userMainId,String appCode);

    int setCancelDefaultByUserMainIdAndAppCode(Long userMainId,String appCode);

    int update(ConsigneeAddress entity);

    List<ConsigneeAddress> findListByEntity(ConsigneeAddressVO query);


    List<ConsigneeAddress> queryListPage(ConsigneeAddressPageInfo queryPageInfo);

    Long queryListPageCount(ConsigneeAddressPageInfo queryPageInfo);

    ConsigneeAddressVO findByIdAndUserMainIdAndAppCode(Long id, Long userMainId, String appCode);

    ConsigneeAddressVO findDefaultByUserMainIdAndAppCode(Long userMainId,String appCode);

    ConsigneeAddressVO findNewestOneByUserMainIdAndAppCode(Long userMainId,String appCode);

}
