package com.shabab.agentpattern.appgenerator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AppGeneratorAgent {
    private final ChatClient.Builder builder;



    public AppGeneratorAgent(ChatClient.Builder builder) {
        this.builder = builder;

    }

    public String start(String userGoal ) throws MalformedURLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {
        String prompt = """
                You are a senior software analyst.
                
                Based on the following high-level user goal and decisions: %s
       
generate a **complete and detailed software requirement specification (SRS)** for the development team, including both **backend and frontend** perspectives.

Your output must clearly define:

- Functional requirements (per feature)
- Non-functional requirements
- API endpoints and expected request/response structures (JSON)
- UI components and screens needed
- User flows and interactions
- Database schema overview
- Technologies to be used (Spring Boot for backend, Vaadin Flow or modern web framework for frontend)
- Suggested project structure (folders/layers)

Format your output as a well-structured JSON or markdown document that the development team can immediately start working from. Do not add any extra explanation or justification.    
        
        """.formatted(userGoal);


        var client = builder.build();
        var response = client.prompt(prompt).call().content();

        String promptDeveloper = """
                You are a senior java backend developer.
                
                Based on the user goal: %s 
                And software analysis: %s 
                
                Generate a complete backend project using **Spring Boot (latest version)** for the backend without security.
                Ensure the backend is production-ready and fully functional, not just sudo code .                
                Use **PostgreSQL** as the database and configure it in `application.yml`.
                
                The output should include:
                - All backend source code (controllers, services, repositories, entities, DTOs) for each feature domain model
                - Configuration files (e.g., `application.yml`)            
                - Maven or Gradle build file
                - Use best practices and layered architecture
                
                
                
                Return the entire project as an array of JSON objects with the following format for each object and duplicate projectName for each object **projectName start with lowercase and without space between characters:
        {
        "fileNameFormat":"User.java"
        "filePathFormat":"src/controller/User.java"
        content:"source code"
        "projectName":"nameOfProject"
        }
         
         Do not add any explanations or extra text. Just return the JSON array containing all source files and configurations.
                
        
        """.formatted(userGoal,response);

        var responseDevelopment = client.prompt(promptDeveloper).call().content();
        Pattern pattern = Pattern.compile("```json\\s*(.*?)\\s*```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(responseDevelopment);
        String srcBackend = null;
        if (matcher.find()) {
            srcBackend = matcher.group(1);
        }
   Map<String,String> pathServices=    writeCode(srcBackend);
       String  backendPath=pathServices.get("backendPath");
       String services=pathServices.get("servicelayer");





        String promptFrontDeveloper = """
              You are a senior frontend engineer.
                
                      Based on the user goal: %s 
                      And software analysis: %s 
                      And backend service layers: %s
                
                      Generate the **complete frontend code** using **Vaadin Flow (latest version)** without security.
                      Ensure the frontend is production-ready and fully functional, not just UI pages.
                
                      Your output must include:
                
                      - All UI views (pages) and navigation logic
                      - Form components with full validation rules
                      - Maven or Gradle build file
                      - Communication with backend via REST API or service layer (e.g., using Vaadin's service calls or Java HttpClient)
                      - State management if needed (e.g., session-scoped beans, UI state logic)
                      - Routing configuration
                      - Component reuse (split complex views into reusable components)
                      - Exception handling and user feedback (notifications, loading indicators, etc.)
                      - Assets, theming, layout structure (e.g., `MainLayout`, menus, headers, etc.)
                      - Project structure (folders and file names)
                      
                
                      Use the following JSON array format for output:
                
                     
                      {
                        "fileNameFormat": "DashboardView.java",
                        "filePathFormat": "src/main/java/com/example/app/views/DashboardView.java",
                        "content": "full source code here",
                        "projectName": "bankingAppFrontend"
                      }
         
         Do not add any explanations or extra text. Just return the JSON array containing all source files and configurations.
                
        
        """.formatted(userGoal,response,services);
Thread.sleep(5000);
        var responseFrontDevelopment = client.prompt(promptFrontDeveloper).call().content();
        Pattern patternFront = Pattern.compile("```json\\s*(.*?)\\s*```", Pattern.DOTALL);
        Matcher matcherFront = patternFront.matcher(responseFrontDevelopment);
        String srcFront = null;
        if (matcherFront.find()) {
            srcFront = matcherFront.group(1);
        }

       writeFront(srcFront,backendPath+"\\views\\");

        return "extracted";

    }




    public  Map<String,String> writeCode(String code) {
        Map<String,String> pathServices=new HashMap<>();
         StringBuilder serviceLayers=new StringBuilder();
        JSONArray files = new JSONArray(code);
String basePath="C:\\generated\\"+System.currentTimeMillis()+"\\"+files.getJSONObject(0).getString("projectName")+"\\";

        for (int i = 0; i < files.length(); i++) {
            JSONObject file = files.getJSONObject(i);
            String filePath = file.getString("filePathFormat");
            String content = file.getString("content");

            // Unescape line breaks (JSON has \\n, we want \n)
            content = content.replace("\\n", "\n").replace("\\\"", "\"");
              if(filePath.contains("service")){
                serviceLayers.append(content +'\n'+'\n')  ;
              }

            try {
                Path path = Paths.get(basePath+filePath);
                Files.createDirectories(path.getParent()); // Ensure directory exists
                Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("Wrote file: " + filePath);
            } catch (IOException e) {
                System.err.println("Error writing file " + filePath + ": " + e.getMessage());
            }
        }
        pathServices.put("backendPath",basePath);
        pathServices.put("servicelayer",serviceLayers.toString());
        return pathServices;
    }

    public String writeFront(String code,String basePath) {
        JSONArray files = new JSONArray(code);

        for (int i = 0; i < files.length(); i++) {
            JSONObject file = files.getJSONObject(i);
            String filePath = file.getString("filePathFormat");
            String content = file.getString("content");

            // Unescape line breaks (JSON has \\n, we want \n)
            content = content.replace("\\n", "\n").replace("\\\"", "\"");

            try {
                Path path = Paths.get(basePath+filePath);
                Files.createDirectories(path.getParent()); // Ensure directory exists
                Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                System.out.println("Wrote file: " + filePath);
            } catch (IOException e) {
                System.err.println("Error writing file " + filePath + ": " + e.getMessage());
            }
        }
        return basePath;
    }



}
