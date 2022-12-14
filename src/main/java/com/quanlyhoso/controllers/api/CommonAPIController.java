package com.quanlyhoso.controllers.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlyhoso.common.constant.CommonConstant;
import com.quanlyhoso.common.util.Config;
import com.quanlyhoso.common.util.SeinoUtil;
import com.quanlyhoso.model.CommonDownloadModel;

@RestController
public class CommonAPIController {

	@Autowired
	private ServletContext servletContext;

	@RequestMapping(value = "/api/common/download")
	public ResponseEntity<InputStreamResource> executePrintDownload(CommonDownloadModel ankenModel, HttpServletRequest request,
			HttpServletResponse response) throws FileNotFoundException {
		try {
			File file = new File(Config.get("common.dir") + ankenModel.getIdFileDownload());
			if (!file.exists()) {
				file = new File(Config.get("common.dir.download") + ankenModel.getIdFileDownload());
			}
			if (!file.exists()) {
				file = new File(Config.get("common.dir.download") + ankenModel.getIdFileDownload() + CommonConstant.EXTENSIONS_EXCEL_DOT);
			}

			String fileName = file.getName();
			MediaType mediaType = SeinoUtil.toContentType(this.servletContext, fileName);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName()).contentType(mediaType)
					.contentLength(file.length()) //
					.body(resource);
		} finally {
		}
	}
}