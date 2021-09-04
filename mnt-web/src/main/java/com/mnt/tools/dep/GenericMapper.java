// BaseDao
package com.mnt.tools.dep;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * dao基类
 *
 * @author 姜彪
 * @date 2017年3月6日下午4:05:55
 * @param <E>
 * @param <PK>
 */
public interface GenericMapper<E, PK> extends IBaseMapper {
	
	/**
	 * 根据主键删除
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(PK id);

	/**
	 * 插入数据
	 * @param record
	 * @return
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(E record);

    /**
	 * 插入数据(为null时为默认值)
	 * @param record
	 * @return
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(E record);

    /**
     * 根据主键查询数据
     * @param id
     * @return
     */
    E selectByPrimaryKey(PK id);

    /**
     * 根据主键修改数据 (参数为 null 时 不修改)
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(E record);

    
    /**
     * 根据主键修改数据
     * @param record
     * @return
     */
    int updateByPrimaryKey(E record);
	
	/**
	 * @description 详细说明
	 * @param list
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int batchAdd(final List<E> list);


//	/**
//	 * 批量删除
//	 * @param ids
//	 * @return
//	 */
//	int batchRemove(final PK[] ids);

	
	/**
	 * 获取所有数据，如果有条件则带参数条件
	 * @param params 条件
	 * @return 数据集合
	 */
	List<E> getAll(Map<String, Object> params);
	
	/**
	 * 获取所有数量
	 * @param params
	 * @return
	 */
	int getAllCount(Map<String, Object> params);

	/**
	 * 获取所有数据,需提供ID集合
	 * @param ids 条件
	 * @return 数据集合
	 */
	List<E> listAllByIds(@Param(value = "ids") final List<PK> ids);
	
	
	/**
	 * 设置为删除状态
	 * @param e
	 * @param userModified
	 */
	void cancelById(@Param(value = "id") PK e, @Param(value = "userModified") String userModified);
	
	/**
	 * 判断是否存在
	 * @param e
	 * @return
	 */
	int exist(E e);
}
