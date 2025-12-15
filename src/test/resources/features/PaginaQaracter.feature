# language: es
# encoding: utf-8

Característica: Validación de inicio de sesión

@VALIDAR_INICIO_DE_SESION_CORRECTO
Esquema del escenario: Inicio de sesión exitoso
    * El usuario navega a la pagina web <urlQaracter>
    * El usuario valida que la pagina web actual sea <urlActual>
  * El usuario valida que el texto no este vacio <campoUsuario>
Ejemplos:
  | urlQaracter                      | urlQaracter                      | campoUsuario                          |  |
  | https://www.qaracter.com/en/home | https://www.qaracter.com/en/home | /html/body/div[2]/div[1]/nav/div[8]/a |  |