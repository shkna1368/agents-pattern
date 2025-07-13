package com.shabab.agentpattern.multi_agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MultiAgentSupervisor {

@Autowired
private DeveloperAgent codeAgent;

  @Autowired
  private PoAgent poAgent;



    public void run()  {

String prompt= """
              make short link project .
              """;
var poResult=poAgent.start(prompt);
var dvelResult=codeAgent.start(prompt,poResult);
      System.out.println(dvelResult);

    }



    }





