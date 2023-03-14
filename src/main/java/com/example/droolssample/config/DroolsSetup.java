package com.example.droolssample.config;

import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DroolsSetup {
    private static final String RULES_PATH = "com.example.droolssample/rules";

    private KieServices kieServices = KieServices.Factory.get();

    public KieFileSystem kieFileSystem() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        List<String> knownRules = Arrays.asList("suggest_rule.drl");
        for (String rule: knownRules) {
            kieFileSystem.write(ResourceFactory.newClassPathResource("rules/" + rule));
        }

        return kieFileSystem;
    }

    @Bean
    public KieContainer kieContainer() throws IOException {
        KieRepository kieRepository = kieServices.getRepository();

        kieRepository.addKieModule(new KieModule() {
            @Override
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });

        KieBuilder kieBuilder = kieServices
                .newKieBuilder(kieFileSystem())
                .buildAll();

        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }


    public KieSession kieSession() throws IOException {
        return kieContainer().newKieSession();
    }
}
