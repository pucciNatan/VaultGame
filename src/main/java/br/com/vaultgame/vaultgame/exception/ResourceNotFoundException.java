package br.com.vaultgame.vaultgame.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " não encontrado com id " + id);
    }
}
