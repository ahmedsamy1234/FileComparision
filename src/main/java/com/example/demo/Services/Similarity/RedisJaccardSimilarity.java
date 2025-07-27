package com.example.demo.Services.Similarity;


import com.example.demo.Services.Interfaces.FileSimilarityStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service("redisSimilarityStrategy")
@RequiredArgsConstructor
public class RedisJaccardSimilarity implements FileSimilarityStrategy {
    private final Jedis jedis;

    @Override
    public double calculateSimilarity(String file1, String file2) {
        String key1 = "file_words:" + file1;
        String key2 = "file_words:" + file2;
        jedis.sinterstore("tmp_intersection", key1, key2);
        jedis.sunionstore("tmp_union", key1, key2);
        long intersection = jedis.scard("tmp_intersection");
        long union = jedis.scard("tmp_union");
        jedis.del("tmp_intersection", "tmp_union");
        return union == 0 ? 0.0 : ((double) intersection / union) * 100;
    }

    @Override
    public String getType() {
        return "redis";
    }
}