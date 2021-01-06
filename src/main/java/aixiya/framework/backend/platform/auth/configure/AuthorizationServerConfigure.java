package aixiya.framework.backend.platform.auth.configure;

import aixiya.framework.backend.platform.auth.common.util.JwtTokenUtil;
import aixiya.framework.backend.platform.auth.properties.AuthProperties;
import aixiya.framework.backend.platform.auth.service.impl.RedisAuthenticationCodeService;
import aixiya.framework.backend.platform.auth.service.impl.RedisClientDetailsService;
import aixiya.framework.backend.platform.auth.token.AixiyaAuthTokenService;
import aixiya.framework.backend.platform.auth.token.JwtTokenGranter;
import aixiya.framework.backend.platform.auth.translator.AixiyaWebResponseExceptionTranslator;
import com.aixiya.framework.backend.redis.starter.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * 认证服务器配置
 *
 * @author wangqun865@163.com
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final AixiyaWebResponseExceptionTranslator exceptionTranslator;
    private final AuthProperties properties;
    private final RedisClientDetailsService redisClientDetailsService;
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisAuthenticationCodeService redisAuthenticationCodeService;
    private final RedisService redisService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(redisClientDetailsService);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore())
                .userDetailsService(userDetailService)
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(redisAuthenticationCodeService)
                .exceptionTranslator(exceptionTranslator)
                .tokenServices(tokenServices());
        if (properties.getEnableJwt()) {
            endpoints.accessTokenConverter(jwtAccessTokenConverter());
        }
        endpoints.tokenGranter(
                (grantType, tokenRequest) -> {
                    CompositeTokenGranter granter =  new CompositeTokenGranter(
                        new ArrayList<>(Arrays.asList(
                                new AuthorizationCodeTokenGranter(
                                        endpoints.getTokenServices(),
                                        endpoints.getAuthorizationCodeServices(),
                                        endpoints.getClientDetailsService(),
                                        endpoints.getOAuth2RequestFactory()
                                ),
                                new RefreshTokenGranter(
                                        endpoints.getTokenServices(),
                                        endpoints.getClientDetailsService(),
                                        endpoints.getOAuth2RequestFactory()
                                ),
                                resourceOwnerPasswordTokenGranter(
                                        authenticationManager,
                                        endpoints.getOAuth2RequestFactory()
                                ),
                                new JwtTokenGranter(
                                        endpoints.getTokenServices(),
                                        endpoints.getClientDetailsService(),
                                        endpoints.getOAuth2RequestFactory(),
                                        authenticationManager,
                                        jwtTokenUtil,
                                        redisService)
                        ))

                    );
                    return granter.grant(grantType,tokenRequest);
                }
        );
    }

    @Bean
    public TokenStore tokenStore() {
        if (properties.getEnableJwt()) {
            return new JwtTokenStore(jwtAccessTokenConverter());
        } else {
            RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
            // 解决每次生成的 token都一样的问题
            redisTokenStore.setAuthenticationKeyGenerator(oAuth2Authentication -> UUID.randomUUID().toString());
            return redisTokenStore;
        }
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        AixiyaAuthTokenService tokenServices = new AixiyaAuthTokenService(redisService);
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(redisClientDetailsService);
        tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        DefaultAccessTokenConverter defaultAccessTokenConverter = (DefaultAccessTokenConverter) accessTokenConverter.getAccessTokenConverter();
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(userDetailService);
        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        accessTokenConverter.setSigningKey(properties.getJwtAccessKey());
        return accessTokenConverter;
    }

    @Bean
    public ResourceOwnerPasswordTokenGranter resourceOwnerPasswordTokenGranter(AuthenticationManager authenticationManager, OAuth2RequestFactory oAuth2RequestFactory) {
        DefaultTokenServices defaultTokenServices = tokenServices();
        if (properties.getEnableJwt()) {
            defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        }
        return new ResourceOwnerPasswordTokenGranter(authenticationManager, defaultTokenServices, redisClientDetailsService, oAuth2RequestFactory);
    }

    @Bean
    public DefaultOAuth2RequestFactory oAuth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(redisClientDetailsService);
    }
}
