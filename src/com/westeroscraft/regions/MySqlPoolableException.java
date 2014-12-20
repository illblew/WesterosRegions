package com.westeroscraft.regions;
@SuppressWarnings("serial")

public class MySqlPoolableException extends Exception {
     public MySqlPoolableException(final String msg, Exception e) {
         super(msg, e);
     }
}
