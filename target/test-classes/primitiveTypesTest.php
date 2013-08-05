<?php

class testObject{}

function getInt() {
    return 32;
}

function getFloat() {
    return 32.2;
}

function getBool() {
    return true;
}

function getString() {
    return "hello";
}

function getArray() {
    return array(1, 2, 3, 4);
}

function getNull() {
    return null;
}

function getObject() {
    return new testObject();
}

function getMap() {
    return array("one" => 1, "two" => 2, "three" => 3, "four" => 4);
}

function setNull($x) {
    return $x;
}

function setBoolean($x){
    return $x;
}

function setByte($x){
    return $x;
}

function setShort($x){
    return $x;
}

function setInt($x){
    return $x;
}

function setLong($x){
    return $x;
}

function setFloat($x){
    return $x;
}

function setDouble($x){
    return $x;
}

function setString($x){
    return $x;
}

function setChar($x){
    return $x;
}

function setCharArray($x){
    return $x;
}

function setByteArray($x){
    return $x;
}

function setArray($x){
    return $x;
}

function setList($x){
    return $x;
}

function setMap($x){
    return $x;
}

function setObject($x){
    return $x;
}

?>