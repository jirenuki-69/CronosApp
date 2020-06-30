<?php
defined('BASEPATH') or exit('No direct script access allowed');

require APPPATH . 'libraries\REST_Controller.php';
require APPPATH . 'libraries\Format.php';

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");

class Api extends REST_Controller
{
    public function __construct()
    {
        // Construct the parent class
        parent::__construct();
        $this->load->model('ApiModel');
        $this->load->helper(['jwt', 'authorization']);
    }

    public function index_get()
    {
        # code...
        $data = [
            ['mensaje' => 'Bienveido a nuestra API'],
            ['Version' => '1.0.0'],
        ];

        $this->response([
            'status' => true,
            'data' => $data,
        ], 200);
    }

    public function loginprofesor_post() //INICIAR SESION = http://localhost/foxweb/Api/login
    {
        // Extract user data from POST request
        $matricula = $this->post('matricula');
        $password = $this->post('password');
        $type = $this->post('type');

        $datauser = $this->ApiModel->flogin($matricula, $password, $type);

        if ($datauser != false) {
            foreach ($datauser as $data) {
                $matricula = $data['matricula'];
                $nombre = $data['nombre'];
                $apellidos = $data['apellidos'];
            }

            $response = ['error' => false, 'status' => parent::HTTP_OK, "matricula" => $matricula, "nombre" => $nombre, "apellidos" => $apellidos];

            $this->response($response, parent::HTTP_OK);

        } else {

            $response = ['error' => true,'status' => parent::HTTP_NOT_FOUND, 'msg' => 'Usuario o Contraseña Invalidos!'];

            $this->response($response, parent::HTTP_NOT_FOUND);

        }
    }

    public function loginalumno_post() //INICIAR SESION = http://localhost/foxweb/Api/login
    {
        // Extract user data from POST request
        $matricula = $this->post('matricula');
        $password = $this->post('password');
        $type = $this->post('type');

        $datauser = $this->ApiModel->flogin($matricula, $password, $type);

        if ($datauser != false) {
            foreach ($datauser as $data) {
                $matricula = $data['matricula'];
                $nombre = $data['nombre'];
                $apellidos = $data['apellidos'];
                $num_grupo = $data['num_grupo'];
            }

            $response = ['error' => false, 'status' => parent::HTTP_OK, "matricula" => $matricula, "nombre" => $nombre, "apellidos" => $apellidos, "num_grupo" => $num_grupo];

            $this->response($response, parent::HTTP_OK);

        } else {

            $response = ['error' => true,'status' => parent::HTTP_NOT_FOUND, 'msg' => 'Usuario o Contraseña Invalidos!'];

            $this->response($response, parent::HTTP_NOT_FOUND);

        }
    }

    public function registro_post() // REGISTRO DE PERSONAS = http://localhost/foxweb/Api/registro
    {
        $matricula = $this->post('matricula');
        $nombre = $this->post('nombre');
        $apellidos = $this->post('apellidos');
        $password = $this->post('password');
        $numgrupo = $this->post('numgrupo'); //SI ES PROFESOR ES 0
        $nombremateria = $this->post('nombremateria'); //SI ES PROFESOR ES 0
        $type = $this->post('type');

        if($type == 'PROFESOR'){
            //AGREGAR AL PROFESOR Y RETORNAR MENSAJE CORRECTO O MALO
            $profesor = $this->ApiModel->registroProfesor($matricula,$nombre,$apellidos,$password, $nombremateria);

            if($profesor){ //ES QUE SI SE AGREGO
                $this->response([
                    'error' => false,
                    'status' => "registrado",
                    'message' => 'Profesor Registrado Satisfactoriamente'
                ], 200 );
            }else{
                $this->response([
                    'error' => true,
                    'status' => "noregistrado",
                    'message' => 'Profesor Existente u Otro Error.'
                ], 400 );
            }
        }else{
            //AGREGAR AL ALUMNO Y RETORNAR MENSAJE CORRECTO O MALO
            $alumno = $this->ApiModel->registroAlumno($matricula,$nombre,$apellidos,$password,$numgrupo);
            if($alumno){ //ES QUE SI SE AGREGO
                $this->response([
                    'error' => false,
                    'status' => "registrado",
                    'message' => 'Alumno Registrado Satisfactoriamente'
                ], 200 );
            }else{
                $this->response([
                    'error' => true,
                    'status' => "noregistrado",
                    'message' => 'Profesor Existente u Otro Error.'
                ], 400 );
            }
        }
    }

    public function materia_post()
    {
        $matriculaprofesor = $this->post('matriculaprofesor');
        $nombremateria = $this->post('nombremateria');
        $materia = $this->ApiModel->registrarMateria($matriculaprofesor, $nombremateria);

        if($materia){ //ES QUE SI SE AGREGO
            $this->response([
                'error' => false,
                'status' => "registrado",
                'message' => 'Materia Registrada Satisfactoriamente'
            ], 200 );
        }else{
            $this->response([
                'error' => true,
                'status' => "noregistrado",
                'message' => 'Materia no Registrada'
            ], 400 );
        }
    }

    public function materias_get()
    {
        // Users from a data store e.g. database
        $iddocente = $this->get( 'iddocente' );

        if ( $iddocente !== null )
        {
            $materias = $this->ApiModel->obtenerMateriasDelDocente($iddocente);

            $this->response($materias, 200 );
        }else{
            $this->response([
                'error' => true,
                'status' => "noencontrado",
                'message' => 'Materias del Profesor no encontradas'
            ], 400 );
        }
    }

    public function materia_get()
    {
        // Users from a data store e.g. database
        $idmateria = $this->get( 'idmateria' );

        if ( $idmateria !== null )
        {
            $materia = $this->ApiModel->obtenerMateria($idmateria);

            $this->response([
                'error' => false,
                'status' => "encontrado",
                'data' => $materia
            ], 400 );
        }else{
            $this->response([
                'error' => true,
                'status' => "noencontrado",
                'message' => 'Materia no encontrada'
            ], 400 );
        }
    }

    public function grupos_get()
    {
        $grupos = $this->ApiModel->obtenerGrupos();

        $this->response($grupos, 200 );
    }

    public function gruposregistro_get()
    {
        $grupos = $this->ApiModel->obtenerGrupos();

        $this->response([
            'error' => false,
            'status' => "encontrado",
            'grupos' => $grupos
        ], 200 );
    }
    
    public function actualizarmateria_post()
    {
        $idmateria = $this->post('idmateria');
        $nuevonombremateria = $this->post('nuevonombremateria');
        $materiaactualizada = $this->ApiModel->actualizarMateria($idmateria, $nuevonombremateria);

        if($materiaactualizada){ //ES QUE SI SE AGREGO
            $this->response([
                'error' => false,
                'status' => "actualizada",
                'message' => 'Materia Actualizada Satisfactoriamente'
            ], 200 );
        }else{
            $this->response([
                'error' => true,
                'status' => "actualizada",
                'message' => 'Materia no Actualizada'
            ], 200 );
        }
    }

    public function crearclase_post()
    {
        $matricula_profesor = $this->post('matricula_profesor');

        $num_materia = $this->post('num_materia');

        $num_grupo = $this->post('num_grupo');

        $clase = $this->ApiModel->crearClase($matricula_profesor, $num_materia, $num_grupo);

        if($clase != false){ //ES QUE SI SE AGREGO

            $datosadevolver = explode("-", $clase);
            $this->response([
                'error' => false,
                'status' => "creada",
                'message' => 'Clase Creada',
                'id_clase' => $datosadevolver[0],
                'hora_limite' => $datosadevolver[1]
            ], 200 );
        }else{
            $this->response([
                'error' => true,
                'status' => "nocreada",
                'message' => 'Clase no Creada'
            ], 200 );
        }
    }

    public function generarasistencia_post()
    {
        $matricula_alumno = $this->post('matricula_alumno');		

        $idclase = $this->post('idclase');

        $asistencia = $this->ApiModel->crearAsistencia($matricula_alumno, $idclase);

        if($asistencia != false){ //ES QUE SI SE AGREGO
            if($asistencia == 1){ //TODO BIEN
                $this->response([
                    'error' => false,
                    'status' => "agregada",
                    'message' => 'Asistencia Correcta'
                ], 200 );
            }else if($asistencia == 2){ //YA HAY ASISTENCIA
                $this->response([
                    'error' => false,
                    'status' => "asistencia",
                    'message' => 'Ya te has registrado a esta clase'
                ], 200 );
            }else if($asistencia == 3){ //YA PASO LA HORA
                $this->response([
                    'error' => false,
                    'status' => "yapaso",
                    'message' => 'Tiempo de Asistencia excedido.'
                ], 200 );
            }
        }else{
            $this->response([
                'error' => true,
                'status' => "nocreada",
                'message' => 'Asistencia no Creada'
            ], 200 );
        }
    }

    public function asistentes_get()
    {
        // Users from a data store e.g. database
        $idclase = $this->get( 'idclase' );

        if ( $idclase !== null )
        {
            $asistentes = $this->ApiModel->obtenerAsistentes($idclase);

            $this->response($asistentes, 200 );
        }else{
            $this->response([
                'error' => true,
                'status' => "noencontrado",
                'message' => 'Asistentes no encontrados'
            ], 400 );
        }
    }

    public function clases_get()
    {
        // Users from a data store e.g. database
        $matriculaprofesor = $this->get( 'matriculaprofesor' );
        
        $numgrupo = $this->get( 'numgrupo' );

        $nummateria = $this->get( 'nummateria' );

        if ( $matriculaprofesor !== null && $numgrupo !== null && $nummateria !== null)
        {
            $clases = $this->ApiModel->obtenerClases($matriculaprofesor,$numgrupo,$nummateria);

            $this->response($clases, 200 );
        }else{
            $this->response([
                'error' => true,
                'status' => "noencontrado",
                'message' => 'Clases no encontrados'
            ], 400 );
        }
    }
}
