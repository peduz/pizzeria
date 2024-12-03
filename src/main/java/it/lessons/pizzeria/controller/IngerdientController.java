package it.lessons.pizzeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.lessons.pizzeria.model.Ingredient;
import it.lessons.pizzeria.model.Pizza;
import it.lessons.pizzeria.repository.IngredientsRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/ingredients")
public class IngerdientController {

	@Autowired
	private IngredientsRepository ingredientRepository;
	
	@GetMapping
	public String index(Model model) {

		model.addAttribute("ingredients", ingredientRepository.findAll());
		model.addAttribute("ingredient", new Ingredient());
		return "ingredients/index";
	}
	
	@PostMapping("/store")
	public String store(@Valid @ModelAttribute Ingredient ingredientForm, 
			BindingResult bindingResult, RedirectAttributes redirectAttribute, Model model) {
		
		if(ingredientRepository.findByName(ingredientForm.getName()).isPresent()) {
			bindingResult.addError(new ObjectError("unique", "This ingredient already exists"));
		}
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("ingredients", ingredientRepository.findAll());
			model.addAttribute("ingredient", ingredientForm);
			return "ingredients/index";		
		}
		
		ingredientRepository.save(ingredientForm);
		
		return "redirect:/ingredients";
	}
	
	
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		
		Ingredient ingredient = ingredientRepository.findById(id).get();
		
		for(Pizza pizza : ingredient.getPizzas()) {
			pizza.getIngredients().remove(ingredient);
		}
		
		ingredientRepository.deleteById(id);

		return "redirect:/ingredients";
	}
	
	
	
	
	
}
