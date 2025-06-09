package com.codewitharrays.utils;

import org.apache.commons.io.FilenameUtils;

public class CommonUtils {

	public static String getContentType(String originalFileName) {
		String extension = FilenameUtils.getExtension(originalFileName); // java_programing.pdf

		switch (extension) {
		case "pdf":
			return "application/pdf";
		case "xlsx":
			return "application/vnd.openxmlformats-officedocument.spreadsheettml.sheet";
		case "txt":
			return "text/plan";
		case "png":
			return "image/png";
		case "jpeg":
			return "image/jpeg";
		default:
			return "application/octet-stream";
		}
	}
}
