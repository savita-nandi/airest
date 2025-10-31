package com.tigerdatademo.tigerdatatest.tools;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import com.tigerdatademo.tigerdatatest.entity.HelpDeskTicket;
import com.tigerdatademo.tigerdatatest.model.TicketRequest;
import com.tigerdatademo.tigerdatatest.service.HelpDeskTicketService;

import lombok.RequiredArgsConstructor;

/*The @RequiredArgsConstructor annotation from Lombok automatically generates a constructor 
 * with arguments for all final fields and any fields marked with @NonNull*/

@Service
@RequiredArgsConstructor
public class HelpDeskTools {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelpDeskTools.class);
	
	private final HelpDeskTicketService helpDeskTicketService;
	
	/*By setting returnDirect=true we are telling the framework not to send the method return
	 * data to LLM, but instead directly send to client. So in the browser we will not see
	 * the LLM message, instead the return type of method*/
	@Tool(name ="createTicket", description = "Create a support ticket", returnDirect = true)
	public String createTicket( @ToolParam(description = "Details to create a support ticket") 
		TicketRequest ticketRequest, ToolContext toolContext) {
		
		String username = (String)toolContext.getContext().get("username");
		LOGGER.info("createTicket(): Creating ticket details for username:" + username);
		
		HelpDeskTicket helpDeskTicket =  helpDeskTicketService.createTicket(ticketRequest, username);
		
		return "Saved Ticket#: " + helpDeskTicket.getId() + " for User: " + username;
	}
	
	
	
	@Tool(name ="getTicketStatus", description = "Get status of ticket")
	public List<HelpDeskTicket> getTicketStatus( @ToolParam(description = "Details to get ticket status") 
		ToolContext toolContext) {
		
		String username = (String)toolContext.getContext().get("username");
		LOGGER.info("getTicketStatus(): Fetching ticket details for username:" + username);
		
		List<HelpDeskTicket> helpDeskTicketList =  helpDeskTicketService.getTicketsByUsername(username);
		LOGGER.info("getTicketStatus(): helpDeskTicketList.sise:" + helpDeskTicketList.size());
		
		
		return helpDeskTicketList;
	}
	
}
