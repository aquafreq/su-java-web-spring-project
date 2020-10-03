package com.example.english.scheduler;

import com.example.english.service.LogService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserActivityScheduler {
    private final UserService userService;
    private final LogService logService;

    //per hour
    @Scheduled(fixedRate = 3600000, initialDelay = 10000)
    //    every day
//    @Scheduled(fixedRate = 864_000_00, initialDelay = 10000)
//    2 min just for demo purpose
//    @Scheduled(fixedRate = 120000, initialDelay = 10000)
    public void manageUserActivity(){
        userService.updateUserActivity();
        logService.clearBrowsingLogs();
        log.info("Done scheduling");
    }
}
