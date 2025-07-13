package com.shabab.agentpattern.plan_and_exeute;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.shabab.agentpattern.config.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PlanAndExecuteAgent {
    private final ChatClient.Builder builder;

    private final DateTimeTools dateTimeTools;


    public PlanAndExecuteAgent(ChatClient.Builder builder, DateTimeTools dateTimeTools) {
        this.builder = builder;
        this.dateTimeTools = dateTimeTools;
    }

    public String planAndExecute(String userGoal) throws JsonProcessingException {
        String prompt = "Given the goal: \"" + userGoal + "\", break it down into a numbered list of steps. " +
                "For each step, specify the tool to use  and the parameters as a JSON object and step name " +
                "Format: Step X: <tool> <params as JSON>.give final result in json  array format like below:" +

                """
                         [
                        {
                        "stepName": "stepName",
                        "tool": "reserveFlight",
                        "parameters": {"origin": "NC", "destination": "London"}
                        }
                        
                        ]
                        """;


        var client = builder.build();
        //  var response = client.prompt("Plan travel from NC to Paris with hotel and recommend place to visit ")
        var response = client.prompt(prompt)
                //var response = client.prompt("get tables and make reports")
                .tools(dateTimeTools)
                .call()
                .content();
        System.out.println(response);


        Pattern pattern = Pattern.compile("(?s)```json\\s*(.*?)\\s*```");
        Matcher matcher = pattern.matcher(response);


        var aiResponse = matcher.group(1).trim();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode steps = (ArrayNode) mapper.readTree(aiResponse);

        StringBuilder stringBuilder = new StringBuilder();
        for (JsonNode element : steps) {

            String promptSteps = """
                    based on user goal : {%s}
                    there are these steps: {%s}
                     for step : {%s}
                     call corresponding tool.
                    
                    """.formatted(userGoal, aiResponse, element.toString());
            var response2 = client.prompt(promptSteps)
                    //var response = client.prompt("get tables and make reports")
                    .tools(dateTimeTools)
                    .call()
                    .content();
            stringBuilder.append(response2);


        }


        System.out.println(stringBuilder.toString());


        String promptFinal = """
                You are text editor.edit below text :
                based on user goal : %s
                
                the result is : %s
                
                """.formatted(userGoal, stringBuilder.toString());

        var responseFinal = client.prompt(promptFinal)
                //var response = client.prompt("get tables and make reports")
                .call()
                .content();



        return responseFinal;
    }
}



