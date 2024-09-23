package com.scit45.kiminomenyuwa.service.verification;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scit45.kiminomenyuwa.domain.dto.ItemDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReceiptDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class ReceiptRecognitionService {
	private final DocumentAnalysisClient client;
	private final ObjectMapper objectMapper;

	public ReceiptRecognitionService(
		@Value("${azure.formrecognizer.endpoint}") String endpoint,
		@Value("${azure.formrecognizer.apikey}") String key,
		ObjectMapper objectMapper) {
		this.client = new DocumentAnalysisClientBuilder()
			.credential(new AzureKeyCredential(key))
			.endpoint(endpoint)
			.buildClient();
		this.objectMapper = objectMapper;
	}

	// 1. API로 결과를 받아오는 메소드
	public String analyzeReceipt(MultipartFile file) throws Exception {

		try (InputStream receiptStream = file.getInputStream()) {
			// BinaryData로 파일 데이터 생성
			long length = receiptStream.available();
			BinaryData receiptData = BinaryData.fromStream(receiptStream, length);

			// 영수증 분석 시작
			SyncPoller<OperationResult, AnalyzeResult> analyzeReceiptPoller =
				client.beginAnalyzeDocument("prebuilt-receipt", receiptData);

			AnalyzeResult analyzeResult = analyzeReceiptPoller.getFinalResult();

			// AnalyzeResult를 JSON 문자열로 변환
			return objectMapper.writeValueAsString(analyzeResult);
		}
	}

	/**
	 * JSON 문자열을 ReceiptDTO 객체로 파싱하는 메소드
	 *
	 * @param jsonString 분석된 영수증 JSON 문자열
	 * @return ReceiptDTO 객체
	 * @throws IOException JSON 파싱 중 발생한 예외
	 */
	public ReceiptDTO parseReceiptJson(String jsonString) throws IOException {
		JsonNode rootNode = objectMapper.readTree(jsonString);
		ReceiptDTO receipt = new ReceiptDTO();

		JsonNode documents = rootNode.path("documents");
		if (documents.isArray() && !documents.isEmpty()) {
			JsonNode fieldsNode = documents.get(0).path("fields");

			// 가게 이름 추출
			String merchantName = getNestedFieldValue(fieldsNode, "MerchantName", "value");
			if (merchantName != null) {
				receipt.setMerchantName(merchantName);
			}

			// 가게 주소 추출 (content 필드 사용)
			String merchantAddress = getNestedFieldValue(fieldsNode, "MerchantAddress", "content");
			if (merchantAddress != null) {
				receipt.setMerchantAddress(merchantAddress);
			}

			// 전화번호 추출 (value 필드 사용)
			String phoneNumber = getNestedFieldValue(fieldsNode, "MerchantPhoneNumber", "value");
			if (phoneNumber != null) {
				receipt.setMerchantPhoneNumber(phoneNumber);
			}

			// 거래 날짜 추출
			LocalDateTime transactionDate = parseTransactionDate(
				getNestedFieldValue(fieldsNode, "TransactionDate", "content"));
			if (transactionDate != null) {
				receipt.setTransactionDate(transactionDate);
			}

			// 아이템 목록 추출
			List<ItemDTO> itemsList = extractItems(fieldsNode.get("Items"));
//			if (itemsList.isEmpty()) {
//				throw new IllegalArgumentException("구매한 아이템 목록이 제공되지 않았습니다.");
//			}
			receipt.setItems(itemsList);

			// 총액 추출
			Double totalPrice = fieldsNode.path("Total").path("value").asDouble(0.0);
			receipt.setTotalPrice(totalPrice);
		}

		return receipt;
	}

	/**
	 * 특정 필드의 하위 필드 값을 추출하는 헬퍼 메소드
	 *
	 * @param parentNode    부모 JsonNode
	 * @param fieldName     추출할 필드 이름
	 * @param childField    추출할 하위 필드 이름 (예: "value", "content")
	 * @return 하위 필드의 문자열 값 또는 null
	 */
	private String getNestedFieldValue(JsonNode parentNode, String fieldName, String childField) {
		JsonNode fieldNode = parentNode.get(fieldName);
		if (fieldNode != null && fieldNode.has(childField)) {
			return fieldNode.get(childField).asText(null);
		}
		return null;
	}

	/**
	 * 거래 날짜 문자열을 LocalDateTime으로 파싱하는 메소드
	 *
	 * @param transactionDateStr 거래 날짜 문자열
	 * @return LocalDateTime 객체
	 */
	private LocalDateTime parseTransactionDate(String transactionDateStr) {
		if (transactionDateStr == null || transactionDateStr.trim().isEmpty()) {
			throw new IllegalArgumentException("영수증 날짜가 제공되지 않았습니다.");
		}

		// 지원할 날짜 형식 목록
		List<DateTimeFormatter> formatters = Arrays.asList(
			DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"), // ISO_LOCAL_DATE_TIME
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
			DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
			DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"),
			DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
			DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss"),
			DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd"), // 날짜만 있는 경우
			DateTimeFormatter.ofPattern("yyyy/MM/dd"),
			DateTimeFormatter.ofPattern("dd-MM-yyyy"),
			DateTimeFormatter.ofPattern("dd/MM/yyyy"),
			DateTimeFormatter.ofPattern("MM-dd-yyyy"),
			DateTimeFormatter.ofPattern("MM/dd/yyyy")
			// 필요에 따라 추가적인 형식을 여기에 추가
		);

		LocalDateTime transactionDate = null;
		boolean parsed = false;

		// 각 형식을 시도하여 날짜 파싱
		for (DateTimeFormatter formatter : formatters) {
			try {
				if (formatter.toString().contains("HH") || formatter.toString().contains("mm") || formatter.toString()
					.contains("ss")) {
					transactionDate = LocalDateTime.parse(transactionDateStr, formatter);
				} else {
					// 시간 정보가 없는 경우 LocalDate로 파싱 후 LocalDateTime으로 변환
					transactionDate = LocalDate.parse(transactionDateStr, formatter).atStartOfDay();
				}
				parsed = true;
				break; // 성공적으로 파싱되면 루프 종료
			} catch (DateTimeParseException e) {
				// 파싱 실패 시 다음 형식 시도
				continue;
			}
		}

		if (!parsed) {
			throw new IllegalArgumentException(
				"영수증 날짜 형식이 유효하지 않습니다. 지원되는 형식: yyyy-MM-dd, yyyy/MM/dd, dd-MM-yyyy, dd/MM/yyyy, MM-dd-yyyy, MM/dd/yyyy 등");
		}

		return transactionDate;
	}

	/**
	 * 아이템 목록을 추출하는 메소드
	 *
	 * @param itemsNode Items 필드의 JsonNode
	 * @return ItemDTO 객체 리스트
	 */
	private List<ItemDTO> extractItems(JsonNode itemsNode) {
		if (itemsNode == null || !itemsNode.has("value")) {
			return new ArrayList<>();
		}

		JsonNode itemsArrayNode = itemsNode.path("value");
		if (!itemsArrayNode.isArray()) {
			return new ArrayList<>();
		}

		return StreamSupport.stream(itemsArrayNode.spliterator(), false)
			.map(this::parseItem)
			.filter(item -> item != null)
			.collect(Collectors.toList());
	}

	/**
	 * 개별 아이템을 파싱하는 메소드
	 *
	 * @param itemNode 아이템의 JsonNode
	 * @return ItemDTO 객체 또는 null
	 */
	private ItemDTO parseItem(JsonNode itemNode) {
		try {
			JsonNode itemFields = itemNode.path("value");
			if (itemFields.isMissingNode()) {
				log.warn("아이템의 'value' 필드가 누락되었습니다.");
				return null;
			}

			String description = getFieldValueAsString(itemFields, "Description");
			Double quantity = getFieldValueAsDouble(itemFields, "Quantity");
			Double price = getFieldValueAsDouble(itemFields, "Price");
			Double totalPrice = getFieldValueAsDouble(itemFields, "TotalPrice");

			if (description == null) {
				log.warn("아이템의 'Description' 필드가 누락되었습니다.");
				return null;
			}

			ItemDTO item = new ItemDTO();
			item.setDescription(description);
			item.setQuantity(quantity != null ? quantity : 0.0);
			item.setPrice(price != null ? price : 0.0);
			item.setTotalPrice(totalPrice != null ? totalPrice : 0.0);

			return item;
		} catch (Exception e) {
			log.error("아이템 파싱 중 오류 발생: {}", e.getMessage());
			return null;
		}
	}

	/**
	 * 특정 필드의 문자열 값을 추출하는 헬퍼 메소드
	 *
	 * @param fieldsNode 부모 JsonNode
	 * @param fieldName  추출할 필드 이름
	 * @return 필드의 문자열 값 또는 null
	 */
	private String getFieldValueAsString(JsonNode fieldsNode, String fieldName) {
		JsonNode fieldNode = fieldsNode.get(fieldName);
		if (fieldNode != null && fieldNode.has("value")) {
			return fieldNode.get("value").asText(null);
		}
		return null;
	}

	/**
	 * 특정 필드의 Double 값을 추출하는 헬퍼 메소드
	 *
	 * @param fieldsNode 부모 JsonNode
	 * @param fieldName  추출할 필드 이름
	 * @return 필드의 Double 값 또는 null
	 */
	private Double getFieldValueAsDouble(JsonNode fieldsNode, String fieldName) {
		JsonNode fieldNode = fieldsNode.get(fieldName);
		if (fieldNode != null && fieldNode.has("value")) {
			return fieldNode.get("value").asDouble();
		}
		return null;
	}

}
