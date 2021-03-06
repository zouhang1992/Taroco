package xyz.taroco.oauth2.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import xyz.taroco.oauth2.handler.DatabaseTokenStoreService;
import xyz.taroco.oauth2.handler.OAuth2DatabaseClientDetailsService;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  @Autowired
  private DatabaseTokenStoreService tokenStoreService;

  @Autowired
  private OAuth2DatabaseClientDetailsService oAuth2DatabaseClientDetailsService;

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Bean
  public ApprovalStore approvalStore() {
    TokenApprovalStore tokenStore = new TokenApprovalStore();
    tokenStore.setTokenStore(tokenStoreService);
    return tokenStore;
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    // 配置授权endpoint
    endpoints.tokenStore(tokenStoreService).authenticationManager(authenticationManager);
  }


  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(oAuth2DatabaseClientDetailsService);
  }

}
