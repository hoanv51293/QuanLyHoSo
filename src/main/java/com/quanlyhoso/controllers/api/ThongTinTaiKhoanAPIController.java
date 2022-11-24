package com.quanlyhoso.controllers.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.quanlyhoso.model.MatKhauModel;
import com.quanlyhoso.service.impl.ThongTinTaiKhoanService;

@RestController
public class ThongTinTaiKhoanAPIController {

	@Autowired
	ThongTinTaiKhoanService ThongTinTaiKhoanService;

	@RequestMapping(value = "/api/ThongTinTaiKhoan/maintenance", method = { RequestMethod.GET, RequestMethod.POST,
			RequestMethod.PUT, RequestMethod.DELETE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> maintenance(HttpServletRequest request, @RequestBody MatKhauModel model) throws Exception {
		try {
			return ThongTinTaiKhoanService.maintenance(model, request.getMethod());
		} finally {
			ThongTinTaiKhoanService.remove();
		}
	}
}