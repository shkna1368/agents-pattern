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
     "Receive an audio input in either Persian or English.
       Detect the language of the input.
        Based on the spoken content, generate a relevant app idea.
        Output only the app name and a list of its essential features in English.
       Do not provide any explanation or translation.
         
  
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


    public String startInterview(String filePath) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        ClassPathResource res= new ClassPathResource(filePath);
        Media media=new Media(MimeTypeUtils.parseMimeType("audio/mp3"), res);
        List<Media> mediaList=new ArrayList<>();

        String prompt= """
     "
       You are a technical interview analysis assistant. I will provide you with an audio file of a technical interview.Receive an audio input in either Persian or English.
        Please do the following:
                
                  1. Transcribe the audio file into text (speech-to-text).
                  2. Extract each technical question and the candidate’s answer from the transcript.
                  3. Evaluate each answer and determine whether it is correct or incorrect based on the content.
                  4. Provide a summary report including:
                     - Total number of questions asked
                     - Number of correct answers
                     - Number of incorrect answers
                     - Overall result: if more than 70% of answers are correct, mark as "Pass", otherwise "Fail".
                
                  Please receive the audio file and start the analysis.
       Do not provide any explanation or translation.
         
  
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

    public String startDailyScrum(String filePath) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        ClassPathResource res= new ClassPathResource(filePath);
        Media media=new Media(MimeTypeUtils.parseMimeType("audio/mp3"), res);
        List<Media> mediaList=new ArrayList<>();

        String prompt= """
     "
      You are a Daily Scrum meeting assistant. I will provide you with an audio recording of a Daily Scrum meeting.Receive an audio input in either Persian or English .Please do the following:
                
                
                 2. Identify and list all the tasks mentioned during the meeting.
                 3. For each task, determine its current status (e.g., To Do, In Progress, Done).
                 4. Identify any blockers or impediments mentioned by team members.
                 5. Summarize the overall progress of the team based on the tasks and blockers.
                 6. Provide a clear and concise report including:
                    - List of tasks with their statuses
                    - List of blockers
                    - Any important notes or action items discussed
                    
                
                 Please receive the audio file and start the analysis.Give me result in persian.
         
  
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
