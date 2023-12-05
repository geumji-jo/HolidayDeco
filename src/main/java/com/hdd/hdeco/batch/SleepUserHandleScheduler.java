package com.hdd.hdeco.batch;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hdd.hdeco.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableScheduling
@Component
public class SleepUserHandleScheduler {

	private final UserService userService;

	//(cronMaker.com)(크론메이커에서 만들어준 크론은 마지막 *(에스터리스크)는 빼고 적기)
	// 매일밤 12시에 sleepUserHandler작동
	//@Scheduled(cron = "0 0 12 1/1 * ?")
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void execute() {
		userService.sleepUserHandle();
	}

}
