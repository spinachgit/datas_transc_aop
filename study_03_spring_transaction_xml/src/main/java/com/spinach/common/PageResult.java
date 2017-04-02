package com.spinach.common;

import java.util.List;

/**
 * @ClassName: PageResult
 * @Description: 页面分页数据封装类
 * @author cloud
 * @date: 2014年5月5日-上午11:47:39
 *
 */
public class PageResult<T> {

	private int pageCount = 0; // 总页数
	private int pageNo = 1; // 当前第几页数据
	private int pageSize = 10; // 一页显示多少条数据
	private int dataCount; // 总数
	private List<T> content = null; // 分页数据

	public PageResult() {}
	
	public PageResult(Integer pageNo) {
		this.pageNo = pageNo != null ? pageNo : 1;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
		if (pageCount == 0) {
			if (dataCount % pageSize > 0) {
				this.pageCount = dataCount / pageSize + 1;
			} else {
				this.pageCount = dataCount / pageSize;
			}
		}
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
}
