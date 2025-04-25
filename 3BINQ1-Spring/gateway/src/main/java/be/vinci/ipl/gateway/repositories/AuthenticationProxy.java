package be.vinci.ipl.gateway.repositories;

import be.vinci.ipl.gateway.models.UnsafeCredentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "authentications")
public interface AuthenticationProxy {

    @PostMapping("/authentication/connect")
    String connect(@RequestBody UnsafeCredentials credentials);

    @PostMapping("/authentication/verify")
    String verify(@RequestBody String token);

}
