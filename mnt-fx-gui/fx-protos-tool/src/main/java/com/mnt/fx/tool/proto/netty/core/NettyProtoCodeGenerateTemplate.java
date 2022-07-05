package com.mnt.fx.tool.proto.netty.core;




import com.mnt.fx.tool.proto.netty.model.NettyProtoModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 代码生成抽象类
 *
 * @author cc
 * @Date 2017年8月8日下午2:52:22
 */
public abstract class NettyProtoCodeGenerateTemplate {

	 //操作日志配置
//    protected OperaRecordService operaPropertiesService;

	protected List<String> forParamNames = Arrays.asList("i", "j", "k", "m", "n", "s", "f", "p", "u");

    /**
     * 代码保留开始标记
     */
    protected static final String APPEND_START = "append__start";

    /**
     * 代码保留结束标记
     */
    protected static final String APPEND_END = "append__end";

    protected static final String TAB = "    ";


	public NettyProtoCodeGenerateTemplate() {
//		operaPropertiesService = OperaRecordService.getInstance();
	}

	public abstract String getType();

	
	/**
	 * 生成代码调用
	 * @param protoModel
	 */
	public final void generate(NettyProtoModel protoModel) {
		//判断并创建文件夹
		checkAndCreateDir(getGeneratePath(protoModel));
		
		generateImpl(protoModel);
	};
	
	/**
	 * 执行生成代码方法
	 * @param protoModel
	 */
	protected abstract void generateImpl(NettyProtoModel protoModel);
	
	/**
	 * 批量执行生成代码方法
	 * @param protoModels
	 */
	public final void generate(List<NettyProtoModel> protoModels) {
		for (NettyProtoModel protoModel : protoModels) {
			generate(protoModel);
		}
	};
	
	
	/**
	 * 获取代码生成路径
	 * @return
	 */
	public abstract String getGeneratePath(NettyProtoModel protoModel);
	

	
//	/**
//	 * 获取对应的实体包路径
//	 * @return
//	 */
//	public String getEntityPackagePath() {
//		String apiPath = operaPropertiesService.getApiPath();
//		apiPath = apiPath.replaceAll("\\\\", "/");
//
//		int packageStartIndex = apiPath.lastIndexOf("/");
//
//		String packagePath = apiPath.substring(packageStartIndex + 1, apiPath.length());
//
//		packagePath = packagePath + ".entity";
//
//		return packagePath;
//	}


	/**
	 * 获取保留代码
	 * @return
	 */
	public String getHoldCode(String filePath) {
		File file = new File(filePath);
		StringBuilder sbResult = new StringBuilder("");
		if(file.exists()) {
			try {
				boolean isStart = false;
				FileReader fr;

					fr = new FileReader(file);

				BufferedReader br=new BufferedReader(fr);
		        String line = "" ;
		        while ((line = br.readLine()) != null) {

		        	if(line.contains(APPEND_END)) {
		        		break;
		        	}

		        	if(isStart) {
//		        		if(!line.trim().isEmpty()) {
		        		sbResult.append(line + "\n");
//		        		}
		        	}
		        	if(line.contains(APPEND_START)) {
		        		isStart = true;
		        	}
		        }
		        br.close();
		        fr.close();

		        return sbResult.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return "";
	}
	
	/**
	 * 检测并创建文件夹
	 */
	public void checkAndCreateDir(String filePath) {
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	
}
