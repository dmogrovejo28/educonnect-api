package org.dmencia.examentcs.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AppHeaderInterceptor implements HandlerInterceptor {

    private static final String HEADER_NAME =
            "X-Academic-Term";

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {

        String term =
                request.getHeader(
                        HEADER_NAME
                );

        if (
                term != null
                        && !term.isBlank()
        ) {

            AppContextHolder
                    .setAcademicTerm(
                            term.trim()
                    );

            log.debug(
                    "Academic term almacenado: {}",
                    term
            );
        }

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {

        AppContextHolder.clear();
    }
}
