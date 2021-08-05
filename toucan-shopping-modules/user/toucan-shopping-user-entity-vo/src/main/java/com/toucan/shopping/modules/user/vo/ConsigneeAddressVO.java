package com.toucan.shopping.modules.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 收货地址
 */
@Data
public class ConsigneeAddressVO extends ConsigneeAddress {


}
