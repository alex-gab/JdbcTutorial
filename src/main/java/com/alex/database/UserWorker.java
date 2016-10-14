package com.alex.database;

public final class UserWorker {
    public static void main(String[] args) {
        final UserRepository userRepository = new UserRepositoryImpl();
        userRepository.insertUser("ION1", "02-FEB-1986", 2500);
    }
}
