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

?>