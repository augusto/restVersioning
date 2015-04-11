package com.augusto.test.spring.version;


public class VersionRange {

    private Version from;
    private Version to;

    public VersionRange(String from, String to) {
        this.from = new Version(from);
        this.to = new Version(to);
    }

    public boolean includes(String other) {
        Version otherVersion = new Version(other);

        if (from.compareTo(otherVersion) <= 0 && to.compareTo(otherVersion) >= 0) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "range[" + from + "-" + to + "]";
    }
}
