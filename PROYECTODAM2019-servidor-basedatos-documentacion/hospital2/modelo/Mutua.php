<?php  

require_once('datosConexion.php');

class Mutua {
	
	public  $id;
	public  $nombre;
	public  $precio_cita;
	
	function __construct($id, $nombre, $precio_cita) {
       $this->id = $id;
	   $this->nombre = $nombre;
	   $this->precio_cita = $precio_cita;
	}
	
	public static function buscar_id($id) {
		$obj = null;
		$sql = "SELECT * from mutua WHERE id='" . $id . "'";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);

		if(!empty($result) && $result->num_rows==1) {
			$row = $result->fetch_assoc();
			$obj = Mutua::from_row($row);
		}	
		
		cerrar_conexion($conn);
		
		return $obj;

	}
	
	public static function lista() {
		$lista = [];
		$sql = "SELECT * from mutua";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);
		if(!empty($result) && $result->num_rows>0) {
			while($row = $result->fetch_assoc()) {
				$obj = Mutua::from_row($row);
				array_push($lista,$obj);
			}		
		}	
		
		cerrar_conexion($conn);
		
		return $lista;
	}
	
	public static function from_row($row) {
		$obj = new Mutua($row["id"],$row["nombre"],$row["precio_cita"]);
		return $obj;
	}
	
}

?>