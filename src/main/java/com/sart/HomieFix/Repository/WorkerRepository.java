package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.Worker;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
	Optional<Worker> findById(Long id);
}