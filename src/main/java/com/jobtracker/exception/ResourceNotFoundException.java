package com.jobtracker.exception;

/**
 * Thrown whenever a lookup scoped to the current user (findByUserIdAndId)
 * comes back empty - covers both "doesn't exist" and "belongs to someone
 * else" so callers can never distinguish the two, which is the point:
 * it always maps to a 404, never leaking that a resource exists for another
 * tenant.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
