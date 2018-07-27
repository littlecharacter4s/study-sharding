package com.lc.sharding.algorithm;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.MultipleKeysTableShardingAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wanghh on 2018-7-26.
 */
public class MultipleKeysTableShardingAlgorithmImpl implements MultipleKeysTableShardingAlgorithm {
    public Collection<String> doSharding(Collection<String> tableNames, Collection<ShardingValue<?>> shardingValues) {
        List<String> shardingSuffix = new ArrayList<String>();
        long partId = 0;
        for (ShardingValue value : shardingValues) {
            if (value.getColumnName().equals("user_id")) {
                partId = ((Long) value.getValue()) % 4;
                break;
            } else if (value.getColumnName().equals("order_id")) {
                partId = ((Long) value.getValue()) % 4;
                break;
            }
        }
        for (String name : tableNames) {
            if (name.endsWith(partId + "")) {
                shardingSuffix.add(name);
                return shardingSuffix;
            }
        }
        return shardingSuffix;
    }
}
