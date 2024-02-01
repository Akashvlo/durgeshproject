package com.backend.demo.services.im;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.demo.entities.Comment;
import com.backend.demo.entities.Post;
import com.backend.demo.exceptions.ResourceNotFoundException;
import com.backend.demo.payloads.CommentDto;
import com.backend.demo.repo.CommentRepo;
import com.backend.demo.repo.PostRepo;
import com.backend.demo.services.CommentService;
@Service
public class CommentServiceimpl implements CommentService {
	
	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	@Autowired 
	private ModelMapper modelmapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
		Comment comment=this.modelmapper.map(commentDto, Comment.class);
		comment.setPost(post);
		Comment savedComment = this.commentRepo.save(comment);
		
		
		return this.modelmapper.map(savedComment,CommentDto.class) ;
	}

	@Override
	public void deleteComment(Integer commentId) {
		// TODO Auto-generated method stub
		Comment com=this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","CommentId",commentId));
        this.commentRepo.delete(com);
        
	}

}
