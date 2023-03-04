package ru.nsk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsk.entity.enums.AppDocument;

public interface AppDocumentDAO extends JpaRepository<AppDocument,Long> {
}
