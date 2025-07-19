package com.shabab.agentpattern.browseragent;

import com.microsoft.playwright.ElementHandle;
import lombok.Data;

import java.util.Optional;
@Data
public class Component {

    String elementId;
    // گرفتن placeholder و name
    String placeholder ;
    String nameAttr ;
    String labelText;
    String userData;
}
