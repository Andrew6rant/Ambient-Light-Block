package io.github.andrew6rant.ambientlightblock;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

import static io.github.andrew6rant.ambientlightblock.AmbientLightBlockMod.AMBIENT_LIGHT_BLOCK;

public class AmbientLightBlockClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(AmbientLightBlockMod.AMBIENT_LIGHT_BLOCK.asItem(), new Identifier("light_level"), (itemStack, clientWorld, livingEntity, j) -> {
            assert clientWorld != null;
            int i = clientWorld.getLightLevel(LightType.SKY, new BlockPos(0, 1024, 0)) - clientWorld.getAmbientDarkness();
            float f = clientWorld.getSkyAngleRadians(1.0F);
            return AmbientLightBlockMod.calcState(i, f) / 16.0F;
        });

        BuiltinItemRendererRegistry.INSTANCE.register(AMBIENT_LIGHT_BLOCK, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            matrices.push();
            matrices.translate(0.5D, 0.5D, 0.5D);
            matrices.scale(0.5F, 0.5F, 0.5F);
            MinecraftClient.getInstance().getItemRenderer().renderItem(stack, mode, light, overlay, matrices, vertexConsumers, null, 0);
            matrices.pop();
        });
    }
}
