package com.trendora.tienda.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();

        // Ejecutar la petición
        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        // Mostrar método, ruta, código de estado y duración
        System.out.printf("%s %s → %d (%d ms)%n",
                req.getMethod(),
                req.getRequestURI(),
                res.getStatus(),
                duration
        );
    }
}
