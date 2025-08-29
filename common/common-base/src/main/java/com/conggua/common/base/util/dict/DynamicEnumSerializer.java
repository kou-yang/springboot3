package com.conggua.common.base.util.dict;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-07-17 11:11
 */
@AllArgsConstructor
public class DynamicEnumSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    /**
     * 使用缓存提升性能
     */
    private static final Map<CacheKey, EnumMetaData> ENUM_CACHE = new ConcurrentHashMap<>();

    private final EnumMetaData metaData;
    private final String targetField;
    private final Boolean ignoreCase;
    private final String unknownValue;

    private DynamicEnumSerializer() {
        this(null, null, null, null);
    }

    /**
     * 枚举元数据缓存对象
     */
    @Data
    private static class EnumMetaData {
        private final Object[] codes;
        private final String[] values;
        // 用于快速查找
        private final Map<Object, String> valueMap;
        // 忽略大小写的查找
        private final Map<Object, String> ignoreCaseValueMap;
    }

    @Value
    private static class CacheKey {
        Class<?> enumClass;
        String codeMethod;
        String valueMethod;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializer) throws IOException {
        // 先写入原始值
        gen.writeObject(value);
        // 处理枚举转换
        if (value != null) {
            String currentField = gen.getOutputContext().getCurrentName();
            String newField = StringUtils.isBlank(targetField) ? currentField + "Name" : targetField;

            String displayName = resolveDisplayName(value);
            gen.writeStringField(newField, displayName);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Dict annotation = property.getAnnotation(Dict.class);
        if (annotation != null) {
            EnumMetaData metaData = ENUM_CACHE.computeIfAbsent(
                    new CacheKey(annotation.enumClass(), annotation.codeMethod(), annotation.valueMethod()),
                    k -> parseEnumMetaData(k.getEnumClass(), k.getCodeMethod(), k.getValueMethod())
            );
            return new DynamicEnumSerializer(
                    metaData,
                    annotation.targetField(),
                    annotation.ignoreCase(),
                    annotation.unknownValue()
            );
        }
        return prov.findValueSerializer(property.getType(), property);
    }

    @SneakyThrows
    private static EnumMetaData parseEnumMetaData(Class<?> enumClass, String codeMethod, String valueMethod) {
        Enum<?>[] enums = (Enum<?>[]) MethodUtils.invokeStaticMethod(enumClass, "values");

        Object[] codes = new Object[enums.length];
        String[] values = new String[enums.length];
        Map<Object, String> valueMap = new HashMap<>();
        Map<Object, String> ignoreCaseValueMap = new HashMap<>();

        Method getCodeMethod = MethodUtils.getAccessibleMethod(enumClass, codeMethod);
        Method getValueMethod = MethodUtils.getAccessibleMethod(enumClass, valueMethod);

        for (int i = 0; i < enums.length; i++) {
            Enum<?> e = enums[i];

            // 获取code
            Object code = getCodeMethod != null
                    ? MethodUtils.invokeMethod(e, getCodeMethod.getName())
                    : e.name();
            codes[i] = code;

            // 获取显示名称
            String value = getValueMethod != null
                    ? (String) MethodUtils.invokeMethod(e, getValueMethod.getName()) :
                    e.name();
            values[i] = value;

            // 填充查找表
            valueMap.put(code, value);
            if (code instanceof String) {
                ignoreCaseValueMap.put((StringUtils.lowerCase((String) code)), value);
            }
        }

        return new EnumMetaData(codes, values, Collections.unmodifiableMap(valueMap), Collections.unmodifiableMap(ignoreCaseValueMap));
    }

    private String resolveDisplayName(Object value) {
        Object key = ignoreCase && value instanceof String
                ? StringUtils.lowerCase((String) value)
                : value;

        Map<Object, String> lookupMap = ignoreCase
                ? metaData.getIgnoreCaseValueMap()
                : metaData.getValueMap();
        return lookupMap.getOrDefault(key, unknownValue);
    }
}
