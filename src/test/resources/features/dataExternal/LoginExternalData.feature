# language: es
@E4 @DataExternal @CSV
Característica: E4 - Datos externos desde CSV
  Como QA automatizador
  Quiero cargar datos de prueba desde archivos externos (CSV)
  Para facilitar el mantenimiento y permitir que no-técnicos editen datasets

  Antecedentes:
    Dado que los datos de prueba están cargados desde "testdata/login_datasets.csv"

  @Smoke
  Escenario: Login con dataset desde CSV - Caso positivo
    Cuando ejecuto el test con el dataset "OK_admin"
    Entonces el resultado del login desde CSV debería ser "SUCCESS"
    Y desde CSV si es éxito debería redirigir al inventario

  @Regression
  Escenario: Login con dataset desde CSV - Password incorrecta
    Cuando ejecuto el test con el dataset "NEG_pwd"
    Entonces el resultado del login desde CSV debería ser "ERROR"
    Y desde CSV si es error debería mostrarse un mensaje de error

  @Regression
  Escenario: Login con dataset desde CSV - Usuario inexistente
    Cuando ejecuto el test con el dataset "NEG_user"
    Entonces el resultado del login desde CSV debería ser "ERROR"
    Y desde CSV si es error debería mostrarse un mensaje de error

  @Regression
  Escenario: Login con dataset desde CSV - Campos vacíos
    Cuando ejecuto el test con el dataset "NEG_empty"
    Entonces el resultado del login desde CSV debería ser "ERROR"
    Y desde CSV si es error debería mostrarse un mensaje de error

  @Regression
  Escenario: Login con dataset desde CSV - Usuario bloqueado
    Cuando ejecuto el test con el dataset "NEG_locked"
    Entonces el resultado del login desde CSV debería ser "ERROR"
    Y desde CSV si es error debería mostrarse un mensaje de error
