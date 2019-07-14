<?php  
/*require_once - hace que solo incluya el primer fichero si es q hay varios*/
require_once('datosConexion.php');
require_once 'PHPMailer/PHPMailerAutoload.php';
require_once 'PHPMailer/class.phpmailer.php';

class Cita {
	
	public  $id;
	public  $paciente;
	public  $especialista;
	public  $fecha;
	public  $hora;
	public  $estado;
	public  $mutua;
	public  $ficha;
	
	function __construct($id,$paciente, $especialista, $fecha, $hora, $estado, $mutua, $ficha) {
       $this->id = $id;
	   $this->paciente = $paciente;
	   $this->especialista = $especialista;
	   $this->fecha = $fecha;
	   $this->hora = $hora;
	   $this->estado = $estado;
	   $this->mutua = $mutua;
	   $this->ficha = $ficha;
	}
	/*funciones que hacen querys a la base de datos*/
	
	public function guardar() {
		$obj = null;
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$sql = "INSERT INTO cita VALUES(NULL,'" . $this->paciente->id . "','" . $this->especialista->id . "','" . $this->fecha . "','" . $this->hora . "','PENDIENTE','" . $this->mutua->id . "')";
		
		$res = $conn->query($sql);
		if($res  === TRUE) {
			$id = $conn->insert_id; // id recien generado
			$obj = Cita::buscar_id($id);
			Cita::enviarEmailConfirmacion($obj,$this->paciente->email); //enviar email
		}
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	/*from_row es una funcion muy util que lo que hace es devolver un objeto a partir de los datos de un registro de una cita*/
	public static function buscar_id($id) {
		$obj = null;
		$sql = "SELECT * from cita WHERE id='" . $id . "'";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);

		if(!empty($result) && $result->num_rows==1) {
			$row = $result->fetch_assoc();
			$obj = Cita::from_row($row);
		}	
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function buscar_id_paciente($id, $estado="TODAS") {
		$lista = [];
		$sql = "SELECT * from cita WHERE id_paciente='" . $id . "'";
		if($estado !== "TODAS")
			$sql .= " AND estado='" . $estado . "'";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);

		if(!empty($result) && $result->num_rows>0) {
			while($row = $result->fetch_assoc())
			{
				$obj = Cita::from_row($row);
				array_push($lista,$obj);
			}
		}	
		
		cerrar_conexion($conn);
		
		return $lista;
	}
	
	public static function buscar_id_especialista($id, $mes, $tipo="TODAS" ) {
		$lista = [];
		$sql = "SELECT * FROM cita WHERE id_especialista='" . $id . "'";
		if($tipo == "MENSUAL") {
			$sql .= " AND MONTH(fecha)='" . $mes . "'";
		}
		else if($tipo == "SEMANAL") {
			$sql .= " AND fecha <= DATE_ADD(curdate(), INTERVAL 7 DAY) AND fecha >= curdate()";
		}
		else if($tipo == "DIARIO") {
			$sql .= " AND fecha = CURDATE()";
		}
		// else todas
		
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);

		if(!empty($result) && $result->num_rows>0) {
			while($row = $result->fetch_assoc())
			{
				$obj = Cita::from_row($row);
				array_push($lista,$obj);
			}
		}	
		
		cerrar_conexion($conn);
		
		return $lista;
	}
	
	public static function cambiar_estado($id, $estado) {
		$status = true;
		$sql = "UPDATE cita SET estado='" . $estado . "' WHERE id='" . $id . "'";
		//echo $sql;
		$conn = abrir_conexion();
		if(!$conn)
			return false;
		
		$result = $conn->query($sql);
		if($result == TRUE)
			$status = true;
		
		cerrar_conexion($conn);
		
		return $status;
	}
	
	public static function eliminar_id_paciente($id_paciente) {
		$obj = true;
		$citas_paciente = Cita::buscar_id_paciente($id_paciente);
		
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		foreach($citas_paciente as $c) {
			FichaCita::eliminar_id($c->id); //elimino su ficha antes de eliminar a la propia cita

			// ya puedo eliminar esta cita
			$sql = "DELETE FROM cita WHERE id='" . $c->id . "'";
			$res = $conn->query($sql);

			if($res  === FALSE) {
				$obj = false;
			}			
		}
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function from_row($row) {		
		$mutua = Mutua::buscar_id($row["id_mutua"]);
		$paciente = Paciente::buscar_id($row["id_paciente"]);
		$especialista = Especialista::buscar_id($row["id_especialista"]);
		$ficha = new FichaCita($row["id"],"","");
		if($row["estado"]=="COMPLETADA") // solo si esta completada hay una ficha
			$ficha = FichaCita::buscar_id($row["id"]); //id_cita es igual que id de la cita	
		$obj = new Cita($row["id"],$paciente,$especialista ,$row["fecha"],$row["hora"],$row["estado"],$mutua,$ficha);
		return $obj;
	}
	
	public static function enviarEmailConfirmacion($cita,$email)
	{
		
		$body = "<b>Email de confirmacion de cita</b><br /><br />";
		//$body.= "<b>E-Mail: </b>" . $email . "<br />";
		$body.= "<b>Cita: </b>" . $cita->fecha . " (" . $cita->hora . ") con "  . $cita->especialista->nombre . " ". $cita->especialista->apellidos .  "<br /><br />";
		
		//echo $body;
		
		$mail = new PHPMailer;
		
		// SMTP configuration
		
		$mail->isSMTP();
		//$mail->isSendMail();
		//$mail->SMTPDebug = 2; //para debug
		
		$mail->SMTPOptions = array(
							'ssl' => array(
							'verify_peer' => false,
							'verify_peer_name' => false,
							'allow_self_signed' => true
						)
					);
		
		$mail->Host = 'smtp.gmail.com';
		$mail->SMTPAuth = true;
		$mail->Username = 'proyectodamclinicasalud@gmail.com';
		$mail->Password = 'proyecto123456';
		$mail->SMTPSecure = 'tls';
		$mail->Port = 587;
		
		$mail->setFrom('proyectodamclinicasalud@gmail.com', 'Soporte Hospital');
		$mail->addReplyTo('proyectodamclinicasalud@gmail.com', 'Soporte Hospital');

		// Add a recipient
		$mail->addAddress($email);

		// Email subject
		$mail->Subject = 'Soporte Hospital. Cita confirmada';

		// Set email format to HTML
		$mail->isHTML(true);

		// Email body content
		$mail->Body = $body;
		// Send email
		//return $mail->send();
		
		if(!$mail->send()){
			//echo 'Message could not be sent.';
			//echo 'Mailer Error: ' . $mail->ErrorInfo;
			return false;
		}else{
			//echo 'Message has been sent';
			return true;
		}
	}
}

?>