package com.taotao.manage.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.ContentCategoryMapper;
import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl extends BaseServiceImpl<ContentCategory> implements ContentCategoryService {

	@Autowired
	private ContentCategoryMapper contentCategoryMaper;

	@Override
	public ContentCategory saveContentCategory(ContentCategory contentCategory) {
		
		//保存内容分类
		contentCategory.setIsParent(false);
		contentCategory.setSortOrder(100);
		contentCategory.setStatus(1);//正常
		insertSelective(contentCategory);
		
		//更新新增的该节点的父节点为父节点（设置isParent为true）
		ContentCategory parent = new ContentCategory();
		parent.setId(contentCategory.getParentId());
		parent.setIsParent(true);
		updateSelective(parent);
		
		return contentCategory;
	}

	@Override
	public void deleteContentCategory(ContentCategory contentCategory) {
		//1、获取当前节点的子孙节点集合
		List<Long> ids = new ArrayList<>();
		ids.add(contentCategory.getId());
		
		//1.1、递归获取子孙节点
		getCategoryIds(contentCategory, ids);
		
		//2、删除
		deleteByIds(ids.toArray(new Long[ids.size()]));
		
		//3、更新该节点的父节点的 isParent 的值
		//父节点是否还有其它子节点,如果有则不需要变化，如果没有其它子节点需要将其值改为false
		ContentCategory param = new ContentCategory();
		param.setParentId(contentCategory.getParentId());
		List<ContentCategory> list = queryListByWhere(param);
		if(list == null || list.size() == 0){
			//说明父节点已经没有其它子节点，所以需要更新父节点的属性isparent为false
			ContentCategory parent = new ContentCategory();
			parent.setId(contentCategory.getParentId());
			parent.setIsParent(false);
			updateSelective(parent);
		}
	}

	/**
	 * 递归获取contentCategory节点对于的所有子孙节点id
	 * @param contentCategory 要获取的节点
	 * @param ids id集合
	 */
	private void getCategoryIds(ContentCategory contentCategory, List<Long> ids) {
		
		//查询当前节点的所有子节点
		ContentCategory param = new ContentCategory();
		param.setParentId(contentCategory.getId());
		List<ContentCategory> list = queryListByWhere(param);
		if(list != null && list.size() > 0){
			for (ContentCategory c : list) {
				
				ids.add(c.getId());
				
				getCategoryIds(c, ids);
			}
		}
	}

}
