package io.github.andrew6rant.ambientlightblock.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;


import java.util.Set;

import static io.github.andrew6rant.ambientlightblock.AmbientLightBlockMod.AMBIENT_LIGHT_BLOCK;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    // Replace the set of blocks that are rendered with a billboard particle
    @Shadow @Final @Mutable private static Set<Item> BLOCK_MARKER_ITEMS;

    static {
        BLOCK_MARKER_ITEMS = Set.of(Items.BARRIER, Items.LIGHT, AMBIENT_LIGHT_BLOCK.asItem());
    }
}
