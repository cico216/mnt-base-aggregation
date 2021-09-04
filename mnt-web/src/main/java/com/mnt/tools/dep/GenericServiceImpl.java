package com.mnt.tools.dep;


import com.mnt.base.utils.TimeUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * 基础服务类实现
 * @author jiangbiao
 * @Date 2017年4月18日上午11:39:21
 */
public abstract class GenericServiceImpl<Model extends GenericDomain<PK>, PK> implements GenericService<Model , PK> {

	/**
	 * 空map
	 */
	protected static final Map<String, Object> EMPTY_MAP = new HashMap<>(0);
	
	protected static final String START_INDEX = "startIndex";
	
	protected static final String PAGE_NO = "pageNo";
	
	/**
	 * 检测是否为null 不为null则放入map
	 * @param name
	 * @param value
	 * @param params
	 */
	protected void checkAndPut(String name, Object value, Map<String, Object> params)
	{
		if(null != value)
		{
			params.put(name, value);
		}
	}
	
	
    /**
     * 定义成抽象方法,由子类实现,完成dao的注入
     *
     * @return GenericDao实现类
     */
    public abstract GenericMapper<Model, PK> getMapper();

	@Override
	public void add(Model model) {
		
		model.setGmtCreate(TimeUtil.newInstance().getCurrentDate());
		
		getMapper().insertSelective(model);
		
	}

	@Override
	public int update(Model model) {
		
		model.setGmtModified(TimeUtil.newInstance().getCurrentDate());
		
		return getMapper().updateByPrimaryKeySelective(model);
	}

	@Override
	public void deleteById(PK id) {
		getMapper().deleteByPrimaryKey(id);
	}

	@Override
	public Model getById(PK id) {
		return getMapper().selectByPrimaryKey(id);
	}


	@Override
	public void cancelById(PK id, String operaUser) {
		getMapper().cancelById(id, operaUser);
		
	}
    
}
