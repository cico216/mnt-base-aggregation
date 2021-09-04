package com.mnt.tools.anno.prop;

import java.lang.annotation.*;


/**
 * 不被克隆的字段注解
 *
 * @author jiangbiao
 * @Date 2017年4月18日下午2:41:10
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropNoClone {

}
