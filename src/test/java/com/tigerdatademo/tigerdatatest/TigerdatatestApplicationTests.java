package com.tigerdatademo.tigerdatatest;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Timeout;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;
import com.tigerdatademo.tigerdatatest.controller.UnitTestChatController;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {
        "spring.ai.openai.api-key=Enter key here/REad from properties file",
        "logging.level.org.springframework.ai=DEBUG"
})
class TigerdatatestApplicationTests {

	@Autowired
	private UnitTestChatController unitTestChatController;

    @Autowired
    private ChatModel chatModel;

    private ChatClient chatClient;
    private RelevancyEvaluator relevancyEvaluator;
    private FactCheckingEvaluator factCheckingEvaluator;
    
    @Value("classpath:/promptTemplates/hrPolicy.st")
    Resource hrPolicyTemplate;
    
    // Minimum acceptable relevancy score for the LLM models response of 0.7
    @Value("${test.relevancy.min-score:0.7}")
    private float minRelevancyScore;
    
    
    @BeforeEach
    void setup() {
        ChatClient.Builder chatClientBuilder =
                ChatClient.builder(chatModel).defaultAdvisors(new SimpleLoggerAdvisor());
        this.chatClient = chatClientBuilder.build();
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
        this.factCheckingEvaluator = new FactCheckingEvaluator(chatClientBuilder);
    }
    
    
    @Test
    @DisplayName("Should return relevant response for basic geography question")
    @Timeout(value = 30)
    void evaluateChatControllerResponseRelevancy() {
        // Given
        String question = "What is the capital of India ?";
    
        // When
        String aiResponse = unitTestChatController.chat(question);
        EvaluationRequest evaluationRequest = new EvaluationRequest(question, aiResponse);
        EvaluationResponse response = relevancyEvaluator.evaluate(evaluationRequest);
        
        Assertions.assertAll(() -> assertThat(aiResponse).isNotBlank(),
                () -> assertThat(response.isPass())
                        .withFailMessage("""
                                ========================================
                                The answer was not considered relevant.
                                Question: "%s"
                                Response: "%s"
                                ========================================
                                """, question, aiResponse)
                        .isTrue(),
                () -> assertThat(response.getScore())
                        .withFailMessage("""
                                ========================================
                                The score %.2f is lower than the minimum required %.2f.
                                Question: "%s"
                                Response: "%s"
                                ========================================
                                """, response.getScore(), minRelevancyScore, question, aiResponse)
                        .isGreaterThan(minRelevancyScore));
    }
}
