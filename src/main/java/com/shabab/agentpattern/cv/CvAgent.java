package com.shabab.agentpattern.cv;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CvAgent {
    private final ChatClient.Builder builder;

private final PdfUtils pdfUtils;

    public CvAgent(ChatClient.Builder builder, PdfUtils pdfUtils) {
        this.builder = builder;

        this.pdfUtils = pdfUtils;
    }

    public String start(String filePath,String jobDescription ) throws MalformedURLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        List<Document> docsFromPdfWithCatalog = pdfUtils.getDocsFromPdfWithCatalog(filePath);
String val=docsFromPdfWithCatalog.toString();
        String prompt="""
                 وظیفه شما ارزیابی یک رزومه براساس آگهی شغلی زیر است.
 
                 --- آگهی شغلی ---
                 %s
 
                 --- رزومه ---
                 %s
 
                 آیا این فرد واجد شرایط این شغل هست؟ لطفاً پاسخ را به صورت زیر بده:
                 - آیا واجد شرایط است؟ (بله/خیر)
                 - دلیل/توضیح
                 - نقاط قوت و ضعف
                 - پیشنهادهایی برای بهبود
 
                 پاسخ را به زبان فارسی بنویس.
                 """.formatted(jobDescription, val);


        var client = builder.build();
        var response = client.prompt(prompt).call().content();
        System.out.println(response);
        return response;

    }



}
