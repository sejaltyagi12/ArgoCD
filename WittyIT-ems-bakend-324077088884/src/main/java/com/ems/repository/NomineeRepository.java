package com.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.Nominee;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {

}
