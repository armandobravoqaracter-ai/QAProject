#language: es
# encoding: utf-8
@VALIDAR_ENDPOINT
Requisito: Como usuario necesito validar que la respuesta de la url consultada devuelva un código 200 (OK), con el fin de confirmar que la solicitud fue procesada correctamente por el aplicativo

  Esquema del escenario: Validación de servicio API
    * El usuario agrega los headers a la petición:
      | Accept       | application/json |
      | Content-Type | application/json |
    * El usuario realiza una petición GET a la url <url>
    * El usuario valida el código de respuesta debe ser <codigoEsperado>
    * El usuario valida el campo <campo> debe ser <valorEsperado>
    * El usuario valida el campo <campoid> debe ser <valorEsperadoid>

    Ejemplos:
      | url                                          | codigoEsperado | campo  | valorEsperado | campoid | valorEsperadoid |
      | https://jsonplaceholder.typicode.com/posts/1 | 200            | userId | 1             | id      | 1               |
