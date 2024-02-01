package com.backend.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.demo.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment,Integer>{

}
