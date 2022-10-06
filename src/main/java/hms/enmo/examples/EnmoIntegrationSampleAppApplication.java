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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class EnmoIntegrationSampleAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnmoIntegrationSampleAppApplication.class, args);
    }

}
