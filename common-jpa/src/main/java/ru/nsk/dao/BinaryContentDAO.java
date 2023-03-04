package ru.nsk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsk.entity.enums.BinaryContent;

public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {
}
