package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataBuilderMeta;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utilities for various stuff
 */
@UtilityClass
public class Utils {

    private final ConcurrentHashMap<Class<?>, String> CLASS_TO_NAME_MAPPING = new ConcurrentHashMap<>();

    public static String name(Object object) {
        return name(object.getClass());
    }

    public static String name(Class<?> clazz) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE,
                CLASS_TO_NAME_MAPPING.computeIfAbsent(clazz, aClass -> clazz.getSimpleName()));
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Map collection) {
        return null == collection || collection.isEmpty();
    }

    public static<T> Set<T> sanitize(Set<T> collection) {
        return isEmpty(collection)
                ? Collections.emptySet()
                : collection;
    }

    public static<T> List<T> sanitize(List<T> collection) {
        return isEmpty(collection)
                ? Collections.emptyList()
                : collection;
    }

    public static<K,V> Map<K, V> sanitize(Map<K,V> collection) {
        return isEmpty(collection)
                ? Collections.emptyMap()
                : collection;
    }

    public static<T extends DataBuilder> DataBuilderMeta meta(T annotatedDataBuilder) {
        return meta(annotatedDataBuilder.getClass());
    }

    public static DataBuilderMeta meta(Class<? extends DataBuilder> annotatedDataBuilder) {
        DataBuilderInfo info = annotatedDataBuilder.getAnnotation(DataBuilderInfo.class);
        if(null != info) {
            return new DataBuilderMeta(
                    ImmutableSet.copyOf(info.consumes()),
                    info.produces(),
                    info.name(),
                    ImmutableSet.copyOf(info.optionals()),
                    ImmutableSet.copyOf(info.accesses()));
        }
        else {
            DataBuilderClassInfo dataBuilderClassInfo = annotatedDataBuilder.getAnnotation(DataBuilderClassInfo.class);
            if(null == dataBuilderClassInfo) {
                return null;
            }
            Set<String> consumes = Sets.newHashSet();
            Set<String> optionals = Sets.newHashSet();
            Set<String> access = Sets.newHashSet();
            //TODO (gokul) remove getCannonicalName() and inject a Handler for client to customize this

            for (Class<? extends Data> data : dataBuilderClassInfo.consumes()) {
                consumes.add(Utils.name(data));
            }

            for (Class<? extends Data> data : dataBuilderClassInfo.optionals()) {
                optionals.add(Utils.name(data));
            }

            for (Class<? extends Data> data : dataBuilderClassInfo.accesses()) {
                access.add(Utils.name(data));
            }

            final String name = dataBuilderClassInfo.name();
            return new DataBuilderMeta(
                    ImmutableSet.copyOf(consumes),
                    Utils.name(dataBuilderClassInfo.produces()),
                    Strings.isNullOrEmpty(name)
                        ? Utils.name(annotatedDataBuilder)
                        : name,
                    ImmutableSet.copyOf(optionals),
                    ImmutableSet.copyOf(access)
            );
        }
    }
}
