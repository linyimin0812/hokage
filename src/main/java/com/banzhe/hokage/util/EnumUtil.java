package com.banzhe.hokage.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author linyimin
 * @date 2020/10/31 2:52 下午
 * @email linyimin520812@gmail.com
 * @description 通过名称获取枚举对象
 */
public class EnumUtil {
    public static <T extends Enum<T>> Optional<T> resolveByName(Class<T> enumClass, String name) {
        if (ArrayUtils.isEmpty(enumClass.getEnumConstants()) || StringUtils.isBlank(name)) {
            return Optional.empty();
        }

        return Stream.of(enumClass.getEnumConstants())
                .filter(enumVal -> StringUtils.equalsIgnoreCase(name, enumVal.name()))
                .findFirst();
    }
}
