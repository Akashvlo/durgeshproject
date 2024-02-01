package com.backend.demo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.MediaType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.util.StreamUtils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.backend.demo.config.AppConstants;
import com.backend.demo.entities.Post;
import com.backend.demo.payloads.ApiResponse;
import com.backend.demo.payloads.PostDto;
import com.backend.demo.payloads.PostResponse;
import com.backend.demo.services.FileService;
import com.backend.demo.services.PostService;

//import lombok.Value;

@RestController
@RequestMapping("/api/v1")
public class PostController {
	
	@Autowired
	private PostService postservice;
	@Autowired
	private FileService fileservice;
	@Value("${project.image}")
	private String path;
	//create
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto>createPost(@RequestBody PostDto postDto,	
		@PathVariable Integer userId,
		@PathVariable Integer categoryId){
		
		PostDto createPost=this.postservice.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createPost,HttpStatus.CREATED);
		
		
	}
	
	//get by user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId){
		
		List<PostDto>posts=this.postservice.getPostsBYUser(userId);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
		
	}
      //get by category
	
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId){
		
		List<PostDto>posts=this.postservice.getPostByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
		
	}
	//get all post
	@GetMapping("/Posts")
	public ResponseEntity<PostResponse>getAllPost(@RequestParam(value="pageNumber",defaultValue="0",required=false) Integer pageNumber,
			@RequestParam(value="pageSize",defaultValue="10",required=false) Integer pageSize, 
	        @RequestParam(value="sortBy",defaultValue="postId",required=false)String sortBy){
		 PostResponse postResponse = this.postservice.getAllPost(pageNumber,pageSize,sortBy);
		return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);
	}
	
	//get post Details by id
	
	@GetMapping("/Posts/{postId}")
	public ResponseEntity<PostDto>getPostById(@PathVariable Integer postId){
		PostDto postDto=this.postservice.getPostById(postId);
		return new ResponseEntity<PostDto>(postDto,HttpStatus.OK);
		
		
	}
	
	//delete post
	@DeleteMapping("/posts/{postId}")
	public ApiResponse deletePost(@PathVariable Integer postId) {
		
		this.postservice.deletePost(postId);
		
		return new ApiResponse("post is successfully deleted!!",true);
		
	}
	//update post
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto>updatePost(@RequestBody PostDto postDto,@PathVariable Integer postId) {
		
		PostDto updatePost=this.postservice.updatePost(postDto,postId);
		
		return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
	}

		//searching
        @GetMapping("/posts/search/{keywords}")
       public ResponseEntity<List<PostDto>>searchPostByTitle(@PathVariable("keywords")String keywords)
        {
        	List<PostDto> result = this.postservice.searchPost(keywords);
        	return new ResponseEntity<List<PostDto>>(result,HttpStatus.OK);
        }
        
        
        //post image upload
        @PostMapping("/post/image/upload/{postId}")
        public ResponseEntity<PostDto>uploadPostImage(@RequestParam("image")MultipartFile image,@PathVariable Integer postId)
        		throws IOException
        		{
        	PostDto postDto = this.postservice.getPostById(postId);
        	String fileName = this.fileservice.uploadingImage(path, image);
        	
        	
        	postDto.setImageName(fileName);
        	PostDto updatePost = this.postservice.updatePost(postDto, postId);
        	
        	
        	return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
        		}

        
        //method to serve files
        
        @GetMapping(value="/post/image/{imageName}", produces=MediaType.IMAGE_JPEG_VALUE)
        public void downloadImage(@PathVariable("imageName") String imageName,
      HttpServletResponse response)throws IOException {
        InputStream resource = this.fileservice.getResource(path, imageName);

      response.setContentType(MediaType.IMAGE_JPEG_VALUE);
         StreamUtils.copy(resource, response.getOutputStream()) ;      
        }
        
}
	

