package com.shabab.agentpattern.multi_agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class DeveloperAgent {
    private final ChatClient.Builder builder;



    public DeveloperAgent(ChatClient.Builder builder) {
        this.builder = builder;

    }

    public String start(String userGoal ,String poTasks){
        String prompt = """
        You are java backend developer.
        Based  on the user goal : %s 
        and product owner tasks: %s
        
        generate code in java and spring boot
        
        """.formatted(userGoal,poTasks);


        var client = builder.build();
        //  var response = client.prompt("Plan travel from NC to Paris with hotel and recommend place to visit ")
        var response = client.prompt(prompt).call().content();
        return response;

    }

}
