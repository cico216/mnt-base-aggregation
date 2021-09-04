package com.mnt.tools.dep;



/**
 * 基础服务类接口
 *
 * @author 姜彪
 * @date 2017年3月6日下午4:16:29
 * @param <Model>
 * @param <PK>
 */
public interface GenericService<Model extends GenericDomain<PK>, PK> {

    /**
     * 插入对象
     *
     * @param model 对象
     */
	void add(Model model);

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
