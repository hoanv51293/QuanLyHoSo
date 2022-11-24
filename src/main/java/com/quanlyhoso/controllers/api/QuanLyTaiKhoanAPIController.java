package com.quanlyhoso.controllers.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quanlyhoso.model.TaiKhoanModel;
import com.quanlyhoso.service.impl.QuanLyTaiKhoanService;

@RestController
public class QuanLyTaiKhoanAPIController {

	@Autowired
	QuanLyTaiKhoanService QuanLyTaiKhoanService;

	@RequestMapping(value = "/api/QuanLyTaiKhoan/search", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> search(HttpServletRequest request,
			@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "allSearch", defaultValue = "") String allSearch,
			@RequestParam(value = "orderColumn", defaultValue = "") String orderColumn,
			@RequestParam(value = "orderDirection", defaultValue = "asc") String orderDirection) throws Exception {
		try {
			// Get List Food
			Map<String, Object> data = QuanLyTaiKhoanService.findList(allSearch, start, orderColumn,
					orderDirection);
			data.put("statusCode", "OK");

			// Return data
			return data;
		} finally {
			QuanLyTaiKhoanService.remove();
		}

	}
	@RequestMapping(value = "/api/QuanLyTaiKhoan/maintenance", method = { RequestMethod.GET, RequestMethod.POST,
			RequestMethod.PUT, RequestMethod.DELETE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> maintenance(HttpServletRequest request, @RequestBody TaiKhoanModel model) throws Exception {
		try {
			return QuanLyTaiKhoanService.maintenance(model, request.getMethod());
		} finally {
			QuanLyTaiKhoanService.remove();
		}
	}

	@RequestMapping(value = "/api/QuanLyTaiKhoan/findById", method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> findById(HttpServletRequest request,
			@RequestParam(value = "primaryKey") String primaryKey) throws Exception {
		try {
			return QuanLyTaiKhoanService.findById(primaryKey);
		} finally {
			QuanLyTaiKhoanService.remove();
		}
	}
}