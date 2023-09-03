package com.feng.baseframework.mapper;

import java.util.List;

/**
 *  通用Mapper
 */
public interface CommonMapper<T> {

    List<T> selectAll();

}
