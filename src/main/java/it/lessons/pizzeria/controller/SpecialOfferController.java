package it.lessons.pizzeria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.lessons.pizzeria.model.SpecialOffer;
import it.lessons.pizzeria.repository.SpecialOfferRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/specialOffers")
public class SpecialOfferController {

	@Autowired
	private SpecialOfferRepository specialOfferRepo;
	
	@PostMapping("/store")
	public String store(@Valid @ModelAttribute SpecialOffer specialOfferForm, 
			BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "specialOffers/create";
		}
		
		specialOfferRepo.save(specialOfferForm);
		return "redirect:/pizza/show/" + specialOfferForm.getPizza().getId();
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		
		SpecialOffer offer = specialOfferRepo.findById(id).get();
		
		model.addAttribute("specialOffer", offer);
		
		return "specialOffers/edit";
	}
	
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute SpecialOffer specialOffer,
			BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "specialOffers/edit";
		}
		
		specialOfferRepo.save(specialOffer);
		return "redirect:/pizza/show/" + specialOffer.getPizza().getId();
	}
}
