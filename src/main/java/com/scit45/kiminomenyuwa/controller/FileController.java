package com.scit45.kiminomenyuwa.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileController {

	// 파일이 저장된 기본 디렉토리
	// application.properties에서 파일 저장 경로를 받아옴
	private final Path fileStorageLocation;

	// 생성자에서 경로를 설정
	public FileController(@Value("${file.storage.location}") String storageLocation) {
		this.fileStorageLocation = Paths.get(storageLocation).toAbsolutePath().normalize();
	}

	/**
	 * 파일을 반환하는 메서드
	 *
	 * @param filename 요청된 파일 이름
	 * @return 파일 리소스
	 */
	@GetMapping("/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		try {
			// 파일 경로 설정
			Path filePath = fileStorageLocation.resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			// 파일이 존재하고 읽을 수 있는지 확인
			if (resource.exists() && resource.isReadable()) {
				String contentType = "application/octet-stream"; // 기본 파일 타입 설정
				return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.header(HttpHeaders.CONTENT_TYPE, contentType)
					.body(resource);
			} else {
				throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다: " + filename);
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("파일 경로 오류: " + filename, e);
		}
	}
}