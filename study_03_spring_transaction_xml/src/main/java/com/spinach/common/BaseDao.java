package com.spinach.common;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;


public interface BaseDao {
	
	public List get(final String  sql, final Object[] ob) ;
	
	public Object get(Class clazz, Serializable id);
	
	/**
	 * 
	 * @version 2010-11-19 下午03:35:39
	 * @author  SQJ(Kira.Sun)
	 * @see		获取该POJO 所有数据集
	 * @param      clazz   Pojo.class
	 * @return      List {返回参数说明}
	 * @exception   {说明在某情况下,将发生什么异常}
	 */
	public List getAll(Class clazz);

	public Long save(Object obj);

	public void delete(Object obj);

	public void update(Object obj);
	
	public void saveOrUpdate(Object object);
	
	public void deleteAll(List entities);
	
	public List queryList(String hql);
	
	public List queryList(String countHql, String hql, PageResult PageResult);
	/**
	 * 
	 * @version 2010-11-18 下午02:46:38
	 * @author  SQJ(Kira.Sun)
	 * @see		带参数的hql查询
	 * @param      hql 查询语句   ， obj 参数数组
	 * @return      List {返回参数说明}
	 * @exception   {说明在某情况下,将发生什么异常}
	 */
	public List findEntitiesByHqlParam(String hql, Object[] obj);
	
	/**
	 * 
	 * @version 2010-11-18 上午11:27:26
	 * @author  SQJ(Kira.Sun)
	 * @see		hql分页方法
	 * @param      hql hql查询语句；  PageResult 分页对象； values 查询参数数组
	 * @return      PageResult 分页对象，已经包含了数据集 getData() 的list
	 * @exception   {说明在某情况下,将发生什么异常}
	 */
	public PageResult pagedQuery(String hql, PageResult PageResult, Object[] values);
	
	
	/**
	 * 
	 * @version 2010-11-18 下午01:52:58
	 * @author  SQJ(Kira.Sun)
	 * @see		{方法的功能/动作描述}
	 * @param      {引入参数名}   {引入参数说明}
	 * @return      PageResult {返回参数说明}
	 * @exception   {说明在某情况下,将发生什么异常}
	 */
	public PageResult pagedQueryByNameParam(String hql, PageResult page,
			String[] paramNames, Object[] values);
	
	/**
	 * 
	 * @version 2010-12-7 下午05:18:46
	 * @author  SQJ(Kira.Sun)
	 * @see		带参数的sql查询
	 * @param      {引入参数名}   {引入参数说明}
	 * @return      List {返回参数说明}
	 * @exception   {说明在某情况下,将发生什么异常}
	 */
	public  List queryForList(String sql, Object[] values);
	
	public  List queryForList(String sql);
	
	/**
	 * 
	 * @version 2010-12-7 下午04:59:09
	 * @author  SQJ(Kira.Sun)
	 * @see		权限sql拼装
	 * @param      tableName 表名，  sql 需要拼装sql语句，accountId登录帐号ID
	 * @return      String {返回参数说明}
	 * @exception   {说明在某情况下,将发生什么异常}
	 */
	public  String assemblingSQL(String tableName,String sql,Long accountId);
	
	/**
	 * 
	 * @version 2010-12-7 下午05:06:05
	 * @author  SQJ(Kira.Sun)
	 * @see		权限hql拼装，支持group by  和order by
	 * @param      ClassName pojo类名 ，objName pojo对象名 ,hql需要拼装的hql语句，accountId登录帐号ID
	 * @see		例如 select t from TmEmployeeInfor t;    ClassName：TmEmployeeInfor.class.getName()，objName：t,hql: select t from TmEmployeeInfor t
	 * @return      String {返回参数说明}
	 * @exception   {说明在某情况下,将发生什么异常}
	 */
	public  String assemblingHQL(String className,String objName,String hql,Long accountId);
	
	public List queryListSQL(String sql) throws Exception;
	
	public PageResult listBySqlPage(final String sql, PageResult PageResult) throws Exception;
	
	
}
