package com.scit45.kiminomenyuwa.service.verification;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.azure.core.util.polling.SyncPoller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scit45.kiminomenyuwa.domain.dto.ItemDTO;
import com.scit45.kiminomenyuwa.domain.dto.ReceiptDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReceiptRecognitionService {
	private final DocumentAnalysisClient client;
	private final ObjectMapper objectMapper;

	public ReceiptRecognitionService(
		@Value("${azure.formrecognizer.endpoint}") String endpoint
		, @Value("${azure.formrecognizer.apikey}") String key
		, ObjectMapper objectMapper) {
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

	// 2. 결과를 파싱하여 필요한 정보를 추출하는 메소드
	public ReceiptDTO parseReceiptJson(String jsonString) {
		try {
			JsonNode rootNode = objectMapper.readTree(jsonString);

			// documents 배열 접근
			JsonNode documentsNode = rootNode.path("documents");
			if (documentsNode.isArray() && !documentsNode.isEmpty()) {
				// 첫 번째 영수증만 사용한다고 가정
				JsonNode receiptNode = documentsNode.get(0);
				ReceiptDTO receipt = new ReceiptDTO();

				// 영수증 유형
				String receiptType = receiptNode.path("docType").asText(null);
				if (receiptType != null) {
					receipt.setReceiptType(receiptType);
				}

				// 필드 접근
				JsonNode fieldsNode = receiptNode.path("fields");

				// 가게 이름
				String merchantName = getFieldValueAsString(fieldsNode, "MerchantName");
				if (merchantName != null) {
					receipt.setMerchantName(merchantName);
				}

				// 거래 날짜
				String transactionDate = getFieldValueAsString(fieldsNode, "TransactionDate");
				if (transactionDate != null) {
					receipt.setTransactionDate(transactionDate);
				}

				// 아이템 목록
				JsonNode itemsNode = fieldsNode.path("Items").path("value");
				if (itemsNode.isArray()) {
					List<ItemDTO> itemsList = new ArrayList<>();
					for (JsonNode itemNode : itemsNode) {
						ItemDTO item = new ItemDTO();
						JsonNode itemFields = itemNode.path("value");

						// 상품명
						String description = getFieldValueAsString(itemFields, "Description");
						if (description != null) {
							item.setDescription(description);
						}

						// 수량
						Double quantity = getFieldValueAsDouble(itemFields, "Quantity");
						if (quantity != null) {
							item.setQuantity(quantity);
						}

						// 단가
						Double price = getFieldValueAsDouble(itemFields, "Price");
						if (price != null) {
							item.setPrice(price);
						}

						// 총액
						Double totalPrice = getFieldValueAsDouble(itemFields, "TotalPrice");
						if (totalPrice != null) {
							item.setTotalPrice(totalPrice);
						}

						itemsList.add(item);
					}
					receipt.setItems(itemsList);
				}

				// 총액
				Double total = getFieldValueAsDouble(fieldsNode, "Total");
				if (total != null) {
					receipt.setTotalPrice(total);
				}

				return receipt;
			} else {
				return null; // 영수증 데이터가 없는 경우
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null; // 파싱 중 오류 발생 시
		}
	}

	private String getFieldValueAsString(JsonNode parentNode, String fieldName) {
		JsonNode fieldNode = parentNode.path(fieldName);
		if (!fieldNode.isMissingNode()) {
			JsonNode valueNode = fieldNode.path("value");
			if (!valueNode.isMissingNode()) {
				return valueNode.asText(null);
			}
		}
		return null;
	}

	private Double getFieldValueAsDouble(JsonNode parentNode, String fieldName) {
		JsonNode fieldNode = parentNode.path(fieldName);
		if (!fieldNode.isMissingNode()) {
			JsonNode valueNode = fieldNode.path("value");
			if (!valueNode.isMissingNode() && valueNode.isNumber()) {
				return valueNode.asDouble();
			}
		}
		return null;
	}
}
