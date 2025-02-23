<?php

$metodo = $_SERVER['REQUEST_METHOD'];

switch ($metodo) {
    case "GET":
        search();
        break;
    case "POST":
        write();
        break;
    case "PUT":
        update();
        break;
    case "DELETE":
        delete();
        break;
}

function search()
{
    require 'bbdd.php';

    $arrMessage = array();

    if (isset($_GET["id"])) {
        $stmt = $conn->prepare("SELECT * FROM students WHERE id = ?");
        $stmt->bind_param("i", $_GET["id"]);
    } else {
        $stmt = $conn->prepare("SELECT * FROM students");
    }

    if (!$stmt->execute()) {
        $arrMessage["estado"] = "error";
        $arrMessage["message"] = "Error en la consulta.";
        $arrMessage["error"] = $stmt->error;
        echo json_encode($arrMessage);
        $stmt->close();
        $conn->close();
        return;
    }

    $result = $stmt->get_result();

    if ($result) {
        $arrStudents = array();
        while ($row = $result->fetch_assoc()) {
            $arrStudents[] = $row;
        }
        $arrMessage["estado"] = "ok";
        $arrMessage["students"] = $arrStudents;
    } else {
        $arrMessage["estado"] = "ok";
        $arrMessage["students"] = [];
    }
    echo json_encode($arrMessage);
    $stmt->close();
    $conn->close();
}

function write()
{
    require 'bbdd.php';
    require 'jsonEsperado.php';
    $arrMessage = array();
    $parameters = file_get_contents("php://input");

    if (isset($parameters)) {
        $mensajeRecibido = json_decode($parameters, true);

        if (JSONCorrectAdd($mensajeRecibido)) {

            $students = $mensajeRecibido["objectAdd"];

            $stmt = $conn->prepare("INSERT INTO students (id, name, age, course) VALUES (?, ?, ?, ?)");

            foreach ($students as $student) {
                $stmt->bind_param("isis", $student["id"], $student["name"], $student["age"], $student["course"]);

                if (!$stmt->execute()) {
                    $arrMessage["estado"] = "error";
                    $arrMessage["message"] = "Error al insertar el estudiante.";
                    $arrMessage["error"] = $stmt->error;
                    echo json_encode($arrMessage);
                    $stmt->close();
                    $conn->close();
                    return;
                }
            }
            $stmt->close();
            $arrMessage["estado"] = "ok";
            $arrMessage["message"] = "Student insertados correctamente.";
        } else {
            $arrMessage["estado"] = "error";
            $arrMessage["message"] = "EL JSON NO CONTIENE LOS CAMPOS ESPERADOS";
        }
    } else {
        $arrMessage["estado"] = "error";
        $arrMessage["message"] = "EL JSON NO SE HA ENVIADO CORRECTAMENTE";
    }

    echo json_encode($arrMessage);
    $conn->close();

    die();
}

function update()
{
    require 'bbdd.php';
    $arrMessage = array();

    $parameters = file_get_contents("php://input");

    if (isset($parameters)) {

        $mensajeRecibido = json_decode($parameters, true);
        if (true) {

            $students = $mensajeRecibido["objectAdd"];

            $stmt = $conn->prepare("UPDATE students SET name = ?, age = ?, course = ? WHERE id = " . $_GET["id"]);

            foreach ($students as $student) {
                $stmt->bind_param("sis", $student["name"], $student["age"], $student["course"]);
                if (!$stmt->execute()) {
                    $arrMessage["estado"] = "error";
                    $arrMessage["message"] = "Error al insertar la canción.";
                    $arrMessage["error"] = $stmt->error;
                    echo json_encode($arrMessage);
                    $stmt->close();
                    $conn->close();
                    return;
                }
            }
            $stmt->close();
            $arrMessage["estado"] = "ok";
            $arrMessage["message"] = "Student insertada correctamente.";
        }
    } else {
        $arrMessage["estado"] = "error";
        $arrMessage["message"] = "EL JSON NO SE HA ENVIADO CORRECTAMENTE";
    }

    echo json_encode($arrMessage);
    $conn->close();

    die();
}

function delete()
{
    require 'bbdd.php';

    $arrMessage = array();

    if (isset($_GET["id"])) {
        $stmt = $conn->prepare("DELETE FROM students WHERE id = ?");
        $stmt->bind_param("i", $_GET["id"]);
        if (!$stmt->execute()) {
            $arrMessage["estado"] = "error";
            $arrMessage["message"] = "Error al eliminar la canción.";
            $arrMessage["error"] = $stmt->error;
            echo json_encode($arrMessage);
            $stmt->close();
            $conn->close();
            return;
        }
        $stmt->close();
        $arrMessage["estado"] = "ok";
        $arrMessage["message"] = "Student eliminada correctamente.";
    } else {
        die();
    }
    echo json_encode($arrMessage);
    $conn->close();
    die();
}

?>