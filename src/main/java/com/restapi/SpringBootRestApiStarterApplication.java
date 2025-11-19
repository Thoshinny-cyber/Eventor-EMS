package com.restapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.restapi.model.Role;
import com.restapi.service.Encryption.Encoder;
import com.restapi.service.Encryption.RSAEncryptor;
import com.restapi.service.EventService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableSwagger2
@EnableCaching
public class SpringBootRestApiStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestApiStarterApplication.class, args);
	}

}


