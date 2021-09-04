package com.mnt.tools.dep;

/**
 * 公共对外接口
 *
 * @author jiangbiao
 * @Date 2017年4月28日下午1:23:07
 */
public interface GenericInterface<Model extends GenericDomain<PK>, PK> {

	/**
     * 插入对象
     *
     * @param model 对象
     */
	Model add(Model model);

    /**
     * 更新对象
     *
     * @param model 对象
     */
	int update(Model model);

    /**
     * 通过主键, 删除对象
     *
     * @param id 主键
     */
	void deleteById(PK id);

    /**
     * 通过主键, 查询对象
     *
     * @param id 主键
     * @return model 对象
     */
    Model getById(PK id);

    
    /**
     * 设置为删除状态
     * @param id
     */
    void cancelById(PK id, String operaUser);
}
