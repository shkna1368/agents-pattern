package com.shabab.agentpattern.browseragent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import org.jsoup.nodes.FormElement;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BrowserAgent {
    private final ChatClient.Builder builder;



    public BrowserAgent(ChatClient.Builder builder) {
        this.builder = builder;

    }

    public void start(String userGoal ) throws MalformedURLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
      /*  String prompt = """
                find 1 valid link of site from below user request without any explanation 
                 Based  on the user goal : %s 
                  result like : http://google.com
                
                """.formatted(userGoal);


        var client = builder.build();
        var response = client.prompt(prompt).call().content();*/
        var response ="https://www.make-it-in-germany.com/en/service/contact-us/email";
        System.out.println("https://www.make-it-in-germany.com/en/service/contact-us/email");
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setArgs(List.of("--kiosk")));
            Page page = browser.newPage();
         var re=   page.navigate(response);

            page.waitForTimeout(1000);



            System.out.println(page.title());
            List<ElementHandle> inputs = page.querySelectorAll("input, textarea, select");

            List<Component> components=new ArrayList<>();
            for (ElementHandle input : inputs) {
                // گرفتن label مرتبط (اگر وجود داشته باشد)
                String labelText = "";
                String id = input.getAttribute("id");
                if (id != null) {
                    ElementHandle label = page.querySelector("label[for='" + id + "']");
                    if (label != null) {
                        labelText = label.innerText().toLowerCase();
                    }
                }
                // گرفتن placeholder و name
                String placeholder = Optional.ofNullable(input.getAttribute("placeholder")).orElse("").toLowerCase();
                String nameAttr = Optional.ofNullable(input.getAttribute("name")).orElse("").toLowerCase();
Component component=new Component();
component.setElementId(id);
component.setLabelText(labelText);
component.setPlaceholder(placeholder);
component.setNameAttr(nameAttr);
if (!"".equals(placeholder) ){
components.add(component);}
            }

   /*         String components= """
                    [Component(id=ctl13_ctl00_ctl07_ctl00__sbSearch_ctl00, placeholder=جستجو, nameAttr=ctl13:ctl00:ctl07:ctl00::sbsearch:ctl00, labelText=), Component(id=ctl13_ctl00_ctl07_ctl00__sbSearch_ctl01, placeholder=جستجو, nameAttr=, labelText=), Component(id=ctl13_ctl03_ctl00_Review4134_ctl00_txtName, placeholder= نام, nameAttr=ctl13$ctl03$ctl00$review4134$ctl00$txtname, labelText=), Component(id=ctl13_ctl03_ctl00_Review4134_ctl00_txtEmail, placeholder=ایمیل, nameAttr=ctl13$ctl03$ctl00$review4134$ctl00$txtemail, labelText=), Component(id=ctl13_ctl03_ctl00_Review4134_ctl00_txtComment, placeholder=نظر شما, nameAttr=ctl13$ctl03$ctl00$review4134$ctl00$txtcomment, labelText=)]
                    """;*/

           // page.waitForTimeout(5000);


           String user ="I'm Shabab koohi  and my email is 'sh.k.na.1368@gmail.com'.Im software developer.My degree is MA.My message is :Hi From kurdistan";

            String prompt = """
                based on page elements ,find corresponding element for correct and extracted user data  .extract correct user data and put in corresponding input.
                page elements :  %s
                
                user data: {%s}
                
                give result as json array as below  and dont  lowercase and uppercase json value:
               [
                {
                "elementId":"elementId",
                "placeholder":"elementId",
                "nameAttr":"elementId",
                "labelText":"elementId",
                
                "userData":""
                
                }
                ]
                
                """.formatted(components,user);
            var client = builder.build();
            var response2 = client.prompt(prompt).call().content();
            System.out.println(response2);
            Pattern pattern = Pattern.compile("```json\\s*(.*?)\\s*```", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(response2);
            String src = null;
            if (matcher.find()) {
                src = matcher.group(1);
            }

            System.out.println(src);



            ObjectMapper objectMapper = new ObjectMapper();

            List<Component> elements = objectMapper.readValue(src, new TypeReference<List<Component>>() {});


          page.evaluate(String.format("document.querySelector('#%s').scrollIntoView({behavior: 'smooth', block: 'center'})",elements.get(0).elementId));

            // Use the elements
            for (Component element : elements) {
                System.out.println(element.getElementId() + " - " + element.getUserData());
                try {
                    page.fill("#"+element.getElementId(), element.getUserData());

                }
                catch (Exception e){}

             //   page.getByPlaceholder(element.getElementId()).fill(element.getUserData());
            }
            page.waitForTimeout(20000);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}




