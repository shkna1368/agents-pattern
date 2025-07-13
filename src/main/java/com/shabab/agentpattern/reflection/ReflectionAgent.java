package com.shabab.agentpattern.reflection;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;

@Service
public class ReflectionAgent {

    private final ChatClient generateChatClient;

    private final ChatClient critiqueChatClient;

    public ReflectionAgent(ChatClient.Builder generatorBuilder, ChatClient.Builder critiqueBuilder) {

        generateChatClient= generatorBuilder.defaultSystem("  You are a Java programmer tasked with generating high quality Java code.\n" +
                        "                        Your task is to Generate the best content possible for the user's request. If the user provides critique,\n" +
                        "                        respond with a revised version of your previous attempt.")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build()).build())
                .build();


        critiqueChatClient=   critiqueBuilder.defaultSystem("""
                        You are tasked with generating critique and recommendations to the user's generated content.
                        If the user content has something wrong or something to be improved, output a list of recommendations
                        and critiques. If the user content is ok and there's nothing to change, output this: <OK>
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build()).build())
                .build();


    }


        public String run(String userQuestion, int maxIterations) {

            String generation = generateChatClient.prompt(userQuestion).call().content();
            System.out.println("##generation\n\n" + generation);
            String critique;
            for (int i = 0; i < maxIterations; i++) {

                critique = critiqueChatClient.prompt(generation).call().content();

                System.out.println("##Critique\n\n" + critique);
                if (critique.contains("<OK>")) {
                    System.out.println("\n\nStop sequence found\n\n");
                    break;
                }
                generation = generateChatClient.prompt(critique).call().content();
            }
            return generation;

        }


}
