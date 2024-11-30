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
import it.lessons.pizzeria.model.SpecialOffer;
import it.lessons.pizzeria.repository.IngredientsRepository;
import it.lessons.pizzeria.repository.PizzaRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizza")
public class PizzaController {
	
	@Autowired
	private PizzaRepository pizzaRepository;
	
	@Autowired
	private IngredientsRepository ingredientsRepository;
	
	
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
		model.addAttribute("allIngredients", ingredientsRepository.findAll());
		return "pizza/create";
	}
	
	@PostMapping("/store")
	public String store(@Valid @ModelAttribute(name = "pizza")Pizza pizza, BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "pizza/create";
		}
		
		pizzaRepository.save(pizza);
		
		return "redirect:/pizza";
	};
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable(name = "id") Long id, Model model) {
		
		Optional<Pizza> pizza = pizzaRepository.findById(id);
		model.addAttribute("allIngredients", ingredientsRepository.findAll());
		if(pizza.isPresent()) {
			model.addAttribute("pizza", pizza.get());
		}
		
		return "pizza/edit";
	}
	
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute(name = "pizza") Pizza pizza, BindingResult bindingResult,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("allIngredients", ingredientsRepository.findAll());
			return "pizza/edit";
		}
		pizzaRepository.save(pizza);
		return "redirect:/pizza";
	}
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable(name = "id") Long id) {
		pizzaRepository.deleteById(id);
		return "redirect:/pizza";
	}
	
	@GetMapping("/{id}/specialOffers")
	public String createOffers(@PathVariable("id") Long id, Model model) {
		Pizza pizza = pizzaRepository.findById(id).get();
		SpecialOffer specialOffer = new SpecialOffer();
		specialOffer.setPizza(pizza);
		model.addAttribute("specialOffer", specialOffer);
		return "specialOffers/create";
	}
}
