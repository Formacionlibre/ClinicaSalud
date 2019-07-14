<?php  
require_once 'PHPMailer/PHPMailerAutoload.php';
require_once 'PHPMailer/class.phpmailer.php';

require_once('datosConexion.php');
require_once('Mutua.php');

class Paciente {
	
	public  $id;
	public  $nombre;
	public  $apellidos;
	public  $tipo_documento;
	public  $valor_documento;
	public  $telefono;
	public  $email;
	public  $sociedad_medica;
	public  $password;
	
	function __construct($id, $nombre, $apellidos, $tipo_documento, $valor_documento, $telefono, $email, $sociedad_medica, $password) {
       $this->id = $id;
	   $this->nombre = $nombre;
	   $this->apellidos = $apellidos;
	   $this->tipo_documento = $tipo_documento;
	   $this->valor_documento = $valor_documento;
	   $this->telefono = $telefono;
	   $this->email = $email;
	   $this->sociedad_medica = $sociedad_medica;
	   $this->password = $password;
	}
	
	public function guardar() {
		$obj = null;
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$this->password = md5($this->password);
		
		$sql = "INSERT INTO paciente VALUES(NULL,'" . $this->nombre . "','" . $this->apellidos . "','" . $this->tipo_documento . "','" . $this->valor_documento . "','" . $this->telefono . "','" . $this->email . "','" . $this->sociedad_medica . "','" .$this->password . "')";
		
		$res = $conn->query($sql);
		if($res  === TRUE) {
			$obj = Paciente::buscar_dni($this->valor_documento);
			Paciente::enviarEmailConfirmacion($obj,$obj->email); //enviar email
		}
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public function editar() {
		$obj = null;
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$sql = "UPDATE paciente SET nombre='" . $this->nombre . "', apellidos='" . $this->apellidos . "',tipo_documento='" . $this->tipo_documento . "',valor_documento='" . $this->valor_documento . "',telefono='" . $this->telefono . "',email='" . $this->email . "',sociedad_medica='" . $this->sociedad_medica . "' WHERE id='" . $this->id . "'";
		
		$res = $conn->query($sql);
		if($res  === TRUE) {
			$obj = Paciente::buscar_dni($this->valor_documento);
		}
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function modificar_contrasena($id, $pass) {
		$obj = false;
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$pass = md5($pass);
		
		$sql = "UPDATE paciente SET password='" . $pass . "' WHERE id='" . $id . "'";
		
		$res = $conn->query($sql);
		if($res  === TRUE) {
			$obj = true;
		}
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function eliminar($id) {
		$obj = false;
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		// antes de nada, eliminar sus citas
		$res = Cita::eliminar_id_paciente($id);
		// si la cosa fue bien, ya puedo eliminarlo
		if($res==true)
		{
			$sql = "DELETE FROM paciente WHERE id='" . $id . "'";
			//echo $sql;
			
			$res = $conn->query($sql);
			if($res  === TRUE) {
				$obj = true;
			}
		}
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	

	public static function buscar_id($id) {
		$obj = null;
		$sql = "SELECT * from paciente WHERE id='" . $id . "'";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);

		if(!empty($result) && $result->num_rows==1) {
			$row = $result->fetch_assoc();
			$obj = Paciente::from_row($row);
		}	
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function buscar_dni($dni) {
		$obj = null;
		$sql = "SELECT * from paciente WHERE valor_documento='" . $dni . "'";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);

		if(!empty($result) && $result->num_rows==1) {
			$row = $result->fetch_assoc();
			$obj = Paciente::from_row($row);
		}	
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function lista() {
		$lista = [];
		$sql = "SELECT * from paciente";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);
		if(!empty($result) && $result->num_rows>0) {
			while($row = $result->fetch_assoc()) {
				$obj = Paciente::from_row($row);
				array_push($lista,$obj);
			}		
		}	
		
		cerrar_conexion($conn);
		
		return $lista;
	}
	
	public static function from_row($row) {
		
		$mutua = Mutua::buscar_id($row["sociedad_medica"]);
		$obj = new Paciente($row["id"],$row["nombre"],$row["apellidos"],$row["tipo_documento"],$row["valor_documento"],$row["telefono"],$row["email"],$mutua,$row["password"]);
		return $obj;
	}
	
	public static function enviarEmailConfirmacion($paciente,$email)
	{
		
		$body = "<b>Email de confirmacion de registro</b><br /><br />";
		//$body.= "<b>E-Mail: </b>" . $email . "<br />";
		$body.= "<b>Paciente: </b>" . $paciente->nombre . " " . $paciente->apellidos . " con DNI "  . $paciente->valor_documento . " y password ". $paciente->password .  "<br /><br />";
		
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