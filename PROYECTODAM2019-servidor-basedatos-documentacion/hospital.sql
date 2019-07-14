-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 14-07-2019 a las 19:09:05
-- Versión del servidor: 5.7.26
-- Versión de PHP: 7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `hospital`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cita`
--

DROP TABLE IF EXISTS `cita`;
CREATE TABLE IF NOT EXISTS `cita` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_paciente` int(11) NOT NULL,
  `id_especialista` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `hora` time NOT NULL,
  `estado` enum('PENDIENTE','COMPLETADA','ANULADA') NOT NULL DEFAULT 'PENDIENTE',
  `id_mutua` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_mutua` (`id_mutua`),
  KEY `fk_paciente` (`id_paciente`),
  KEY `fk_especialista` (`id_especialista`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `cita`
--

INSERT INTO `cita` (`id`, `id_paciente`, `id_especialista`, `fecha`, `hora`, `estado`, `id_mutua`) VALUES
(9, 2, 2, '2019-06-06', '12:30:00', 'COMPLETADA', 1),
(10, 2, 2, '2019-06-06', '12:45:00', 'ANULADA', 1),
(31, 13, 14, '2019-07-28', '11:15:00', 'COMPLETADA', 4),
(32, 13, 9, '2019-07-01', '10:00:00', 'COMPLETADA', 4),
(33, 13, 9, '2019-08-07', '10:00:00', 'COMPLETADA', 4),
(34, 13, 13, '2019-07-31', '17:00:00', 'PENDIENTE', 4),
(42, 13, 9, '2019-07-30', '09:15:00', 'COMPLETADA', 4),
(47, 15, 9, '2019-07-21', '09:45:00', 'ANULADA', 3),
(49, 15, 9, '2019-07-28', '09:00:00', 'ANULADA', 3),
(50, 15, 9, '2019-07-30', '09:00:00', 'COMPLETADA', 3),
(51, 15, 14, '2019-07-23', '09:00:00', 'PENDIENTE', 3),
(52, 15, 13, '2019-07-30', '09:15:00', 'PENDIENTE', 3),
(53, 15, 9, '2019-07-23', '09:15:00', 'ANULADA', 3),
(55, 13, 14, '2019-07-30', '10:15:00', 'PENDIENTE', 4),
(56, 16, 9, '2019-07-30', '09:45:00', 'ANULADA', 3),
(57, 16, 9, '2019-07-29', '10:00:00', 'PENDIENTE', 3),
(58, 16, 9, '2019-07-30', '10:00:00', 'ANULADA', 3),
(59, 15, 13, '2019-07-23', '10:00:00', 'COMPLETADA', 3),
(60, 19, 9, '2019-07-30', '11:00:00', 'PENDIENTE', 1),
(61, 2, 14, '2019-07-22', '09:00:00', 'PENDIENTE', 1),
(62, 16, 14, '2019-07-22', '10:30:00', 'PENDIENTE', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `especialista`
--

DROP TABLE IF EXISTS `especialista`;
CREATE TABLE IF NOT EXISTS `especialista` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellidos` varchar(255) NOT NULL,
  `especialidad` varchar(50) NOT NULL,
  `salario` decimal(10,2) NOT NULL,
  `tipo` enum('MEDICO','ENFERMERO') NOT NULL,
  `dni` varchar(9) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `especialista`
--

INSERT INTO `especialista` (`id`, `nombre`, `apellidos`, `especialidad`, `salario`, `tipo`, `dni`, `password`) VALUES
(2, 'SARA', 'FLORES PEREZ', 'DERMATOLOGIA', '1800.00', 'MEDICO', '00000002J', 'b59c67bf196a4758191e42f76670ceba'),
(8, 'MANUEL ', 'LOPEZ DEL GANSO', 'PEDIATRIA', '1900.00', 'MEDICO', '22222222A', 'b59c67bf196a4758191e42f76670ceba'),
(9, 'ALBA', 'GUADALUPE ALMENDOR', 'PEDIATRIA', '1900.00', 'MEDICO', '33333333A', 'b59c67bf196a4758191e42f76670ceba'),
(10, 'JUAN', 'FERNANDEZ BARNO', 'PEDIATRIA', '1900.00', 'MEDICO', '44444444A', 'b59c67bf196a4758191e42f76670ceba'),
(11, 'PEDRO', 'ABANCO FERNANDEZ', 'PEDIATRIA', '1900.00', 'MEDICO', '55555555A', 'b59c67bf196a4758191e42f76670ceba'),
(12, 'DAVID ', 'BLANCO MEJIA', 'ALERGOLOGÍA', '1900.00', 'MEDICO', '66666666A', 'b59c67bf196a4758191e42f76670ceba'),
(13, 'BEGOÑA ', 'BOTEANU BLANCO', 'PSICOLOGIA', '1900.00', 'MEDICO', '7777777A', 'b59c67bf196a4758191e42f76670ceba'),
(14, 'RODRIGO', 'SANCHEZ PEZ', 'TRAUMATOLOGIA', '1900.00', 'MEDICO', '88888888A', 'b59c67bf196a4758191e42f76670ceba'),
(16, 'FEDERICA', 'ALVAREZ CARTON', 'ALERGOLOGÍA', '1900.00', 'MEDICO', '98986565A', 'b59c67bf196a4758191e42f76670ceba'),
(17, 'PILAR', 'ALBURQUERQUE GARROTE', 'CARDIOLOGIA', '1900.00', 'MEDICO', '98323298', 'b59c67bf196a4758191e42f76670ceba'),
(18, 'ANA', 'LINDON AMARO', 'CARDIOLOGIA', '1900.00', 'MEDICO', '98765432A', 'b59c67bf196a4758191e42f76670ceba'),
(19, 'ANTONIO', 'DE LA FUENTE CASADO', 'GINECOLOGIA', '1900.00', 'MEDICO', '98765432A', 'b59c67bf196a4758191e42f76670ceba'),
(20, 'JAVIER', 'CASADO SACRISTAN', 'GINECOLOGIA', '1900.00', 'MEDICO', '74185232A', 'b59c67bf196a4758191e42f76670ceba'),
(21, 'CARMEN', 'FERNANDEZ DE LA UZ', 'LOGOPEDIA', '1900.00', 'MEDICO', '11119999', 'b59c67bf196a4758191e42f76670ceba'),
(22, 'ALEJANDRA', 'ALCANTARA PEREZ', 'LOGOPEDIA', '1900.00', 'MEDICO', '11112222A', 'b59c67bf196a4758191e42f76670ceba'),
(23, 'ALVARO', 'MUÑOZ SIETEDEDOS', 'MEDICINA GENERAL', '1900.00', 'MEDICO', '11114444A', 'b59c67bf196a4758191e42f76670ceba'),
(24, 'PATRICIA', 'RODRIGUEZ MENENDEZ', 'MEDICINA GENERAL', '1900.00', 'MEDICO', '33334444A', 'b59c67bf196a4758191e42f76670ceba'),
(25, 'SARA', 'DIAZ MARCOS', 'DERMATOLOGIA', '1900.00', 'MEDICO', '22226666A', 'b59c67bf196a4758191e42f76670ceba'),
(26, 'SANTIAGO', 'MEZQUITA POLLO', 'DERMATOLOGIA', '1900.00', 'MEDICO', '22228888A', 'b59c67bf196a4758191e42f76670ceba'),
(27, 'RAUL', 'LOPEZ MATOS', 'ALERGOLOGIA', '1900.00', 'MEDICO', '99999999A', 'b59c67bf196a4758191e42f76670ceba');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ficha_cita`
--

DROP TABLE IF EXISTS `ficha_cita`;
CREATE TABLE IF NOT EXISTS `ficha_cita` (
  `id_cita` int(11) NOT NULL,
  `observaciones` text NOT NULL,
  `recomendaciones` text NOT NULL,
  KEY `fk_cita` (`id_cita`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `ficha_cita`
--

INSERT INTO `ficha_cita` (`id_cita`, `observaciones`, `recomendaciones`) VALUES
(31, 'Refiere dolor en tobillo derecho. Posible esguince. \r\nSe remite al servicio de radio para realizar pruebas.\r\nHa sido operado hace dos años de este tobillo. \r\nNo han surgido complicaciones durante este tiempo.', 'Se requiere reposo y medicación para el dolor.\r\nA la espera de los resultados.\r\n'),
(32, 'Refiere dolor en la garganta. Presenta placas en lateral derecho y se realizan pruebas a la espera de resultados.\r\nHace dos días ha acudido a urgencias por problemas de respiración. \r\n', 'Se recetas medicación para tomar durante los próximos 4 días. Evitar las comidas muy calientes y muy frías.\r\nAcudir de nuevo a consulta si en cinco días no ha mejorado.\r\n'),
(33, 'Se realiza revisión después de unos días de tratamiento y no notar mucha mejoría.\r\nVista el resultado de las pruebas no se observa ninguna anomalía.\r\n', 'Se receta antibiótico en gotas para aplicar tres veces al dia.\r\nTomar mucha agua y no forzar la garganta durante 24 horas.'),
(50, 'ESTO ES UNA PRUEBA', 'esto es una prueba'),
(50, 'OTRA PRUEBA MASSSSS', 'PRUEBAAAA'),
(59, 'OTRA PRUEBA MASSSSS', 'PRUEBAAAA'),
(42, 'ADFADF', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mutua`
--

DROP TABLE IF EXISTS `mutua`;
CREATE TABLE IF NOT EXISTS `mutua` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `precio_cita` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `mutua`
--

INSERT INTO `mutua` (`id`, `nombre`, `precio_cita`) VALUES
(1, 'ADESLAS', '5.00'),
(2, 'ASISA', '8.00'),
(3, 'MAPFRE', '8.00'),
(4, 'SANITAS', '7.00'),
(5, 'DKV', '6.00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paciente`
--

DROP TABLE IF EXISTS `paciente`;
CREATE TABLE IF NOT EXISTS `paciente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellidos` varchar(255) NOT NULL,
  `tipo_documento` enum('Pasaporte','DNI') NOT NULL,
  `valor_documento` varchar(20) NOT NULL,
  `telefono` varchar(9) NOT NULL,
  `email` varchar(255) NOT NULL,
  `sociedad_medica` int(11) NOT NULL,
  `password` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_sociedad_medica` (`sociedad_medica`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `paciente`
--

INSERT INTO `paciente` (`id`, `nombre`, `apellidos`, `tipo_documento`, `valor_documento`, `telefono`, `email`, `sociedad_medica`, `password`) VALUES
(2, 'Sara', 'Perez Ruiz', 'DNI', '00000004G', '968909876', 'su@email.es', 1, '1111'),
(13, 'ALEJANDRO', 'FERNANDEZ MARIN', 'DNI', '11111122A', '656525352', 'Alejandro@p.com', 4, 'b59c67bf196a4758191e42f76670ceba'),
(15, 'ANDRES', 'LOPEZ MARCOS', 'DNI', '44444444A', '656332233', 'RB@L.COM', 3, 'b59c67bf196a4758191e42f76670ceba'),
(16, 'CARLOS', 'AYUSO GIL', 'DNI', '44447777A', '666332233', 'FAC@N.COM', 3, 'b59c67bf196a4758191e42f76670ceba'),
(19, 'ALICIA', 'RODRIGUEZ FUENTES', 'DNI', '11117777A', '699999999', 'alicia@g.es', 1, 'b59c67bf196a4758191e42f76670ceba'),
(20, 'LUCIA', 'BLANCO PASTOR', 'DNI', '88888822A', '600666666', 'blanco@g.es', 3, 'b59c67bf196a4758191e42f76670ceba');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cita`
--
ALTER TABLE `cita`
  ADD CONSTRAINT `fk_especialista` FOREIGN KEY (`id_especialista`) REFERENCES `especialista` (`id`),
  ADD CONSTRAINT `fk_mutua` FOREIGN KEY (`id_mutua`) REFERENCES `mutua` (`id`),
  ADD CONSTRAINT `fk_paciente` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`);

--
-- Filtros para la tabla `ficha_cita`
--
ALTER TABLE `ficha_cita`
  ADD CONSTRAINT `fk_cita` FOREIGN KEY (`id_cita`) REFERENCES `cita` (`id`);

--
-- Filtros para la tabla `paciente`
--
ALTER TABLE `paciente`
  ADD CONSTRAINT `fk_sociedad_medica` FOREIGN KEY (`sociedad_medica`) REFERENCES `mutua` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
