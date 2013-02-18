<?php

$empty = False;
$type = "LIME";
$id =  "14";

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

function getId() {
    global $id;
    return $id;
}

function type() {
    global $type;
    return $type;
}

function isEmpty() {
    global $empty;

    return $empty;
}

function eatIt() {
    global $empty;
    $empty = True;
}

?>