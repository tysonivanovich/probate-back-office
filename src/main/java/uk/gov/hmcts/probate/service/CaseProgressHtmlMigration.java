package uk.gov.hmcts.probate.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@EnableAsync
public class CaseProgressHtmlMigration {

    @Scheduled(fixedDelay = 5000)
    @Async
    public void updateAppsWithMissingCaseProgressHtml() throws InterruptedException {

    }
}
