package com.hokage.biz.interceptor;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * @author yiminlin
 * @date 2021/07/08 11:32 pm
 * @description user context
 **/
@Data
@Accessors(chain = true)
public class UserContext {
    private static final TransmittableThreadLocal<UserContext> LOCAL = new TransmittableThreadLocal<>();

    private Long userId;
    private Integer role;

    public static UserContext ctx() {
        if (Objects.isNull(LOCAL.get())) {
            LOCAL.set(new UserContext());
        }
        return LOCAL.get();
    }

    public static void setUserContext(UserContext context) {
        LOCAL.set(context);
    }

    public static void clean() {
        LOCAL.remove();
    }
}
