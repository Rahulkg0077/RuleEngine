package com.hsbc.ruleengine.config;

import com.hsbc.ruleengine.service.PaymentService;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    private static final String DRL_FILE = "C:Users/10820651/Downloads/rules.drl";

    @Autowired
    private PaymentService paymentService;
    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        return kieServices.getKieClasspathContainer();
    }

    @Bean
    public KieSession kieSession() {
        KieSession kieSession = kieContainer().newKieSession("ksession-rules");
        kieSession.setGlobal("paymentService", paymentService);
        return kieSession;
    }

}