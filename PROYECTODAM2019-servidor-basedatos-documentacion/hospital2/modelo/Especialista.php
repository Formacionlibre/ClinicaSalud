<?php  

require_once('datosConexion.php');

class Especialista {
	
	public  $id;
	public  $nombre;
	public  $apellidos;
	public  $especialidad;
	public  $salario;
	public  $tipo;
	public  $dni;
	public  $password;
	
	function __construct($id, $nombre, $apellidos, $especialidad, $salario, $tipo, $dni, $password) {
       $this->id = $id;
	   $this->nombre = $nombre;
	   $this->apellidos = $apellidos;
	   $this->especialidad = $especialidad;
	   $this->salario = $salario;
	   $this->tipo = $tipo;
	   $this->dni = $dni;
	   $this->password = $password;
	}
	
	public static function buscar_id($id) {
		$obj = null;
		$sql = "SELECT * from especialista WHERE id='" . $id . "'";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);

		if(!empty($result) && $result->num_rows==1) {
			$row = $result->fetch_assoc();
			$obj = Especialista::from_row($row);
		}	
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function buscar_dni($dni) {
		$obj = null;
		$sql = "SELECT * from especialista WHERE dni='" . $dni . "'";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);
		if(!empty($result) && $result->num_rows==1) {
			$row = $result->fetch_assoc();
			$obj = Especialista::from_row($row);
		}	
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function lista() {
		$lista = [];
		$sql = "SELECT * from especialista";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);
		if(!empty($result) && $result->num_rows>0) {
			while($row = $result->fetch_assoc()) {
				$obj = Especialista::from_row($row);
				array_push($lista,$obj);
			}		
		}	
		
		cerrar_conexion($conn);
		
		return $lista;
	}
	
	public static function from_row($row) {
		$obj = new Especialista($row["id"],$row["nombre"],$row["apellidos"],$row["especialidad"],$row["salario"],$row["tipo"],$row["dni"],$row["password"]);
		return $obj;
	}
}

?>