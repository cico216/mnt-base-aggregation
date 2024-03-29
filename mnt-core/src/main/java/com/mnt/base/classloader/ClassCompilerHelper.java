package com.mnt.base.classloader;

import javafx.util.Pair;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * <p>
 * fx类加载器
 * </p>
 * 
 * @author mnt.cico
 * @version 2015年5月24日 下午7:00:13 mnt.cico .
 * @since FX8.0
 */
public class ClassCompilerHelper {

	/**
	 * 编译器
	 */
	private static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

	/**
	 * 
	 * <p>
	 * 编译指定文件夹下面的java文件
	 * </p>
	 * @create mnt.cico
	 * @param fileDirector
	 * @return
	 */
	private final static List<String> compilerJavaFile(String[] fileDirector)
	{
		final List<String> result = new ArrayList<>();
		StandardJavaFileManager manager = javac.getStandardFileManager(null, null, null);  
		
		 Iterable<? extends JavaFileObject> it = manager.getJavaFileObjects(fileDirector);
		 it.forEach(javaFileObject -> {
			 try {
				 result.add(parsePackageClassName(javaFileObject.getName(), javaFileObject.getCharContent(true).toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		 });
//		 StringWriter sw = new StringWriter();
		 String jarPath;

		if(ClassLoadUtil.isMac()) {
			String basePath = "." + FILE_SEPARATOR;
			jarPath = basePath + "bin/:";

			try {
				jarPath += basePath + "target" + FILE_SEPARATOR + "classes/:";
				jarPath += basePath +"lib/*:";
				jarPath += getJarFiles(System.getProperty("user.dir") + FILE_SEPARATOR +"app",":");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else {
			jarPath = System.getProperty("user.dir") + FILE_SEPARATOR + "bin;";

			try {
				jarPath += System.getProperty("user.dir") + FILE_SEPARATOR + "target" + FILE_SEPARATOR + "classes;";
				jarPath += getJarFiles(System.getProperty("user.dir") + FILE_SEPARATOR +"lib", ";");
				jarPath += getJarFiles(System.getProperty("user.dir") + FILE_SEPARATOR +"app",";");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
//		System.err.println("jarPath = " + jarPath);
		//修改为替换系统获取的路径
		 List<String> options = Arrays.asList(new String[] { "-encoding", "UTF-8", "-classpath", System.getProperty("java.class.path")});
		 CompilationTask compilationTask = javac.getTask(null, manager, null, options, null, it);  
		 
		 compilationTask.call();
		 try {
			manager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		return result;
	}
	
	
//	private final static List<String> compilerJavaFile(String srcPath,  String[] fileDirector)
//	{
//		final List<String> result = new ArrayList<>();
//		String filePath = System.getProperty("user.dir") + FILE_SEPARATOR + srcPath;
//		for (String javafile : fileDirector) {
//			
//		    Main.compile(new String[] {"-d", filePath, javafile});
//		    
//		    result.add(javafile);
//		}
//		// System.out.println(System.getProperty("java.home"));  
//		
//		return result;
//    }
	
	
	/**
	 * 
	 * <p>
	 * 获取类加载器
	 * </p>
	 * @create mnt.cico
	 * @param classPath
	 * @return
	 */
	private static ClassLoader getClassLoader(String classPath, List<String> classNames, URLClassLoader appClassLoad)
	{
//		URL[] urls = null;
//		try {
//			urls = new URL[] {new URL("file:/"+ System.getProperty("user.dir") + "/" + classPath)};
//			
//			File [] javaFiles = new File(classPath).listFiles((name)->{return name.getName().endsWith(".class");});
//			urls = new URL[javaFiles.length];
//			for (int i = 0; i < javaFiles.length; i++) {
//				urls[i] = javaFiles[i].toURI().toURL();
//			}
//			//因为URLClassLoader中的addURL方法的权限为protected所以只能采用反射的方法调用addURL方法  
//			Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });                                 
//			add.setAccessible(true);
//			for (URL url : urls) {
//				add.invoke(appClassLoad, url); 
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		 System.err.println(System.getProperty("file.encoding"));
		
		List<String> jarPathUrl = new ArrayList<>();
		try {
			jarPathUrl.addAll(getJarFilesPath(System.getProperty("user.dir") + FILE_SEPARATOR +"lib"));
			jarPathUrl.addAll(getJarFilesPath(System.getProperty("user.dir") + FILE_SEPARATOR +"app"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String [] classNameArrays = new String[classNames.size()];
		for (int i = 0; i < classNames.size(); i++) {
			classNameArrays[i] = classNames.get(i);
		}
		
		URL [] urls = new URL[jarPathUrl.size()];
		for (int i = 0; i < jarPathUrl.size(); i++) {
			try {
        		urls[i] = new File(jarPathUrl.get(i)).toURI().toURL();
        	} catch (MalformedURLException e) {
        		e.printStackTrace();
        	}
		}
		
		CustomClassLoader cl = new CustomClassLoader(appClassLoad, System.getProperty("user.dir") + "/" + classPath, classNameArrays, urls);
		
		
		
		return cl;
	}
	
	/**
	 * <p>
	 * 加载指定src路径下的class文件
	 * </p>
	 * 
	 * @create mnt.cico
	 * @param srcPath
	 */
	public final static Pair<ClassLoader, List<String>> loadClasses(String srcPath, URLClassLoader appClassLoad) {
		File [] javaFiles = new File(srcPath).listFiles((name)->{return name.getName().endsWith(".java");});
		String [] filePaths = new String[javaFiles.length];
		for (int i = 0; i < javaFiles.length; i++) {
			filePaths[i] = javaFiles[i].getAbsolutePath();
		}
		final List<String> classNames = compilerJavaFile(filePaths);
		final ClassLoader classLoad = getClassLoader(srcPath, classNames, appClassLoad);
		return new Pair<ClassLoader, List<String>>(classLoad, classNames);
	}
	
	/**
	 * 分隔符
	 */
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	/**
	 * 
	 * <p>
	 * 解析java包名
	 * </p>
	 * @create mnt.cico
	 * @param filePath
	 * @param javaContent
	 * @return
	 */
	private static String parsePackageClassName(String filePath, String javaContent)
	{
		String javaName = filePath.substring(filePath.lastIndexOf(FILE_SEPARATOR) + 1);
		
		String filePathUrl = javaContent.substring(0, javaContent.indexOf(";")).trim().substring(8) + "." + javaName.substring(0, javaName.length() - 5);
		
				
		return filePathUrl;
	}
	
	
	
//	   /**
//     * 查找该目录下的所有的java文件
//     *
//     * @param sourceFile
//     * @param sourceFileList
//     * @throws Exception
//     */
//    private void getSourceFiles(File sourceFile, List<File> sourceFileList) throws Exception {
//        if (sourceFile.exists() && sourceFileList != null) {//文件或者目录必须存在
//            if (sourceFile.isDirectory()) {// 若file对象为目录
//                // 得到该目录下以.java结尾的文件或者目录
//                File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
//                    public boolean accept(File pathname) {
//                        if (pathname.isDirectory()) {
//                            try {
//                                new CopyDirectory().copyDirectiory(pathname.getPath(), targetDir + pathname.getPath().substring(pathname.getPath().indexOf("src") + 3, pathname.getPath().length()));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            return true;
//                        } else {
//                            String name = pathname.getName();
//                            if (name.endsWith(".java") ? true : false) {
//                                return true;
//                            }
//                            try {
//                                new CopyDirectory().copyFile(pathname, new File(targetDir + pathname.getPath().substring(pathname.getPath().indexOf("src") + 3, pathname.getPath().length())));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            return false;
//                        }
//                    }
//                });
//                // 递归调用
//                for (File childFile : childrenFiles) {
//                    getSourceFiles(childFile, sourceFileList);
//                }
//            } else {// 若file对象为文件
//                sourceFileList.add(sourceFile);
//            }
//        }
//    }

    /**
     * 查找该目录下的所有的jar文件
     *
     * @param jarPath
     * @throws Exception
     */
    private static String getJarFiles(String jarPath, String endflag) throws Exception {
    	StringBuilder jars = new StringBuilder();
        File sourceFile = new File(jarPath);
        // String jars="";
        if (sourceFile.exists()) {// 文件或者目录必须存在
            if (sourceFile.isDirectory()) {// 若file对象为目录
                // 得到该目录下以.java结尾的文件或者目录
                sourceFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory()) {
                            return true;
                        } else {
                            String name = pathname.getName();
                            if (name.endsWith(".jar")) {
                            	jars.append(pathname.getPath() + endflag);
                                return true;
                            }
                            return false;
                        }
                    }
                });
            }
        }
        return jars.toString();
    }

    
    /**
     * 获取jar包的url
     * @param jarPath
     * @return
     * @throws Exception
     */
    private static List<String> getJarFilesPath(String jarPath) throws Exception {
    	List<String> result = new ArrayList<>();
        File sourceFile = new File(jarPath);
        // String jars="";
        if (sourceFile.exists()) {// 文件或者目录必须存在
            if (sourceFile.isDirectory()) {// 若file对象为目录
                // 得到该目录下以.java结尾的文件或者目录
                sourceFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory()) {
                            return true;
                        } else {
                            String name = pathname.getName();
                            if (name.endsWith(".jar")) {
                            	result.add(pathname.getPath());
                                return true;
                            }
                            return false;
                        }
                    }
                });
            }
        }
        return result;
    }

}
