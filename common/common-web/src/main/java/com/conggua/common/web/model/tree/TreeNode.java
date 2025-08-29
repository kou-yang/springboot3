package com.conggua.common.web.model.tree;

import lombok.Data;

import java.util.List;

/**
 * @author ky
 * @description
 * @date 2024-05-17 15:15
 */
@Data
public class TreeNode {

    private String id;

    private String parentId;

    private Integer orderNum;

    private List<TreeNode> children;
}
