package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import com.toucan.shopping.modules.user.entity.UserTrueNameApproveRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;



/**
 * 用户实名制审核记录
 */
@Mapper
public interface UserTrueNameApproveRecordMapper {

    int insert(UserTrueNameApproveRecord entity);

    List<UserTrueNameApproveRecord> findListByEntity(UserTrueNameApproveRecord entity);


}
