package com.runspec.producer.util;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class CustomizedPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic("runner-data-event");
        int numPartitions = partitions.size();
        // use Math.abs() may cause overflow [-2147483648,2147483647]
        // so here we should use bit operation , since every bit of MAX_VALUE is 1 except the highest bit
        // the result should be the hashcode without negative sign (but should not be equal to abs of it because of
        // java two's complement
        int hash = key.hashCode() & Integer.MAX_VALUE;
//
//        // Other records will get hashed to the rest of the partitions
//        return (Math.abs(Utils.murmur2(keyBytes)) % numPartitions)
        return (hash % numPartitions);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
