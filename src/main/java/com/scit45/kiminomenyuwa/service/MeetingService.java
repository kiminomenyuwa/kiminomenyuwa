package com.scit45.kiminomenyuwa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scit45.kiminomenyuwa.domain.entity.MeetingEntity;
import com.scit45.kiminomenyuwa.domain.repository.MeetingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MeetingService {

	private final MeetingRepository meetingRepository;

	public MeetingEntity createMeetingEntity(MeetingEntity meeting) {
		return meetingRepository.save(meeting);
	}

	public List<MeetingEntity> getAllMeetings() {
		return meetingRepository.findAll();
	}

	public MeetingEntity getMeetingEntityById(Integer meetingId) {
		return meetingRepository.findById(meetingId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid meeting ID: " + meetingId));
	}

	// 필요한 추가 메서드를 정의할 수 있습니다.
}
