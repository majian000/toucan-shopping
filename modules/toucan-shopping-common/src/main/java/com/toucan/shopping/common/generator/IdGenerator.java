package com.toucan.shopping.common.generator;


import com.toucan.shopping.common.util.SnowflakeIdWorker;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
public class IdGenerator {

    private SnowflakeIdWorker snowflakeIdWorker = null;

    public synchronized Long id()
    {
        if(snowflakeIdWorker==null)
        {
            throw new IllegalArgumentException("ID生成器没有找到雪花算法实例");
        }
        return snowflakeIdWorker.nextId();
    }

}
