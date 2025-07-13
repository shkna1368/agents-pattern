package com.shabab.agentpattern;






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

            multiAgentSupervisor.run();

        };
    }
}