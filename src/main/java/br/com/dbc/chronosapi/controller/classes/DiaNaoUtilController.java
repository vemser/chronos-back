package br.com.dbc.chronosapi.controller.classes;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/dia-nao-util")
public class DiaNaoUtilController {
}
