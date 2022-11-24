package com.quanlyhoso.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quanlyhoso.common.base.BaseController;
import com.quanlyhoso.model.NhapLieuHoSoModel;
import com.quanlyhoso.service.impl.CategoryService;

@Controller
public class NhapLieuHoSoController extends BaseController {
	@Autowired
	private CategoryService category;

	@RequestMapping(value = "/Master/NhapLieuHoSo", method = RequestMethod.GET)
	public String init(Model model, HttpServletResponse response, HttpServletRequest request, @ModelAttribute("form") NhapLieuHoSoModel nhapLieuHoSoModel) {

		initial(model);
		model.addAttribute("form", nhapLieuHoSoModel);
		model.addAttribute("LOAI_HO_SO", this.category.getListCategory("LOAI_HO_SO"));
		model.addAttribute("DON_VI_NOP_LUU", this.category.getListCategory("DON_VI_NOP_LUU"));
		return "app/NhapLieuHoSo";
	}
}