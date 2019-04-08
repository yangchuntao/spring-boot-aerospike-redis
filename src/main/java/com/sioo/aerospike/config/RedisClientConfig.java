/**
 * Project Name:
 * Class Name:com.sioo.aerospike.config.java
 * <p>
 * Version     Date         Author
 * -----------------------------------------
 * 1.0    2015年11月17日      HanKeQi
 * <p>
 * Copyright (c) 2019, sioo All Rights Reserved.
 */
package com.sioo.aerospike.config;

import com.aerospike.client.Host;
import com.sioo.aerospike.jedis.RedisClient;
import com.sioo.aerospike.properties.AerospikeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HanKeQi
 * @Description
 * @date 2019/4/8 4:14 PM
 **/

@Configuration
@EnableConfigurationProperties(AerospikeProperties.class)
public class RedisClientConfig {

    @Autowired
    private AerospikeProperties properties;

    @Bean
    public RedisClient redisClient() throws Exception{
        if (StringUtils.isEmpty(properties.getHosts())) {
            try {
                return new RedisClient(properties.getHost(), properties.getPort(), properties.getNamespace(), properties.getSet(), properties.getTimeout());
            } catch (Exception e) {
                throw new Exception("Please check the configuration.");
            }
        }
        return new RedisClient(getHostsCluster(),properties.getNamespace(), properties.getSet(), properties.getTimeout());
    }
    //集群操作
    private List<Host> getHostsCluster() throws Exception {
        List<Host> list = new ArrayList<>();
        try {
            String[] commaDelimited = StringUtils.commaDelimitedListToStringArray(properties.getHosts());
            for (String str : commaDelimited){
                String[] colonDelimited = StringUtils.delimitedListToStringArray(str, ":");
                list.add(new Host(colonDelimited[0], Integer.valueOf(colonDelimited[1]).intValue()));
            }
        }catch (Exception e){
            throw new Exception("Please check the configuration file of the configuration cluster.");
        }
        return list;

    }

}
