<?php
defined('BASEPATH') or exit('No direct script access allowed');

class ApiModel extends CI_model
{

    public function __construct()
    {
        parent::__construct();
    }

    public function flogin($matricula, $password, $type)
    {
        if ($type == "PROFESOR") {
            $this->db->select('matricula, nombre, apellidos');
            $this->db->where('matricula', $matricula);
            $this->db->where('contraseña', $password);
            $query = $this->db->get('profesor');
            if ($query->num_rows() > 0) {
                return $query->result_array();
            } else {
                return false;
            }
        } else if ($type == "ALUMNO") {
            $this->db->select('matricula, nombre, apellidos,num_grupo');
            $this->db->where('matricula', $matricula);
            $this->db->where('contraseña', $password);
            $query = $this->db->get('alumno');
            if ($query->num_rows() > 0) {
                return $query->result_array();
            } else {
                return false;
            }
        } else {
            return "Metodo no existente";
        }
    }

    public function registroProfesor($matricula,$nombre,$apellidos,$password, $nombremateria)
    {
        $consulta = "SELECT * FROM `profesor` WHERE matricula = $matricula";
        //echo $consulta;
        $sql = $this->db->query($consulta);
        if ($sql->num_rows() > 0) {
            return false;
        }else{
            $profesor = array(
                'matricula' => $matricula,
                'nombre' => $nombre,
                'apellidos' => $apellidos,
                'contraseña' => $password
            );
    
            $primermateria = array(
                'matricula_profesor' => $matricula,
                'num_materia' => NULL,
                'nombre_materia' => $nombremateria
            );
    
            if($this->db->insert("profesor", $profesor) && $this->db->insert("materia", $primermateria)){
                return true;
            }else{
                return false;
            }
        }
    }

    public function registroAlumno($matricula,$nombre,$apellidos,$password,$numgrupo)
    {
        $consulta = "SELECT * FROM `alumno` WHERE matricula = $matricula";
        //echo $consulta;
        $sql = $this->db->query($consulta);
        if ($sql->num_rows() > 0) {
            return false;
        }else{
            $alumno = array(
                'matricula' => $matricula,
                'nombre' => $nombre,
                'apellidos' => $apellidos,
                'contraseña' => $password,
                'num_grupo' => $numgrupo
            );
    
            if($this->db->insert("alumno", $alumno)){
                return true;
            }else{
                return false;
            }
        }
    }

    public function registrarMateria($matriculaprofesor, $nombremateria)
    {
        $materia = array(
            'matricula_profesor' => $matriculaprofesor,
            'num_materia' => NULL,
            'nombre_materia' => $nombremateria
        );

        if($this->db->insert("materia", $materia)){
            return true;
        }else{
            return false;
        }
    }

    public function obtenerMateriasDelDocente($matriculaprofesor)
    {
        $query = $this->db->query("SELECT num_materia,nombre_materia FROM `materia` WHERE matricula_profesor = ".$matriculaprofesor);
        
        return $query->result_array();
    }

    public function obtenerMateria($idmateria)
    {
        $query = $this->db->query("SELECT num_materia,nombre_materia FROM `materia` WHERE num_materia = ".$idmateria);
        
        return $query->result_array();
    }

    public function obtenerGrupos()
    {
        $query = $this->db->query("SELECT * FROM grupo");
        
        return $query->result_array();
    }

    public function actualizarMateria($idmateria, $nuevonombremateria)
    {
        $materia = array(
			'nombre_materia' => $nuevonombremateria
        );
        
        $this->db->where('num_materia',$idmateria);
        
        if($this->db->update('materia',$materia)){
            return true;
        }else{
            return false;
        }
    }

    public function crearClase($matricula_profesor, $num_materia, $num_grupo)
    {
        $date = new DateTime();

        $hora_actual = $date->format('H:i:s');
        
        $date->modify('+7 minute');

        $hora_con7m = $date->format('H:i:s');

        $clase = array(
            'id_clase' => NULL,
            'matricula_profesor' => $matricula_profesor,
            'num_materia' => $num_materia,
            'num_grupo' => $num_grupo,
            'hora_generadaqr' => $hora_actual,
            'hora_limiteqr' => $hora_con7m
        );

        if($this->db->insert("clase", $clase)){
            return "".$this->db->insert_id()."-".date('h:i:s a', strtotime($hora_con7m));
        }else{
            return false;
        }
    }

    public function crearAsistencia($matricula_alumno, $idclase)
    {
        $date = new DateTime();

        $fecha_actual = $date->format('Y-m-d');

        $hora_actual = $date->format('H:i:s');

        //BUSCAR SI YA ASISTI A ESTA CLASE
        $consulta = "SELECT * FROM `asistencia` WHERE matricula_alumno = ".$matricula_alumno." AND idclase = ".$idclase;

        $consulta_de_tardia = "SELECT hora_limiteqr FROM `clase` WHERE id_clase = ".$idclase;

        $asdasdas =  $this->db->query($consulta_de_tardia);

        $ret = $asdasdas->row();

        $hora_vencimiento = $ret->hora_limiteqr;

        $hora1 = strtotime( $hora_actual );
        $hora2 = strtotime( $hora_vencimiento );

        if( $hora1 > $hora2 ) {
            return 3; //SIGNIFICA QUE YA PASO LA HORA   
        } else {
            //echo $consulta;
            $sql = $this->db->query($consulta);
            if ($sql->num_rows() > 0) { //SI ENTRA AQUI SIGNIFCA QUE YA REGISTRO => RETORNAMOS UN 2 QUE ES QUE YA ASISTIO
                return 2;   
            }else{ //SI NO ASISTIO ENTONCES PROCEDEMOS A ASISITRLO
                $asistencia = array( 				
                    'id_asistencia' => NULL,
                    'fecha' => $fecha_actual,
                    'hora' => $hora_actual,
                    'matricula_alumno' => $matricula_alumno,
                    'idclase' => $idclase
                );
        
                if($this->db->insert("asistencia", $asistencia)){ //SI TODO SALIO BIEN => RETORNAMOS UN 1 QUE SIGNIFICA QUE TODO EXCELENTE
                    return 1;
                }else{
                    return false; //RETORNAMOS UN FALSE QUE SIGNIFICA QUE ALGO SALIO MAL
                }
            }   
        }
    }

    public function obtenerAsistentes($idclase)
    {
        $query = $this->db->query("SELECT alumno.* FROM asistencia INNER JOIN clase ON (clase.id_clase = asistencia.idclase) INNER JOIN alumno ON (alumno.matricula = asistencia.matricula_alumno) WHERE clase.id_clase = ".$idclase);
        
        if($query->num_rows() > 0){
            return $query->result_array();
        }else{
            return null;
        }
        
    }

    public function obtenerClases($matriculaprofesor,$numgrupo,$nummateria)
    {
        $query = $this->db->query("SELECT * FROM `clase` WHERE matricula_profesor = '".$matriculaprofesor."' AND num_materia = '".$nummateria."' AND num_grupo = ".$numgrupo);
        
        if($query->num_rows() > 0){
            return $query->result_array();
        }else{
            return null;
        }
    }
}