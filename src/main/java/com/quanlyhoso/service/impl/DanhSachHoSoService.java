package com.quanlyhoso.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.quanlyhoso.common.base.BaseService;
import com.quanlyhoso.common.constant.CommonConstant;
import com.quanlyhoso.common.util.DateUtil;
import com.quanlyhoso.common.util.GenerateUtil;
import com.quanlyhoso.entity.HoSo;
import com.quanlyhoso.model.HoSoModel;
import com.quanlyhoso.repository.IHoSoRepository;

@Service
public class DanhSachHoSoService extends BaseService {
	@Autowired
	private IHoSoRepository hoSoRepo;
	@Autowired
	private CategoryService category;
	public Map<String, Object> findList(String allSearch, String maDvNl, String maLoai, int start, String orderColumn, String orderDirection) {

		Pageable pageable = GenerateUtil.generatePageable(start, orderColumn, orderDirection);
		Map<String, Object> data = new HashMap<>();

		List<HoSoModel> listData = new ArrayList<>();
		Page<HoSo> listSearch = hoSoRepo.findList(allSearch,maDvNl,maLoai, pageable);
		HoSoModel model = null;
		for (HoSo entity : listSearch.getContent()) {
			model = new HoSoModel();
			model.setSoLt(entity.getSoLt());
			model.setCbnl(entity.getCbnl());
			model.setMaDvNl(category.getValue("DON_VI_NOP_LUU", entity.getMaDvNl()));
			model.setMaLoai(entity.getMaLoai());
			model.setSoDk(entity.getSoDk());
			model.setNgayDk(DateUtil.formatDisplay(entity.getNgayDk()));
			model.setNgayNl(DateUtil.formatDisplay(entity.getNgayNl()));
			model.setThoiHanBaoQuan(DateUtil.formatDisplay(entity.getThoiHanBaoQuan()));
			model.setNgayKt(DateUtil.formatDisplay(entity.getNgayKt()));
			model.setNgayLap(DateUtil.formatDisplay(entity.getNgayLap()));
			model.setTrichYeu(entity.getTrichYeu());

			model.setCmnUpdateDate(null);
			// REQUIRED: Setting common
			model.setPrimaryKey(GenerateUtil.generateKey(entity.getSoLt()));
			listData.add(model);
		}

		// Push data
		data.put(CommonConstant.DATA, listData);
		data.put(CommonConstant.RECORD_TOTAL, listSearch.getTotalElements());
		data.put(CommonConstant.RECORD_FILTERED, listSearch.getTotalElements());
		return data;
	}
}