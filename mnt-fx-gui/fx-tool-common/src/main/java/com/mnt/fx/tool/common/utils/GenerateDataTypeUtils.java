package com.mnt.fx.tool.common.utils;


/**
 * 生成数据类型转换工具类
 *
 * @author cc
 * @Date 2017年8月10日上午9:25:01
 */
public class GenerateDataTypeUtils {

	
	/**
	 * 
	 * 获取 postgresql 对应的java类型
	 * @param typeName
	 * @return
	 */
	public static String getJavaTypeByPostgresql(String typeName) {
		if(typeName.equals("varchar")) {
			return "String";
		} else if(typeName.equals("int8")) {
			return "Long";
		} else if(typeName.equals("int4")) {
			return "Integer";
		} else if(typeName.equals("bool")) {
			return "Boolean";
		} else if(typeName.equals("float4")) {
			return "Float";
		} else if(typeName.equals("float8")) {
			return "Double";
		}  else if(typeName.equals("timestamp")) {
			return "Date";
		}  else if(typeName.equals("bpchar")) {
			return "String";
		}  else if(typeName.equals("date")) {
			return "Date";
		}  else if(typeName.equals("numeric")) {
			return "BigDecimal";
		} else if(typeName.equals("text")) {
			return "String";
		}
		
		return "String";
	}
	
	/**
	 * 
	 * 获取 postgresql 对应的jdbc类型
	 * @param typeName
	 * @return
	 */
	public static String getJdbcTypeByPostgresql(String typeName) {
		if(typeName.equals("varchar")) {
			return "VARCHAR";
		} else if(typeName.equals("int8")) {
			return "BIGINT";
		} else if(typeName.equals("int4")) {
			return "INTEGER";
		} else if(typeName.equals("bool")) {
			return "BOOLEAN";
		} else if(typeName.equals("float4")) {
			return "FLOAT";
		} else if(typeName.equals("float8")) {
			return "DOUBLE";
		}  else if(typeName.equals("timestamp")) {
			return "TIMESTAMP";
		}  else if(typeName.equals("bpchar")) {
			return "CHAR";
		}  else if(typeName.equals("date")) {
			return "DATE";
		}  else if(typeName.equals("numeric")) {
			return "NUMERIC";
		} else if(typeName.equals("text")) {
			return "VARCHAR";
		}
		
		return "VARCHAR";
	}


	/**
	 *
	 * 获取 postgresql 对应的java类型
	 * @param typeName
	 * @return
	 */
	public static String getJavaTypeByMysql(String typeName) {
		if(typeName.equals("varchar")) {
			return "String";
		} else if(typeName.equals("bigint")) {
			return "Long";
		} else if(typeName.equals("int")) {
			return "Integer";
		} else if(typeName.equals("bit")) {
			return "Boolean";
		} else if(typeName.equals("float")) {
			return "Float";
		} else if(typeName.equals("double")) {
			return "Double";
		}  else if(typeName.equals("timestamp")) {
			return "Date";
		}  else if(typeName.equals("char")) {
			return "String";
		}  else if(typeName.equals("date")) {
			return "Date";
		}  else if(typeName.equals("numeric")) {
			return "BigDecimal";
		} else if(typeName.equals("text")) {
			return "String";
		}else if(typeName.equals("time")) {
			return "Date";
		}

		return "String";
	}

	/**
	 *
	 * 获取 postgresql 对应的jdbc类型
	 * @param typeName
	 * @return
	 */
	public static String getJdbcTypeByMysql(String typeName) {
		if(typeName.equals("varchar")) {
			return "VARCHAR";
		} else if(typeName.equals("bigint")) {
			return "BIGINT";
		} else if(typeName.equals("int")) {
			return "INTEGER";
		} else if(typeName.equals("bit")) {
			return "BOOLEAN";
		} else if(typeName.equals("float")) {
			return "FLOAT";
		} else if(typeName.equals("double")) {
			return "DOUBLE";
		}  else if(typeName.equals("timestamp")) {
			return "TIMESTAMP";
		}  else if(typeName.equals("char")) {
			return "CHAR";
		}  else if(typeName.equals("date")) {
			return "DATE";
		}  else if(typeName.equals("numeric")) {
			return "NUMERIC";
		} else if(typeName.equals("text")) {
			return "VARCHAR";
		} else if(typeName.equals("time")) {
			return "DATE";
		}

		return "VARCHAR";
	}

	/**
	 *
	 * 获取 postgresql 对应的java类型
	 * @param typeName
	 * @return
	 */
	public static String getJavaTypeByDB(String dbType, String typeName) {

		if("postgres".equals(dbType)) {
			return getJavaTypeByPostgresql(typeName);
		}

		return getJavaTypeByMysql(typeName);
	}

	/**
	 *
	 * 获取 postgresql 对应的jdbc类型
	 * @param typeName
	 * @return
	 */
	public static String getJdbcTypeByDB(String dbType, String typeName) {


		if("postgres".equals(dbType)) {
			return getJdbcTypeByPostgresql(typeName);
		}

		return getJdbcTypeByMysql(typeName);
	}

}
