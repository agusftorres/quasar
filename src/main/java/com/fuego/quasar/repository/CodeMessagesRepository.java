package com.fuego.quasar.repository;

import com.fuego.quasar.entity.CodeMessages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("codeMessagesRepository")
public interface CodeMessagesRepository extends JpaRepository<CodeMessages, String> {
}
