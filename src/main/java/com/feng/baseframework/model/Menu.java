package com.feng.baseframework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ProjectName: baseframework
 * @Description: 菜单实体类
 * @Author: lanhaifeng
 * @CreateDate: 2019/10/7 18:00
 * @UpdateUser:
 * @UpdateDate: 2019/10/7 18:00
 * @UpdateRemark:
 * @Version: 1.0
 */
@Accessors(chain = true)
@Setter
@Getter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class Menu implements Serializable {

    private static final long serialVersionUID = 1458868512556863009L;
    private Integer id;
    private String url;
    private Integer seq;
    private Integer parentId;
    private String name;
    private String icon;
    private Integer order;
    private Integer isLeaf;
    private String mark;

    private String roleName;
}
