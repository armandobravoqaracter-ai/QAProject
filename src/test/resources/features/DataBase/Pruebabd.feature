#language: es
# encoding: utf-8
@VALIDAR_DB_POSTGRES
Requisito: Como usuario necesito validar datos almacenados en PostgreSQL

  Esquema del escenario: Validar información en base de datos
    * El usuario se conecta a PostgreSQL con host <host>, puerto <port>, bd <database>, usuario <user> y password <password>
    * El usuario ejecuta la consulta SQL <query>
    * El usuario valida que el campo <columna> sea <valorEsperado>

    Ejemplos:
      | host      | port | database | user    | password | query                                    | columna | valorEsperado |
      | localhost | 5432 | qa_db    | qa_user | qa_pass  | SELECT nombre FROM usuarios WHERE id = 1 | nombre  | Armand        |
