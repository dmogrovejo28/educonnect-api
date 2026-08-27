package org.dmencia.examentcs.interceptor;

public class AppContextHolder {

    private static final ThreadLocal<String>
            ACADEMIC_TERM =
            new ThreadLocal<>();

    private AppContextHolder() {
    }

    public static void setAcademicTerm(
            String term
    ) {

        ACADEMIC_TERM.set(term);
    }

    public static String getAcademicTerm() {

        return ACADEMIC_TERM.get();
    }

    public static void clear() {

        ACADEMIC_TERM.remove();
    }
}
