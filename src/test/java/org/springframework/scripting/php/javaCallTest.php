<?php

/**
 *
 * @copyright  Copyright (C) 2013 World Page Company -- all rights reserved.
 * @license    Apache License, Version 2.0; see LICENSE.txt
 * @author Sergej Varjuhin <cepreu.mail@gmail.com>
 */

import java.util.Date;

class JavaCallTest {
    public function getResult(){
        $a = new Date();
        return $a->toString();
    }
}

?>