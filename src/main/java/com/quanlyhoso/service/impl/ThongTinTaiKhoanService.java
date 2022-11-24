package com.quanlyhoso.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quanlyhoso.common.base.BaseService;
import com.quanlyhoso.common.component.Messages;
import com.quanlyhoso.common.constant.CommonConstant;
import com.quanlyhoso.common.util.EncrytedPasswordUtils;
import com.quanlyhoso.common.util.GenerateUtil;
import com.quanlyhoso.common.validate.Validate;
import com.quanlyhoso.entity.AppUser;
import com.quanlyhoso.model.MatKhauModel;
import com.quanlyhoso.model.TaiKhoanModel;
import com.quanlyhoso.repository.IUserRepository;

@Service
@Transactional
public class ThongTinTaiKhoanService extends BaseService {
	@Autowired
	private IUserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Map<String, Object> findById(String primaryKey) {
		Map<String, Object> data = new HashMap<>();
		Optional<AppUser> optional = userRepo.findUser(primaryKey);
		if (optional.isPresent()) {
			TaiKhoanModel model = new TaiKhoanModel();
			AppUser entity = optional.get();
			model = new TaiKhoanModel();
			model.setUserName(entity.getUserName());
			model.setHoTen(entity.getName());
			model.setQuyen(entity.getUserRoles().get(0).getAppRole().getRoleId());
			model.setPrimaryKey(GenerateUtil.generateKey(String.valueOf(entity.getUserName())));
			data.put(CommonConstant.STATUS_CODE, CommonConstant.STATUS_OK);
			data.put(CommonConstant.MODEL_DATA, model);
		} else {
			getValidate().addError(data,
					Messages.getMessage("common.validate.deletedRecord", Messages.getMessage("common.message.taikhoan")));
		}
		return data;
	}

	public Map<String, Object> maintenance(MatKhauModel model, String method) {
		// model.setSoHieu(model.getSoHieu());
		Map<String, Object> data = new HashMap<>();
		getValidate().add(String.valueOf(model.getOldPassword()), "oldPassword", "QuanLyTaiKhoan.literal.oldPassword",
				true, 20, false, Validate.FORMAT_HALF_SIZE);
		getValidate().add(model.getNewPassword(), "newPassword", "QuanLyTaiKhoan.literal.newPassword", true, 20,
				Validate.FORMAT_HALF_SIZE);
		getValidate().add(model.getRePassword(), "rePassword", "QuanLyTaiKhoan.literal.rePassword", true, 20,
				Validate.FORMAT_HALF_SIZE);
		if (!getValidate().check(data)) {
			return data;
		}
		AppUser appUser = userRepo.findUser(getUser().getUserId()).get();
		if (!passwordEncoder.matches(model.getOldPassword(), appUser.getEncrytedPassword())) {
			getValidate().addError(data, "oldPassword","Mật khẩu cũ không đúng");
			return data;
		}
		if (!StringUtils.equals(model.getNewPassword(), model.getRePassword())) {
			getValidate().addError(data, "newPassword","Mật khẩu đang không khớp");
			getValidate().addError(data, "rePassword","Mật khẩu đang không khớp");
			return data;
		}

		appUser.setEncrytedPassword(EncrytedPasswordUtils.encrytePassword(model.getNewPassword()));
		appUser.setName(model.getHoTen());
		userRepo.save(appUser);
		
		getValidate().addInfor(data, "Đã thay đổi mật khẩu thành công");
		return data;
	}
}