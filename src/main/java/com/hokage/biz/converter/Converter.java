package com.hokage.biz.converter;

/**
 * @author linyimin
 * @date 2020/12/23 23:33
 * @email linyimin520812@gmail.com
 * @description
 */
public interface Converter<A, B> {
    /**
     * A converter to B
     * @param a A
     * @return B
     */
    B doForward(A a);

    /**
     * B converter to A
     * @param b B
     * @return A
     */
    A doBackward(B b);
}