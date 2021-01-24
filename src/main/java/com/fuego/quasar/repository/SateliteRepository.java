package com.fuego.quasar.repository;

import com.fuego.quasar.entity.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("sateliteRepository")
public interface SateliteRepository extends JpaRepository<Satellite, String> {
    @Query(value = "SELECT u FROM Satelite u WHERE u.name = ?1",nativeQuery = true)
    Satellite findByName(String name);
}
