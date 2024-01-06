package com.sfa.stock_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sfa.stock_management.dto.Response;
import com.sfa.stock_management.serviceImpl.FileSystemStorageService;

@RestController
@CrossOrigin
public class FileUploadController {

	@Autowired
	private FileSystemStorageService fileSystemStorageService;
	
	@SuppressWarnings("rawtypes")
	@Lookup
	public Response getResponse() {
		return null;
	}
	
	@PostMapping("/v1/uploadFile")
	public ResponseEntity<?> uploadFileInLocalAndResponseAsDownloadUrl(@ModelAttribute MultipartFile file)
			throws Exception {

		Response<String> response = new Response<>();
		String fileUrl = fileSystemStorageService.getUserExpenseFileUrl(file);
		if (fileUrl != null && !fileUrl.isEmpty()) {
			response.setData(fileUrl);
			response.setResponseCode(HttpStatus.OK.value());
			response.setMessage("File uploaded successfully");
		} else {
			response.setData(fileUrl);
			response.setResponseCode(405);
			response.setMessage("Please Select a Valid File");
		}
		return new ResponseEntity<Response<String>>(response, HttpStatus.OK);

	}

	@GetMapping("/download/{filename}")
	public ResponseEntity<?> downloadFile(@PathVariable("filename") String filename) {

		Resource resource = fileSystemStorageService.downloadDocument(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

	}
	
}
