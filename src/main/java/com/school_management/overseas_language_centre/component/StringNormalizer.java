package com.school_management.overseas_language_centre.component;

import org.springframework.stereotype.Component;

@Component
public class StringNormalizer {
    public String normalizer(String value){
        if (value == null){ //This checks if the input doesn't contain any String object.
            return null;
        }
        return value.trim().replaceAll("\\s+", " ");
        // trim() removes whitespace from the beginning and end of the String.
        //replaceAll("\\s+", " ") :::: Find one or more whitespace characters and replace them with one space " ".
    }

    public  String normalizerUpperCase(String value){
        if (value == null || value.isBlank()){ // || this one mean or
            return value;
        }
        return value.trim().toUpperCase();
    }

    public  String normalizerLowerCase(String value){
        if (value == null || value.isBlank()){ // || this one mean or
            return value;
        }
        return value.trim().toLowerCase();
    }
}
