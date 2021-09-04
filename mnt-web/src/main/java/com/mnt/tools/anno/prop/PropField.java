package com.mnt.tools.anno.prop;

import java.lang.annotation.*;


/**
 * 属性克隆字段 
 *
 * @author jiangbiao
 * @Date 2017年4月18日下午2:40:00
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropField {

	/**
	 * 对应克隆的字段名称
	 * @return
	 */
	String value();

}
