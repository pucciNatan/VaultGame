package com.vaultgame.customer.application.usecase.identity;

public record RegisterCommand(String email, String password, String fullName) {
}
