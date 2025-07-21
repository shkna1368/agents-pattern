package com.shabab.agentpattern.voicetotext;

import com.shabab.agentpattern.cv.PdfUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.document.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoiceToTextAgent {
    private final ChatClient.Builder builder;


    public VoiceToTextAgent(ChatClient.Builder builder, PdfUtils pdfUtils) {
        this.builder = builder;


    }

    public String start(String filePath) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        ClassPathResource res= new ClassPathResource(filePath);
        Media media=new Media(MimeTypeUtils.parseMimeType("audio/mp3"), res);
 List<Media> mediaList=new ArrayList<>();
 mediaList.add(media);;
        var userMessage=UserMessage.builder()
                .text("Give me text of voice without any explanation in english.if the result is not english translate to english then return.your input voice is Persian or English.")
                .media(mediaList)
                .build();




        //ChatResponse response = chatModel.call(new Prompt(List.of(this.userMessage)));


        var client = builder.build();

        var response = client.prompt(new Prompt(List.of(userMessage))).call().content();
        System.out.println(response);
        return response;

    }

public String startMeeting(String filePath) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        ClassPathResource res= new ClassPathResource(filePath);
        Media media=new Media(MimeTypeUtils.parseMimeType("audio/mp3"), res);
 List<Media> mediaList=new ArrayList<>();

 String prompt= """
         Your input voice is Persian or English.
         
         
        just give me important decisions were made without any explanation;
      
         give me result in English.
         """;
 mediaList.add(media);;
        var userMessage=UserMessage.builder()
                .text(prompt)
                .media(mediaList)
                .build();




        //ChatResponse response = chatModel.call(new Prompt(List.of(this.userMessage)));


        var client = builder.build();

        var response = client.prompt(new Prompt(List.of(userMessage))).call().content();
        System.out.println(response);
        return response;

    }



}
