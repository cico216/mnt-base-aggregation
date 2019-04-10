package com.mnt.base.thread;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author 姜彪
 * @date 2016年5月16日
 */
public abstract class ExecuteWarp implements Runnable 
{
	protected final Logger log	= LoggerFactory.getLogger(getClass());
	
	@Override
	public void run() 
	{
		try 
		{
			runImpl();
		}
		catch (Throwable e)
		{
			log.warn("线程运行时，出现的异常", e);
		}
	}
	
	protected abstract void runImpl();
}
