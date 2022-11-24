package com.quanlyhoso.common.base;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import com.quanlyhoso.common.constant.CommonConstant;
import com.quanlyhoso.common.util.AuditorAwareImpl;
import com.quanlyhoso.common.util.WebUtils;
import com.quanlyhoso.entity.AppUser;
import com.quanlyhoso.model.User;
import com.quanlyhoso.repository.AppUserRepository;
import com.quanlyhoso.repository.IAppRoleRepository;

public abstract class BaseController {

	@Autowired
	private AppUserRepository appUserRepo;
	@Autowired
	private IAppRoleRepository appRole;
	@Autowired
	private HttpServletRequest request;

	private AuditorAwareImpl auditorAwareImpl = new AuditorAwareImpl();

	public void initial(Model model) {

		AppUser entity = appUserRepo.findUserAccount(auditorAwareImpl.getCurrentAuditor().get());
		User user = new User();
		user.setUserId(entity.getUserName());
		user.setLocale(new Locale(entity.getLocale()));
		user.setName(entity.getName());

		user.setRole(entity.getUserRoles().get(0).getAppRole().getRoleName());
		if (StringUtils.equals(user.getRole(), "ROLE_THU_KHO")) {
			user.setRoleName("Thủ kho");
		}
		if (StringUtils.equals(user.getRole(), "ROLE_ADMIN")) {
			user.setRoleName("Admin (Người quản lý hệ thống)");
		}
		if (StringUtils.equals(user.getRole(), "ROLE_CBPN")) {
			user.setRoleName("Cán bộ phòng nhập");
		}
		user.setRoleId(String.valueOf(appRole.getOneByRoleName(WebUtils.getUserRole()).getRoleId()));
		model.addAttribute(CommonConstant.USER_INFO, user);
		request.getSession().setAttribute(CommonConstant.USER_INFO, user);
		model.addAttribute("modSub", null);
	}

	public boolean isAuthen() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication instanceof AnonymousAuthenticationToken) {
			return false;

		}
		return true;
	}
}
