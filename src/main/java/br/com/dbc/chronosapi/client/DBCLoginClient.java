package br.com.dbc.chronosapi.client;

import feign.Headers;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(value="dbc-login", url="!!link!!")
@Headers("Content-Type: application/json")
public interface DBCLoginClient {

//
//    @RequestLine("POST /login-dbc")
//    UsernameDTO post(UsernameDTO usernameDTO);


}