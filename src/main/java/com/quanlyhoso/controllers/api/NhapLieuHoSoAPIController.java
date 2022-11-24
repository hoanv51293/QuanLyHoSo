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

import com.quanlyhoso.model.HoSoModel;
import com.quanlyhoso.service.impl.NhapLieuHoSoService;

@RestController
public class NhapLieuHoSoAPIController {

	@Autowired
	NhapLieuHoSoService service;

	@RequestMapping(value = "/api/NhapLieuHoSo/maintenance", method = { RequestMethod.GET, RequestMethod.POST,
			RequestMethod.PUT, RequestMethod.DELETE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> maintenance(HttpServletRequest request, @RequestBody HoSoModel model) throws Exception {
		try {
			return service.maintenance(model, request.getMethod());
		} finally {
			service.remove();
		}
	}
	@RequestMapping(value = "/api/NhapLieuHoSo/findById", method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> findById(HttpServletRequest request,
			@RequestParam(value = "primaryKey") String primaryKey) throws Exception {
		try {
			return service.findById(primaryKey);
		} finally {
			service.remove();
		}
	}
}