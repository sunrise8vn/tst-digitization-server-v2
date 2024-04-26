package com.tst.utils;

import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;


@Component
public class AppUtils {

    public Map<String, String> mapErrorToResponse(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            field = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field);
            errors.put(field, fieldError.getDefaultMessage());
        }
        return errors;
    }

    public String removePdfTypeChar(String str) {
        str = str.toLowerCase();
        str = str.replaceAll(".pdf", "");

        return str;
    }

    public String replaceNonEnglishChar(String str) {
        str = str.toLowerCase();
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("đ", "d");
        str = str.replaceAll("[^\\x00-\\x7F]", "");

        return str;
    }

    public String removeNonAlphanumeric(String str) {
        do {
            str = str.replace(" ","-");
            str = str.replaceAll("[^a-zA-Z0-9\\-]", "-");
            str = str.replaceAll("--", "-");

            while (str.charAt(0) == '-') {
                str = str.substring(1);
            }

            while (str.charAt(str.length() - 1) == '-') {
                str = str.substring(0, str.length() - 1);
            }
        }
        while (str.contains("--"));

        return str.trim().toLowerCase(Locale.ENGLISH);
    }

    public boolean containsVietnamese(String input) {
        // Regex cho ký tự tiếng Việt có dấu và khoảng trắng
        String vietnameseRegex = "[\\p{L}\\p{M}\\s]*";

        Pattern pattern = Pattern.compile(vietnameseRegex);

        return pattern.matcher(input).matches();
    }

}