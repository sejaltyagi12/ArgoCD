package com.ems.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


/**
 * Holds the user's credentials.
 * 
 * @author Avinash Tyagi
 * @since 2017-03-20
 */
public class EmsUserDetails extends User {

    private static final long serialVersionUID = 1L;

    private long id;

    /**
     * Instantiates a new user details.
     *
     * @param id the id
     * @param username the username
     * @param password the password
     * @param authorities the authorities
     */
    public EmsUserDetails(long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(long id) {
        this.id = id;
    }

}

