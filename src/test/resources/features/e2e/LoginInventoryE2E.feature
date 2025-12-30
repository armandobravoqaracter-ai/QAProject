@E1 @E2E
Feature: E1 - Flujo completo Login e Inventario (flujo feliz)

  Background:
    Given el usuario está en la página de login

  @E1_001 @Login @Inventory
  Scenario: Login exitoso y validación de inventario con Page Objects
    When el usuario realiza login con credenciales válidas
    Then el usuario debería estar en la página de inventario
    And el título de la página debería ser "Products"
    And debería haber productos disponibles en el inventario
    When el usuario agrega el producto "Sauce Labs Backpack" al carrito
    Then el carrito debería mostrar 1 producto
    And el producto "Sauce Labs Backpack" debería estar en el carrito

  @E1_002 @Login @ErrorHandling
  Scenario: Validación de mensaje de error con credenciales inválidas
    When el usuario intenta hacer login con usuario "invalid_user" y contraseña "wrong_password"
    Then debería mostrarse un mensaje de error
    And el mensaje de error debería contener "Username and password do not match"
