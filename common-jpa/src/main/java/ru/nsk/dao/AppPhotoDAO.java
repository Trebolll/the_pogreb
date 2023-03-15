package ru.nsk.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsk.entity.AppPhoto;

public interface AppPhotoDAO extends JpaRepository<AppPhoto,Long> {
}
