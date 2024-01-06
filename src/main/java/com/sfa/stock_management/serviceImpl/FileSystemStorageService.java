package com.sfa.stock_management.serviceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;
import com.sfa.stock_management.config.FileUploadProperties;
import com.sfa.stock_management.dto.FileUrlResponse;
import com.sfa.stock_management.dto.Response;

@Service
public class FileSystemStorageService {

//	@Autowired
//	private FileUploadProperties fileUploadProperties;

	private Path dirLocation;

	@Value("${fileAccessUrl}")
	private String fileAccessUrl;
	@Value("${imageUrlToken}")
	private String imageUrlToken;

	@Autowired
	public FileSystemStorageService(FileUploadProperties fileUploadProperties) {
		this.dirLocation = Paths.get(fileUploadProperties.getLocation()).toAbsolutePath().normalize();
	}

	@PostConstruct
	public void init() throws Throwable {
		try {
			Files.createDirectories(this.dirLocation);
		} catch (Exception ex) {
			throw new Throwable("Could not create upload dir!");
		}

	}

	public Response<?> storeFileInLocalDirectoryResponseIsDownloadUrl(MultipartFile file, Long currentDate) {
		String fileName = StringUtils.cleanPath(currentDate + file.getOriginalFilename());

		try {
			Path targetLocation = this.dirLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
					.path(fileName).toUriString();
			return new Response<>(HttpStatus.OK.value(), "File successfull uploaded", fileDownloadUri);
		} catch (IOException ex) {
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "File fail to uploaded", null);
		}
	}

	public Resource downloadDocument(String fileName) {

		try {

			Path file = this.dirLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not find file");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Could not download file");
		}

	}

	public String getUserExpenseFileUrl(MultipartFile file) {
		String fileUrl = null;
		try {
			if (file != null && file.getBytes().length > 0) {
				fileUrl = getFileUrl(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	private String getFileUrl(MultipartFile file) {
		String fileUrl = null;
		try {
			String url = fileAccessUrl;
			// create an instance of RestTemplate
			RestTemplate restTemplate = new RestTemplate(); // create headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			// headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			// request body parameters
			LinkedMultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("files", new MultipartInputStreamFileResources(file.getInputStream(), file.getOriginalFilename()));

			// send POST request
			ResponseEntity<String> response = null;
			String responseBody = null;
			FileUrlResponse responseBodyDto = new FileUrlResponse();
			if (imageUrlToken != null && !imageUrlToken.isEmpty() && file != null && !file.isEmpty()) {
				try {
					map.add("token", imageUrlToken);
					HttpEntity<LinkedMultiValueMap<Object, Object>> entity = new HttpEntity<>(map, headers);
					response = restTemplate.postForEntity(url, entity, String.class);
					responseBody = response.getBody();
					responseBodyDto = new Gson().fromJson(responseBody, responseBodyDto.getClass());
					if (responseBodyDto.getResponseCode().equals(200) && responseBodyDto.getData() != null
							&& responseBodyDto.getData().getFileUrls() != null
							&& !responseBodyDto.getData().getFileUrls().isEmpty()) {
						fileUrl = responseBodyDto.getData().getFileUrls().get(0);
					}
				} catch (Exception e) {
					e.printStackTrace();

				}
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return fileUrl;
	}

}