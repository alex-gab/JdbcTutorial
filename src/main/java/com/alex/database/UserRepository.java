package com.alex.database;

public interface UserRepository {
    void insertUser(String name, String hireDate, int salary);
}
