package com.taotao.manage.service;

import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.pojo.Item;

public interface ItemService extends BaseService<Item> {

	/**
	 * 保存商品基本和描述信息
	 * @param item 商品基本信息
	 * @param desc 商品描述信息
	 * @return 商品id
	 */
	Long saveItem(Item item, String desc);

	/**
	 * 更新商品基本和描述信息
	 * @param item 商品基本信息
	 * @param desc 商品描述信息
	 */

	void updateItem(Item item, String desc);

	/**
	 * 根据标题分页查询商品列表数据并且按照更新时间降序排序
	 * @param title 商品标题
	 * @param page 页号
	 * @param rows 页大小
	 * @return
	 */
	DataGridResult queryItemListByPage(String title, Integer page, Integer rows);

}
