package cl.automind.empathy.data.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SqlMetadata {
	public static final String DSNAME = "#DSNAME#";
	/**
	 * The preferred table name. If not set, it will be automatically generated.
	 * @return The preferred table name
	 */
	public String name() default  "";
	/**
	 * The name of the schema containing this table
	 * @return The schema name
	 */
	public String schema() default "";
	/**
	 * Determines if the table should be created when the annotated
	 * {@link ISqlDataSource} is instantiated
	 * @return <b>true</b> if table should be created on datasource instantiation. </br><b>false</b> if not.
	 */
	public boolean createOnInit() default false;
	/**
	 * Queries are using a keyword for databasename, all occurrences of
	 * {@code "@name@"} are replaced with the final name of the annotated {@link ISqlDataSource}.
	 * @return <b>true</b> if queries use {@code "@name@"} pattern</br> <b>false</b> if not
	 */
	public boolean dynamicName() default false;
	/**
	 * Returns the set of queries that should be executed upon instantiation of the annotated
	 * {@link ISqlDataSource}. These queries are independent of the creational query, and will
	 * run even if {@code createOnInit()} is <b>false</b>
	 * @return An array containing queries to be executed upon datasource creation
	 */
	public NamedQuery[] initQueries() default {};
	/**
	 * Returns a set of queries. These queries are intendended for recurrent use and are 
	 * compiled into {@link java.sql.PreparedStatement}s. Queries can be called by name using
	 * the {@link IQueryOption} {@code Query}.
	 * @return An array of pre-compiled queries callable by name
	 */
	public NamedQuery[] namedQueries() default {};
}
