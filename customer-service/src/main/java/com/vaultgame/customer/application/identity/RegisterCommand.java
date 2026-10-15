package com.vaultgame.customer.application.identity;

public record RegisterCommand(String email, String password, String fullName) {
}
