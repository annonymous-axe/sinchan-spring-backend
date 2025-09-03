package com.sinchan.dao;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDAO {

    private String email;

    private String fullNameEn;

    private String fullNameMh;

    private String contactNumber;

    private String firmNameEn;

    private String firmNameMh;

    private String gstNumber;

    private String addressEn;

    private String addressMh;

    private MultipartFile image;

}
