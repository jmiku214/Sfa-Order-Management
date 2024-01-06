package com.sfa.stock_management.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileRequestDto {

	private MultipartFile files;

	private String directory;

	public FileRequestDto(MultipartFile files, String directory) {
		this.files = files;
		this.directory = directory;
	}

	public FileRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MultipartFile getFiles() {
		return files;
	}

	public void setFiles(MultipartFile files) {
		this.files = files;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

}
