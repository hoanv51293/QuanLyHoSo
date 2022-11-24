package com.quanlyhoso.common.util;

import javax.servlet.ServletContext;

import org.springframework.http.MediaType;

public class SeinoUtil {

	public static MediaType toContentType(ServletContext servletContext, String fileName) {
		String mineType = servletContext.getMimeType(fileName);
		try {
			return MediaType.parseMediaType(mineType);
		} catch (Exception e) {
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	public static Object[] createLineData(Object... args) {
		Object[] val = new Object[args.length];
		for (int i = 0; i < args.length; i++) {
			val[i] = convertToString(args[i]);
		}
		return val;
	}

	private static Object convertToString(Object obj) {
		return obj == null ? "" : obj;
	}
	public static String replaceBreakBySlash(String str) {
		return str.replaceAll("\r\n","/").replaceAll("\n","/");
	}
}