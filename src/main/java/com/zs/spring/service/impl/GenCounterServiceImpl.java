package com.zs.spring.service.impl;

import com.zs.spring.annoation.DataSource;
import com.zs.spring.mapper.CounterMapper;
import com.zs.spring.service.GenCounterService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @auther: madisonzhuang
 * @date: 2019-02-27 17:17
 * @description:
 */
//@Service
@Slf4j
//@Transactional
public class GenCounterServiceImpl implements GenCounterService {
    private static final String COUNTER_KEY = "hub:cache:creditLimit:counter:*";

    @Autowired
    private CounterMapper counterMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询redis里面counter数据并插入到表中
     */
    @Override
    @DataSource(value = "dataSource")
    public void genCounter() {
        Set<String> counterKeys = stringRedisTemplate.keys(COUNTER_KEY);

        for (String key : counterKeys) {
            String counterValue = stringRedisTemplate.opsForValue().get(key);
            String groupId = key.replace("hub:cache:creditLimit:counter:", "");
            String[] groupIdArr = groupId.split(":");
            String dpGroupId = groupIdArr[0];
            List<String> dpGroupNames = counterMapper.findGroupNameById(dpGroupId);
            String cpGroupId = "";
            String cpGroupName = "";
            if (groupIdArr.length > 1) {
                cpGroupId = groupIdArr[1];
                cpGroupName = counterMapper.findGroupNameById(cpGroupId).get(0);
            }
            String groupIds = "";
            String groupNames = "";
            if (!cpGroupId.equals("")) {
                groupIds = dpGroupId + ":" + cpGroupId;
                groupNames = dpGroupNames.get(0) + ":" + cpGroupName;
            }

            counterMapper.insertCounter(groupIds, groupNames, counterValue);

//            throw new RuntimeException();
        }
        log.info("生成{}条数据", counterKeys.size());
    }
}
