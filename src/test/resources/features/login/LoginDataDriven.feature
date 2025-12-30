@E3 @Login @DataDriven
Feature: E3 - Login con múltiples datasets (positivos y negativos)
  Como QA Engineer
  Quiero probar el login con diferentes combinaciones de credenciales
  Para validar tanto casos exitosos como errores esperados

  @E3_001 @Parametrizado
  Scenario Outline: Validar login con dataset <datasetName>
    Given el usuario está en la página de login
    When el usuario intenta hacer login con usuario "<usuario>" y contraseña "<password>"
    Then el resultado debería ser "<expectedOutcome>"
    And si es ERROR debería mostrarse un mensaje de error
    
    Examples:
      | datasetName | usuario        | password     | expectedOutcome | nota                  |
      | OK_admin    | standard_user  | secret_sauce | SUCCESS         | Caso feliz            |
      | NEG_pwd     | standard_user  | wrong_pwd    | ERROR           | Password incorrecto   |
      | NEG_user    | no_user        | secret_sauce | ERROR           | Usuario inválido      |
      | NEG_empty   |                |              | ERROR           | Campos vacíos         |
