package com.scit45.kiminomenyuwa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scit45.kiminomenyuwa.domain.entity.MeetingEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("meetings")
@Controller
public class MeetingController {
	/**
	 * 약속 설정 페이지 표시
	 */
	@GetMapping("/schedule")
	public String showScheduleForm(Model model) {
		model.addAttribute("meeting", new MeetingEntity());
		return "meetingView/schedule";
	}
}
