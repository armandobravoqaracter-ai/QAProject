# language: es
# encoding: utf-8

Característica: Validación de inicio de sesión

  @VALIDAR_INICIO_DE_SESION_CORRECTO
  Esquema del escenario: Inicio de sesión exitoso
    * El usuario navega a la pagina web <url>
    * El usuario valida que la pagina web actual sea <urlActual>
    * El usuario escribe texto limpiando el cambo <campoUsuario> <usuario>
    * El usuario escribe texto limpiando el cambo <campoContrasena> <contrasena>
    * El usuario da click en el boton <btnLogin>
    * El usuario realiza un espera de <time>
    * El usuario valida que la pagina web actual sea <urlInicio>

    Ejemplos:
      | url                        | urlActual                  | campoUsuario | usuario       | time  | campoContrasena | contrasena   | btnLogin                | urlInicio                                |
      | https://www.saucedemo.com/ | https://www.saucedemo.com/ | id:user-name | standard_user | 10000 | name:password   | secret_sauce | //*[@id="login-button"] | https://www.saucedemo.com/inventory.html |

  @VALIDAR_INICIO_DE_SESION_FALLIDO_USUARIO_INCORRECTO
  Esquema del escenario: Inicio de sesión fallido por usuario incorrecto
    * El usuario navega a la pagina web <url>
    * El usuario valida que la pagina web actual sea <urlActual>
    * El usuario escribe texto limpiando el cambo <campoUsuario> <usuario>
    * El usuario escribe texto limpiando el cambo <campoContrasena> <contrasena>
    * El usuario da click en el boton <btnLogin>
    * El usuario realiza un espera de <time>
    * El usuario valida el texto en el campo <campoError> sea igual a <error>
    Ejemplos:
      | url                        | urlActual                  | campoUsuario | usuario       | time  | campoContrasena | contrasena   | btnLogin                | campoError                                                                                | error                                                                     |
      | https://www.saucedemo.com/ | https://www.saucedemo.com/ | id:user-name | usuario_fallo | 10000 | name:password   | secret_sauce | //*[@id="login-button"] | cssSelector:#login_button_container > div > form > div.error-message-container.error > h3 | Epic sadface: Username and password do not match any user in this service |
@VALIDAR_INICIO_DE_SESION_FALLIDO_PASSWORD_INCORRECTO
Esquema del escenario: Inicio de sesión fallido por usuario incorrecto
  * El usuario navega a la pagina web <url>
  * El usuario valida que la pagina web actual sea <urlActual>
  * El usuario escribe texto limpiando el cambo <campoUsuario> <usuario>
  * El usuario escribe texto limpiando el cambo <campoContrasena> <contrasena>
  * El usuario da click en el boton <btnLogin>
  * El usuario realiza un espera de <time>
  * El usuario valida el texto en el campo <campoError> sea igual a <error>

    Ejemplos:
      | url                        | urlActual                  | campoUsuario | usuario       | time  | campoContrasena | contrasena | btnLogin                | campoError                                                                                | error                                                                     |
      | https://www.saucedemo.com/ | https://www.saucedemo.com/ | id:user-name | standard_user | 10000 | name:password   | pass_error | //*[@id="login-button"] | cssSelector:#login_button_container > div > form > div.error-message-container.error > h3 | Epic sadface: Username and password do not match any user in this service |