# easyTable

A continuación un ejemplo de como utilizar la librería.

``` java
// Declarar un atributo con los atributos que serán utilizados como columnas
final private String COLUMNAS[] = {
		"id",
		"nombre+apellido=>Nombre y Apellido", // Concatenando dos atributos (+) y asignando un nombre alternativo (=>)
		"documento",
		"fechaRegistro",
		"activo"
	};
  
  // Instanciando el componente
  private MyTable table = new MyTable();
  
  // Configurando la tabla
  table.config(COLUMNAS);
  
  // La columna 4 se pinta en verde si su valor es true 
  table.agregarCondicion(4, Color.green, Comparacion.EQ, true);
  
  // La columna 4 se pinta en rojo si su valor no es true
  table.agregarCondicion(4, Color.red, Comparacion.NE, true);
  ```
