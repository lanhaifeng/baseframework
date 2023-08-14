package com.feng.baseframework.util;

import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 相似性计算测试
 *
 * @author lanhaifeng
 * @version v3.0
 * @apiNote 时间:2023/8/14 16:00创建:SimilarityTest
 * @since v3.0
 */
public class SimilarityTest {

    @Test
    public void similarityTest() {
        //Jaccard相似度
        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        double jcdSimilarity1 = jaccardSimilarity.apply("hello", "hell");
        System.out.println("jcdSimilarity1:" + jcdSimilarity1);

        //余弦相似度
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        Map<CharSequence, Integer> leftVector = new HashMap<>();
        Map<CharSequence, Integer> rightVector = new HashMap<>();

        leftVector.put("a", 1);
        leftVector.put("b", 0);
        leftVector.put("c", 1);

        rightVector.put("a", 1);
        rightVector.put("b", 1);
        rightVector.put("c", 0);

        double cosSimilarity1 = cosineSimilarity.cosineSimilarity(leftVector, rightVector);
        System.out.println("cosSimilarity1:" + cosSimilarity1);
    }
}
