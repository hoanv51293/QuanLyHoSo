package com.quanlyhoso.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quanlyhoso.common.base.BaseController;
import com.quanlyhoso.service.impl.CategoryService;
import com.quanlyhoso.service.impl.QuanLyTaiKhoanService;

@Controller
public class QuanLyTaiKhoanController extends BaseController {

	@Autowired
	QuanLyTaiKhoanService QuanLyTaiKhoanService;

	@Autowired
	private CategoryService category;

	@RequestMapping(value = "/Master/QuanLyTaiKhoan", method = RequestMethod.GET)
	public String init(Model model, HttpServletResponse response, HttpServletRequest request) {

		initial(model);
		model.addAttribute("QUYEN", this.category.getListCategoryDistince("QUYEN", new String[] { "2" }));
		return "app/QuanLyTaiKhoan";
	}
}