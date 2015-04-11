package com.augusto.test.spring.version;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class VersionTest {

    @Test
    public void testEqual() {
        Version actual = new Version("1.0");
        Version other = new Version("1.0");

        assertThat(actual.compareTo(other), equalTo(0));
    }

    @Test
    public void testGreater() {
        Version actual = new Version("1.1");
        Version other = new Version("1.0");

        assertThat(actual.compareTo(other), equalTo(1));
    }

    @Test
    public void testLess() {
        Version actual = new Version("1.0");
        Version other = new Version("1.1");

        assertThat(actual.compareTo(other), equalTo(-1));
    }
}
