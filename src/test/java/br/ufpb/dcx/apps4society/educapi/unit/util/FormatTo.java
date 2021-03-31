package br.ufpb.dcx.apps4society.educapi.unit.util;

/**
 * This class has utilities for the testes cases
 */
public class FormatTo {

    /**
     * Format String to token format adding "Bearer " before the String
     * Example:
     *      FormatTo.token("any String");
     *      // return "Bearer any String"
     *
     * @param token any String
     * @return token formatted
     */
    public static String token(String token) {
        return "Bearer " + token;
    }
}
