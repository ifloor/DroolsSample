package com.example.droolssample.rest;

import com.example.droolssample.model.Applicant;
import com.example.droolssample.model.SuggestedRole;
import org.json.JSONObject;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("applicant")
public class ApplicantController {

    @Autowired
    KieContainer kieContainer;

    @GetMapping("suggestRole")
    public SuggestedRole suggestRole(@RequestBody Applicant applicant) {

        KieSession kieSession = kieContainer.newKieSession();
        try {
            SuggestedRole suggestedRole = new SuggestedRole();

            kieSession.insert(applicant);
            kieSession.setGlobal("suggestedRole", suggestedRole);
            kieSession.fireAllRules();

            return suggestedRole;
        } finally {
            kieSession.dispose();
        }
    }
}
