package com.backend.demo.services.im;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.backend.demo.entities.Category;
import com.backend.demo.entities.Post;
import com.backend.demo.entities.User;
import com.backend.demo.exceptions.ResourceNotFoundException;
import com.backend.demo.payloads.PostDto;
import com.backend.demo.payloads.PostResponse;
import com.backend.demo.repo.CategoryRepo;
import com.backend.demo.repo.PostRepo;
import com.backend.demo.repo.UserRepo;
import com.backend.demo.services.PostService;
@Service
public class PostServiceImp implements PostService {

	@Autowired
	private PostRepo postRepo;
	@Autowired
	private ModelMapper modelmapper;
	@Autowired
	private UserRepo userrepo;
	@Autowired
	private CategoryRepo categoryrepo;
	@Override
	public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {
		// TODO Auto-generated method stub
		
		User user=this.userrepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","User id",userId));
		Category category=this.categoryrepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category id",categoryId));
	
		Post post=this.modelmapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddeddate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost=this.postRepo.save(post);
		
		
		return this.modelmapper.map(newPost,PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		Post updatedPost=this.postRepo.save(post);
		return this.modelmapper.map(updatedPost,PostDto.class);
		
		// TODO Auto-generated method stub
		
		
		
		
	}

	@Override
	public void deletePost(Integer postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
		this.postRepo.delete(post);
		// TODO Auto-generated method stub

	}

	@Override
	public PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy) {
		// TODO Auto-generated method stub
		
		//int pageSize=5;
		//int pageNumber=1;
		Pageable p=PageRequest.of(pageNumber, pageSize,Sort.by(sortBy).descending());
		
		Page<Post>pagePost=this.postRepo.findAll(p);
		List<Post>allPosts=pagePost.getContent();
		
		List<PostDto>postDtos=allPosts.stream().map((post)->this.modelmapper.map(post,PostDto.class)).collect(Collectors.toList());
		PostResponse postResponse=new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		// TODO Auto-generated method stub
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
		return this.modelmapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostByCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		
		Category cat=this.categoryrepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category","category id", categoryId));
		List<Post> posts=this.postRepo.findByCategory(cat);
		List<PostDto> postDtos=posts.stream().map((post->this.modelmapper.map(post,PostDto.class))).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> getPostsBYUser(Integer userId) {
		
		User user=this.userrepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id",userId));
		List<Post> posts=this.postRepo.findByUser(user);
		List<PostDto> postDtos=posts.stream().map((post->this.modelmapper.map(post,PostDto.class))).collect(Collectors.toList());
		// TODO Auto-generated method stub
		return postDtos;
	}

	@Override
	public List<PostDto> searchPost(String keyword) {
		
		List<Post> posts = this.postRepo.searchByTitle(keyword);
		List<PostDto> postDtos = posts.stream().map((post->this.modelmapper.map(post,PostDto.class))).collect(Collectors.toList());
	  	
	
		return postDtos;
	}

}
