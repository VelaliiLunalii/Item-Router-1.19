package io.github.velaliilunalii.item_router.block.properties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Socket implements StringRepresentable {
    EMPTY("empty", 0),
    OBSIDIAN("obsidian", 4),
    ENDER("ender", 8),
    AMETHYST("amethyst", 16);

    public final String name;
    public final int range;

    Socket(String name, int range) {
        this.name = name;
        this.range = range;
    }

    public boolean isEmpty(){return this.name.equals("empty");}

    @Override
    public String toString() {
        return this.getSerializedName();
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
