package com.hsbc.ruleengine.config;

import com.hsbc.ruleengine.service.PaymentService;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Configuration
public class DroolsConfig {

   @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        return kieServices.getKieClasspathContainer();
    }

    @Bean
    public KieSession kieSession() {
        KieSession kieSession = kieContainer().newKieSession("ksession-rules");
        PaymentService paymentService = new PaymentService();
//        kieSession.setGlobal("paymentService", paymentService);
        return kieSession;
    }

}