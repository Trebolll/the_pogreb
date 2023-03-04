package ru.nsk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsk.entity.enums.AppPhoto;

public interface AppPhotoDAO extends JpaRepository<AppPhoto,Long> {
}
