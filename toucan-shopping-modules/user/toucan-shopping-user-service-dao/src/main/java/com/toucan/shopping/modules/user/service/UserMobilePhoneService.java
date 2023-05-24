package com.toucan.shopping.modules.user.service;




import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.UserMobilePhone;
import com.toucan.shopping.modules.user.page.UserPageInfo;

import java.util.List;

public interface UserMobilePhoneService {

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<UserMobilePhone> findListByEntity(UserMobilePhone entity);

    /**
     * 根据实体查询列表
     * @param entity
     * @return
     */
    List<UserMobilePhone> findListByEntityNothingDeleteStatus(UserMobilePhone entity);

    /**
     * 根据手机号查询列表
     * @param mobilePhone
     * @return
     */
    List<UserMobilePhone> findListByMobilePhone(String mobilePhone);

    /**
     * 根据手机号查询列表
     * @param mobilePhone
     * @return
     */
    List<UserMobilePhone> findListByMobilePhone(String mobilePhone,List<Long> userMianIdList);
    /**
     * 根据手机号查询列表
     * @param mobilePhone
     * @return
     */
    List<UserMobilePhone> findListByMobilePhoneLike(String mobilePhone);

    int save(UserMobilePhone entity);

    List<UserMobilePhone> queryListByUserMainId(Long[] userIdArray);

    List<UserMobilePhone> queryListByUserMainIdNothingDeleteStatus(Long[] userIdArray);

    PageInfo<UserMobilePhone> queryListPageNothingDeleteStatus(UserPageInfo queryPageInfo);


    int updateDeleteStatus(Short deleteStatus,Long userMainId,String mobilePhone);

    int updateDeleteStatusById(Short deleteStatus,Long userMainId,String mobilePhone,Long id);

    /**
     * 删除指定用户ID下所有关联手机号
     * @param userMainId
     * @return
     */
    int deleteByUserMainId(Long userMainId);

    /**
     * 查询有效的手机号关联
     * @param userMainId
     * @return
     */
    UserMobilePhone findByUserMainId(Long userMainId);

}
