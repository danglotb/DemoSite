package com.community;

import com.community.controller.StartStopListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.config.EnableBroadleafSiteAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableAutoConfiguration
public class SiteApplication {

    @Configuration
    @EnableBroadleafSiteAutoConfiguration
    public static class BroadleafFrameworkConfiguration {
    }

    protected static final Log LOG = LogFactory.getLog(SiteApplication.class);

    public static void main(String[] args) {
        LOG.info("===========================================================");
        LOG.info("=========               START-UP         ==================");
        LOG.info("===========================================================");
        SpringApplication.run(SiteApplication.class, args);
        LOG.info("===========================================================");
        LOG.info("=========               RUN DONE         ==================");
        LOG.info("===========================================================");
        StartStopListener.init();
    }

}
