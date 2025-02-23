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

function search() {
    require 'bbdd.php';
    
    $arrMessage = array();

    if(isset($_GET["id"])){
        $stmt = $conn->prepare("SELECT * FROM songs WHERE id = ?");
        $stmt->bind_param("i", $_GET["id"]);
    }
    else{
        $stmt = $conn->prepare("SELECT * FROM songs");
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
        $arrSongs = array();
        while ($row = $result->fetch_assoc()) {
            $arrSongs[] = $row;
        }
        $arrMessage["estado"] = "ok";
        $arrMessage["songs"] = $arrSongs;
    } else {
        $arrMessage["estado"] = "ok";
        $arrMessage["songs"] = [];
    }
    echo json_encode($arrMessage);
    $stmt->close();
    $conn->close();
}

function write() {
    require 'bbdd.php';
    require 'jsonEsperado.php';
    $arrMessage = array();
    $parameters = file_get_contents("php://input");

    if (isset($parameters)) {
        $mensajeRecibido = json_decode($parameters, true);
        
        if (JSONCorrectAdd( $mensajeRecibido)) {

            $songs = $mensajeRecibido["objectAdd"];

            $stmt = $conn->prepare("INSERT INTO songs (id, name, author, album) VALUES (?, ?, ?, ?)");

            foreach ($songs as $song) {
                $stmt->bind_param("isss", $song["id"], $song["name"], $song["author"], $song["album"]);

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
            $arrMessage["message"] = "Song insertada correctamente.";
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

function update(){
    require 'bbdd.php';
    $arrMessage = array();

    $parameters = file_get_contents("php://input");

    if (isset($parameters)) {

        $mensajeRecibido = json_decode($parameters, true);
        if (true) {

            $songs = $mensajeRecibido["objectAdd"];

            $stmt = $conn->prepare("UPDATE songs SET name = ?, author = ?, album = ? WHERE id = ".$_GET["id"]);
            
            foreach ($songs as $song) {
                $stmt->bind_param("sss", $song["name"], $song["author"], $song["album"]);
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
            $arrMessage["message"] = "Song insertada correctamente.";
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

function delete(){
    require 'bbdd.php';
    
    $arrMessage = array();

    if(isset($_GET["id"])){
        $stmt = $conn->prepare("DELETE FROM songs WHERE id = ?");
        $stmt->bind_param("i",$_GET["id"]);
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
        $arrMessage["message"] = "Song eliminada correctamente.";
    } else{
        die();
    }
    echo json_encode($arrMessage);
    $conn->close();
    die();
}

?>