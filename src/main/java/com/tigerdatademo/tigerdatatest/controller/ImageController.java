package com.tigerdatademo.tigerdatatest.controller;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageController {
	
	private final ImageModel imageModel;

    public ImageController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    
    /* http://localhost:8080/image/getimagefromtext?message=Black-and-white close-up of a smiling woman
       with closed eyes , her hair framing her face naturally, exuding warmth and joy
     * 
	 *getUrl(): OpenAI uploads at its Url which will be availabel for short duration
	 * */
    @GetMapping("/getimagefromtext")
    public String getImageFromText(@RequestParam("message") String message) {
    	
    	ImageResponse imageResponse = imageModel.call(new ImagePrompt(message));    	
        return imageResponse.getResult().getOutput().getUrl();
    }

    
    /*http://localhost:8080/image/getimagefromtext?message=THree people collaborate in a model office,
     discussing a presentation projected on the wall.Minimalist design with warm lighting.
    */
    @GetMapping("/getimagefromtext-options")
    public String getImageFromTextWithOptions(@RequestParam("message") String message) {
    
    	ImageResponse imageResponse = imageModel.call(new ImagePrompt(message,
            OpenAiImageOptions.builder()
                    .N(1)
                    .quality("hd")
                    .style("natural")
                    .height(1024)
                    .width(1024).responseFormat("url").build()));
    return imageResponse.getResult().getOutput().getUrl();
    }
}
