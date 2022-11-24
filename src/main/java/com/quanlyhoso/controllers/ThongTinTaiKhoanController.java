package com.quanlyhoso.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quanlyhoso.common.base.BaseController;
import com.quanlyhoso.common.constant.CommonConstant;
import com.quanlyhoso.entity.AppUser;
import com.quanlyhoso.model.User;
import com.quanlyhoso.repository.IUserRepository;
import com.quanlyhoso.service.impl.CategoryService;

@Controller
public class ThongTinTaiKhoanController extends BaseController {

	@Autowired
	private CategoryService category;
	@Autowired
	private IUserRepository userRepo;
	@RequestMapping(value = "/Master/ThongTinTaiKhoan", method = RequestMethod.GET)
	public String init(Model model, HttpServletResponse response, HttpServletRequest request) {

		initial(model);
		model.addAttribute("QUYEN", this.category.getListCategory("QUYEN"));
		User user = (User) request.getSession().getAttribute(CommonConstant.USER_INFO);

		Optional<AppUser> optional = userRepo.findUser(user.getUserId());
		if (optional.isPresent()) {
			AppUser entity = optional.get();
			model.addAttribute("userName", entity.getUserName());
			model.addAttribute("hoTen", entity.getName());
		}
		return "app/ThongTinTaiKhoan";
	}
}