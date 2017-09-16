package com.taotao.manage.service;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {

	//查询全部
	public List<T> queryAll();
	//根据id查询
	public T queryById(Serializable id);
	//根据条件查询
	public List<T> queryListByWhere(T t);
	//根据条件查询总数
	public int queryCountByWhere(T t);
	//分页查询
	public List<T> queryListByPage(Integer page, Integer rows);
	//选择性新增
	public void insertSelective(T t);
	//选择性更新
	public void updateSelective(T t);
	//根据id删除
	public void deleteById(Serializable id);
	//批量删除
	public void deleteByIds(Serializable[] ids);
}
