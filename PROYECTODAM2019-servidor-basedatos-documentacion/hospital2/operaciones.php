<?php  
/*fichero que tengo que incluir para utilizar todas estas funciones*/
require_once('modelo/Paciente.php');
require_once('modelo/Especialista.php');
require_once('modelo/Cita.php');
require_once('modelo/Mutua.php');
require_once('modelo/FichaCita.php');

/*
Parametros que se necesitan para login:
	dni, password
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : { 	tipo: 'tipo',
				usuario : {datos del usuario recien registrado}
			}
*/
function login($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "ERR";
	$respuesta["mensaje"] = "mensaje";
	$respuesta["datos"]["tipo"] = "";
	$respuesta["datos"]["usuario"] = "";
	
	$paciente = Paciente::buscar_dni($datos->dni);
	$especialista = Especialista::buscar_dni($datos->dni);
	
	$usuario = ($paciente!=null) ? $paciente : $especialista;
	
	$md5_pass = md5($datos->password);
	
	if($usuario->password==$md5_pass) {
		$respuesta["estado"] = "OK";
		if($usuario instanceof Paciente)
			$respuesta["datos"]["tipo"] = "paciente";
		else
			$respuesta["datos"]["tipo"] = "especialista";
		
		$respuesta["datos"]["usuario"] = $usuario;
	}
	else {
		// no encontro, login incorrecto
		$respuesta["mensaje"] = "DNI o password invalidos";
	}
	/*respuesta es un array hasta que json_encode lo convierte en json*/
	/*json_encode es una funcion muy potente que convierte cualquier objeto o array en su cadena json asociada*/
	return json_encode($respuesta);
}

/*
Parametros que se necesitan para registro:
	todos los de un paciente, menos el ID
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : { 	tipo: 'tipo',
				usuario : {datos del usuario recien registrado}
			}
*/
function registro_paciente($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "ERR";
	$respuesta["mensaje"] = "mensaje";
	$respuesta["datos"]["tipo"] = "";
	$respuesta["datos"]["usuario"] = "";
	
	$paciente = new Paciente(-1, $datos->nombre, $datos->apellidos, $datos->tipo_documento, $datos->valor_documento, $datos->telefono, $datos->email, $datos->sociedad_medica, $datos->password);
	
	$paciente = $paciente->guardar();
	if($paciente) {
		$respuesta["estado"] = "OK";
		$respuesta["mensaje"] = "Paciente registrado";
		$respuesta["datos"]["tipo"] = "paciente";
		$respuesta["datos"]["usuario"] = $paciente;
	}
	else {
		$respuesta["mensaje"] = "Error en el registro";
	}
	
	return json_encode($respuesta);	
}

/*
Parametros que se necesitan para editar:
	todos los de un paciente, incluido el ID
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : { 	tipo: 'tipo',
				usuario : {datos del usuario recien registrado}
			}
*/
function editar_paciente($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "ERR";
	$respuesta["mensaje"] = "mensaje";
	$respuesta["datos"]["tipo"] = "";
	$respuesta["datos"]["usuario"] = "";
	
	$paciente = new Paciente($datos->id, $datos->nombre, $datos->apellidos, $datos->tipo_documento, $datos->valor_documento, $datos->telefono, $datos->email, $datos->sociedad_medica, $datos->password);
	
	$paciente = $paciente->editar();
	if($paciente) {
		$respuesta["estado"] = "OK";
		$respuesta["mensaje"] = "Paciente editado";
		$respuesta["datos"]["tipo"] = "paciente";
		$respuesta["datos"]["usuario"] = $paciente;
	}
	else {
		$respuesta["mensaje"] = "Error en el registro";
	}
	
	return json_encode($respuesta);	
}

/*
Parametros que se necesitan para eliminar:
	el ID del paciente
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
*/
function eliminar_paciente($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "ERR";
	$respuesta["mensaje"] = "mensaje";
		
	$res = Paciente::eliminar($datos->id);
	if($res) {
		$respuesta["estado"] = "OK";
		$respuesta["mensaje"] = "Paciente eliminado";
	}
	else {
		$respuesta["mensaje"] = "Error en el borrado";
	}
	
	return json_encode($respuesta);	
}

/*
Parametros que se necesitan para modificar contraseña:
	el ID del paciente, la nueva contraseña
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
*/
function modificar_contrasena($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "ERR";
	$respuesta["mensaje"] = "mensaje";
		
	$res = Paciente::modificar_contrasena($datos->id_paciente, $datos->nueva_contrasena);
	if($res==true) {
		$respuesta["estado"] = "OK";
		$respuesta["mensaje"] = "Contraseña modificada";
	}
	else {
		$respuesta["mensaje"] = "Error modificando contraseña";
	}
	
	return json_encode($respuesta);	
}

/*
Parametros que se necesitan para pedir cita:
	id_paciente, id_especialista, fecha, hora, id_mutua
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : la cita
*/
function confirmar_cita($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "ERR";
	$respuesta["mensaje"] = "mensaje";
	$respuesta["datos"]="";
	
	$paciente = Paciente::buscar_id($datos->id_paciente);
	$especialista = Especialista::buscar_id($datos->id_especialista);
	$mutua = Mutua::buscar_id($datos->id_mutua);
	$ficha = new FichaCita(-1,"","");
	
	$cita = new Cita(-1, $paciente, $especialista, $datos->fecha, $datos->hora, "PENDIENTE", $mutua, $ficha);
	
	$cita = $cita->guardar();
	if($cita) {
		$respuesta["estado"] = "OK";
		$respuesta["mensaje"] = "Cita realizada";
		$respuesta["datos"] = $cita;
	}
	else {
		$respuesta["mensaje"] = "Error en la cita";
	}

	return json_encode($respuesta);
}

/*
Parametros que se necesitan para completar cita:
	id_cita, observaciones, recomendaciones
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : la ficha generada
*/
function completar_cita($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "ERR";
	$respuesta["mensaje"] = "mensaje";
	$respuesta["datos"]="";

	$ficha = new FichaCita($datos->id_cita,$datos->observaciones,$datos->recomendaciones);
	
	$cita = Cita::cambiar_estado($datos->id_cita,"COMPLETADA");
	$ficha = $ficha->guardar();
	if($ficha) {
		$respuesta["estado"] = "OK";
		$respuesta["mensaje"] = "Cita completada";
		$respuesta["datos"] = $ficha;
	}
	else {
		$respuesta["mensaje"] = "Error en completar cita";
	}

	return json_encode($respuesta);
}

/*
Parametros que se necesitan para ver citas paciente:
	id_paciente, estado
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : array con las citas
*/
function citas_paciente($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "ERR";
	$respuesta["mensaje"] = "mensaje";
	$respuesta["datos"]=[];
	
	$lista = Cita::buscar_id_paciente($datos->id_paciente, $datos->estado);
	
	if($lista) {
		$respuesta["estado"] = "OK";
		$respuesta["mensaje"] = "Lista de citas solicitadas";
		$respuesta["datos"] = $lista;
	}
	else {
		$respuesta["mensaje"] = "Error en el servidor";
	}
	

	return json_encode($respuesta);	
}

/*
Parametros que se necesitan para ver citas especialista:
	id_especialista, estado
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : array con las citas
*/
function citas_especialista($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "ERR";
	$respuesta["mensaje"] = "mensaje";
	$respuesta["datos"]=[];
	
	$mes = "";
	if($datos->tipo=="MENSUAL") // solo se coge el mes si el tipo es mensual, para que no falle la cosa
		$mes = $datos->mes;
	$lista = Cita::buscar_id_especialista($datos->id_especialista, $mes, $datos->tipo);
	
	if($lista) {
		$respuesta["estado"] = "OK";
		$respuesta["mensaje"] = "Lista de citas solicitadas";
		$respuesta["datos"] = $lista;
	}
	else {
		$respuesta["mensaje"] = "Error en el servidor";
	}
	

	return json_encode($respuesta);	
}

/*
Parametros que se necesitan para ver lista especialistas:
	
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : array con los especialistas
*/
function lista_especialistas($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "OK";
	$respuesta["mensaje"] = "Lista de especialistas";
	$respuesta["datos"]=[];
	
	$lista = Especialista::lista();
	if($lista) {
		$respuesta["datos"]=$lista;
	}
	else {
		$respuesta["mensaje"] = "error en la conexion";
	}
	
	return json_encode(utf8ize($respuesta));	
}

/*
Parametros que se necesitan para ver lista pacientes:
	
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : array con los pacientes
*/
function lista_pacientes($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "OK";
	$respuesta["mensaje"] = "Lista de pacientes";
	$respuesta["datos"]=[];
	
	$lista = Paciente::lista();
	if($lista) {
		$respuesta["datos"]=$lista;
	}
	else {
		$respuesta["mensaje"] = "error en la conexion";
	}
	
	return json_encode(utf8ize($respuesta));	
}

/*
Parametros que se necesitan para ver lista mutuas:
	
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : array con las mutuas
*/
function lista_mutuas($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "OK";
	$respuesta["mensaje"] = "Lista de mutuas";
	$respuesta["datos"]=[];
	
	$lista = Mutua::lista();
	if($lista) {
		$respuesta["datos"]=$lista;
	}
	else {
		$respuesta["mensaje"] = "error en la conexion";
	}
	
	return json_encode(utf8ize($respuesta));	
}

/*
Parametros que se necesitan para anular una cita:
	id_cita
Respuesta
	JSON con campos
	estado : OK/ERR,
	mensaje: el mensaje
	datos : la cita anulada
*/
function anular_cita($datos) {
	
	$respuesta = [];
	$respuesta["estado"] = "OK";
	$respuesta["mensaje"] = "Cita anulada correctamente";
	$respuesta["datos"]=[];
	
	$status = Cita::cambiar_estado($datos->id_cita, "ANULADA");
	if($status) {
		$cita = Cita::buscar_id($datos->id_cita);
		$respuesta["datos"] = $cita ;
	}
	else {
		$respuesta["mensaje"] = "No se pudo anular la cita";
	}
	
	return json_encode($respuesta);	
}

function utf8ize($d) {
    if (is_array($d)) {
        foreach ($d as $k => $v) {
            $d[$k] = utf8ize($v);
        }
    } else if (is_string ($d)) {
        return utf8_encode($d);
    }
    return $d;
}

?>