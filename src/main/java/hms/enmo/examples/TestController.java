/*
 * (C) Copyright 2010-2022 hSenid Mobile Solutions (Pvt) Limited.
 * All Rights Reserved.
 *
 * This is only a SAMPLE CODE.
 *
 *  This code is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE.
 */

package hms.enmo.examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private EnmoConnector enmoConnector;

    @GetMapping("/test")
    public String test(){
        enmoConnector.sendEnmoRichTextContent();
        return "Called";
    }
}
