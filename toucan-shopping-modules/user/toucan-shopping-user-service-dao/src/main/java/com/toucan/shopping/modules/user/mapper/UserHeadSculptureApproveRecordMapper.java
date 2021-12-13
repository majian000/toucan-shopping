package com.toucan.shopping.modules.user.mapper;

import com.toucan.shopping.modules.user.entity.UserHeadSculptureApproveRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 用户头像审核记录
 */
@Mapper
public interface UserHeadSculptureApproveRecordMapper {

    int insert(UserHeadSculptureApproveRecord entity);

    List<UserHeadSculptureApproveRecord> findListByEntity(UserHeadSculptureApproveRecord entity);


}
