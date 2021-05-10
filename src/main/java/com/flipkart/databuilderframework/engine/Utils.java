package com.flipkart.databuilderframework.engine;

import com.flipkart.databuilderframework.annotations.DataBuilderClassInfo;
import com.flipkart.databuilderframework.annotations.DataBuilderInfo;
import com.flipkart.databuilderframework.model.Data;
import com.flipkart.databuilderframework.model.DataBuilderMeta;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * Utilities for various stuff
 */
public interface Utils {

    static String name(Object object) {
        return name(object.getClass());
    }

    static String name(Class<?> clazz) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, clazz.getSimpleName());
    }

    static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    static boolean isEmpty(Map collection) {
        return null == collection || collection.isEmpty();
    }

    static<T> Set<T> sanitize(Set<T> collection) {
        return isEmpty(collection)
                ? Collections.emptySet()
                : collection;
    }

    static<T> List<T> sanitize(List<T> collection) {
        return isEmpty(collection)
                ? Collections.emptyList()
                : collection;
    }

    static<K,V> Map<K, V> sanitize(Map<K,V> collection) {
        return isEmpty(collection)
                ? Collections.emptyMap()
                : collection;
    }

    static<T extends DataBuilder> DataBuilderMeta meta(T annotatedDataBuilder) {
        return meta(annotatedDataBuilder.getClass());
    }

    static DataBuilderMeta meta(Class<? extends DataBuilder> annotatedDataBuilder) {
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

    static DataBuilderMeta meta(Set<String> consumes, Set<String> optionals, Set<String> accesses, Class<? extends DataBuilder> annotatedDataBuilder) {
        DataBuilderInfo info = annotatedDataBuilder.getAnnotation(DataBuilderInfo.class);
            return new DataBuilderMeta(
                    consumes != null ? consumes:ImmutableSet.copyOf(info.consumes()),
                    info.produces(),
                    info.name(),
                    optionals != null ? optionals:ImmutableSet.copyOf(info.optionals()),
                    accesses != null ? accesses:ImmutableSet.copyOf(info.accesses()));
    }
}
