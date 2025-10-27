package com.tigerdatademo.tigerdatatest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;


/*22Aug25: Custom Class created from Udemy course
 * Section2:Custom Spring Advisors in AI
 * */

public class TokenUsageAuditAdvisor implements CallAdvisor{

	private static final Logger logger = LogManager.getLogger(TokenUsageAuditAdvisor.class);
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "TokenUsageAuditAdvisor";
	}

	
	@Override
	public int getOrder() {
		/*This advisor will be given highest priority*/
		return 1;
	}

	
	@Override
	public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
		ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
		ChatResponse chatResponse = chatClientResponse.chatResponse();
		
		if (chatResponse.getMetadata() != null) {
			
			Usage usage = chatResponse.getMetadata().getUsage();
			if (usage != null) {
				logger.info("Token Usage details:"+ usage.toString());
			}
		}
		return chatClientResponse;
	}

}
