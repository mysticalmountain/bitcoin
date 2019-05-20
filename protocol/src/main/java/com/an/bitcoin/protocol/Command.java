package com.an.bitcoin.protocol;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Command {

    public String name();

    public int version() default 0;

}
