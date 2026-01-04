# language: es
@E4_Evidence @FailureDemo
Característica: E4 - Demostración de evidencia automática en fallas
  
  Como QA Engineer
  Quiero que el sistema capture evidencia automáticamente cuando un test falle
  Para poder diagnosticar problemas rápidamente

  @IntentionalFailure
  Escenario: Provocar falla intencional para validar captura de evidencia
    Dado que tengo una suite mínima definida con 2 pruebas
    Cuando ejecuto la corrida número 1
    Y ejecuto el test granular de login con "standard_user" y "secret_sauce"
    Entonces el resultado de estabilidad debería ser "FAILURE"
    Y registro el resultado de la corrida 1

  @SuccessfulTest
  Escenario: Test exitoso para comparar evidencia
    Dado que tengo una suite mínima definida con 2 pruebas
    Cuando ejecuto la corrida número 2
    Y ejecuto el test granular de login con "standard_user" y "secret_sauce"
    Entonces el resultado de estabilidad debería ser "SUCCESS"
    Y registro el resultado de la corrida 2
