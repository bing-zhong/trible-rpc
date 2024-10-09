package com.abing.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author CaptainBing
 * @Date 2024/9/26 18:07
 * @Description
 */
@Data
public class User implements Serializable {

    private Long id;

    private String name;

    private Integer age;

}
