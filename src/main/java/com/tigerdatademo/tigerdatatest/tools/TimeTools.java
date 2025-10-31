package com.tigerdatademo.tigerdatatest.tools;

import java.time.LocalTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;


@Component
public class TimeTools {

	private static final Logger logger = LoggerFactory.getLogger(TimeTools.class);  

	
	/*Expose a method as a Tool, which provides specific real-time data
	 * Questions on time related my location/India comes to this method
	 * */
	@Tool(name= "getCurrentLocalTime", description="Get the current local time as requested by User")
	public String getCurrentLocalTime() {		
		logger.info("getCurrentLocalTime(): Current local time:" + LocalTime.now().toString());		
		return LocalTime.now().toString();
	}
	
	
	  /*Expose a method as a Tool, which provides specific real-time data
	   * Questions on time related of non-Indian cities comes to this method
	   * */	
	  @Tool(name= "getCurrentTime",description="Get the specific time in specified Time zone") 
	  public String getCurrentTime(@ToolParam(description ="Value representing timezone") String
	  timezone) { 
		  
		//timezone value is empty, I gather LLM reads the place and generates the timevalue like AEST/IST
		  logger.info("getCurrentTime(): The timezone asked is:",timezone.toString()); 
		  return LocalTime.now(ZoneId.of(timezone)).toString(); }
	 
}
