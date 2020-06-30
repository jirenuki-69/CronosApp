-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-06-2020 a las 04:11:20
-- Versión del servidor: 10.4.8-MariaDB
-- Versión de PHP: 7.3.10


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
--
-- Base de datos: `api_cronos`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumno`
--

CREATE TABLE `alumno` (
  `matricula` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `contraseña` varchar(45) NOT NULL,
  `num_grupo` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `alumno`
--

INSERT INTO `alumno` (`matricula`, `nombre`, `apellidos`, `contraseña`, `num_grupo`) VALUES
(9876543, 'Pancracio ', 'jimenez', '1234', 0),
(15184523, 'Manuel', 'Jesus', 'macawi2004', 0),
(79312864, 'La bamba', 'Mera', 'macawi2004', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asistencia`
--

CREATE TABLE `asistencia` (
  `id_asistencia` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `matricula_alumno` int(11) NOT NULL,
  `idclase` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `asistencia`
--

INSERT INTO `asistencia` (`id_asistencia`, `fecha`, `hora`, `matricula_alumno`, `idclase`) VALUES
(11, '2020-06-23', '21:00:20', 15184523, 7),
(12, '2020-06-23', '21:02:25', 15184523, 8),
(13, '2020-06-23', '21:03:27', 9876543, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clase`
--

CREATE TABLE `clase` (
  `id_clase` int(11) NOT NULL,
  `matricula_profesor` int(11) NOT NULL,
  `num_materia` int(11) NOT NULL,
  `num_grupo` int(11) NOT NULL,
  `hora_generadaqr` time NOT NULL,
  `hora_limiteqr` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `clase`
--

INSERT INTO `clase` (`id_clase`, `matricula_profesor`, `num_materia`, `num_grupo`, `hora_generadaqr`, `hora_limiteqr`) VALUES
(6, 21896516, 2, 0, '20:53:41', '21:00:41'),
(7, 21896516, 3, 0, '20:53:56', '21:00:56'),
(8, 21896516, 2, 0, '21:02:12', '21:03:12');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `grupo`
--

CREATE TABLE `grupo` (
  `num_grupo` int(11) NOT NULL,
  `semestre` int(11) NOT NULL,
  `carrera` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `grupo`
--

INSERT INTO `grupo` (`num_grupo`, `semestre`, `carrera`) VALUES
(0, 4, 'IDTS'),
(1, 2, 'IDTS'),
(2, 4, 'Automotriz');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `materia`
--

CREATE TABLE `materia` (
  `matricula_profesor` int(11) NOT NULL,
  `num_materia` int(11) NOT NULL,
  `nombre_materia` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `materia`
--

INSERT INTO `materia` (`matricula_profesor`, `num_materia`, `nombre_materia`) VALUES
(21896516, 1, 'Calculo Integral'),
(21896516, 2, 'Calculo Diferencial'),
(98783237, 3, 'Circuitos Electricos'),
(98783237, 4, 'Algoritmos'),
(21896518, 5, 'Diseño UI/UX'),
(28586995, 6, 'electronica'),
(28586992, 7, 'electronica2'),
(21896516, 8, 'fluidos');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `profesor`
--

CREATE TABLE `profesor` (
  `matricula` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `apellidos` varchar(45) NOT NULL,
  `contraseña` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `profesor`
--

INSERT INTO `profesor` (`matricula`, `nombre`, `apellidos`, `contraseña`) VALUES
(21896516, 'Carlos', 'Arjona', 'macawi2004'),
(21896518, 'Profe Cañon', 'era', 'macawi2004'),
(28586992, 'juan2', 'martinez2', '1234562'),
(28586995, 'juan', 'martinez', '123456'),
(98783237, 'Mambe', 'Roy', 'macawi2004');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alumno`
--
ALTER TABLE `alumno`
  ADD PRIMARY KEY (`matricula`,`num_grupo`),
  ADD KEY `FK_num_grupo_idx` (`num_grupo`);

--
-- Indices de la tabla `asistencia`
--
ALTER TABLE `asistencia`
  ADD PRIMARY KEY (`id_asistencia`),
  ADD KEY `matricula_alumno` (`matricula_alumno`),
  ADD KEY `idclase` (`idclase`);

--
-- Indices de la tabla `clase`
--
ALTER TABLE `clase`
  ADD PRIMARY KEY (`id_clase`),
  ADD KEY `FK_num_materia_idx` (`num_materia`),
  ADD KEY `FK_num_grupo_idx` (`num_grupo`),
  ADD KEY `FK_matricula_profesor2` (`matricula_profesor`);

--
-- Indices de la tabla `grupo`
--
ALTER TABLE `grupo`
  ADD PRIMARY KEY (`num_grupo`);

--
-- Indices de la tabla `materia`
--
ALTER TABLE `materia`
  ADD PRIMARY KEY (`num_materia`,`matricula_profesor`),
  ADD KEY `FK_matricula_profesor` (`matricula_profesor`);

--
-- Indices de la tabla `profesor`
--
ALTER TABLE `profesor`
  ADD PRIMARY KEY (`matricula`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `asistencia`
--
ALTER TABLE `asistencia`
  MODIFY `id_asistencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `clase`
--
ALTER TABLE `clase`
  MODIFY `id_clase` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `materia`
--
ALTER TABLE `materia`
  MODIFY `num_materia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `alumno`
--
ALTER TABLE `alumno`
  ADD CONSTRAINT `FK_num_grupo` FOREIGN KEY (`num_grupo`) REFERENCES `grupo` (`num_grupo`);

--
-- Filtros para la tabla `asistencia`
--
ALTER TABLE `asistencia`
  ADD CONSTRAINT `asistencia_ibfk_1` FOREIGN KEY (`matricula_alumno`) REFERENCES `alumno` (`matricula`),
  ADD CONSTRAINT `asistencia_ibfk_2` FOREIGN KEY (`idclase`) REFERENCES `clase` (`id_clase`);

--
-- Filtros para la tabla `clase`
--
ALTER TABLE `clase`
  ADD CONSTRAINT `FK_matricula_profesor2` FOREIGN KEY (`matricula_profesor`) REFERENCES `profesor` (`matricula`),
  ADD CONSTRAINT `FK_num_grupo2` FOREIGN KEY (`num_grupo`) REFERENCES `grupo` (`num_grupo`),
  ADD CONSTRAINT `FK_num_materia` FOREIGN KEY (`num_materia`) REFERENCES `materia` (`num_materia`);

--
-- Filtros para la tabla `materia`
--
ALTER TABLE `materia`
  ADD CONSTRAINT `FK_matricula_profesor` FOREIGN KEY (`matricula_profesor`) REFERENCES `profesor` (`matricula`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
