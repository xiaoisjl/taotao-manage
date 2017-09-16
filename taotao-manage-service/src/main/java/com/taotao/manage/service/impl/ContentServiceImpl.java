package com.taotao.manage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.DataGridResult;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;
import com.taotao.common.service.redis.RedisService;

import tk.mybatis.mapper.entity.Example;

@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {

	@Autowired
	private ContentMapper contentMapper;

	@Value("${CONTENT_CATEGORY_BIG_AD_ID}")
	private Long CONTENT_CATEGORY_BIG_AD_ID;
	@Value("${TAOTAO_PORTAL_INDEX_BIG_AD_NUMBER}")
	private Integer TAOTAO_PORTAL_INDEX_BIG_AD_NUMBER;

	@Autowired
	private RedisService redisService;

	//大广告数据在redis中的key
	private static final String REDIS_BIG_AD_KEY = "TAOTAO_PORTAL_INDEX_BIG_AD_DATA";
	//大广告数据在redis中的过期时间
	private static final int REDIS_BIG_AD_EXPIRE_TIME = 60*60*24;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public DataGridResult queryContentListByPage(Long categoryId, Integer page, Integer rows) {
		Example example = new Example(Content.class);

		//设置查询条件；根据内容分类查询该分类下的内容
		example.createCriteria().andEqualTo("categoryId", categoryId);

		//排序，根据更新时间降序排序
		example.orderBy("updated").desc();

		//分页
		PageHelper.startPage(page, rows);

		List<Content> list = contentMapper.selectByExample(example);
		PageInfo<Content> pageInfo = new PageInfo<>(list);
		return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());
	}

	@Override
	public String getIndexBigAdData() throws Exception {
		String resultStr = "";

		try {
			//先到redis中获取大广告的数据，然后获得到则直接返回
			resultStr = redisService.get(REDIS_BIG_AD_KEY);
			if(StringUtils.isNotBlank(resultStr)){
				return resultStr;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		//1、获取6条大广告内容数据
		DataGridResult dataGridResult = queryContentListByPage(CONTENT_CATEGORY_BIG_AD_ID, 1, TAOTAO_PORTAL_INDEX_BIG_AD_NUMBER);

		//2、封装大广告数据
		if(dataGridResult.getRows() != null){
			List<Map<String, Object>> resultList = new ArrayList<>(TAOTAO_PORTAL_INDEX_BIG_AD_NUMBER);
			List<Content> list = (List<Content>)dataGridResult.getRows();

			Map<String, Object> map = null;
			for (Content c : list) {
				map = new HashMap<>();
				map.put("alt", c.getTitle());
				map.put("height", 240);
				map.put("heightB", 240);
				map.put("href", c.getUrl());
				map.put("src", c.getPic());
				map.put("srcB", c.getPic2());
				map.put("width", 670);
				map.put("widthB", 550);

				resultList.add(map);
			}

			//将返回结果转换为json格式字符串
			resultStr = MAPPER.writeValueAsString(resultList);

			try {
				//将返回的结果存入到redis，并且设置过期时间，1天
				redisService.setex(REDIS_BIG_AD_KEY, REDIS_BIG_AD_EXPIRE_TIME, resultStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return resultStr;
	}

}
