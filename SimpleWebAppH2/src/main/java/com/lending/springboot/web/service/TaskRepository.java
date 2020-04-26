package com.lending.springboot.web.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lending.springboot.web.model.Task;


public interface TaskRepository extends JpaRepository<Task, Integer> {
	List<Task> findByUser(String user);
}