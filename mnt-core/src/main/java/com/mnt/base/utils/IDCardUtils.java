package com.mnt.base.utils;


import java.text.SimpleDateFormat;

/**
 * 身份证工具类
 *
 * @author jiangbiao
 * @Date 2017年8月23日上午9:14:52
 */
public class IDCardUtils {

	
	/**
	 * 解析身份证的年龄
	 * @param idCard
	 * @return
	 */
	public static int parseIdCardAge(String idCard) {
		if(null == idCard) {
			return 0;
		}
		idCard = idCard.trim();
		
		int leh = idCard.length();
		String dates = "";
		if (leh == 18) {
			dates = idCard.substring(6, 10);
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			String year = df.format(TimeUtil.newInstance().getCurrentDate());
			int u = Integer.parseInt(year) - Integer.parseInt(dates);

			df = new SimpleDateFormat("MMdd");
			String monthDay = df.format(TimeUtil.newInstance().getCurrentDate());
			String idCardMonthDay = idCard.substring(10, 14);
			int addValue = (Integer.parseInt(monthDay) - Integer.parseInt(idCardMonthDay)) > 0 ? 0 : -1;

			return u + addValue;
		} else if(leh == 15) {
			idCard = IdCar15to18(idCard);
			if(null != idCard && idCard.length() == 18) {
				return parseIdCardAge(idCard);
			}
		} 
		return 0;
	}

//	/**
//	 * dengzaiqiang
//	 * 解析身份证性别
//	 *
//	 * @param idCard
//	 * @return
//	 */
//	public static String parseIdCardSex(String idCard) {
//		if (StringUtils.isNotBlank(idCard)) {
//			idCard = idCard.trim();
//			if (15 == StringUtils.length(idCard)) {
//				idCard = IdCar15to18(idCard);
//			}
//			if (18 == StringUtils.length(idCard)) {
//				int num = Integer.parseInt(StringUtils.substring(idCard, 16, 17));
//				if (num % 2 == 0) {
//					return "女";
//				} else {
//					return "男";
//				}
//			}
//		}
//		return "";
//	}

//	/**
//	 * dengzaiqiang
//	 * 解析身份证出生日期
//	 *
//	 * @return yyyy-MM-dd
//	 */
//	public static String parseIdCardBirthDate(String idCard) {
//		if (StringUtils.isNotBlank(idCard)) {
//			idCard = idCard.trim();
//			if (15 == StringUtils.length(idCard)) {
//				idCard = IdCar15to18(idCard);
//			}
//			if (18 == StringUtils.length(idCard)) {
//				String year = StringUtils.substring(idCard, 6, 10);
//				String month = StringUtils.substring(idCard, 10, 12);
//				String day = StringUtils.substring(idCard, 12, 14);
//				StringBuilder result = new StringBuilder();
//				result.append(year).append("-").append(month).append("-").append(day);
//				return result.toString();
//			}
//		}
//		return "";
//	}
	
	/**
	 * 将15位身份证号转换为18位
	 * @param idCard
	 * @return
	 */
	public static String IdCar15to18(String idCard) {
		idCard = idCard.trim();
		StringBuffer idCard18 = new StringBuffer(idCard);
		// 加权因子
		// 校验码值
		char[] checkBit = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		int sum = 0;
		if (idCard != null && idCard.length() == 15) {
			idCard18.insert(6, "19");
			for (int index = 0; index < idCard18.length(); index++) {
				char c = idCard18.charAt(index);
				int ai = Integer.parseInt(new Character(c).toString());
				// 加权因子的算法
				int Wi = ((int) Math.pow(2, idCard18.length() - index)) % 11;
				sum = sum + ai * Wi;
			}
			int indexOfCheckBit = sum % 11; // 取模
			idCard18.append(checkBit[indexOfCheckBit]);

		}
		return idCard18.toString();
	}


	/**
	 * 身份证验证- 只验证校验位，不验证其他
	 * @param idCard 号码
	 * @return 是否有效
	 */
	public static boolean isIdCard(String idCard) {

		int[] PARITY_BIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
		int[] POWER_LIST = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

		if (idCard == null || (idCard.length() != 15 && idCard.length() != 18))
			return false;
		char[] cs = idCard.toUpperCase().toCharArray();
		// 校验位数
		int power = 0;
		for (int i = 0; i < cs.length; i++) {
			// 最后一位可以 是X或x
			if (i == cs.length - 1 && cs[i] == 'X')
				break;
			if (cs[i] < '0' || cs[i] > '9')
				return false;
			if (i < cs.length - 1) {
				power += (cs[i] - '0') * POWER_LIST[i];
			}
		}

		// TODO 校验区位码

		// TODO 校验年份

		// TODO 校验月份

		// TODO 校验天数

		// 校验 `校验码`
		if (idCard.length() == 15)
			return true;
		return cs[cs.length - 1] == PARITY_BIT[power % 11];
	}



	public static void main(String[] args) {
		System.err.println(parseIdCardAge("310108800902023"));
	}
}
