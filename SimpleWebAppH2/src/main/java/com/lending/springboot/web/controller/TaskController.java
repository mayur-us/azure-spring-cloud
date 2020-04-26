package com.lending.springboot.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lending.springboot.web.InstanceInformationService;
import com.lending.springboot.web.model.Task;
import com.lending.springboot.web.service.TaskRepository;

@Controller
public class TaskController {
	
	@Autowired
	TaskRepository repository;
	
	

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date - dd/MM/yyyy
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	@RequestMapping(value = "/list-tasks", method = RequestMethod.GET)
	public String showTasks(ModelMap model) {
		String name = getLoggedInUserName(model);
		model.put("tasks", repository.findByUser(name));
		//model.put("tasks", service.retrieveTasks(name));
		return "list-tasks";
	}

	private String getLoggedInUserName(ModelMap model) {
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		if (principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		
		return principal.toString();
	}

	@RequestMapping(value = "/add-task", method = RequestMethod.GET)
	public String showAddTaskPage(ModelMap model) {
		model.addAttribute("task", new Task(0, getLoggedInUserName(model),
				"Default Desc", new Date(), false));
		return "task";
	}

	@RequestMapping(value = "/delete-task", method = RequestMethod.GET)
	public String deleteTask(@RequestParam int id) {

		//if(id==1)
			//throw new RuntimeException("Something went wrong");
		repository.deleteById(id);
		//service.deleteTodo(id);
		return "redirect:/list-tasks";
	}

	@RequestMapping(value = "/update-task", method = RequestMethod.GET)
	public String showUpdateTaskPage(@RequestParam int id, ModelMap model) {
		Task task = repository.findById(id).get();
		//Task task = service.retrieveTask(id);
		model.put("task", task);
		return "task";
	}

	@RequestMapping(value = "/update-task", method = RequestMethod.POST)
	public String updateTask(ModelMap model, @Valid Task task,
			BindingResult result) {

		if (result.hasErrors()) {
			return "task";
		}

		task.setUser(getLoggedInUserName(model));

		repository.save(task);


		return "redirect:/list-tasks";
	}

	@RequestMapping(value = "/add-task", method = RequestMethod.POST)
	public String addTask(ModelMap model, @Valid Task task, BindingResult result) {

		if (result.hasErrors()) {
			return "task";
		}

		task.setUser(getLoggedInUserName(model));
		repository.save(task);
		/*service.addTask(getLoggedInUserName(model), task.getDesc(), task.getTargetDate(),
				false);*/
		return "redirect:/list-tasks";
	}
}
