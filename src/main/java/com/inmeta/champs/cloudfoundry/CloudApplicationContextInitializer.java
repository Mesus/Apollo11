package com.inmeta.champs.cloudfoundry;

import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Guri Lunnan
 */

public class CloudApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext applicationContext) {
        CloudEnvironment env = new CloudEnvironment();
        if (env.getInstanceInfo() != null) {
            applicationContext.getEnvironment().setActiveProfiles("cloud");
        } else {
            applicationContext.getEnvironment().setActiveProfiles("default");
        }
    }

}
