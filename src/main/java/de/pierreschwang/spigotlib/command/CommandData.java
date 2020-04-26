package de.pierreschwang.spigotlib.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandData {

    String name();

    String permission() default "";

    String description() default "";

    String usage() default "";

    String[] aliases() default {};

}
