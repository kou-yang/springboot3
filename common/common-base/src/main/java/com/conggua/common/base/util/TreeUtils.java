package com.conggua.common.base.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;

/**
 * @author ky
 * @description
 * @date 2024-05-04 23:48
 */
public class TreeUtils {

    /**
     * 将列表转换为树形结构（默认子节点名称为 "children"）
     * 支持任意节点开始构建
     *
     * @param list           原始列表
     * @param idGetter       获取节点ID的函数
     * @param parentIdGetter 获取父节点ID的函数
     * @param <T>            节点类型
     * @param <K>            ID类型
     * @return 树形结构的根节点列表
     */
    public static <T, K> List<T> translate(List<T> list, Function<T, K> idGetter, Function<T, K> parentIdGetter) {
        return translate(list, idGetter, parentIdGetter, new DefaultChildrenSetter<>());
    }

    /**
     * 将列表转换为树形结构
     * 支持任意节点开始构建
     *
     * @param list           原始列表
     * @param idGetter       获取节点ID的函数
     * @param parentIdGetter 获取父节点ID的函数
     * @param childrenSetter 设置子节点的函数
     * @param <T>            节点类型
     * @param <K>            ID类型
     * @return 树形结构的根节点列表
     */
    public static <T, K> List<T> translate(List<T> list, Function<T, K> idGetter, Function<T, K> parentIdGetter, ChildrenSetter<T> childrenSetter) {
        // 创建一个 Map，用于存储每个节点的引用
        Map<K, T> nodeMap = CollStreamUtils.toMap(list, idGetter, Function.identity());
        // 存储根节点
        List<T> rootNodes = new ArrayList<>();
        // 构建树形结构
        for (T node : list) {
            K parentId = parentIdGetter.apply(node);
            if (Objects.isNull(parentId) || !nodeMap.containsKey(parentId)) {
                // 如果 parentId 为 null 或者 parentId 不在 Map 中，说明是根节点
                rootNodes.add(node);
            } else {
                // 否则，找到其父节点，并将其添加到父节点的 children 列表中
                T parentNode = nodeMap.get(parentId);
                if (Objects.nonNull(parentNode)) {
                    childrenSetter.setChildren(parentNode, node);
                }
            }
        }
        return rootNodes;
    }

    /**
     * 查找指定节点及其子节点（BFS）
     *
     * @param list           原始列表
     * @param ids            待查找的节点ID
     * @param idGetter       获取节点ID的函数
     * @param parentIdGetter 获取父节点ID的函数
     * @param <T>            节点类型
     * @param <K>            ID类型
     * @return 找到的节点列表
     */
    public static <T, K> List<T> getChildrenAndSelf(List<T> list, Collection<K> ids, Function<T, K> idGetter, Function<T, K> parentIdGetter) {
        if (CollectionUtils.isEmpty(list) || CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        // 结果集
        Set<T> result = new HashSet<>();
        // 数据预处理
        Map<K, T> nodeMap = new HashMap<>(list.size());
        Map<K, List<T>> childrenMap = new HashMap<>();
        list.forEach(node -> {
            K id = idGetter.apply(node);
            nodeMap.put(id, node);

            K parentId = parentIdGetter.apply(node);
            childrenMap
                    .computeIfAbsent(parentId, k -> new ArrayList<>())
                    .add(node);
        });
        // 待处理的节点ID队列
        Queue<K> queue = new LinkedList<>(ids);
        while (!queue.isEmpty()) {
            K currentId = queue.poll();
            T currentNode = nodeMap.get(currentId);
            if (currentNode != null && result.add(currentNode)) {
                // 获取子节点并加入队列
                List<T> children = childrenMap.getOrDefault(currentId, Collections.emptyList());
                children.forEach(child -> queue.offer(idGetter.apply(child)));
            }
        }
        return new ArrayList<>(result);
    }

    /**
     * 设置子节点的函数式接口
     */
    @FunctionalInterface
    public interface ChildrenSetter<T> {
        void setChildren(T parent, T child);
    }

    /**
     * 默认的 ChildrenSetter 实现
     */
    private static class DefaultChildrenSetter<T> implements ChildrenSetter<T> {
        @Override
        public void setChildren(T parent, T child) {
            try {
                // 通过反射获取父节点的 children 列表
                List<T> children = (List<T>) parent.getClass()
                        .getMethod("getChildren")
                        .invoke(parent);
                if (Objects.isNull(children)) {
                    children = new ArrayList<>();
                    parent.getClass()
                            .getMethod("setChildren", List.class)
                            .invoke(parent, children);
                }
                children.add(child);
            } catch (Exception e) {
                throw new RuntimeException("Failed to set children for parent node", e);
            }
        }
    }
}

