package com.data.warehouse.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.data.warehouse.annotation.Key;


/**
 * SQL 语句工具类
 * 
 * @Title: SqlUtil
 * @Description:
 * @author: 尹雄标
 * @date 2018年6月4日
 */
public class SqlUtil {

	private static final String SELECT = "SELECT * FROM ";
	private static final String SELECTCOUNT = "SELECT COUNT(*) ";
	private static final String INSERT = "INSERT INTO ";
	private static final String UPDATE = "UPDATE ";
	private static final String SET = " SET ";
	private static final String WHERE = " WHERE ";
	private static final String WHEREOK = " WHERE 1=1";
	private static final String WHERENO = " WHERE 1=2";
	private static final String DELETE = "DELETE FROM ";
	private static final String LEFT = "(";
	private static final String VALUES = ") VALUES (";
	private static final String RIGHT = ")";
	private static final String AND = " AND ";
	private static final String OR = " OR ";
	private static final String LINK = ",";
	private static final String OCCUPY = "?,";
	private static final String EQUAL = "=?";
	private static final String EQUAL_LINK = "=?,";
	private static final String EQUAL_AND = "=? AND ";
	private static final String FROM = "FROM";
	private static final String ORDER = "ORDER BY";
	private static final String LIMIT = " LIMIT ";
	private static final String order = "order by";
	private static final String limit = " limit ";
	private static final String from = "from";
	private static final String EMPTY = "";
	private static final String R_ORDER = "ORDER.*";
	private static final String R_LIMIT = "LIMIT.*";

	/**
	 * 构建insert语句
	 *
	 * @param bean
	 *            实体映射对象
	 * @return sql上下文
	 */
	public static SqlContext getInsert(Object bean) {
		List<Object> values = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		StringBuilder cols = new StringBuilder();
		StringBuilder placeholder = new StringBuilder();
		try {
			Class<?> cls = bean.getClass();
			for (Field field : cls.getDeclaredFields()) {
				field.setAccessible(true);
				Object val = field.get(bean);
				if (val != null) {
					cols.append(field.getName()).append(LINK);
					placeholder.append(OCCUPY);
					values.add(val);
				}
			}
			cols.deleteCharAt(cols.length() - 1);
			placeholder.deleteCharAt(placeholder.length() - 1);
			sql.append(INSERT);
			sql.append(NameUtil.getUnderlineName(cls.getSimpleName()));
			sql.append(LEFT);
			sql.append(cols);
			sql.append(VALUES);
			sql.append(placeholder);
			sql.append(RIGHT);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new SqlContext(sql, values.toArray());
	}

	/**
	 * 构建update语句
	 *
	 * @param bean
	 *            实体映射对象
	 * @return sql上下文
	 */
	public static SqlContext getUpdate(Object bean) {
		List<Object> values = new ArrayList<Object>();
		List<Object> wheresValue = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		StringBuilder cols = new StringBuilder();
		StringBuilder where = new StringBuilder();
		try {
			Class<?> cls = bean.getClass();
			for (Field field : cls.getDeclaredFields()) {
				field.setAccessible(true);
				Object val = field.get(bean);
				if (val != null) {
					if (field.isAnnotationPresent(Key.class)) {
						where.append(field.getName()).append(EQUAL_AND);
						wheresValue.add(val);
					} else {
						cols.append(field.getName()).append(EQUAL_LINK);
						values.add(val);
					}
				}
			}
			cols.deleteCharAt(cols.length() - 1);
			where.delete(where.length() - 4, where.length());
			values.addAll(wheresValue);
			sql.append(UPDATE);
			sql.append(NameUtil.getUnderlineName(cls.getSimpleName()));
			sql.append(SET);
			sql.append(cols);
			sql.append(WHERE);
			sql.append(where);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new SqlContext(sql, values.toArray());
	}

	/**
	 * 构建update语句
	 *
	 * @param bean
	 *            实体映射对象
	 * @param wheres
	 *            更新条件
	 * @return
	 */
	public static SqlContext getUpdate(Object bean, String... wheres) {
		List<Object> values = new ArrayList<Object>();
		List<Object> wheresValue = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		StringBuilder cols = new StringBuilder();
		StringBuilder where = new StringBuilder();
		try {
			Class<?> cls = bean.getClass();
			for (Field field : cls.getDeclaredFields()) {
				field.setAccessible(true);
				Object val = field.get(bean);
				if (val != null) {
					boolean isKey = false;
					String name = field.getName();
					for (String key : wheres) {
						if (name.equals(key)) {
							isKey = true;
							break;
						}
					}
					if (isKey) {
						where.append(name).append(EQUAL_AND);
						wheresValue.add(val);
					} else {
						cols.append(name).append(EQUAL_LINK);
						values.add(val);
					}
				}
			}
			cols.deleteCharAt(cols.length() - 1);
			where.delete(where.length() - 4, where.length());
			values.addAll(wheresValue);
			sql.append(UPDATE);
			sql.append(NameUtil.getUnderlineName(cls.getSimpleName()));
			sql.append(SET);
			sql.append(cols);
			sql.append(WHERE);
			sql.append(where);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new SqlContext(sql, values.toArray());
	}

	/**
	 * 构建delete语句（删除条件为实体对象@key字段）
	 *
	 * @param bean
	 *            实体映射对象
	 * @return
	 */
	public static SqlContext getDelete(Object bean) {
		List<Object> wheresValue = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		try {
			Class<?> cls = bean.getClass();
			for (Field field : cls.getDeclaredFields()) {
				field.setAccessible(true);
				Object val = field.get(bean);
				if (val != null && field.isAnnotationPresent(Key.class)) {
					where.append(field.getName()).append(EQUAL_AND);
					wheresValue.add(val);
				}
			}
			where.delete(where.length() - 4, where.length());
			sql.append(DELETE);
			sql.append(NameUtil.getUnderlineName(cls.getSimpleName()));
			sql.append(WHERE);
			sql.append(where);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new SqlContext(sql, wheresValue.toArray());
	}

	/**
	 * 构建delete语句
	 *
	 * @param bean
	 *            实体映射对象
	 * @param wheres
	 *            删除条件
	 * @return sql上下文
	 */
	public static SqlContext getDelete(Object bean, String... wheres) {
		List<Object> wheresValue = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		try {
			Class<?> cls = bean.getClass();
			for (Field field : cls.getDeclaredFields()) {
				field.setAccessible(true); // 设置些属性是可以访问的
				Object val = field.get(bean); // 得到此属性的值
				String name = field.getName();
				for (String key : wheres) {
					if (name.equals(key)) {
						where.append(field.getName()).append(EQUAL_AND);
						wheresValue.add(val);
					}
				}
			}
			where.delete(where.length() - 4, where.length());
			sql.append(DELETE);
			sql.append(NameUtil.getUnderlineName(cls.getSimpleName()));
			sql.append(WHERE);
			sql.append(where);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new SqlContext(sql, wheresValue.toArray());
	}

	/**
	 * 构建delete语句
	 *
	 * @param cls
	 *            实体映射对象
	 * @return sql上下文
	 */
	public static SqlContext getDelete(Class<?> cls, Object[] ids) {
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		for (Field field : cls.getDeclaredFields()) {
			if (field.isAnnotationPresent(Key.class)) {
				for (int i = 0; i < ids.length; i++) {
					where.append(OR).append(field.getName()).append(EQUAL);
				}
				break;
			}
		}
		sql.append(DELETE);
		sql.append(NameUtil.getUnderlineName(cls.getSimpleName()));
		sql.append(WHERENO);
		sql.append(where);
		return new SqlContext(sql, ids);
	}

	/**
	 * 构建delete语句
	 *
	 * @param cls
	 *            实体映射对象
	 * @return sql上下文
	 */
	public static SqlContext getDelete(Class<?> cls, Object[] ids, Object... key2) {
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		int keyIndex = 1;
		for (Field field : cls.getDeclaredFields()) {
			if (field.isAnnotationPresent(Key.class)) {
				if (keyIndex == 1) {
					where.append(OR).append(LEFT);
					for (int i = 0; i < ids.length; i++) {
						if (i > 0)
							where.append(OR);
						where.append(field.getName()).append(EQUAL);
					}
					where.append(RIGHT);
					keyIndex++;
				} else {
					where.append(AND).append(field.getName()).append(EQUAL);
				}
			}
		}
		sql.append(DELETE);
		sql.append(NameUtil.getUnderlineName(cls.getSimpleName()));
		sql.append(WHERENO);
		sql.append(where);
		ids = ToolsUtil.concat(ids, key2);
		return new SqlContext(sql, ids);
	}

	/**
	 * 构建select语句
	 *
	 * @param cls
	 *            类对象
	 * @return sql上下文
	 */
	public static SqlContext getSelect(Class<?> cls) {
		StringBuilder sql = new StringBuilder(SELECT);
		sql.append(NameUtil.getUnderlineName(cls.getSimpleName()));
		return new SqlContext(sql);
	}

	/**
	 * 构建单个对象查询语条件为带有@key的字段
	 *
	 * @param cls
	 * @param id
	 *            查询条件
	 * @return sql上下文
	 */
	public static SqlContext getByKey(Class<?> cls, Object id) {
		SqlContext sqlContext = getSelect(cls);
		StringBuilder sql = sqlContext.getSqlBuilder();
		sql.append(WHERE);
		for (Field field : cls.getDeclaredFields()) {
			field.setAccessible(true);
			if (field.isAnnotationPresent(Key.class)) {
				sql.append(field.getName()).append(EQUAL);
				sqlContext.setParams(new Object[] { id });
			}
		}
		return sqlContext;
	}

	/**
	 * 构建select语句
	 *
	 * @param cls
	 *            类对象
	 * @param params
	 *            查询参数
	 * @return sql上下文
	 */
	public static SqlContext getSelect(Class<?> cls, Map<String, Object> params) {
		SqlContext sqlContext = getSelect(cls);
		StringBuilder sql = sqlContext.getSqlBuilder();
		sql.append(WHEREOK);
		List<Object> values = new ArrayList<Object>();
		for (Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			sql.append(AND).append(key).append(EQUAL);
			values.add(value);
		}
		sqlContext.setParams(values.toArray());
		return sqlContext;
	}

	/**
	 * 构建select语句
	 *
	 * @param sql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @return sql上下文
	 */
	public static SqlContext getSelect(StringBuilder sql, Map<String, Object> params) {
		sql.append(WHEREOK);
		List<Object> values = new ArrayList<Object>();
		for (Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			sql.append(AND).append(key).append(EQUAL);
			values.add(value);
		}
		return new SqlContext(sql, values.toArray());
	}

	/**
	 * 构建select语句
	 *
	 * @param cls
	 *            类对象
	 * @param page
	 *            当前页码
	 * @param rows
	 *            每页条数
	 * @param params
	 *            查询参数
	 * @return sql上下文
	 */
	public static SqlContext getSelect(Class<?> cls, int page, int rows, Map<String, Object> params) {
		SqlContext sqlContext = getSelect(cls, params);
		StringBuilder sql = sqlContext.getSqlBuilder();
		sql.append(LIMIT);
		sql.append((page - 1) * rows);
		sql.append(LINK);
		sql.append(rows);
		return sqlContext;
	}

	/**
	 * 构建select语句
	 *
	 * @param sql
	 *            类对象
	 * @param page
	 *            当前页码
	 * @param rows
	 *            每页条数
	 * @param params
	 *            查询参数
	 * @return sql上下文
	 */
	public static SqlContext getSelect(String sql, int page, int rows, Map<String, Object> params) {
		SqlContext sqlContext = getSelect(new StringBuilder(sql), params);
		StringBuilder pageSql = sqlContext.getSqlBuilder();
		pageSql.append(LIMIT);
		pageSql.append((page - 1) * rows);
		pageSql.append(LINK);
		pageSql.append(rows);
		return sqlContext;
	}

	/**
	 * 构建count语句(查询语句中主FROM必须大写，其他from小写)
	 *
	 * @param sqlContext
	 *            类对象
	 * @return sql上下文
	 */
	public static SqlContext getCount(SqlContext sqlContext) {
		sqlContext.setSql(getCount(sqlContext.getSql()));
		return sqlContext;
	}

	/**
	 * 构建count语句(查询语句中主FROM必须大写，其他from小写)
	 *
	 * @param sql
	 *            类对象
	 * @return sql语句
	 */
	public static String getCount(String sql) {
		int num = 0;
		String xing = "*";
		StringBuilder sb = new StringBuilder();
		for (char cr : sql.toCharArray()) {
			if (')' == cr) {
				num++;
			}
			if (num == 0) {
				sb.append(cr);
			} else {
				sb.append(xing);
			}
			if ('(' == cr) {
				num--;
			}
		}
		int i = sb.toString().replace(from, FROM).indexOf(FROM);
		sql = sql.substring(i);
		sql = sql.replace(order, ORDER).replace(limit, LIMIT).replaceAll(R_ORDER, EMPTY).replaceAll(R_LIMIT, EMPTY);
		return SELECTCOUNT.concat(sql);
	}

	/**
	 * 构建select语句
	 *
	 * @param sql
	 *            类对象
	 * @param page
	 *            当前页码
	 * @param rows
	 *            每页条数
	 * @return sql语句
	 */
	public static String getSelect(String sql, int page, int rows) {
		StringBuilder pageSql = new StringBuilder(sql);
		pageSql.append(LIMIT);
		pageSql.append((page - 1) * rows);
		pageSql.append(LINK);
		pageSql.append(rows);
		return pageSql.toString();
	}

	/**
	 * 构建select语句
	 *
	 * @param cls
	 *            类对象
	 * @param fields
	 *            当前页码
	 * @param parmas
	 *            每页条数
	 * @return sql上下文
	 */
	public static SqlContext getByParams(Class<?> cls, String[] fields, Object... parmas) {
		SqlContext sqlContext = getSelect(cls);
		StringBuilder sql = sqlContext.getSqlBuilder();
		sql.append(WHERE);
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < fields.length; i++) {
			if (i > 0)
				sql.append(AND);
			sql.append(fields[i]).append(EQUAL);
			values.add(parmas[i]);
		}
		sqlContext.setParams(values.toArray());
		return sqlContext;
	}
}