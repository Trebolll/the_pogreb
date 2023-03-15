package ru.nsk.service;

import ru.nsk.entity.AppUser;

public interface AppUserService {
    String registerUser(AppUser appUser);
    String setEmail(AppUser appUser, String email);
}