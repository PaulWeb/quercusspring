<?php

class Lime {

    protected $id=14;
    protected $empty=false;
    protected $typeb="LIME";

    public function __construct(/* ... */) {
        $this->id =  14;
        $this->typeb="LIME";
    }

    public function getId() {
        return $this->id;
    }

    public function type() {
        return $this->typeb;
    }

    public function isEmpty() {
        return $this->empty;
    }

    public function eatIt() {
        $this->empty = true;
    }

}

$id=14;
$empty=false;
$typeb="LIME";

function __construct(/* ... */) {
    global $id, $empty, $typeb;
    $id = 14;
    $typeb = "LIME";
}

function getId() {
    global $id, $empty, $typeb;
    return $id;
}

function type() {
    global $id, $empty, $typeb;
    return $typeb;
}

function isEmpty() {
    global $id, $empty, $typeb;
    return $empty;
}

function eatIt() {
    global $id, $empty, $typeb;
    $empty = true;
}

?>