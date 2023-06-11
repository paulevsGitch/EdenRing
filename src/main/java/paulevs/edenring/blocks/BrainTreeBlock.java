package paulevs.edenring.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import org.betterx.bclib.blocks.BaseBlock;
import org.betterx.bclib.blocks.BlockProperties;
import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.client.models.PatternsHelper;
import org.betterx.bclib.client.render.BCLRenderLayer;
import org.betterx.bclib.interfaces.BlockModelProvider;
import org.betterx.bclib.interfaces.RenderLayerProvider;
import org.betterx.bclib.util.MHelper;
import paulevs.edenring.EdenRing;
import paulevs.edenring.entities.LightningRayEntity;
import paulevs.edenring.registries.EdenEntities;
import paulevs.edenring.registries.EdenSounds;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BrainTreeBlock extends BaseBlock implements BlockModelProvider, RenderLayerProvider {
	public static final BooleanProperty	ACTIVE = BlockProperties.ACTIVE;
	public static final BooleanProperty	POWERED = BlockStateProperties.POWERED;
	private static final ArmorMaterial[] PROTECTIVE = new ArmorMaterial[] {
		ArmorMaterials.CHAIN,
		ArmorMaterials.IRON,
		ArmorMaterials.GOLD,
		ArmorMaterials.NETHERITE
	};
	
	public BrainTreeBlock(MapColor color) {
		super(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK).mapColor(color).lightLevel(state -> state.getValue(ACTIVE) ? 15 : 0).randomTicks());
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false).setValue(POWERED, false));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVE, POWERED);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos pos = ctx.getClickedPos();
		Level level = ctx.getLevel();
		BlockState state = defaultBlockState();
		if (level.getBestNeighborSignal(pos) > 0) {
			state = state.setValue(ACTIVE, true).setValue(POWERED, true);
		}
		return state;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean bl) {
		boolean hasSignal = level.getBestNeighborSignal(pos) > 0;
		boolean powered = state.getValue(POWERED);
		if (hasSignal && !powered) {
			level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true).setValue(POWERED, true));
		}
		else if (!hasSignal && powered) {
			level.setBlockAndUpdate(pos, defaultBlockState());
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(ACTIVE)) {
			hitLighting(level, random, pos);
			if (!state.getValue(POWERED)) {
				level.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
			}
		}
		else if (random.nextInt(4) == 0) {
			level.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
			Vec3i[] offsets = MHelper.getOffsets(random);
			MutableBlockPos p = new MutableBlockPos();
			for (Vec3i offset: offsets) {
				p.set(pos).move(offset);
				BlockState sideBlock = level.getBlockState(p);
				if (sideBlock.getBlock() instanceof BrainTreeBlock && sideBlock.getValue(ACTIVE) && !sideBlock.getValue(POWERED)) {
					level.setBlockAndUpdate(p, sideBlock.setValue(ACTIVE, false));
					hitLighting(level, random, p);
				}
			}
		}
	}
	
	private void hitLighting(ServerLevel world, RandomSource random, BlockPos pos) {
		List<LivingEntity> entities = world.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat(), null, new AABB(pos).inflate(16, 16, 16));
		if (!entities.isEmpty()) {
			LivingEntity entity = entities.get(random.nextInt(entities.size()));
			
			if (entity instanceof Player && ((Player) entity).isCreative()) {
				return;
			}
			
			MutableBlockPos mpos = pos.mutable();
			float dx = (float) (entity.getX() - pos.getX() - 0.5);
			float dy = (float) (entity.getY() - pos.getY() - 0.5);
			float dz = (float) (entity.getZ() - pos.getZ() - 0.5);
			float ax = Mth.abs(dx);
			float ay = Mth.abs(dy);
			float az = Mth.abs(dz);
			float max = MHelper.max(ax, ay, az);
			int count = Mth.ceil(max);
			dx /= count;
			dy /= count;
			dz /= count;
			
			boolean hit = true;
			for (int i = 2; i < count; i++) {
				mpos.set(pos.getX() + dx * i, pos.getY() + dy * i, pos.getZ() + dz * i);
				BlockState blockState = world.getBlockState(mpos);
				if (blockState.blocksMotion() && !(blockState.getBlock() instanceof BrainTreeBlock)) {
					hit = false;
					break;
				}
			}
			
			if (hit) {
				LightningRayEntity lightningRay = EdenEntities.LIGHTNING_RAY.create(world);
				lightningRay.teleportTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
				lightningRay.setEnd(entity.position());
				world.addFreshEntity(lightningRay);
				
				world.playLocalSound(
					entity.getX(),
					entity.getY(),
					entity.getZ(),
					SoundEvents.LIGHTNING_BOLT_IMPACT,
					SoundSource.WEATHER,
					MHelper.randRange(1.0F, 5.0F, random),
					MHelper.randRange(0.5F, 1.5F, random),
					false
				);
				
				if (entity.isInvulnerable()) {
					return;
				}
				
				float resistance = 0;
				Iterator<ItemStack> iterator = entity.getArmorSlots().iterator();
				while (iterator.hasNext()) {
					ItemStack stack = iterator.next();
					if (stack.getItem() instanceof ArmorItem) {
						ArmorItem item = (ArmorItem) stack.getItem();
						ArmorMaterial material = item.getMaterial();
						for (ArmorMaterial m: PROTECTIVE) {
							if (material == m) {
								resistance += 0.25F;
								break;
							}
						}
					}
				}
				
				if (resistance < 1) {
					entity.hurt(world.damageSources().lightningBolt(), (1 - resistance) * 3F);
				}
			}
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		Optional<String> pattern = PatternsHelper.createBlockSimple(blockState.getValue(ACTIVE) ? EdenRing.makeID(stateId.getPath() + "_active") : stateId);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
		if (!blockState.getValue(ACTIVE)) {
			return;
		}
		
		if (random.nextInt(24) == 0) {
			level.playLocalSound(
				blockPos.getX() + 0.5,
				blockPos.getY() + 0.5,
				blockPos.getZ() + 0.5,
				EdenSounds.BLOCK_ELECTRIC,
				SoundSource.BLOCKS,
				MHelper.randRange(1.0F, 5.0F, random),
				MHelper.randRange(0.5F, 1.5F, random),
				false
			);
		}
		
		byte side = (byte) random.nextInt(3);
		float dx = side == 0 ? random.nextFloat() : random.nextInt(2) * 1.01F - 0.005F;
		float dy = side == 1 ? random.nextFloat() : random.nextInt(2) * 1.01F - 0.005F;
		float dz = side == 2 ? random.nextFloat() : random.nextInt(2) * 1.01F - 0.005F;
		float l = MHelper.length(dx - 0.5F, dy - 0.5F, dz - 0.5F);
		float sx = (dx - 0.5F) / l;
		float sy = (dy - 0.5F) / l;
		float sz = (dz - 0.5F) / l;
		
		level.addParticle(ParticleTypes.ELECTRIC_SPARK, blockPos.getX() + dx, blockPos.getY() + dy, blockPos.getZ() + dz, sx, sy, sz);
	}
}
