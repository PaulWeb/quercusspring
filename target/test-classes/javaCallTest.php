<?php

import java.util.Date;

class JavaCallTest {
    public function getResult(){
        $a = new Date();
        return $a->toString();
    }
}

?>