package com.lucidworks.fusion.connector.plugin;

import com.google.inject.AbstractModule;
import com.lucidworks.fusion.connector.plugin.api.plugin.ConnectorPlugin;
import com.lucidworks.fusion.connector.plugin.api.plugin.ConnectorPluginModule;
import com.lucidworks.fusion.connector.plugin.config.SecurityFilteringConfig;
import com.lucidworks.fusion.connector.plugin.fetcher.SecurityFilteringAccessControlFetcher;
import com.lucidworks.fusion.connector.plugin.fetcher.SecurityFilteringContentFetcher;
import com.lucidworks.fusion.connector.plugin.impl.DefaultRandomContentGenerator;
import com.lucidworks.fusion.connector.plugin.security.SecurityFilteringSecurityFilterComponent;
import com.lucidworks.fusion.connector.plugin.validation.SecurityFilteringValidationComponent;
import org.pf4j.PluginWrapper;

import javax.inject.Inject;

import static com.lucidworks.fusion.connector.plugin.util.SecurityFilteringConstants.ACCESS_CONTROL;
import static com.lucidworks.fusion.connector.plugin.util.SecurityFilteringConstants.CONTENT;

public class SecurityFilteringPlugin extends ConnectorPluginModule {
  
  @Inject
  public SecurityFilteringPlugin(PluginWrapper wrapper) {
    super(wrapper);
  }
  
  @Override
  public ConnectorPlugin getConnectorPlugin() {
    AbstractModule nonGenModule = new AbstractModule() {
      @Override
      protected void configure() {
        bind(RandomContentGenerator.class).to(DefaultRandomContentGenerator.class).asEagerSingleton();
      }
    };
    return builder(SecurityFilteringConfig.class)
        .withFetcher(CONTENT, SecurityFilteringContentFetcher.class, nonGenModule)
        .withFetcher(ACCESS_CONTROL, SecurityFilteringAccessControlFetcher.class, nonGenModule)
        .withSecurityFilter(SecurityFilteringSecurityFilterComponent.class, nonGenModule)
        .withValidator(SecurityFilteringValidationComponent.class, nonGenModule)
        .build();
  }
}
