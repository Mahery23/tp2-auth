# TP2 – Authentification Fragile

## Objectifs TP2
- Politique de mot de passe stricte (12 caractères, majuscule, minuscule, chiffre, spécial)
- Stockage sécurisé avec BCrypt
- Anti brute force (5 échecs = blocage 2 minutes)
- Analyse qualité avec SonarCloud

## Améliorations par rapport au TP1
- Mot de passe hashé avec BCrypt (plus de stockage en clair)
- Validation stricte du mot de passe
- Protection contre les attaques par force brute
- Couverture de tests minimum 60%

## Lancer l'application

### Prérequis
- Java 17+
- MySQL
- IntelliJ IDEA

### Base de données
- Créer une base MySQL nommée `tp2-auth`
- Configurer application.properties avec vos identifiants MySQL

### Lancer l'API
Dans IntelliJ : cliquer sur Run sur AuthServerTp2Application.java
L'API démarre sur : http://localhost:8080

### Compte de test
- Email : toto@example.com
- Mot de passe : Toto@1234567890

### Endpoints
- POST /api/auth/register?email=...&password=...
- POST /api/auth/login?email=...&password=...
- GET /api/me (Header: Authorization: token)

### Lancer les tests
Maven → Lifecycle → test

---

## Analyse de sécurité TP2

### Pourquoi TP2 reste fragile
Même avec BCrypt, le mot de passe transite en clair dans la requête de login.
Si un attaquant capture la requête, il peut tenter de la rejouer.
TP3 corrigera cela avec une clé secrète partagée et un nonce.

### SonarCloud
(à compléter après configuration)

### Couverture de tests
(à compléter après mesure)