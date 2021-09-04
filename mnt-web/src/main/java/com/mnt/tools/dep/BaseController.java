package com.mnt.tools.dep;

//import org.springframework.ui.Model;

/**
 * controller 基类
 *
 * @author jiangbiao
 * @Date 2017年4月18日上午11:36:08
 */
public abstract class BaseController {

	protected final static int DEFUALT_PAGE_INDEX = 0; //默认页面索引
	protected final static int DEFUALT_PAGE_SIZE = 20; //默认页面大小
	protected final static String REDIRECT = "redirect:"; //重定向

	/**
	 * ajax请求异常返回
	 * @param request
	 * @param e
	 * @return
	 */
//	@ExceptionHandler(AjaxException.class)
//	@ResponseBody
//	public AjaxResult ajaxException(HttpServletRequest request, AjaxException e)
//	{
//		return AjaxResult.failed(e.getErrorcode(), e.getErrorMessage());
//	}
//
    /**
	 * 异常处理
	 * @param request
	 * @param e
	 * @return
	 */
//	@ExceptionHandler(Exception.class)
//	public String exception(HttpServletRequest request, Model model, Exception e)
//	{
//		if(e instanceof BusinessException)
//		{
//			BusinessException busExcep = (BusinessException)e;
//			model.addAttribute("message", busExcep.getErrorMessage());
//			return "pages/403";
//		}
//		else
//		{
//			model.addAttribute("message", e.getMessage());
//			e.printStackTrace();
//		}
//		return "pages/500";
//	}

//	/**
//	 * 检测如果不为空则设置属性
//	 * @param model
//	 * @param name
//	 * @param value
//	 */
//	protected void checkAndPut(Model model, String name, Object value)
//	{
//		if(value != null)
//		{
//			model.addAttribute(name, value);
//		}
//	}
	
}
