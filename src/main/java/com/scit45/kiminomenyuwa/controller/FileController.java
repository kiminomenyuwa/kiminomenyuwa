package com.scit45.kiminomenyuwa.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("files")
public class FileController {

	// 파일이 저장된 기본 디렉토리
	@Value("${file.storage.location}")
	private String uploadDir;

	/**
	 * 파일을 반환하는 메서드
	 *
	 * @param reviewId 리뷰 ID
	 * @param filename 파일 이름
	 * @return 파일 리소스
	 */
	@GetMapping("/{reviewId}/{filename:.+}") // 파일 경로: /files/{reviewId}/{filename}
	public ResponseEntity<Resource> getFile(@PathVariable String reviewId, @PathVariable String filename) throws
		IOException {
		try {
			// 파일 경로 설정: {uploadDir}/{reviewId}/{filename}
			Path filePath = Paths.get(uploadDir).resolve(reviewId).resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			// 파일이 존재하고 읽을 수 있는지 확인
			if (resource.exists() && resource.isReadable()) {
				String contentType = Files.probeContentType(filePath); // 파일의 MIME 타입을 자동으로 설정
				return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
					.header(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : "application/octet-stream")
					.body(resource);
			} else {
				throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다: " + filename);
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("파일 경로 오류: " + filename, e);
		}
	}
}
