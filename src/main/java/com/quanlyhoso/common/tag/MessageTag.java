package com.quanlyhoso.common.tag;

import java.io.Serializable;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.quanlyhoso.common.component.Messages;
import com.quanlyhoso.common.constant.CommonConstant;
import com.quanlyhoso.model.User;

public class MessageTag implements Serializable {

	private static final long serialVersionUID = 1L;

	public String message(String key, HttpServletRequest req) {

		User user = (User) req.getAttribute(CommonConstant.USER_INFO);
		if (user == null) {
			user = new User();
			user.setLocale(new Locale("vn"));
		}

		return Messages.getMessage(key);
	}

	public String message(String key, HttpServletRequest req, String... arg) {

		User user = (User) req.getAttribute(CommonConstant.USER_INFO);
		if (user == null) {
			user = new User();
			user.setLocale(new Locale("vn"));
		}

		return Messages.getMessage(key, arg);
	}
}