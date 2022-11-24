package com.quanlyhoso.controllers.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quanlyhoso.service.impl.DanhSachHoSoService;

@RestController
public class DanhSachHoSoAPIController {
	private static Logger logger = LoggerFactory.getLogger(DanhSachHoSoAPIController.class);

	@Autowired
	DanhSachHoSoService DanhSachHoSoService;

	@RequestMapping(value = "/api/DanhSachHoSo/search", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> search(HttpServletRequest request,
			@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "allSearch", defaultValue = "") String allSearch,
			@RequestParam(value = "maDvNl", defaultValue = "") String maDvNl,
			@RequestParam(value = "maLoai", defaultValue = "") String maLoai,
			@RequestParam(value = "orderColumn", defaultValue = "") String orderColumn,
			@RequestParam(value = "orderDirection", defaultValue = "asc") String orderDirection) throws Exception {
		try {
			logger.info("Start search");
			// Get List Food
			Map<String, Object> data = DanhSachHoSoService.findList(allSearch,maDvNl,maLoai, start, orderColumn,
					orderDirection);
			data.put("statusCode", "OK");

			// Return data
			logger.info("END search");
			return data;
		} finally {
			DanhSachHoSoService.remove();
		}
	}
}