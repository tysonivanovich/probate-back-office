package uk.gov.hmcts.probate.config;

import com.launchdarkly.sdk.server.LDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LDClientConfiguration {
    @Bean
    public LDClient ldClient(@Value("${ld.sdk_key}") String ldSdkKey) {
       return new LDClient(ldSdkKey);
    }
}