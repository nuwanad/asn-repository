package com.asn.app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.asn.app.product.service", "com.asn.app.product.controller"})
public class TestConfiguration {

}
