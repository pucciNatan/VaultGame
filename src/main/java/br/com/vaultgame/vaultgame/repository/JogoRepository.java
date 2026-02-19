package br.com.vaultgame.vaultgame.repository;

import br.com.vaultgame.vaultgame.model.JogoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogoRepository extends JpaRepository<JogoModel, Long> {}
