package com.quanlyhoso.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quanlyhoso.common.base.BaseService;
import com.quanlyhoso.common.component.Messages;
import com.quanlyhoso.common.constant.CommonConstant;
import com.quanlyhoso.common.util.EncrytedPasswordUtils;
import com.quanlyhoso.common.util.GenerateUtil;
import com.quanlyhoso.common.validate.Validate;
import com.quanlyhoso.entity.AppRole;
import com.quanlyhoso.entity.AppUser;
import com.quanlyhoso.entity.UserRole;
import com.quanlyhoso.model.CommonUploadModel;
import com.quanlyhoso.model.TaiKhoanModel;
import com.quanlyhoso.repository.IRoleRepository;
import com.quanlyhoso.repository.IUserRepository;
import com.quanlyhoso.repository.IUserRoleRepository;

@Service
@Transactional
public class QuanLyTaiKhoanService extends BaseService {
	@Autowired
	private IUserRepository userRepo;
	@Autowired
	private IUserRoleRepository userRoleRepo;
	@Autowired
	private IRoleRepository roleRepo;
	@Autowired
	private CategoryService category;
	public Map<String, Object> findList(String allSearch, int start, String orderColumn, String orderDirection) {

		Pageable pageable = GenerateUtil.generatePageable(start, orderColumn, orderDirection);
		Map<String, Object> data = new HashMap<>();

		List<TaiKhoanModel> listData = new ArrayList<>();
		Page<AppUser> listSearch = userRepo.findList(allSearch, pageable);
		TaiKhoanModel model = null;
		for (AppUser entity : listSearch.getContent()) {
			model = new TaiKhoanModel();
			model.setUserName(entity.getUserName());
			model.setHoTen(entity.getName());
			model.setQuyen(this.category.getValue("QUYEN", entity.getUserRoles().get(0).getAppRole().getRoleId()));

			// model.addAttribute("DON_VI", this.donVi.findAll());
			model.setCmnUpdateDate(null);
			// REQUIRED: Setting common
			model.setPrimaryKey(GenerateUtil.generateKey(String.valueOf(entity.getUserName())));
			listData.add(model);
		}

		// Push data
		data.put(CommonConstant.DATA, listData);
		data.put(CommonConstant.RECORD_TOTAL, listSearch.getTotalElements());
		data.put(CommonConstant.RECORD_FILTERED, listSearch.getTotalElements());
		return data;
	}
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

	public Map<String, Object> upload(CommonUploadModel m0001model) {
		Map<String, Object> data = new HashMap<>();
		return data;
	}

	public Map<String, Object> maintenance(TaiKhoanModel model, String method) {
		Timestamp timestamp = new Timestamp(new Date().getTime());
		Map<String, Object> data = new HashMap<>();
		if (!validate(model, method, data)) {
			return data;
		}
		switch (method) {
		case CommonConstant.METHOD_POST:
			if (!create(model, timestamp)) {
				getValidate().addError(data, Messages.getMessage("common.message.creatdFail"));
			} else {
				getValidate().addInfor(data, Messages.getMessage("common.message.creatdDone"));
			}
			break;
		case CommonConstant.METHOD_PUT:
			if (!update(model, timestamp)) {
				getValidate().addError(data, Messages.getMessage("common.message.updateFail"));
			} else {
				getValidate().addInfor(data, Messages.getMessage("common.message.updateDone"));
			}
			break;

		case CommonConstant.METHOD_DELETE:
			if (!delete(model)) {
				getValidate().addError(data, Messages.getMessage("common.message.deleteFail"));
			} else {
				getValidate().addInfor(data, Messages.getMessage("common.message.deleteDone"));
			}
			break;
		default:
			break;
		}
		return data;

	}

	private boolean validate(TaiKhoanModel model, String method, Map<String, Object> data) {

		if (!"DELETE".equals(method)) {
			getValidate().add(model.getUserName(), "userName", "QuanLyTaiKhoan.literal.taiKhoan", true, 36,
					Validate.FORMAT_HALF_SIZE);
			getValidate().add(model.getHoTen(), "hoTen", "QuanLyTaiKhoan.literal.hoTen", true, 50, Validate.FORMAT_ALL);
			getValidate().add(model.getQuyen(), "quyen", "QuanLyTaiKhoan.literal.quyen", true, 2,
					Validate.FORMAT_INTEGER);
			if (!getValidate().check(data)) {
				return false;
			}
		}
		if ("POST".equals(method)) {
			Optional<AppUser> optional = userRepo.findUser(model.getUserName());
			if (optional.isPresent()) {
				getValidate().addError(data, "userName", Messages.getMessage("common.validate.existRecord",
						Messages.getMessage("QuanLyTaiKhoan.literal.taiKhoan")));
				return false;
			}
		} else {
			Optional<AppUser> optional = userRepo.findUser(model.getPrimaryKey());
			if (!optional.isPresent()) {
				getValidate().addError(data, Messages.getMessage("common.validate.notExistRecord",
						Messages.getMessage("QuanLyTaiKhoan.literal.taiKhoan")));
				return false;
			}
		}
		return true;
	}

	private boolean create(TaiKhoanModel model, Timestamp timestamp) {

		AppUser appUser = new AppUser();
		appUser.setUserId(GenerateUtil.generateID());
		appUser.setUserName(model.getUserName());
		appUser.setEncrytedPassword(EncrytedPasswordUtils.encrytePassword("1"));

		appUser.setName(model.getHoTen());
		appUser.setEnabled(1);
		appUser.setLocale("vn");
		appUser.setDelFlag("0");
		appUser.setCreateDate(timestamp);
		appUser.setCreateProgram(getClass().getSimpleName());
		appUser.setCreateUser(getUser().getUserId());
		appUser.setUpdateDate(timestamp);
		appUser.setUpdateProgram(getClass().getSimpleName());
		appUser.setUpdateUser(getUser().getUserId());
		userRepo.save(appUser);

		Optional<AppRole> role = roleRepo.findById(model.getQuyen());
		UserRole userRole = new UserRole();
		userRole.setId(GenerateUtil.generateID());
		userRole.setAppRole(role.get());
		userRole.setAppUser(appUser);
		userRole.setDelFlag("0");
		userRole.setCreateDate(timestamp);
		userRole.setCreateProgram(getClass().getSimpleName());
		userRole.setCreateUser(getUser().getUserId());
		userRole.setUpdateDate(timestamp);
		userRole.setUpdateProgram(getClass().getSimpleName());
		userRole.setUpdateUser(getUser().getUserId());
		userRoleRepo.save(userRole);
		return true;
	}

	private boolean update(TaiKhoanModel model, Timestamp timestamp) {

		AppUser appUser = userRepo.findUser(model.getUserName()).get();
		appUser.setName(model.getHoTen());
		appUser.setLocale("vn");
		userRepo.save(appUser);
		
		Optional<AppRole> role = roleRepo.findById(model.getQuyen());
		UserRole userRole = appUser.getUserRoles().get(0);
		userRole.setAppRole(role.get());
		userRole.setUpdateDate(timestamp);
		userRole.setUpdateProgram(getClass().getSimpleName());
		userRole.setUpdateUser(getUser().getUserId());
		userRoleRepo.save(userRole);
		return true;
	}

	private boolean delete(TaiKhoanModel model) {
		AppUser appUser = userRepo.findUser(model.getPrimaryKey()).get();
		UserRole userRole = userRoleRepo.findIdByOne(appUser.getUserId());
		userRoleRepo.delete(userRole);
		userRepo.delete(appUser);
		return true;
	}
}