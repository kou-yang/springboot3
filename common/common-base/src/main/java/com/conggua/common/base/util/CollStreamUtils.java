package com.conggua.common.base.util;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ky
 * @description Stream流常用操作工具类
 * @date 2024-04-12 16:25
 */
public class CollStreamUtils {

    /**
     * 转换成一个新的List
     *
     * @param collection 集合
     * @param function   表达式
     * @param <T>        泛型
     * @param <R>        泛型
     * @return 新的集合
     */
    public static <T, R> List<R> toList(Collection<T> collection, Function<? super T, ? extends R> function) {
        return nullDefaultEmpty(collection).stream().map(function).collect(Collectors.toList());
    }

    /**
     * 转换成一个新的List
     *
     * @param collection 集合
     * @param action     表达式
     * @param <T>        泛型
     * @return 新的集合
     */
    public static <T> List<T> toList(Collection<T> collection, Consumer<? super T> action) {
        return nullDefaultEmpty(collection).stream().peek(action).collect(Collectors.toList());
    }

    /**
     * 转换成一个新的List并去重
     *
     * @param collection 集合
     * @param function   表达式
     * @param <T>        泛型
     * @param <R>        泛型
     * @return 新的集合
     */
    public static <T, R> List<R> toDistinctList(Collection<T> collection, Function<? super T, ? extends R> function) {
        return nullDefaultEmpty(collection).stream().map(function).distinct().collect(Collectors.toList());
    }

    /**
     * 转换成一个新的Map
     *
     * @param collection  集合
     * @param keyMapper   表达式
     * @param valueMapper 表达式
     * @param <T>         泛型
     * @param <K>         泛型
     * @param <U>         泛型
     * @return Map
     */
    public static <T, K, U> Map<K, U> toMap(Collection<T> collection, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
        return nullDefaultEmpty(collection).stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * 转换成一个新的Map
     *
     * @param collection    集合
     * @param keyMapper     表达式
     * @param valueMapper   表达式
     * @param mergeFunction 表达式
     * @param <T>           泛型
     * @param <K>           泛型
     * @param <U>           泛型
     * @return Map
     */
    public static <T, K, U> Map<K, U> toMap(Collection<T> collection, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction) {
        return nullDefaultEmpty(collection).stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }

    /**
     * 转换成一个新的Set
     *
     * @param collection 集合
     * @param function   表达式
     * @param <T>        泛型
     * @param <R>        泛型
     * @return 新的集合
     */
    public static <T, R> Set<R> toSet(Collection<T> collection, Function<? super T, ? extends R> function) {
        return nullDefaultEmpty(collection).stream().map(function).collect(Collectors.toSet());
    }

    /**
     * 集合过滤返回List
     *
     * @param collection 源集合
     * @param predicate  表达试
     * @param <T>        泛型
     * @return 过滤后的List
     */
    public static <T> List<T> filterList(Collection<T> collection, Predicate<? super T> predicate) {
        return nullDefaultEmpty(collection).stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 集合过滤返回Set
     *
     * @param collection 源集合
     * @param predicate  表达试
     * @param <T>        泛型
     * @return 过滤后的Set
     */
    public static <T> Set<T> filterSet(Collection<T> collection, Predicate<? super T> predicate) {
        return nullDefaultEmpty(collection).stream().filter(predicate).collect(Collectors.toSet());
    }

    /**
     * 先过滤再Map转新List
     *
     * @param collection 源集合
     * @param predicate  filter表达式
     * @param function   map表达式
     * @param <T>        泛型
     * @param <R>        泛型
     * @return 新的list
     */
    public static <T, R> List<R> filterThenToList(Collection<T> collection, Predicate<? super T> predicate, Function<? super T, ? extends R> function) {
        return nullDefaultEmpty(collection).stream().filter(predicate).map(function).collect(Collectors.toList());
    }

    /**
     * 先过滤再Map转新Set
     *
     * @param collection 源集合
     * @param predicate  filter表达式
     * @param function   map表达式
     * @param <T>        泛型
     * @param <R>        泛型
     * @return 新的Set
     */
    public static <T, R> Set<R> filterThenToSet(Collection<T> collection, Predicate<? super T> predicate, Function<? super T, ? extends R> function) {
        return nullDefaultEmpty(collection).stream().filter(predicate).map(function).collect(Collectors.toSet());
    }

    /**
     * 集合分组
     *
     * @param collection 集合
     * @param classifier key
     * @param <T>        泛型
     * @return Map
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<? super T, ? extends K> classifier) {
        return nullDefaultEmpty(collection).stream().collect(Collectors.groupingBy(classifier));
    }

    /**
     * Integer求和
     *
     * @param collection 源集合
     * @param mapper     表达试
     * @param <T>        泛型
     * @return 和
     */
    public static <T> Integer sumInt(Collection<T> collection, ToIntFunction<? super T> mapper) {
        return nullDefaultEmpty(collection).stream().mapToInt(mapper).filter(Objects::nonNull).sum();
    }

    /**
     * Long求和
     *
     * @param collection 源集合
     * @param mapper     表达试
     * @param <T>        泛型
     * @return 和
     */
    public static <T> Long sumLong(Collection<T> collection, ToLongFunction<? super T> mapper) {
        return nullDefaultEmpty(collection).stream().mapToLong(mapper).filter(Objects::nonNull).sum();
    }

    /**
     * Double求和
     *
     * @param collection 源集合
     * @param mapper     表达试
     * @param <T>        泛型
     * @return 和
     */
    public static <T> Double sumDouble(Collection<T> collection, ToDoubleFunction<? super T> mapper) {
        return nullDefaultEmpty(collection).stream().mapToDouble(mapper).filter(Objects::nonNull).sum();
    }

    /**
     * BigDecimal求和
     *
     * @param collection 源集合
     * @param mapper     表达式
     * @param <T>        泛型
     * @return 和
     */
    public static <T> BigDecimal sumBigDecimal(Collection<T> collection, Function<? super T, BigDecimal> mapper) {
        return nullDefaultEmpty(collection).stream().map(mapper).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 根据集合某字段排序（升序）
     *
     * @param list         排序的集合
     * @param keyExtractor 排序字段
     * @param <T>          泛型
     * @param <U>          泛型
     */
    public static <T, U extends Comparable<? super U>> List<T> sortAsc(List<T> list, Function<? super T, ? extends U> keyExtractor) {
        return nullDefaultEmpty(list).stream().sorted(Comparator.comparing(keyExtractor)).collect(Collectors.toList());
    }

    /**
     * 根据集合某字段排序（降序）
     *
     * @param list         排序的集合
     * @param keyExtractor 排序字段
     * @param <T>          泛型
     * @param <U>          泛型
     */
    public static <T, U extends Comparable<? super U>> List<T> sortDesc(List<T> list, Function<? super T, ? extends U> keyExtractor) {
        return nullDefaultEmpty(list).stream().sorted(Comparator.comparing(keyExtractor).reversed()).collect(Collectors.toList());
    }

    /**
     * 合并集合
     *
     * @param collections 集合
     * @param <T>         泛型
     * @return 合并后的集合
     */
    @SafeVarargs
    public static <T> List<T> mergeCollection(Collection<T>... collections) {
        return Stream.of(collections).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 合并集合（重复了丢弃后面的）
     *
     * @param function    匹配字段
     * @param collections 多个集合
     * @param <T>         泛型
     * @return 合并后的集合
     */
    @SafeVarargs
    public static <T> List<T> leftMergeCollection(Function<? super T, ?> function, Collection<T>... collections) {
        return merge(function, (o1, o2) -> o1, collections);
    }


    /**
     * 合并集合（重复了覆盖前面的）
     *
     * @param function    匹配字段
     * @param collections 多个集合
     * @param <T>         泛型
     * @return 合并后的集合
     */
    @SafeVarargs
    public static <T> List<T> rightMergeCollection(Function<? super T, ?> function, Collection<T>... collections) {
        return merge(function, (o1, o2) -> o2, collections);
    }

    /**
     * 截取集合
     *
     * @param collection 集合
     * @param start      起始位置（1开始）
     * @param size       截取大小
     * @param <T>        泛型
     * @return 截取后的集合
     */
    public static <T> List<T> interceptCollection(Collection<T> collection, long start, long size) {
        return nullDefaultEmpty(collection).stream().skip(start).limit(size).collect(Collectors.toList());
    }

    /**
     * 将集合中的某个字段用delimiter分隔，包含前缀后缀
     *
     * @param delimiter  分隔符
     * @param prefix     前缀
     * @param suffix     后缀
     * @param collection 源集合
     * @param function   表达式
     * @param <T>        泛型
     * @return 分隔后的字符串
     */
    public static <T> String join(Collection<T> collection, Function<T, String> function, CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return nullDefaultEmpty(collection).stream().map(function).collect(Collectors.joining(delimiter, prefix, suffix));
    }

    /**
     * 将集合中的某个字段用delimiter分隔
     *
     * @param delimiter  分隔符
     * @param collection 源集合
     * @param function   表达式
     * @param <T>        泛型
     * @return 分隔后的字符串
     */
    public static <T> String join(Collection<T> collection, Function<T, String> function, CharSequence delimiter) {
        return nullDefaultEmpty(collection).stream().map(function).collect(Collectors.joining(delimiter));
    }

    /**
     * 返回集合中第一个元素
     *
     * @param collection 源集合
     * @param <T>        泛型
     * @return 第一个元素
     */
    public static <T> T findFirst(Collection<T> collection) {
        return nullDefaultEmpty(collection).stream().findFirst().orElse(null);
    }

    /**
     * 返回集合中最后一个元素
     *
     * @param collection 源集合
     * @param <T>        泛型
     * @return 最后一个元素
     */
    public static <T> T findLast(Collection<T> collection) {
        return nullDefaultEmpty(collection).stream().reduce((first, second) -> second).orElse(null);
    }

    /**
     * 判断集合是否为空
     *
     * @param list 源数据
     * @param <T>  泛型
     * @return 集合为空返回空集合，否则返回源数据
     */
    private static <T> Collection<T> nullDefaultEmpty(Collection<T> list) {
        return Optional.ofNullable(list).orElse(Collections.emptyList());
    }

    /**
     * 转换成stream流
     *
     * @param t   源始类型
     * @param <T> 泛型
     * @return Stream
     */
    @SafeVarargs
    private static <T> Stream<T> toStream(T... t) {
        return Stream.of(t);
    }

    /**
     * 自定义条件合并集合
     *
     * @param function      匹配字段
     * @param mergeFunction 合并规则
     * @param collections   多个集合
     * @param <T>           泛型
     * @return 合并后的集合
     */
    @SafeVarargs
    private static <T> List<T> merge(Function<? super T, ?> function, BinaryOperator<T> mergeFunction, Collection<T>... collections) {
        return new ArrayList<>(toStream(collections)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(function, Function.identity(), mergeFunction))
                .values());
    }
}
