package com.fooddelivery.foodbackend.config;

import com.fooddelivery.foodbackend.dto.response.UserResponse;
import com.fooddelivery.foodbackend.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // ── User → UserResponse: map roles Set<Role> to Set<String> ──────────
        TypeMap<User, UserResponse> userMap =
                mapper.createTypeMap(User.class, UserResponse.class);

        userMap.addMappings(m -> m.skip(UserResponse::setRoles));
        userMap.setPostConverter(ctx -> {
            User src  = ctx.getSource();
            UserResponse dst = ctx.getDestination();
            if (src.getRoles() != null) {
                dst.setRoles(
                        src.getRoles().stream()
                                .map(role -> role.getRoleName().name())
                                .collect(Collectors.toSet())
                );
            }
            return dst;
        });

        return mapper;
    }
}