package io.github.andrew6rant.ambientlightblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class AmbientLightBlockEntity extends BlockEntity {
    public AmbientLightBlockEntity(BlockPos pos, BlockState state) {
        super(AmbientLightBlockMod.AMBIENT_LIGHT_BLOCK_ENTITY, pos, state);
    }
}
