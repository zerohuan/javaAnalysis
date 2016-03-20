package com.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static void print(Object obj) {
		System.out.println(obj);
	}
	
	/**
	 * �Ƿ������������ʽ��ʾ�����ַ���
	 * @param regex
	 * @param str
	 * @return
	 */
	public static boolean containsRegex(String regex, String str) {
		boolean flag = false;
		
		Matcher matcher = Pattern.compile(regex).matcher(str);
		while(matcher.find()) {
			flag = true;
		}
		
		return flag;
	}

	/**
	 * ת���ɱ�׼�ļ���
	 * @param name
	 * @return
	 */
	public static String standardFileName(String name) {
		return name == null ?
				null :
				name.replaceAll("\"|\\\\|\\*|\\|\\?|/|<|>|\\.", "").replaceAll(":", "��").replace("?", "");
	}

	/**
	 * ���������ַ�����һ��ƥ��������ʽ���Ӵ�
	 * @param str
	 * @param regex
	 * @param filter
	 * @return
	 */
	public static String findFirst(String str, String regex, StrFilter filter) {
		Pattern resultCountP = Pattern.compile(regex);
		Matcher resultCountM = resultCountP.matcher(str);
		if (resultCountM.find()) {
			if (filter == null)
				filter = DEFAULT_FILTER;
			return filter.filter(resultCountM);
		}
		return null;
	}
	public static final StrFilter DEFAULT_FILTER = new StrFilter() {
		@Override
		public String filter(Matcher matcher) {
			return matcher.group();
		}
	};
	public interface StrFilter {
		String filter(Matcher matcher);
	}
}
