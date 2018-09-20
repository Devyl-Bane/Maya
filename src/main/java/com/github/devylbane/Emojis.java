package com.github.devylbane;

public enum Emojis
{
    CHECK_MARK    ("\u2705"),
    RED_CROSS_MARK("\u274C"),
    INVALID("");

    private final String unicode;

    Emojis(String unicode)
    {
        this.unicode = unicode;
    }

    public String getUnicode()
    {
        return this.unicode;
    }

    @Override
    public String toString()
    {
        return this.unicode;
    }

    public static Emojis ofUnicode(String unicode)
    {
        Emojis[] values = Emojis.values();

        for (Emojis value : values)
        {
            if (value.unicode.equalsIgnoreCase(unicode))
                return value;
        }

        return Emojis.INVALID;
    }
}
