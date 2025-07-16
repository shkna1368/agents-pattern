package com.shabab.agentpattern;






import com.shabab.agentpattern.appgenerator.AppGeneratorAgent;
import com.shabab.agentpattern.codeagent.CodeAgent;
import com.shabab.agentpattern.multi_agent.MultiAgentSupervisor;
import com.shabab.agentpattern.plan_and_exeute.PlanAndExecuteAgent;
import com.shabab.agentpattern.reflection.ReflectionAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class App {


    @Autowired
    PlanAndExecuteAgent planAndExecuteService;

    @Autowired
    MultiAgentSupervisor multiAgentSupervisor;


    @Autowired
    ReflectionAgent reflectionAgent;

    @Autowired
    AppGeneratorAgent appGeneratorAgent;

    @Autowired
    CodeAgent codeAgent;

    public static void main(String[] args) {

        SpringApplication.run(App.class, args);

    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {

            var userGoal = "cheap travel from NC to London consist hotel and visit famous place";
           // var userGoal = "cheap travel from NC to London without reserve hotel and visit famous place";

         //var result=   planAndExecuteService.planAndExecute(userGoal);

       //     System.out.println(result);


     //   var result=    reflectionAgent.run("generate find max and min function in java", 5);

        //    System.out.println(result);

       //     multiAgentSupervisor.run();

        // var code=   codeAgent.start("give me find max number in list of number ");
     //   var code1=   codeAgent.start("give me  function find 3 biggest number in list");
      //   var code=   codeAgent.startRequest("based on salary deduction formula : (salary-0.2)+(salary-0.5) .my salary is 2500 ");
         //   System.out.println(code);
        //   System.out.println(code1);

          //  appGeneratorAgent.start("make shortlink project with ui that convert long link to short link");
            appGeneratorAgent.start("make banking app support : open account,fund transfer,transaction histories,check balance and close account");
        };
    }
}