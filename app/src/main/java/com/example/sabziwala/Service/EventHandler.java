package com.example.sabziwala.Service;

public interface EventHandler {
    void handle();

    void handle(int position);

    void handle(String id);
}