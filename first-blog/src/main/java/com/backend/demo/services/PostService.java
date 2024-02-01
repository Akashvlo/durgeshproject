package com.backend.demo.services;

import java.util.List;

import com.backend.demo.entities.Post;
import com.backend.demo.payloads.PostDto;
import com.backend.demo.payloads.PostResponse;

public interface PostService {
	
	//create
	
	
	
	PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);
	//update
	
	PostDto updatePost(PostDto postDto,Integer postId);
	
	//delete
	
	void deletePost(Integer postId);
	//get all post
	
	PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy );
	
	//get single Post
	
	PostDto getPostById(Integer postId);
	
	//get all post by category
	
	List<PostDto>getPostByCategory(Integer categoryId);
	
	//get all post by user
	
	List<PostDto>getPostsBYUser(Integer userId);
	
	//search
	List<PostDto>searchPost(String keyword);
	

}
