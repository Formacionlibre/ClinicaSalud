<?php 
/*
este fichero recibe la peticion del cliente y en funcion de lo que tenga el campo operacion llama a una funcion u otra
del modulo operaciones.php al que le pasa el campo datos.

*/

require("operaciones.php");


// Leer peticion json del cliente
$json = file_get_contents('php://input');

// Convertirlo a objeto PHP para leerlo mas facilmente
$data = json_decode($json);


if($data->operacion == 'login') {
	echo login($data->datos);
}
else if($data->operacion == 'registro_paciente') {
	echo registro_paciente($data->datos);
}
else if($data->operacion == 'editar_paciente') {
	echo editar_paciente($data->datos);
}
else if($data->operacion == 'eliminar_paciente') {
	echo eliminar_paciente($data->datos);
}
else if($data->operacion == 'modificar_contrasena') {
	echo modificar_contrasena($data->datos);
}
else if($data->operacion == 'citas_paciente') {
	echo citas_paciente($data->datos);
}
else if($data->operacion == 'citas_especialista') {
	echo citas_especialista($data->datos);
}
else if($data->operacion == 'lista_especialistas') {
	echo lista_especialistas(null);;
}
else if($data->operacion == 'lista_pacientes') {
	echo lista_pacientes(null);;
}
else if($data->operacion == 'lista_mutuas') {
	echo lista_mutuas(null);;
}
else if($data->operacion == 'confirmar_cita') {
	echo confirmar_cita($data->datos);
}
else if($data->operacion == 'completar_cita') {
	echo completar_cita($data->datos);
}
else if($data->operacion == 'anular_cita') {
	echo anular_cita($data->datos);
}
else if($data->operacion == 'prueba') {
	//echo prueba(1);
	echo "sin implementar";
}
else { //operacion desconocida
	$respuesta = [];
	$respuesta["estado"] = "ERR";
	$respuesta["mensaje"] = "Operacion desconocida";
	$respuesta["datos"] = [];
	return json_encode($respuesta);
}

?>