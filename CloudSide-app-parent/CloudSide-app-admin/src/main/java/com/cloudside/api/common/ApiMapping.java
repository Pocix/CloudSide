/**
 * @author Poci.
 * @date 2018/1/1
 */
package com.cloudside.api.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMapping {
	
	String value();
	
	//TODO 权限验证待实现
	boolean checkLogin() default false;
	
	/**
	 * 方法名
	 * @return
	 */
	String name() default "";
	
	/**
	 * 参数
	 */
	String[] params() default "";
	
	/**
	 * 调用样例
	 */
	String json() default "";
	
}
