package com.taotao.manage.service;

import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.pojo.Content;

public interface ContentService extends BaseService<Content> {

    /**
     * 根据内容类目id查询内容列表数据并且按照更新时间降序排序
     * @param categoryId 内容类目id
     * @param page 页号
     * @param rows 页大小
     * @return
     */
    DataGridResult queryContentListByPage(Long categoryId, Integer page, Integer rows);

    /**
     * 获取门户系统首页的6条大广告数据
     * @return
     * @throws Exception
     */
    String getIndexBigAdData() throws Exception;

}
