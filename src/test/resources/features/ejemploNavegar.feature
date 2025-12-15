#language: es
# encoding: utf-8
@VALIDAR_RESPUESTA_CONEXION_HISDWH
Requisito: Como usuario necesito validar que la respuesta de la url consultada devuelva un código 200 (OK), con el fin de confirmar que la solicitud fue procesada correctamente por el aplicativo

    Esquema del escenario: El usuario inicia sesion
    * El usuario navega a la pagina web <url> (del navegador web de google)
    * El usuario valida que la pagina web actual sea <urlActual>
    * El usuario realiza un espera de <time>
    * El usuario da click en el boton <btnAccept>
    * El usuario realiza un scroll sin click en el elemento <btnConocenos>
    * El usuario da click en el boton <btnConocenos>
    Ejemplos:
        | url                              | urlActual                        | btnConocenos                      | time  | btnAccept                                   |
        | https://www.qaracter.com/en/home | https://www.qaracter.com/en/home | /html/body/div[3]/div[1]/div[1]/a | 10000 | /html/body/div[7]/div[1]/div[1]/div[2]/a[3] |