package com.quanlyhoso.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quanlyhoso.common.base.BaseService;
import com.quanlyhoso.common.component.Messages;
import com.quanlyhoso.common.constant.CommonConstant;
import com.quanlyhoso.common.util.DateUtil;
import com.quanlyhoso.common.util.GenerateUtil;
import com.quanlyhoso.common.validate.Validate;
import com.quanlyhoso.entity.HoSo;
import com.quanlyhoso.model.HoSoModel;
import com.quanlyhoso.repository.IHoSoRepository;

@Service
@Transactional
public class NhapLieuHoSoService extends BaseService {
	@Autowired
	private IHoSoRepository hoSoRepo;

	public Map<String, Object> findById(String primaryKey) {
		Map<String, Object> data = new HashMap<>();
		Optional<HoSo> optional = hoSoRepo.findById(primaryKey);
		if (optional.isPresent()) {
			HoSoModel model = new HoSoModel();
			HoSo entity = optional.get();
			model.setSoLt(entity.getSoLt());
			model.setCbnl(entity.getCbnl());
			model.setMaDvNl(entity.getMaDvNl());
			model.setMaLoai(entity.getMaLoai());
			model.setSoDk(entity.getSoDk());
			model.setNgayDk(DateUtil.formatDisplay(entity.getNgayDk()));
			model.setNgayNl(DateUtil.formatDisplay(entity.getNgayNl()));
			model.setThoiHanBaoQuan(DateUtil.formatDisplay(entity.getThoiHanBaoQuan()));
			model.setNgayKt(DateUtil.formatDisplay(entity.getNgayKt()));
			model.setNgayLap(DateUtil.formatDisplay(entity.getNgayLap()));
			model.setTrichYeu(entity.getTrichYeu());
			model.setPrimaryKey(GenerateUtil.generateKey(entity.getSoLt()));
			data.put(CommonConstant.STATUS_CODE, CommonConstant.STATUS_OK);
			data.put(CommonConstant.MODEL_DATA, model);
		} else {
			getValidate().addError(data, Messages.getMessage("common.validate.notExistRecord", Messages.getMessage("common.message.hoSo")));
		}
		return data;
	}

	public Map<String, Object> maintenance(HoSoModel model, String method) {
		Map<String, Object> data = new HashMap<>();
		if (!CommonConstant.METHOD_POST.equals(method)) {
			if (!StringUtils.equals(model.getPrimaryKey(), model.getSoLt())) {
				getValidate().addError(data, "Số hồ sơ lưu trữ đã bị thay đổi. Hãy kiểm tra lại!");
				return data;
			}
		}
		if (!validate(model, method, data)) {
			return data;
		}
		int stt = 0;
		String key = model.getSoLt();
		String[] arrCheck = key.split("/");
		if (CommonConstant.METHOD_POST.equals(method) || CommonConstant.METHOD_PUT.equals(method)) {
			if (arrCheck.length != 2) {
				getValidate().addError(data, "soLt","Số hồ sơ lưu trữ đang nhập sai. Hãy kiểm tra lại!");
				return data;
			}
			int lengthFirst = arrCheck[0].length();
			if (lengthFirst <= 4) {
				getValidate().addError(data, "soLt","Số hồ sơ lưu trữ đang nhập sai. Hãy kiểm tra lại!");
				return data;
			}
			if (!StringUtils.equals(arrCheck[1], "201CN")) {
				getValidate().addError(data, "soLt","Mã đơn vị quản lý lưu phải cố định là 201CN. Hãy kiểm tra lại!");
				return data;
			}
			if (!StringUtils.contains(arrCheck[0], model.getMaLoai())) {
				getValidate().addError(data, "soLt","Loại hồ sơ không khớp. Hãy kiểm tra lại!");
				getValidate().addError(data, "maLoai","Loại hồ sơ không khớp. Hãy kiểm tra lại!");
				return data;
			}
			
			String namNopLuu = arrCheck[0].substring(lengthFirst-2, lengthFirst);
			String ngayNopLuu = DateUtil.format(model.getNgayNl(),DateUtil.DD_MM_YYYY, "yy");
			if (!StringUtils.equals(namNopLuu, ngayNopLuu)) {
				getValidate().addError(data, "soLt","Ngày nộp lưu không khớp. Hãy kiểm tra lại!");
				getValidate().addError(data, "ngayNl","Ngày nộp lưu không khớp. Hãy kiểm tra lại!");
				return data;
			}
			
			if (DateUtil.formatCompare(model.getNgayDk()).compareTo(DateUtil.formatCompare(model.getNgayKt())) > 0) {
				getValidate().addError(data, "ngayDk", Messages.getMessage("common.validate.fromTo","Ngày đăng ký","Ngày kết thúc"));
				getValidate().addError(data, "ngayKt", Messages.getMessage("common.validate.fromTo","Ngày đăng ký","Ngày kết thúc"));
				return data;
			}
			if (DateUtil.formatCompare(model.getNgayKt()).compareTo(DateUtil.formatCompare(model.getNgayNl())) > 0) {
				getValidate().addError(data, "ngayKt", Messages.getMessage("common.validate.fromTo","Ngày kết thúc","Ngày nộp lưu"));
				getValidate().addError(data, "ngayNl", Messages.getMessage("common.validate.fromTo","Ngày kết thúc","Ngày nộp lưu"));
				return data;
			}
			if (DateUtil.formatCompare(model.getNgayLap()).compareTo(DateUtil.formatCompare(model.getNgayDk())) > 0) {
				getValidate().addError(data, "ngayLap", Messages.getMessage("common.validate.fromTo","Ngày lập","Ngày đăng ký"));
				getValidate().addError(data, "ngayDk", Messages.getMessage("common.validate.fromTo","Ngày lập","Ngày đăng ký"));
				return data;
			}
			
			String[] soDangKyAr = model.getSoDk().split("/");
			if (soDangKyAr.length != 2) {
				getValidate().addError(data, "soDk","Số hồ sơ đăng ký đang nhập sai. Hãy kiểm tra lại!");
				return data;
			}
			int lengthsoDkFirst = soDangKyAr[0].length();
			if (lengthsoDkFirst <= 4) {
				getValidate().addError(data, "soDk","Số hồ sơ đăng ký đang nhập sai. Hãy kiểm tra lại!");
				return data;
			}
			if (!StringUtils.equals(soDangKyAr[1], model.getMaDvNl())) {
				getValidate().addError(data, "soDk","Đơn vị nộp lưu không khớp với Số hồ sơ. Hãy kiểm tra lại!");
				getValidate().addError(data, "maDvNl","Đơn vị nộp lưu không khớp với Số hồ sơ. Hãy kiểm tra lại!");
				return data;
			}
			
			String ngayThangDk = soDangKyAr[0].substring(lengthsoDkFirst-4, lengthsoDkFirst);
			String ngayDk = DateUtil.format(model.getNgayDk(),DateUtil.DD_MM_YYYY, "MMyy");
			if (!StringUtils.equals(ngayThangDk, ngayDk)) {
				getValidate().addError(data, "soDk","Ngày tháng đăng ký không khớp với Số hồ sơ. Hãy kiểm tra lại!");
				getValidate().addError(data, "ngayDk","Ngày tháng đăng ký không khớp với Số hồ sơ. Hãy kiểm tra lại!");
				return data;
			}
			List<HoSo> checkSoDk = hoSoRepo.findBySoDk(model.getSoDk());
			if (CommonConstant.METHOD_POST.equals(method)) {
				if (CollectionUtils.isNotEmpty(checkSoDk)) {
					getValidate().addError(data, "soDk",
							Messages.getMessage("common.validate.existRecord", "Hồ sơ đăng ký"));
					return data;
				}
			} else {
				if (CollectionUtils.isNotEmpty(checkSoDk)) {
					if (!StringUtils.equals(checkSoDk.get(0).getSoDk(), hoSoRepo.findById(model.getSoLt()).get().getSoDk())) {
						getValidate().addError(data, "soDk",
								Messages.getMessage("common.validate.existRecord", "Hồ sơ đăng ký"));
						return data;
					}
				}
			}
		}
		if (CommonConstant.METHOD_POST.equals(method)) {
			try {
				int currentIndex = hoSoRepo.maxByMaLoai(model.getMaLoai());
				int newIndex = Integer.parseInt(arrCheck[0].split(model.getMaLoai())[0]);
				if (newIndex <= currentIndex) {
					getValidate().addError(data, "soLt", "Hãy nhập [Số lưu] của loại hồ sơ [" + model.getMaLoai()
							+ "] lớn hơn " + currentIndex + "!");
					return data;
				}
				stt = newIndex;
			} catch (Exception e) {
				getValidate().addError(data, "soLt", "Số hồ sơ lưu trữ đang nhập sai. Hãy kiểm tra lại!");
				return data;
			}

		}
		
		switch (method) {
		case CommonConstant.METHOD_POST:
			if (!create(model, stt)) {
				getValidate().addError(data, Messages.getMessage("common.message.creatdFail"));
			} else {
				getValidate().addInfor(data, Messages.getMessage("common.message.creatdDone"));
			}
			break;
		case CommonConstant.METHOD_PUT:
			if (!update(model)) {
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
		data.put("primaryKey", model.getSoLt());
		return data;
	}

	private boolean validate(HoSoModel model, String method, Map<String, Object> data) {

		if (!"DELETE".equals(method)) {
			getValidate().add(model.getCbnl(), "cbnl", "NhapLieuHoSo.literal.cbnl", true, 50,Validate.FORMAT_ALL);
			getValidate().add(model.getMaDvNl(), "maDvNl", "NhapLieuHoSo.literal.maDvNl", true, 10,Validate.FORMAT_ALL);
			getValidate().add(model.getMaLoai(), "maLoai", "NhapLieuHoSo.literal.maLoai", true, 10,Validate.FORMAT_ALL);
			//getValidate().add(model.getSoDk(), "maLoai", "NhapLieuHoSo.literal.soDk", true, 50,Validate.FORMAT_ALL);
			getValidate().add(model.getNgayDk(), "ngayDk", "NhapLieuHoSo.literal.ngayDk", true, 8,Validate.FORMAT_DATE);
			getValidate().add(model.getNgayNl(), "ngayNl", "NhapLieuHoSo.literal.ngayNl", true, 8,Validate.FORMAT_DATE);
			getValidate().add(model.getThoiHanBaoQuan(), "thoiHanBaoQuan", "NhapLieuHoSo.literal.thoiHanBaoQuan", true, 8,Validate.FORMAT_DATE);
			getValidate().add(model.getNgayKt(), "ngayKt", "NhapLieuHoSo.literal.ngayKt", true, 8,Validate.FORMAT_DATE);
			getValidate().add(model.getNgayLap(), "ngayLap", "NhapLieuHoSo.literal.ngayLap", true, 8,Validate.FORMAT_DATE);
			getValidate().add(model.getTrichYeu(), "trichYeu", "NhapLieuHoSo.literal.trichYeu", true, 500,Validate.FORMAT_ALL);
			if (!getValidate().check(data)) {
				return false;
			}
		}
		if ("POST".equals(method)) {
			Optional<HoSo> optional = hoSoRepo.findById(model.getSoLt());
			if (optional.isPresent()) {
				getValidate().addError(data, "soLt", Messages.getMessage("common.validate.existRecord", Messages.getMessage("NhapLieuHoSo.literal.soLt")));
				return false;
			}
		} else {
			Optional<HoSo> function = hoSoRepo.findById(model.getPrimaryKey());
			if (!function.isPresent()) {
				getValidate().addError(data, Messages.getMessage("common.validate.notExistRecord", Messages.getMessage("common.message.hoSo")));
				return false;
			}
		}
		return true;
	}

	private boolean create(HoSoModel model, int index) {

		Optional<HoSo> optional = hoSoRepo.findById(model.getSoLt());
		HoSo entity = null;
		if (optional.isPresent()) {
			entity = optional.get();
		} else {
			entity = new HoSo();
		}
		//String soDk = (index)+model.getMaLoai()+DateUtil.getCurrentDate("MMyy")+"/201CD";
		entity.setSoLt(model.getSoLt());
		entity.setCbnl(model.getCbnl());
		entity.setMaDvNl(model.getMaDvNl());
		entity.setMaLoai(model.getMaLoai());
		entity.setSoDk(model.getSoDk());
		entity.setNgayDk(DateUtil.formatDB(model.getNgayDk()));
		entity.setNgayNl(DateUtil.formatDB(model.getNgayNl()));
		entity.setThoiHanBaoQuan(DateUtil.formatDB(model.getThoiHanBaoQuan()));
		entity.setNgayKt(DateUtil.formatDB(model.getNgayKt()));
		entity.setNgayLap(DateUtil.formatDB(model.getNgayLap()));
		entity.setTrichYeu(model.getTrichYeu());
		entity.setStt(index);
		hoSoRepo.save(entity);
		return true;
	}

	private boolean update(HoSoModel model) {
		Optional<HoSo> optional = hoSoRepo.findById(model.getSoLt());
		if (!optional.isPresent()) {
			return false;
		}
		HoSo entity = optional.get();
		entity.setCbnl(model.getCbnl());
		entity.setMaDvNl(model.getMaDvNl());
		entity.setMaLoai(model.getMaLoai());
		entity.setSoDk(model.getSoDk());
		entity.setNgayDk(DateUtil.formatDB(model.getNgayDk()));
		entity.setNgayNl(DateUtil.formatDB(model.getNgayNl()));
		entity.setThoiHanBaoQuan(DateUtil.formatDB(model.getThoiHanBaoQuan()));
		entity.setNgayKt(DateUtil.formatDB(model.getNgayKt()));
		entity.setNgayLap(DateUtil.formatDB(model.getNgayLap()));
		entity.setTrichYeu(model.getTrichYeu());
		hoSoRepo.save(entity);
		return true;
	}

	private boolean delete(HoSoModel model) {
		Optional<HoSo> optional = hoSoRepo.findById(model.getPrimaryKey());
		if (!optional.isPresent()) {
			return false;
		}
		HoSo entity = optional.get();
		hoSoRepo.delete(entity);
		return true;
	}
}