package io.github.andrew6rant.ambientlightblock;


import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.DaylightDetectorBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class AmbientLightBlock extends BlockWithEntity {
    public static final IntProperty LEVEL_15;
    public static final BooleanProperty WATERLOGGED;
    public static final ToIntFunction<BlockState> STATE_TO_LUMINANCE;
    public AmbientLightBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LEVEL_15, 0).with(WATERLOGGED, false));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL_15, WATERLOGGED);
    }


    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return context.isHolding(AmbientLightBlockMod.AMBIENT_LIGHT_BLOCK.asItem()) ? VoxelShapes.fullCube() : VoxelShapes.empty();
    }

    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getDimension().hasSkyLight()) {
            updateState(state, world, pos);
        }
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return !world.isClient && world.getDimension().hasSkyLight() ? checkType(type, BlockEntityType.DAYLIGHT_DETECTOR, AmbientLightBlock::tick) : null;
    }

    private static void tick(World world, BlockPos pos, BlockState state, DaylightDetectorBlockEntity blockEntity) {
        if (world.getTime() % 20L == 0L) { // Vanilla's DaylightDetectorBlock ticks this often
            updateState(state, world, pos);
        }
    }



    private static void updateState(BlockState state, World world, BlockPos pos) {
        BlockPos topOfTheWorld = new BlockPos(pos.getX(), 1024, pos.getZ()); // 1023 is the max y value possible in 1.17+
        int i = world.getLightLevel(LightType.SKY, topOfTheWorld) - world.getAmbientDarkness();
        float f = world.getSkyAngleRadians(1.0F);
        i = AmbientLightBlockMod.calcState(i, f);
        if (state.get(LEVEL_15) != i) {
            world.setBlockState(pos, state.with(LEVEL_15, i), 2);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DaylightDetectorBlockEntity(pos, state);
    }

    static {
        LEVEL_15 = Properties.LEVEL_15;
        WATERLOGGED = Properties.WATERLOGGED;
        STATE_TO_LUMINANCE = (state) -> (Integer)state.get(LEVEL_15);
    }
}