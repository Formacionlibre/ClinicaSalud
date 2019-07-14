<?php
/*datos necesarios para la conexion con la base de datos y la funcion que abre y cierra conexion*/
 
define("SERVERNAME", "localhost");
define("USER", "root");
define("PASSWORD", "");
define("DBNAME", "hospital");

function abrir_conexion() {
	$enlace =  new mysqli(SERVERNAME, USER, PASSWORD, DBNAME);
	if ($enlace->connect_error) {
		return null;
	}
	else {
		$enlace->set_charset("utf8");
		return $enlace;
	}
}

function cerrar_conexion($enlace) {
	if($enlace)
		$enlace->close();
}

?>