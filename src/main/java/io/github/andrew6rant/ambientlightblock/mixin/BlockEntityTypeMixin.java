package io.github.andrew6rant.ambientlightblock.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Arrays;

import static io.github.andrew6rant.ambientlightblock.AmbientLightBlockMod.AMBIENT_LIGHT_BLOCK;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {

	// Gross hack to add AMBIENT_LIGHT_BLOCK to the list of blocks that can use DaylightDetectorBlockEntity
	@ModifyArg(
			method = "<clinit>",
			slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=daylight_detector")),
			at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntityType$Builder;create(Lnet/minecraft/block/entity/BlockEntityType$BlockEntityFactory;[Lnet/minecraft/block/Block;)Lnet/minecraft/block/entity/BlockEntityType$Builder;", ordinal = 0)
	)
	private static Block[] addCampfireBlocks(Block... original) {
		Block[] newBlocks = Arrays.copyOf(original, original.length + 1);
		newBlocks[newBlocks.length - 1] = AMBIENT_LIGHT_BLOCK;
		return newBlocks;
	}
}