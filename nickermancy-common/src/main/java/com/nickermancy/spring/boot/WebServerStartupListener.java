package com.nickermancy.spring.boot;

import com.nickermancy.spring.repositories.BinaryAssetRepositoryProperties;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.event.EventListener;
import org.springframework.lang.Nullable;

import static java.util.Objects.requireNonNullElse;

public class WebServerStartupListener {

    @Nullable
    private WebServer webServer;

    @EventListener(WebServerInitializedEvent.class)
    public void webServerInitialized(WebServerInitializedEvent event) {
        this.webServer = event.getWebServer();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyEvent(ApplicationReadyEvent event) {
        var serverProperties = event.getApplicationContext().getBean(ServerProperties.class);
        var imageCuratorProperties = event.getApplicationContext().getBean(BinaryAssetRepositoryProperties.class);
        var ch = new char[80];
        Arrays.fill(ch, '-');
        var separator = new String(ch);
        System.out.printf("%s%n  The web server is now listening:%n  %s//%s:%s%s%n%s%n", separator,
            serverProperties.getSsl() != null && serverProperties.getSsl().isEnabled() ? "https:" : "http:",
            requireNonNullElse(serverProperties.getAddress(), "localhost"),
            webServer != null ? webServer.getPort() : serverProperties.getPort(),
            imageCuratorProperties.getResourceRoot(),
            separator);
    }
}
