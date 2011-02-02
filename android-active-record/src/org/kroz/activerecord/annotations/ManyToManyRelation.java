package org.kroz.activerecord.annotations;

import org.kroz.activerecord.ActiveRecordBase;

public @interface ManyToManyRelation {
	public Class<? extends ActiveRecordBase> target();
	public String joinTableName() default "";
}
