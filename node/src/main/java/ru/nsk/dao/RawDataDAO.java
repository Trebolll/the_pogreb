package ru.nsk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsk.entity.RawData;

public interface RawDataDAO extends JpaRepository<RawData, Long> {
}
