package com.tigerdatademo.tigerdatatest.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;

@RestController
@RequestMapping("/audio")
public class AudioToTextController {

	/*Audio to text transcription class*/
	private OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
	private final SpeechModel speechModel;
	
	public AudioToTextController(
			OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel,
			SpeechModel speechModel) {
		this.openAiAudioTranscriptionModel= openAiAudioTranscriptionModel;
		this.speechModel = speechModel;
	}
	
	
	/* http://localhost:8080/audio/gettranscribe
	 *
	 * */
	@GetMapping("/gettranscribe")
	public String getTranscription(@Value("classpath:SbsRecording.mp3") 
		Resource audioFile) {
		
		String response = openAiAudioTranscriptionModel.call(audioFile);
		return response;
	}
	
	
	/* URL: http://localhost:8080/audio/gettranscribe-options	 
	 * This method is used when we need some customizations in the transcription of the 
	 * Audio file. Prompt()->IS supposed to give context of the Audio file.
	 * Currently as of this training only English is supported.
	 * Temperature splits the conversation every 0.5 secs
	 * VTT: to generate output with subtitles satndards
	 * */
	@GetMapping("/gettranscribe-options")
    public String transcribeWithOptions(@Value("classpath:SbsRecording.mp3") 
    		Resource audioFile) {
        
		
		AudioTranscriptionResponse audioTranscriptionResponse = 
				openAiAudioTranscriptionModel.call(new AudioTranscriptionPrompt(
                audioFile, OpenAiAudioTranscriptionOptions.builder()
                .prompt("Casual monologue")
                .language("en")
                .temperature(0.5f)
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.VTT).build()));
        
		return audioTranscriptionResponse.getResult().getOutput();
    }
	
	
	/* URL: http://localhost:8080/audio/speech?message=How are you my friend	*/
	@GetMapping("/speech")
    String spech(@RequestParam("message") String message) throws IOException {
        byte[] audioBytes = speechModel.call(message);
        Path path = Paths.get("output.mp3");
        Files.write(path, audioBytes);
        return "MP3 saved successfully to " + path.toAbsolutePath();
        //Above file saved to: C:\Savitha\Projects\2025_Sep_tigerdatatestrest\tigerdatatest
    }
	
	
	/* URL: http://localhost:8080/audio/speech-options?message=Spring AI is an exciting framework that 
	 * simplifies building AI powered application. It integrates seamlessly with spring boot enabling 
	 * developers to leverage large language models and create intelligent scalable solutions with ease
	 * 	
	 * OpenAiAudioApi.SpeechRequest.Voice.NOVA: Is configuration for Male voice
	 * speed(2.0f): Speech speed has been doubled
	 * */
	@GetMapping("/speech-options")
    String spechWithOptions(@RequestParam("message") String message) throws IOException {
		
        SpeechResponse speechResponse = speechModel.call(new SpeechPrompt(message,
                OpenAiAudioSpeechOptions.builder().voice(OpenAiAudioApi.SpeechRequest.Voice.NOVA)
                        .speed(2.0f)
                        .responseFormat
                        (OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3).build()));
        Path path = Paths.get("speech-options.mp3");
        Files.write(path, speechResponse.getResult().getOutput());
        return "MP3 saved successfully to " + path.toAbsolutePath();
    }
}
