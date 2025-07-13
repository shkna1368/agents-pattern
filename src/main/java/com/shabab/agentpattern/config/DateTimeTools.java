package com.shabab.agentpattern.config;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class DateTimeTools {

    @Tool(description = "Get the current date and time in the user's timezone")
    String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(description = "Get total balance")
    String getTotalBalance() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(description = "Set a user alarm for the given time")
    void setAlarm(@ToolParam(description = "Time in ISO-8601 format") String time) {
        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("Alarm set for " + alarmTime);
    }
    @Tool(description = "Reserve flight")
    String reserveFlight(@ToolParam(description = "Origin of trip") String origin,@ToolParam(description = "destination of travel") String destination) {
        return "flight from  " +origin+" -"+destination;
    }

    @Tool(description = "Reserve hotel")
    String reserveHotel() {
        return " MB Hotel reserve";
    }

    @Tool(description = "place to visit")
    String placeToVisit() {
        return "ifel,park ,river";
    }

    @Tool(description = "get all tables")
    List<String> getTables() {
        return List.of("users","transactions");


    }

    @Tool(description = "generate reports based on tables")
    String makeSuggestionReport(@ToolParam(description = "List of tables") List<String> tables) {
        return "users,iptv ,transaction";
    }



}