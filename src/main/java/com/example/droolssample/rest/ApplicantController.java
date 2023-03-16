package com.example.droolssample.rest;

import com.example.droolssample.config.DroolsSetup;
import com.example.droolssample.model.Applicant;
import com.example.droolssample.model.SuggestedRole;
import org.json.JSONObject;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("applicant")
public class ApplicantController {

    @Autowired
    DroolsSetup droolsSetup;

    @GetMapping("suggestRole")
    public SuggestedRole suggestRole(@RequestBody Applicant applicant) {

        KieSession kieSession = droolsSetup.kieSession();
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

    @GetMapping("suggestRole/rule")
    public String getRule() {

        return this.droolsSetup.getSuggestionRule();
    }

    @PutMapping("suggestRole/rule")
    public String updateRule(@RequestBody String newRuleBody) {

        this.droolsSetup.setSuggestionRule(newRuleBody);

        return "OK";
    }

}
