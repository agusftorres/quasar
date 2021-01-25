package com.fuego.quasar.repository;

import com.fuego.quasar.entity.EquationXY;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("equationXYRepository")
public interface EquationXYRepository extends JpaRepository<EquationXY, String> {
}
