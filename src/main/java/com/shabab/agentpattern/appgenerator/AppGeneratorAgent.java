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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AppGeneratorAgent {
    private final ChatClient.Builder builder;



    public AppGeneratorAgent(ChatClient.Builder builder) {
        this.builder = builder;

    }

    public String start(String userGoal ) throws MalformedURLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String prompt = """
        You are  software analysis
        Based  on the user goal : %s .
        generate requirement for development team consist of backend and front.
    
        
        """.formatted(userGoal);


        var client = builder.build();
        var response = client.prompt(prompt).call().content();

        String promptDeveloper = """
        You are developer
        Based  on the user goal : %s 
        and software analysis :%s
        
        generate complete spring boot backend  and vaadin flow project with postgres database in application.yml in json array format like blow format without any explanation.use last version of libraries.
        {
        "fileNameFormat":"User.java"
        "filePathFormat":"src/controller/User.java"
        content:"source code"
        "projectName":"nameOfProject"
        }
    
        
        """.formatted(userGoal,response);

        var responseDevelopment = client.prompt(promptDeveloper).call().content();
        Pattern pattern = Pattern.compile("```json\\s*(.*?)\\s*```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(responseDevelopment);
        String src = null;
        if (matcher.find()) {
            src = matcher.group(1);
        }
     /*   String src= """
[
  {
    "fileNameFormat": "pom.xml",
    "filePathFormat": "pom.xml",
    "content": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n\txsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n\t<modelVersion>4.0.0</modelVersion>\n\t<parent>\n\t\t<groupId>org.springframework.boot</groupId>\n\t\t<artifactId>spring-boot-starter-parent</artifactId>\n\t\t<version>3.2.3</version>\n\t\t<relativePath/> <!-- lookup parent from repository -->\n\t</parent>\n\t<groupId>com.example</groupId>\n\t<artifactId>shortlink</artifactId>\n\t<version>0.0.1-SNAPSHOT</version>\n\t<name>shortlink</name>\n\t<description>Shortlink project with Spring Boot and Vaadin</description>\n\t<properties>\n\t\t<java.version>17</java.version>\n\t\t<vaadin.version>24.3.9</vaadin.version>\n\t</properties>\n\t<dependencies>\n\t\t<dependency>\n\t\t\t<groupId>org.springframework.boot</groupId>\n\t\t\t<artifactId>spring-boot-starter-data-jpa</artifactId>\n\t\t</dependency>\n\t\t<dependency>\n\t\t\t<groupId>org.springframework.boot</groupId>\n\t\t\t<artifactId>spring-boot-starter-web</artifactId>\n\t\t</dependency>\n\t\t<dependency>\n\t\t\t<groupId>com.vaadin</groupId>\n\t\t\t<artifactId>vaadin-spring-boot-starter</artifactId>\n\t\t</dependency>\n\n\t\t<dependency>\n\t\t\t<groupId>com.h2database</groupId>\n\t\t\t<artifactId>h2</artifactId>\n\t\t\t<scope>runtime</scope>\n\t\t</dependency>\n\t\t<dependency>\n\t\t\t<groupId>org.springframework.boot</groupId>\n\t\t\t<artifactId>spring-boot-starter-test</artifactId>\n\t\t\t<scope>test</scope>\n\t\t</dependency>\n\t\t<dependency>\n\t\t\t<groupId>org.projectlombok</groupId>\n\t\t\t<artifactId>lombok</artifactId>\n\t\t\t<optional>true</optional>\n\t\t</dependency>\n\t</dependencies>\n\t<dependencyManagement>\n\t\t<dependencies>\n\t\t\t<dependency>\n\t\t\t\t<groupId>com.vaadin</groupId>\n\t\t\t\t<artifactId>vaadin-bom</artifactId>\n\t\t\t\t<version>${vaadin.version}</version>\n\t\t\t\t<type>pom</type>\n\t\t\t\t<scope>import</scope>\n\t\t\t</dependency>\n\t\t</dependencies>\n\t</dependencyManagement>\n\n\t<build>\n\t\t<plugins>\n\t\t\t<plugin>\n\t\t\t\t<groupId>org.springframework.boot</groupId>\n\t\t\t\t<artifactId>spring-boot-maven-plugin</artifactId>\n\t\t\t\t<configuration>\n\t\t\t\t\t<excludes>\n\t\t\t\t\t\t<exclude>\n\t\t\t\t\t\t\t<groupId>org.projectlombok</groupId>\n\t\t\t\t\t\t\t<artifactId>lombok</artifactId>\n\t\t\t\t\t\t</exclude>\n\t\t\t\t\t</excludes>\n\t\t\t\t</configuration>\n\t\t\t</plugin>\n\t\t\t<plugin>\n\t\t\t\t<groupId>com.vaadin</groupId>\n\t\t\t\t<artifactId>vaadin-maven-plugin</artifactId>\n\t\t\t\t<version>${vaadin.version}</version>\n\t\t\t\t<executions>\n\t\t\t\t\t<execution>\n\t\t\t\t\t\t<goals>\n\t\t\t\t\t\t\t<goal>prepare-frontend</goal>\n\t\t\t\t\t\t</goals>\n\t\t\t\t\t</execution>\n\t\t\t\t</executions>\n\t\t\t</plugin>\n\t\t</plugins>\n\t</build>\n\n</project>" ,
    "projectName": "shortlink"
  },
  {
    "fileNameFormat": "ShortlinkApplication.java",
    "filePathFormat": "src/main/java/com/example/shortlink/ShortlinkApplication.java",
    "content": "package com.example.shortlink;\n\nimport org.springframework.boot.SpringApplication;\nimport org.springframework.boot.autoconfigure.SpringBootApplication;\n\n@SpringBootApplication\npublic class ShortlinkApplication {\n\n\tpublic static void main(String[] args) {\n\t\tSpringApplication.run(ShortlinkApplication.class, args);\n\t}\n\n}",
    "projectName": "shortlink"
  },
  {
    "fileNameFormat": "Shortlink.java",
    "filePathFormat": "src/main/java/com/example/shortlink/entity/Shortlink.java",
    "content": "package com.example.shortlink.entity;\n\nimport jakarta.persistence.*;\nimport lombok.Data;\nimport java.time.LocalDateTime;\n\n@Entity\n@Data\npublic class Shortlink {\n\n    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n    private Long id;\n\n    @Column(nullable = false)\n    private String longUrl;\n\n    @Column(nullable = false, unique = true)\n    private String shortUrl;\n\n    private LocalDateTime createdAt;\n\n    private LocalDateTime updatedAt;\n\n    @PrePersist\n    public void prePersist() {\n        createdAt = LocalDateTime.now();\n        updatedAt = LocalDateTime.now();\n    }\n\n    @PreUpdate\n    public void preUpdate() {\n        updatedAt = LocalDateTime.now();\n    }\n}",
    "projectName": "shortlink"
  },
  {
    "fileNameFormat": "ShortlinkRepository.java",
    "filePathFormat": "src/main/java/com/example/shortlink/repository/ShortlinkRepository.java",
    "content": "package com.example.shortlink.repository;\n\nimport com.example.shortlink.entity.Shortlink;\nimport org.springframework.data.jpa.repository.JpaRepository;\n\npublic interface ShortlinkRepository extends JpaRepository<Shortlink, Long> {\n    Shortlink findByShortUrl(String shortUrl);\n}",
    "projectName": "shortlink"
  },
  {
    "fileNameFormat": "ShortlinkService.java",
    "filePathFormat": "src/main/java/com/example/shortlink/service/ShortlinkService.java",
    "content": "package com.example.shortlink.service;\n\nimport com.example.shortlink.entity.Shortlink;\nimport com.example.shortlink.repository.ShortlinkRepository;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.stereotype.Service;\nimport java.util.Random;\n\n@Service\npublic class ShortlinkService {\n\n    @Autowired\n    private ShortlinkRepository shortlinkRepository;\n\n    private static final String ALPHANUMERIC = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\";\n    private static final int SHORTLINK_LENGTH = 7;\n\n    public Shortlink generateShortLink(String longUrl) {\n        String shortUrl = generateShortUrl();\n        Shortlink shortlink = new Shortlink();\n        shortlink.setLongUrl(longUrl);\n        shortlink.setShortUrl(shortUrl);\n        return shortlinkRepository.save(shortlink);\n    }\n\n    public String generateShortUrl() {\n        Random random = new Random();\n        StringBuilder sb = new StringBuilder(SHORTLINK_LENGTH);\n        for (int i = 0; i < SHORTLINK_LENGTH; i++) {\n            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));\n        }\n        String shortUrl = sb.toString();\n        if (shortlinkRepository.findByShortUrl(shortUrl) != null) {\n            return generateShortUrl(); // Recursively generate a new one\n        }\n        return shortUrl;\n    }\n\n    public String getLongURL(String shortURL) {\n        Shortlink shortlink = shortlinkRepository.findByShortUrl(shortURL);\n        if(shortlink != null) {\n            return shortlink.getLongUrl();\n        }\n        return null;\n    }\n}",
    "projectName": "shortlink"
  },
  {
    "fileNameFormat": "ShortlinkController.java",
    "filePathFormat": "src/main/java/com/example/shortlink/controller/ShortlinkController.java",
    "content": "package com.example.shortlink.controller;\n\nimport com.example.shortlink.entity.Shortlink;\nimport com.example.shortlink.service.ShortlinkService;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.http.HttpStatus;\nimport org.springframework.http.ResponseEntity;\nimport org.springframework.web.bind.annotation.*;\nimport org.springframework.web.servlet.view.RedirectView;\n\n@RestController\npublic class ShortlinkController {\n\n    @Autowired\n    private ShortlinkService shortlinkService;\n\n    @PostMapping(\"/api/shorten\")\n    public ResponseEntity<String> shortenUrl(@RequestBody String longUrl) {\n        Shortlink shortlink = shortlinkService.generateShortLink(longUrl);\n        return new ResponseEntity<>(\"http://localhost:8080/\" + shortlink.getShortUrl(), HttpStatus.OK);\n    }\n\n    @GetMapping(\"/{shortUrl}\")\n    public RedirectView redirectUrl(@PathVariable String shortUrl) {\n        String longURL = shortlinkService.getLongURL(shortUrl);\n\n        if(longURL != null) {\n            RedirectView redirectView = new RedirectView();\n            redirectView.setUrl(longURL);\n            return redirectView;\n        } else {\n            return null; // Or redirect to an error page\n        }\n    }\n}",
    "projectName": "shortlink"
  },
  {
    "fileNameFormat": "MainView.java",
    "filePathFormat": "src/main/java/com/example/shortlink/ui/MainView.java",
    "content": "package com.example.shortlink.ui;\n\nimport com.vaadin.flow.component.button.Button;\nimport com.vaadin.flow.component.notification.Notification;\nimport com.vaadin.flow.component.orderedlayout.VerticalLayout;\nimport com.vaadin.flow.component.textfield.TextField;\nimport com.vaadin.flow.router.Route;\nimport com.vaadin.flow.server.VaadinSession;\nimport org.springframework.http.MediaType;\nimport org.springframework.web.reactive.function.client.WebClient;\n\n@Route(\"\")\npublic class MainView extends VerticalLayout {\n\n    private TextField longUrlField = new TextField(\"Enter Long URL:\");\n    private Button shortenButton = new Button(\"Shorten\");\n    private TextField shortUrlField = new TextField(\"Short URL:\");\n\n    public MainView() {\n        shortUrlField.setReadOnly(true);\n        add(longUrlField, shortenButton, shortUrlField);\n\n        shortenButton.addClickListener(event -> {\n            String longUrl = longUrlField.getValue();\n            if (longUrl != null && !longUrl.isEmpty()) {\n                try {\n                    String shortUrl = shortenURL(longUrl);\n                    shortUrlField.setValue(shortUrl);\n                } catch (Exception e) {\n                    Notification.show(\"Error shortening URL: \" + e.getMessage());\n                }\n            } else {\n                Notification.show(\"Please enter a valid URL\");\n            }\n        });\n    }\n\n    private String shortenURL(String longUrl) {\n        WebClient client = WebClient.create(\"http://localhost:8080\");\n\n        String shortUrl = client.post()\n                .uri(\"/api/shorten\")\n                .contentType(MediaType.APPLICATION_JSON)\n                .bodyValue(longUrl)\n                .retrieve()\n                .bodyToMono(String.class)\n                .block();\n\n        return shortUrl;\n    }\n}",
    "projectName": "shortlink"
  }
]

                """;

*/
writeCode(src);

        return "extracted";

    }

    public void writeCode(String code) {
        JSONArray files = new JSONArray(code);
String basePath="C:\\generated\\"+System.currentTimeMillis()+"\\"+files.getJSONObject(0).getString("projectName")+"\\";

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
    }



}
