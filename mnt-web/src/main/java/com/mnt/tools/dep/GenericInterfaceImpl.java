package com.mnt.tools.dep;

/**
 * 基础接口类实现
 * @author jiangbiao
 * @Date 2017年4月18日上午11:39:21
 */
public abstract class GenericInterfaceImpl<Model extends GenericDomain<PK>, PK> implements GenericInterface<Model , PK> {

    /**
     * 定义成抽象方法,由子类实现,完成service的注入
     *
     * @return GenericService实现类
     */
    public abstract GenericService<Model, PK> getManager();

	@Override
	public Model add(Model model) {
		
		getManager().add(model);
		
		return model;
	}

	@Override
	public int update(Model model) {
		return getManager().update(model);
	}

	@Override
	public void deleteById(PK id) {
		getManager().deleteById(id);
	}

	@Override
	public Model getById(PK id) {
		return getManager().getById(id);
	}


	@Override
	public void cancelById(PK id, String operaUser) {
		getManager().cancelById(id, operaUser);
		
	}
    
}
