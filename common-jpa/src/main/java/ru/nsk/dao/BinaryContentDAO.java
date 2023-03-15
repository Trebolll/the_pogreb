package ru.nsk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsk.entity.BinaryContent;

public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {
}
