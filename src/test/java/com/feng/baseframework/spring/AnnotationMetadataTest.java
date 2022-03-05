package com.feng.baseframework.spring;

import com.feng.baseframework.annotation.CustomOnProfileCondition;
import com.feng.baseframework.processor.PrintBeanFactoryPostPorcessor;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.StandardAnnotationMetadata;

/**
 * baseframework
 * 2021/5/14 0:04
 * 测试org.springframework.core.type.AnnotationMetadata
 *
 * @author lanhaifeng
 * @since
 **/
public class AnnotationMetadataTest {

    @Test
    public void annotationMetadataTest() {
        String annotationName = CustomOnProfileCondition.class.getName();
        StandardAnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(PrintBeanFactoryPostPorcessor.class, true);
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(annotationName, true));
        System.out.println(attributes.getString("value"));
    }
}
