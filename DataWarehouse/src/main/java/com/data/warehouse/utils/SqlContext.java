package com.data.warehouse.utils;

/**
 * sql 上下文获取
 * 
 * @Title: SqlContext
 * @Description:
 * @author: 尹雄标
 * @date 2018年8月4日
 */
public class SqlContext {

	// sql语句
	private StringBuilder sql;
	// sql中?对应的参数
	private Object[] params;

	public SqlContext() {
	}

	public SqlContext(StringBuilder sql) {
		this.sql = sql;
	}

	public SqlContext(StringBuilder sql, Object[] params) {
		this.sql = sql;
		this.params = params;
	}

	public String getSql() {
		return sql.toString();
	}

	public StringBuilder getSqlBuilder() {
		return sql;
	}

	public void setSql(StringBuilder sql) {
		this.sql = sql;
	}

	public void setSql(String sql) {
		this.sql = new StringBuilder(sql);
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

}
