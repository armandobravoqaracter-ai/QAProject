# language: es
# encoding: utf-8

Característica: Validar busqueda web de chespirito

@VALIDAR_INICIO_DE_SESION_CORRECTO
Esquema del escenario: Validar Busqueda de chespirito
  * El usuario navega a la pagina web <url>
  * El usuario valida que la pagina web actual sea <urlActual>
  * El usuario realiza un espera de <time>
  * El usuario escribe texto limpiando el cambo <campoBuscar> <TextoBusqueda>
  * El usuario realiza un espera de <time>
  * El usuario da click en el boton <Buscar>
  * El usuario realiza un espera de <time>
  * El usuario valida el texto en el campo <campoBuscar> sea igual a <TextoBusqueda>

  Ejemplos:
    | url                     | urlActual               | campoBuscar | TextoBusqueda | time | Buscar                                                             |  | busqueda | urlInicio |
    | https://www.google.com/ | https://www.google.com/ | name:q      | chespirito    | 5000 | /html/body/div[1]/div[3]/form/div[1]/div[1]/div[3]/center/input[1] |  |          |           |