package com.taotao.manage.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.taotao.manage.pojo.BasePojo;
import com.taotao.manage.service.BaseService;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public abstract class BaseServiceImpl<T extends BasePojo> implements BaseService<T> {
	//在使用了spring 4.x以后；可以使用泛型依赖注入；必须使用@Autowired
	@Autowired
	private Mapper<T> mapper;
	
	private Class<T> clazz;
	
	public BaseServiceImpl(){
		//this是当前实例化的对象（**ServiceImpl）--- BaseServiceImpl<**>
		ParameterizedType pt = (ParameterizedType)this.getClass().getGenericSuperclass();
		
		clazz = (Class<T>)pt.getActualTypeArguments()[0];
	}

	@Override
	public List<T> queryAll() {
		return mapper.selectAll();
	}

	@Override
	public T queryById(Serializable id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public List<T> queryListByWhere(T t) {
		return mapper.select(t);
	}

	@Override
	public int queryCountByWhere(T t) {
		return mapper.selectCount(t);
	}

	@Override
	public List<T> queryListByPage(Integer page, Integer rows) {
		//设置分页
		PageHelper.startPage(page, rows);
		
		return mapper.selectAll();
	}

	@Override
	public void insertSelective(T t) {
		//如果用户在新增时没有指定创建和更新时间时，给与默认的值
		if(t.getCreated() == null){
			t.setCreated(new Date());
			t.setUpdated(t.getCreated());
		} else if(t.getUpdated() == null){
			t.setUpdated(new Date());
		}
		mapper.insertSelective(t);
	}

	@Override
	public void updateSelective(T t) {
		if(t.getUpdated() == null){
			t.setUpdated(new Date());
		}
		
		mapper.updateByPrimaryKeySelective(t);
	}

	@Override
	public void deleteById(Serializable id) {
		mapper.deleteByPrimaryKey(id);
	}

	@Override
	public void deleteByIds(Serializable[] ids) {
		//创建查询对象
		Example example = new Example(clazz);
		
		Criteria criteria = example.createCriteria();
		
		criteria.andIn("id", Arrays.asList(ids));
		
		mapper.deleteByExample(example);
	}

}
