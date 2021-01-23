package com.fuego.quasar.repository;

import com.fuego.quasar.entity.EquationXY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("equationXYRepository")
public interface EquationXYRepository extends JpaRepository<EquationXY, String> {
    @Query(value = "SELECT u FROM EquationXY u WHERE u.name = ?1",nativeQuery = true)
    EquationXY findByName(String name);
}
