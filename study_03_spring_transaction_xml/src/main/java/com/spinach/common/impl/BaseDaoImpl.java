package com.spinach.common.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.spinach.common.BaseDao;
import com.spinach.common.PageResult;

public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {

	protected Logger logger;
	protected HibernateTemplate hibernateTemplate;
	protected JdbcTemplate jdbcTemplate;

	public List queryList(String hql) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().find(hql);
	}

	public List queryList(String countHql, String hql, PageResult PageResult) {
		return getHibernateTemplate().find(hql);
	}

	public void deleteAll(List entities) {
		getHibernateTemplate().deleteAll(entities);
	}

	public void delete(Object obj) {
		getHibernateTemplate().delete(obj);
	}

	public Object get(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	public List get(final String sql, final Object[] ob) {
		List list = (List) getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(sql);
				int i = 0;
				for (Object o : ob) {
					query.setParameter(i, o);
					i++;
				}
				return query.list();

			}
		});

		return list;
	}

	public List getAll(Class clazz) {
		return getHibernateTemplate().loadAll(clazz);
	}

	public Long save(Object obj) {
		return (Long) getHibernateTemplate().save(obj);
	}

	public void update(Object obj) {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	public void saveOrUpdate(Object obj) {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	public List findEntitiesByHql(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	public List findEntitiesByHqlParam(String hql, Object[] obj) {
		return this.getHibernateTemplate().find(hql, obj);
	}

	/**
	 * 
	 * @version 2010-11-18 上午11:27:26
	 * @author SQJ(Kira.Sun)
	 * @see hql分页方法
	 * @param hql
	 *            hql查询语句； PageResult 分页对象； values 查询参数数组
	 * @return PageResult 分页对象，已经包含了数据集 getData() 的list
	 * @exception {说明在某情况下,将发生什么异常}
	 */
	public PageResult pagedQuery(String hql, PageResult PageResult, Object[] values) {
		String countQueryString = " select count (*) " + checkLeftJoinFetch(removeSelect(removeOrders(hql)));
		List countlist = getHibernateTemplate().find(countQueryString,
				(values == null || values.length == 0) ? null : values);
		int totalCount = ((Number) countlist.get(0)).intValue();
		if (totalCount < 1)
			return PageResult;

		int startIndex = PageResult.getPageSize();
		Query query = createQuery(hql, values);
		List list = query.setFirstResult(startIndex).setMaxResults(PageResult.getPageSize()).list();

		PageResult.setContent(list);
		PageResult.setPageCount(totalCount);

		return PageResult;
	}

	public PageResult listBySqlPage(final String sql, PageResult PageResult) throws Exception {
		final int startIndex = PageResult.getPageSize();
		final int pageSize = PageResult.getPageSize();

		String countSql = "select count(*) from ( " + sql + " ) tempCountSql";
		List countList = this.queryListSQL(countSql);
		int count = 0;
		if (countList != null && countList.size() > 0) {
			count = Integer.valueOf(countList.get(0).toString());
		}
		List ls = this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(sql);
				query.setFirstResult(startIndex).setMaxResults(pageSize);
				List queryList = query.list();
				return queryList;
			}
		});
		PageResult.setContent(ls);
		PageResult.setDataCount(count);
		PageResult.setPageCount((count - 1) / pageSize + 1);
		return PageResult;
	}

	/**
	 * 
	 * @version 2010-11-18 下午01:52:58
	 * @author SQJ(Kira.Sun)
	 * @see {方法的功能/动作描述}
	 * @param {引入参数名}
	 *            {引入参数说明}
	 * @return PageResult {返回参数说明}
	 * @exception {说明在某情况下,将发生什么异常}
	 */
	public PageResult pagedQueryByNameParam(String hql, PageResult page, String[] paramNames, Object[] values) {

		String countQueryString = " select count (*) " + removeSelect(removeOrders(hql));
		List countlist = this.getHibernateTemplate().findByNamedParam(countQueryString, paramNames, values);

		int totalCount = ((Number) countlist.get(0)).intValue();

		if (totalCount < 1)
			return page;

		int startIndex = page.getPageSize();

		Query query = currentSession().createQuery(hql);
		int len = values == null ? 0 : values.length;
		for (int i = 0; i < len; i++) {
			applyNamedParameterToQuery(query, paramNames[i], values[i]);
		}

		List list = query.setFirstResult(startIndex).setMaxResults(page.getPageSize()).list();

		page.setContent(list);
		page.setPageSize(totalCount);

		return page;
	}

	protected Query createQuery(String hql, Object[] values) {
		Query query = currentSession().createQuery(hql);
		int len = values == null ? 0 : values.length;
		for (int i = 0; i < len; i++) {
			query.setParameter(i, values[i]);
		}
		return query;

	}

	protected void applyNamedParameterToQuery(Query queryObject, String paramName, Object value)
			throws HibernateException {

		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection) value);
		} else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		} else {
			queryObject.setParameter(paramName, value);
		}
	}

	private static String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from");
		return hql.substring(beginPos);
	}

	private static String removeOrders(String hql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private String checkLeftJoinFetch(String hql) {
		final String regex = "left join fetch";
		final String replacement = "left join";
		String temp = hql;
		while (temp.indexOf(regex) > 0) {
			temp = temp.replaceAll(regex, replacement);
		}
		return temp;
	}

	public List queryForList(String sql) {
		return jdbcTemplate.queryForList(sql);
	}

	public List queryForList(String sql, Object[] values) {
		return jdbcTemplate.queryForList(sql, values);
	}

	/**
	 * 
	 * @version 2010-12-7 下午04:59:09
	 * @author SQJ(Kira.Sun)
	 * @see 权限sql拼装
	 * @param tableName
	 *            表名， sql 需要拼装sql语句，accountId登录帐号ID
	 * @return String {返回参数说明}
	 * @exception {说明在某情况下,将发生什么异常}
	 */
	public String assemblingSQL(String tableName, String sql, Long accountId) {
		String tempSQL = "SELECT * FROM " + "(" + sql + ") tempTable_oms," + "TM_DATA_AUTHORITY tempAuthority_oms "
				+ "WHERE tempAuthority_oms.RES_ID=tempTable_oms.ID " + "AND tempAuthority_oms.PATH='" + tableName + "' "
				+ "AND tempAuthority_oms.LOGIN_ID=" + accountId;
		logger.info(System.currentTimeMillis() + "execute sql is:" + tempSQL);
		return tempSQL;
	}

	/**
	 * 
	 * @version 2010-12-7 下午05:06:05
	 * @author SQJ(Kira.Sun)
	 * @see 权限hql拼装，支持group by 和order by
	 * @param ClassName
	 *            pojo类名 ，objName pojo对象名 ,hql需要拼装的hql语句，accountId登录帐号ID
	 * @see 例如 select t from TmEmployeeInfor t;
	 *      ClassName：TmEmployeeInfor.class.getName()，objName：t,hql: select t
	 *      from TmEmployeeInfor t
	 * @return String {返回参数说明}
	 * @exception {说明在某情况下,将发生什么异常}
	 */
	public String assemblingHQL(String className, String objName, String hql, Long accountId) {
		String tempHQL = hql;
		tempHQL = tempHQL.replace("ORDER BY", "order by");
		tempHQL = tempHQL.replace("GROUP BY", "group by");
		// 如果存在group by
		if (tempHQL.indexOf("group by") != -1) {
			String argGroup[] = tempHQL.split("group by ");
			int size = argGroup.length;
			tempHQL = argGroup[0];
			for (int i = 1; i < size - 1; i++) {
				tempHQL = tempHQL + argGroup[i];
			}
			// 若存在where
			tempHQL = isWhere(tempHQL, objName, className, accountId);
			tempHQL = tempHQL + " group by " + argGroup[size - 1];
		} else if ((tempHQL.indexOf("order by")) != -1) {
			String argOrder[] = tempHQL.split("order by ");
			int size = argOrder.length;
			tempHQL = argOrder[0];
			for (int i = 1; i < size - 1; i++) {
				tempHQL = tempHQL + argOrder[i];
			}
			// 若存在where
			tempHQL = isWhere(tempHQL, objName, className, accountId);
			tempHQL = tempHQL + " order by " + argOrder[size - 1];
		} else {
			tempHQL = isWhere(tempHQL, objName, className, accountId);
		}
		logger.info(System.currentTimeMillis() + "execute hql is:" + tempHQL);
		return tempHQL;
	}

	private static String assemblingQuery(String tempHQL, String objName, String className, Long accountId,
			String temp) {
		tempHQL = tempHQL + " " + temp + " " + objName + ".id in (select tempAuthority_oms.resId "
				+ "from TmDataAuthority tempAuthority_oms " + "where tempAuthority_oms.objName='" + className + "'"
				+ " and tempAuthority_oms.loginId=" + accountId + ")";
		return tempHQL;
	}

	private static String isWhere(String tempHQL, String objName, String className, Long accountId) {
		if (tempHQL.indexOf("where") != -1) {
			tempHQL = assemblingQuery(tempHQL, objName, className, accountId, "and");
		} else {
			tempHQL = assemblingQuery(tempHQL, objName, className, accountId, "where");
		}
		return tempHQL;
	}

	public List queryListSQL(final String sql) throws Exception {
		List ls = this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query query = session.createSQLQuery(sql);
				List queryList = query.list();
				return queryList;
			}
		});
		return ls;
	}

	/*************** getter && setter ***************/
	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

}
