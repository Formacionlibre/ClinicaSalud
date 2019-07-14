<?php  

require_once('datosConexion.php');

class FichaCita {
	
	public  $id_cita;
	public  $observaciones;
	public  $recomendaciones;
	
	function __construct($id_cita,$observaciones,$recomendaciones) {
       $this->id_cita = $id_cita;
	   $this->observaciones = $observaciones;
	   $this->recomendaciones = $recomendaciones;
	}
	
	public function guardar() {
		$obj = null;
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$sql = "INSERT INTO ficha_cita VALUES('" . $this->id_cita . "','" . $this->observaciones . "','" . $this->recomendaciones . "')";

		$res = $conn->query($sql);
		if($res  === TRUE) {
			$obj = FichaCita::buscar_id($this->id_cita);
		}
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function buscar_id($id) {
		$obj = null;
		$sql = "SELECT * from ficha_cita WHERE id_cita='" . $id . "'";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$result = $conn->query($sql);

		if(!empty($result) && $result->num_rows==1) {
			$row = $result->fetch_assoc();
			$obj = FichaCita::from_row($row);
		}	
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function eliminar_id($id) {
		$obj = false;
		$sql = "DELETE FROM ficha_cita WHERE id_cita='" . $id . "'";
		$conn = abrir_conexion();
		if(!$conn)
			return null;
		
		$res = $conn->query($sql);

		if($res  === TRUE) {
			$obj = true;
		}	
		
		cerrar_conexion($conn);
		
		return $obj;
	}
	
	public static function from_row($row) {		
		$obj = new FichaCita($row["id_cita"],$row["observaciones"],$row["recomendaciones"]);
		return $obj;
	}
}

?>