package com.shabab.agentpattern.multi_agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class PoAgent {
    private final ChatClient.Builder builder;



    public PoAgent(ChatClient.Builder builder) {
        this.builder = builder;
    }
    public String start(String userGoal){
        String prompt = "You are product owner .based on the goal: \"" + userGoal + "\", make tasks for developers ";


        var client = builder.build();
        //  var response = client.prompt("Plan travel from NC to Paris with hotel and recommend place to visit ")
        var response = client.prompt(prompt).call().content();
        return response;

    }





}
