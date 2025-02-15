package com.sart.HomieFix.Repository;

import com.sart.HomieFix.Entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
}