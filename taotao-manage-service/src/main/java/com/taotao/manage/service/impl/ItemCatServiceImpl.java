package com.taotao.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.ItemCatMapper;
import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;

@Service
public class ItemCatServiceImpl extends BaseServiceImpl<ItemCat> implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMaper;

	/*@Override
	public List<ItemCat> queryItemCatListByPage(Integer page, Integer rows) {
		//设置分页
		PageHelper.startPage(page, rows);
		
		List<ItemCat> list = itemCatMaper.select(null);
		
		return list;
	}*/

}
