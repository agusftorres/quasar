package com.fuego.quasar.repository;

import com.fuego.quasar.entity.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("satelliteRepository")
public interface SatelliteRepository extends JpaRepository<Satellite, String> {
}
