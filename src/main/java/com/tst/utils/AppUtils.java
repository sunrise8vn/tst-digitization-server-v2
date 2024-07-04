package com.tst.utils;

import com.google.common.base.CaseFormat;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class AppUtils {
    private static final String YEAR_PATTERN = "\\d{4}";
    private static final String MONTH_YEAR_PATTERN_DOT = "(0[1-9]|1[0-2])\\.\\d{4}";
    private static final String DATE_MONTH_YEAR_PATTERN_DOT = "(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}";

    public Map<String, String> mapErrorToResponse(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            // convert to snake case
            field = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field);
            errors.put(field, fieldError.getDefaultMessage());
        }
        return errors;
    }

    public boolean isValidDateDot(String dateStr) {
        Pattern pattern = Pattern.compile(
                String.format("(%s)|(%s)|(%s)", YEAR_PATTERN, MONTH_YEAR_PATTERN_DOT, DATE_MONTH_YEAR_PATTERN_DOT)
        );

        Matcher matcher = pattern.matcher(dateStr);

        return matcher.matches();
    }

    public String convertDayMonthYearDotToYearMonthDayKebab(String dateStr) {
        String[] dayMonthYearArr = dateStr.split("\\.");

        return dayMonthYearArr[2] + "-" + dayMonthYearArr[1] + "-" + dayMonthYearArr[0];
    }

    public String formatNumber(String input) {
        if (input == null) {
            return null;
        }

        // Kiểm tra xem chuỗi có phải là số và có độ dài từ 1 đến 3 ký tự
        if (input.matches("\\d{1,3}")) {
            // Định dạng lại chuỗi để có độ dài là 3 ký tự, thêm các số 0 vào đầu nếu cần
            return String.format("%03d", Integer.parseInt(input));
        } else {
            throw new IllegalArgumentException("Dữ liệu nhập không phải là số hợp lệ hoặc có nhiều hơn 3 chữ số.");
        }
    }

    public String getFirstCharactersOfEachWord(String input) {
        StringBuilder output = new StringBuilder();

        for (String word : input.split(" ")) {
            output.append(word.charAt(0));
        }

        return output.toString().toLowerCase();
    }

    public String removePdfTypeChar(String str) {
        str = str.toLowerCase();
        str = str.replace(".pdf", "");

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
            str = str.replace(" ", "-");
            str = str.replaceAll("[^a-zA-Z0-9\\-]", "-");
            str = str.replace("--", "-");

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

    public boolean isValidNumberNotZero(String input) {
        String regex = "^[1-9]\\d*$";

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(input).matches();
    }

    public boolean isValidPhoneNumber(String input) {
        String regex = "^0(\\d{9})$";

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(input).matches();
    }

    public boolean isValidEvenNumber(String input) {
        String regex = "^(?!0+$)[0-9]*[02468]$";

        Pattern pattern = Pattern.compile(regex);

        return pattern.matcher(input).matches();
    }



    public boolean compareFields(Object obj1, Object obj2) {
        Field[] fields1 = obj1.getClass().getDeclaredFields();
        Field[] fields2 = obj2.getClass().getDeclaredFields();

        for (Field field1 : fields1) {
            field1.setAccessible(true); // Cho phép truy cập các trường private
            for (Field field2 : fields2) {
                field2.setAccessible(true);
                if (field1.getName().equals(field2.getName())) {
                    try {
                        Object value1 = field1.get(obj1);
                        Object value2 = field2.get(obj2);
                        if ((value1 != null && !value1.equals(value2)) || (value1 == null && value2 != null)) {
                            return false; // Trả về false ngay lập tức nếu tìm thấy sự khác biệt
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return false; // Trả về false nếu có lỗi truy cập trường
                    }
                }
            }
        }

        return true; // Trả về true nếu tất cả các trường giống nhau có giá trị bằng nhau
    }

}