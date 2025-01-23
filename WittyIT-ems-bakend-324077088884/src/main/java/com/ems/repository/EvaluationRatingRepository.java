package com.ems.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.EvaluationRating;

public interface EvaluationRatingRepository extends
		JpaRepository<EvaluationRating, Long> {

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return Evaluation Rating
	 */
	EvaluationRating findByRatingId(Long id);
	
	/**
	 * Find by Description
	 * 
	 * @param id
	 * @return Evaluation Rating
	 */
	EvaluationRating findByDescription(String description);

	/**
	 * Find all Evaluation Rating
	 * 
	 * @return EvaluationRating List.
	 */
	List<EvaluationRating> findAll();

}
