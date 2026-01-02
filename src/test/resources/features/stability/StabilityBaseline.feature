# language: es
@E1_Stability @Baseline
Característica: E1 - Baseline de estabilidad (medición de inestabilidad)
  Como QA automatizador
  Quiero medir la repetibilidad de mi suite de tests
  Para identificar tests flaky (inestables) y mejorar la confiabilidad

  Antecedentes:
    Dado que tengo una suite mínima definida con 2 pruebas

  @GranularTest
  Esquema del escenario: Suite mínima - Test granular de login
    Cuando ejecuto la corrida número <corrida>
    Y ejecuto el test granular de login con "<usuario>" y "<password>"
    Entonces el resultado de estabilidad debería ser "<resultado>"
    Y registro el resultado de la corrida <corrida>

    Ejemplos:
      | corrida | usuario       | password     | resultado |
      | 1       | standard_user | secret_sauce | SUCCESS   |
      | 2       | standard_user | secret_sauce | SUCCESS   |
      | 3       | standard_user | secret_sauce | SUCCESS   |
      | 4       | standard_user | secret_sauce | SUCCESS   |
      | 5       | standard_user | secret_sauce | SUCCESS   |
      | 6       | standard_user | secret_sauce | SUCCESS   |
      | 7       | standard_user | secret_sauce | SUCCESS   |
      | 8       | standard_user | secret_sauce | SUCCESS   |
      | 9       | standard_user | secret_sauce | SUCCESS   |
      | 10      | standard_user | secret_sauce | SUCCESS   |

  @EndToEndTest
  Esquema del escenario: Suite mínima - Test end-to-end de compra
    Cuando ejecuto la corrida número <corrida>
    Y ejecuto el test e2e: login, agregar producto y validar carrito
    Entonces el flujo e2e debería completarse exitosamente
    Y registro el resultado e2e de la corrida <corrida>

    Ejemplos:
      | corrida |
      | 1       |
      | 2       |
      | 3       |
      | 4       |
      | 5       |
      | 6       |
      | 7       |
      | 8       |
      | 9       |
      | 10      |
