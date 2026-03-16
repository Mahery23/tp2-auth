package com.example.tp2_auth;

import com.example.tp2_auth.exception.*;
import com.example.tp2_auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class Tp2AuthApplicationTests {

	@Autowired
	private AuthService authService;

	// Test 1 : inscription OK
	@Test
	void testInscriptionOk() {
		assertDoesNotThrow(() -> authService.register("nouveau@test.com", "Test@1234567890"));
	}

	// Test 2 : email invalide
	@Test
	void testEmailInvalide() {
		assertThrows(InvalidInputException.class,
				() -> authService.register("pasunmail", "Test@1234567890"));
	}

	// Test 3 : mot de passe trop court
	@Test
	void testMotDePasseTropCourt() {
		assertThrows(InvalidInputException.class,
				() -> authService.register("user@test.com", "Ab@1"));
	}

	// Test 4 : mot de passe sans majuscule
	@Test
	void testMotDePasseSansMajuscule() {
		assertThrows(InvalidInputException.class,
				() -> authService.register("user@test.com", "test@1234567890"));
	}

	// Test 5 : mot de passe sans caractère spécial
	@Test
	void testMotDePasseSansSpecial() {
		assertThrows(InvalidInputException.class,
				() -> authService.register("user@test.com", "Test1234567890"));
	}

	// Test 6 : email déjà existant
	@Test
	void testEmailDejaExistant() {
		authService.register("double@test.com", "Test@1234567890");
		assertThrows(ResourceConflictException.class,
				() -> authService.register("double@test.com", "Test@1234567890"));
	}

	// Test 7 : login OK
	@Test
	void testLoginOk() {
		authService.register("login@test.com", "Test@1234567890");
		assertDoesNotThrow(() -> authService.login("login@test.com", "Test@1234567890"));
	}

	// Test 8 : login mauvais mot de passe
	@Test
	void testLoginMauvaisMotDePasse() {
		authService.register("mdp@test.com", "Test@1234567890");
		assertThrows(AuthenticationFailedException.class,
				() -> authService.login("mdp@test.com", "Mauvais@1234567890"));
	}

	// Test 9 : login email inconnu
	@Test
	void testLoginEmailInconnu() {
		assertThrows(AuthenticationFailedException.class,
				() -> authService.login("inconnu@test.com", "Test@1234567890"));
	}

	// Test 10 : accès /api/me sans token
	@Test
	void testAccesSansToken() {
		assertThrows(AuthenticationFailedException.class,
				() -> authService.getUserByToken(null));
	}

	// Test 11 : non-divulgation des erreurs (même message pour email inconnu ou mauvais mdp)
	@Test
	void testNonDivulgationErreur() {
		authService.register("secret@test.com", "Test@1234567890");
		AuthenticationFailedException ex1 = assertThrows(AuthenticationFailedException.class,
				() -> authService.login("inconnu@test.com", "Test@1234567890"));
		AuthenticationFailedException ex2 = assertThrows(AuthenticationFailedException.class,
				() -> authService.login("secret@test.com", "Mauvais@1234567890"));
		assertEquals(ex1.getMessage(), ex2.getMessage());
	}

	// Test 12 : lockout après 5 échecs
	@Test
	void testLockoutApres5Echecs() {
		authService.register("lock@test.com", "Test@1234567890");
		for (int i = 0; i < 5; i++) {
			try {
				authService.login("lock@test.com", "Mauvais@1234567890");
			} catch (Exception ignored) {}
		}
		assertThrows(AccountLockedException.class,
				() -> authService.login("lock@test.com", "Test@1234567890"));
	}
}