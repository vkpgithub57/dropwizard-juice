package com.poc.model;

public class SearchCustomer {

	private String search;
	private SortCustomer sort;
	private int limit;
	private int page;
	
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public SortCustomer getSort() {
		return sort;
	}
	public void setSort(SortCustomer sort) {
		this.sort = sort;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
}
