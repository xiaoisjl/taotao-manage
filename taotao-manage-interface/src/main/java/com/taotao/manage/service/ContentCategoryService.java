package com.taotao.manage.service;

import com.taotao.manage.pojo.ContentCategory;

public interface ContentCategoryService extends BaseService<ContentCategory> {

	ContentCategory saveContentCategory(ContentCategory contentCategory);
	/**
	 * 删除节点及其所有子孙节点；并且判断其父节点是否还为父节点（父节点是否还有其它子节点）
	 * @param contentCategory
	 * @return
	 */
	void deleteContentCategory(ContentCategory contentCategory);

}
