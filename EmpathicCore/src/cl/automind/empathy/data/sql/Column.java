package cl.automind.empathy.data.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column{
	String name() default "";
	int length() default 255;
	boolean nullable() default false;
	SqlType type() default SqlType.NONE;
}
