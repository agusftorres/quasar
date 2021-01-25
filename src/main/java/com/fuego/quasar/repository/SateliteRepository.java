package com.fuego.quasar.repository;

import com.fuego.quasar.entity.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("sateliteRepository")
public interface SateliteRepository extends JpaRepository<Satellite, String> {
}
