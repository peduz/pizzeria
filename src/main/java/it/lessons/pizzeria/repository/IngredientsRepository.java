package it.lessons.pizzeria.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.pizzeria.model.Ingredient;

public interface IngredientsRepository extends JpaRepository<Ingredient, Long> {

	
	Optional<Ingredient> findByName(String name);
}
