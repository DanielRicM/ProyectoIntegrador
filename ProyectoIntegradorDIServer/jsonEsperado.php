<?php

$arrEsperado = array();

$arrEsperado["peticion"] = "add";


function JSONCorrectAdd($recibido){
	
	$auxCorrect = false;
	
	if(isset($recibido["peticion"]) && $recibido["peticion"] =="add"){
		$auxCorrect = true;
	}
	return $auxCorrect;
	
}
?>