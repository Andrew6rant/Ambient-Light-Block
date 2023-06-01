package io.github.andrew6rant.ambientlightblock;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmbientLightBlockMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ambient_light_block");
    public static final AmbientLightBlock AMBIENT_LIGHT_BLOCK = new AmbientLightBlock(AbstractBlock.Settings.of(Material.AIR).strength(-1.0F, 3600000.8F).dropsNothing().nonOpaque().luminance(LightBlock.STATE_TO_LUMINANCE));
    public static final BlockEntityType<AmbientLightBlockEntity> AMBIENT_LIGHT_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier("ambient_light_block", "ambient_light_block"),
            FabricBlockEntityTypeBuilder.create(AmbientLightBlockEntity::new, AMBIENT_LIGHT_BLOCK).build()
    );

    public static int calcState(int i, float f) {
        if (i > 0) {
            float g = f < 3.1415927F ? 0.0F : 6.2831855F;
            f += (g - f) * 0.2F;
            i = Math.round((float)i * MathHelper.cos(f));
        }
        return MathHelper.clamp(i, 0, 15);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Ambient Light Block");
        Registry.register(Registries.BLOCK, new Identifier("ambient_light_block", "ambient_light_block"), AMBIENT_LIGHT_BLOCK);
        Registry.register(Registries.ITEM, new Identifier("ambient_light_block", "ambient_light_block"), new BlockItem(AMBIENT_LIGHT_BLOCK, new FabricItemSettings().rarity(Rarity.EPIC)));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register(context -> context.addAfter(Items.LIGHT, AMBIENT_LIGHT_BLOCK.asItem()));
    }
}