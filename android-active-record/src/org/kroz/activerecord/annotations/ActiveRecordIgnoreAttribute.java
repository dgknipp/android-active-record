package org.kroz.activerecord.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Informs the database builder to ignore this field
 * @author csaunders
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ActiveRecordIgnoreAttribute {
	public boolean ignore() default true;
}
