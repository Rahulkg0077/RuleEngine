package com.hsbc.ruleengine.config;

import com.hsbc.ruleengine.service.PaymentService;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    private static final String DRL_FILE = "rules/FileRules.drl";

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        return kieServices.getKieClasspathContainer();
    }

    @Bean
    public KieSession kieSession() {
        KieSession kieSession = kieContainer().newKieSession("ksession-rules");
        PaymentService paymentService = new PaymentService();
        kieSession.setGlobal("paymentService", paymentService);
        return kieSession;
    }

}