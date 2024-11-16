package it.lessons.pizzeria.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.lessons.pizzeria.model.Pizza;
import it.lessons.pizzeria.repository.PizzaRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizza")
public class PizzaController {
	
	@Autowired
	private PizzaRepository pizzaRepository;
	
	
	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword, 
			Model model) {
		
		List<Pizza> allPizzas;
		if(keyword != null && !keyword.isBlank()) {
			model.addAttribute("keyword", keyword);
			allPizzas = pizzaRepository.findByNameContaining(keyword);
		} else {
			allPizzas = pizzaRepository.findAll();
		}
		model.addAttribute("pizzas", allPizzas);
		
		return "pizza/index";
	}
	
	
	@GetMapping("/show/{id}")
	public String show(@PathVariable(name = "id")Long id, Model model) {
		
		Optional<Pizza> pizzaById = pizzaRepository.findById(id);
		
		if(pizzaById.isPresent()) {
			model.addAttribute("pizza", pizzaById.get());
		}
		
		return "pizza/show";
	}
	
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("pizza", new Pizza());
		return "pizza/create";
	}
	
	@PostMapping("/store")
	public String store(@Valid @ModelAttribute(name = "pizza")Pizza pizza, BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "pizza/create";
		}
		
		pizzaRepository.save(pizza);
		
		return "redirect:/pizza";
	}
	
}
