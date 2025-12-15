# language: es
# encoding: utf-8

Característica: Validación de inicio de sesión

@VALIDAR_SELECCION_DE_OPCION
  Esquema del escenario: Seleccion de opcion correcta
  * El usuario navega a la pagina web <url>
  * El usuario valida que la pagina web actual sea <url>
  * El usuario selecciona la opcion <opcionCombo> del combo <combo>
  * El usuario realiza un espera de <time>
  * El usuario extrae el valor del elemento y lo guarda en un archivo <combo> <archivo>
  * El usuario realiza un espera de <time>
  * El usuario extrae el texto del campo <combo> y lo almacena en un archivo <archivo>

  Ejemplos:
    | url                                         | time | combo       | opcionCombo | archivo                                          |  |
    | https://the-internet.herokuapp.com/dropdown | 5000 | id:dropdown | Option 2    | src/test/resources/DataDriven/OpcionDelCombo.txt |  |