# language: es
# encoding: utf-8

Característica: Validación de inicio de sesión

  @VALIDAR_INICIO_DE_SESION_CORRECTO
  Esquema del escenario: Inicio de sesión exitoso
    * El usuario navega a la pagina web <url>
    * El usuario valida que la pagina web actual sea <urlActual>
    * El usuario realiza un espera de <time>
    * El usuario escribe texto limpiando el cambo <campoUsuario> <usuario>
    * El usuario escribe texto limpiando el cambo <campoContrasena> <contrasena>
    * El usuario da click en el boton <btnLogin>
    * El usuario realiza un espera de <time>
    * El usuario da click en el boton <btnPrimerProducto>
    * El usuario da click en el boton <btnSegundoProducto>
    * El usuario realiza un scroll sin click en el elemento <campoCarrito>
    * El usuario valida el texto en el campo <campoCarrito> sea igual a <TextoBusqueda>
    * El usuario da click en el boton <btnPrimerProducto>
    * El usuario da click en el boton <btnSegundoRemove>
    * El usuario valida el texto en el campo <campoCarrito> sea igual a <TextoBusqueda2>
    * El usuario da click en el boton <btnCarrito>
    * El usuario valida que la pagina web actual sea <urlCarrito>

    Ejemplos:
      | url                        | urlActual                  | campoUsuario | usuario       | time  | campoContrasena | contrasena   | btnLogin                | btnPrimerProducto                                                     | btnSegundoProducto                     | campoCarrito               | TextoBusqueda | btnCarrito                                    | btnSegundoRemove                  | urlCarrito                          | TextoBusqueda2 |
      | https://www.saucedemo.com/ | https://www.saucedemo.com/ | id:user-name | standard_user | 10000 | name:password   | secret_sauce | //*[@id="login-button"] | /html/body/div/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/button | name:add-to-cart-sauce-labs-bike-light | id:shopping_cart_container | 2             | /html/body/div/div/div/div[1]/div[1]/div[3]/a | name:remove-sauce-labs-bike-light | https://www.saucedemo.com/cart.html | 0              |