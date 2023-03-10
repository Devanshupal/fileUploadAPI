package com.imageapi.imageapi.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imageapi.imageapi.payloads.FileResponse;
import com.imageapi.imageapi.service.ImageService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class Controller {
	
	@Autowired
	private ImageService imageService;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping("/upload")
	public ResponseEntity<FileResponse> fileUpload(@RequestParam ("image") MultipartFile image){
		
		String fileName=null;
		
		try {
	     fileName = this.imageService.uploadImage(path, image);
		}
		catch(Exception e) {
			
			e.printStackTrace();
		
			return new ResponseEntity<>(new FileResponse(null,"file not uploaded"),HttpStatus.INTERNAL_SERVER_ERROR);
		
		}

		return new ResponseEntity<>(new FileResponse(fileName, "uploaded"),HttpStatus.OK);
	
}
	//     method to serve files
	
	@GetMapping("/images/{imageName}")
	public void downloadImage(@PathVariable ("imageName") String imageName,HttpServletResponse response) throws IOException {
		
		InputStream resourses = this.imageService.getResourses(path, imageName);
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		
		StreamUtils.copy(resourses, response.getOutputStream());
		
	}
	
}

