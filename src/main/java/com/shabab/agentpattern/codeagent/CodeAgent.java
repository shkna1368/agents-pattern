package com.shabab.agentpattern.codeagent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CodeAgent {
    private final ChatClient.Builder builder;



    public CodeAgent(ChatClient.Builder builder) {
        this.builder = builder;

    }

    public String start(String userGoal ) throws MalformedURLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String prompt = """
        You are java  developer.if its required set value in corresponding function parameter.
        Based  on the user goal : %s 
        
        generate code in java contains class which name is CodeAgentTest and function  without any explanation.
      the above code is string.now generate another function that name is generateAndRun without any parameter  for run  first function  which is string with all requirements and random parameter in runtime by reflection.generateAndRun return corresponding data type.Dont include main method in class.
      
    
        
        """.formatted(userGoal);


        var client = builder.build();
        var response = client.prompt(prompt).call().content();

        Pattern pattern = Pattern.compile("```java\\s*(.*?)\\s*```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);
        String extracted = null;
        if (matcher.find()) {
            extracted = matcher.group(1);
        }

        run3(extracted);
        return extracted;

    }

    public String startRequest(String userGoal ) throws MalformedURLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String prompt = """
        You are java  developer. based on user goal generate java code.set value in corresponding function parameter.
        goal:%s
        
        generate code in java contains class which name is CodeAgentTest and function  without any explanation.
      the above code is string.now generate another function that name is generateAndRun without any parameter  for run  first function  which is string with all requirements and random parameter in runtime by reflection.generateAndRun return corresponding data type.Dont include main method in class.
      
    
        
        """.formatted(userGoal);


        var client = builder.build();
        var response = client.prompt(prompt).call().content();

        Pattern pattern = Pattern.compile("```java\\s*(.*?)\\s*```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);
        String extracted = null;
        if (matcher.find()) {
            extracted = matcher.group(1);
        }

        run3(extracted);
        return extracted;

    }

  /*  public void runJavaCode(String javaCode)throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException {
        String sourceCode = "public class MyRuntimeClass { public void greet(int x) { System.out.println(x+\"Hello from runtime code!\"); } }";
        File javaFile = new File("MyRuntimeClass.java");

        try (FileWriter writer = new FileWriter(javaFile)) {
            writer.write(sourceCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, javaFile.getPath());

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(".").toURI().toURL()});
        Class<?> dynamicClass = classLoader.loadClass("CodeAgentTest");
        Object instance = dynamicClass.getDeclaredConstructor().newInstance();
        Method method = dynamicClass.getMethod("greet",int.class);
        method.invoke(instance);

        javaFile.delete(); // Clean up the temporary file
        new File("MyRuntimeClass.class").delete(); // Clean up the compiled class file
    }*/

    public void run3(String code) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, MalformedURLException {
        String className = "CodeAgentTest";
        String methodName = "generateAndRun";

        // Compile source code in-memory

      String calssFile=className+".java";
        File javaFile = new File(calssFile);

        try (FileWriter writer = new FileWriter(javaFile)) {
            writer.write(code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, javaFile.getPath());

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(".").toURI().toURL()});
        Class<?> dynamicClass = classLoader.loadClass(className);
        Object instance = dynamicClass.getDeclaredConstructor().newInstance();
        Method method = dynamicClass.getMethod(methodName);
        Object result =   method.invoke(instance);

        javaFile.delete(); // Clean up the temporary file
        new File(className+".class").delete(); // Clean up the compiled class file
        System.out.println("Result: " + result.toString());







    /*    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileObject file = new JavaSourceFromString("CodeAgentTest", code);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        compiler.getTask(null, null, null, null, null, compilationUnits).call();
        // Load and invoke compiled class
        Class<?> cls = Class.forName("CodeAgentTest");

        Method method = cls.getMethod("generateAndRun");
        Object result = method.invoke(method);*/

        System.out.println(result);



    }

}
