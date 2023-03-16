package com.example.droolssample.config;

import jakarta.annotation.PostConstruct;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

@Configuration
public class DroolsSetup {
    private static final String INITIAL_RULE_FILE = "rules/suggest_rule.drl";
    private static final String SOURCES_PATH = "src/main/resources/";

    private final HashMap<String, String> knownRules = new HashMap<>();
    private final KieServices kieServices = KieServices.Factory.get();
    private KieContainer kieContainer = null;

    @PostConstruct
    private void initialSetup() throws IOException {
        File ruleFile = new ClassPathResource(INITIAL_RULE_FILE).getFile();
        String ruleText = new String(Files.readAllBytes(ruleFile.toPath()));

        knownRules.put(SOURCES_PATH + INITIAL_RULE_FILE, ruleText);
    }

    private KieFileSystem kieFileSystem() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        this.knownRules.forEach((filePath, ruleText) -> {
            System.out.println("Loading rule: " + filePath);
            System.out.println(ruleText);
            kieFileSystem.write(filePath, ruleText);
        });

        return kieFileSystem;
    }


    private void buildKieContainer() {
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

        this.kieContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    public KieSession kieSession() {
        if (this.kieContainer == null) this.buildKieContainer();

        return this.kieContainer.newKieSession();
    }

    public String getSuggestionRule() {
        return this.knownRules.get(SOURCES_PATH + INITIAL_RULE_FILE);
    }

    public void setSuggestionRule(String newRuleText) {
        this.knownRules.put(SOURCES_PATH + INITIAL_RULE_FILE, newRuleText);
        this.buildKieContainer();
    }
}
