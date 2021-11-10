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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import paulevs.edenring.EdenRing;
import paulevs.edenring.entities.LightningRayEntity;
import paulevs.edenring.registries.EdenBiomes;
import paulevs.edenring.registries.EdenEntities;
import paulevs.edenring.registries.EdenSounds;
import ru.bclib.api.BiomeAPI;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.BlockModelProvider;
import ru.bclib.interfaces.RenderLayerProvider;
import ru.bclib.util.MHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class BrainTreeBlock extends BaseBlock implements BlockModelProvider, RenderLayerProvider {
	public static final BooleanProperty	ACTIVE = BlockProperties.ACTIVE;
	private static final ArmorMaterial[] PROTECTIVE = new ArmorMaterial[] {
		ArmorMaterials.CHAIN,
		ArmorMaterials.IRON,
		ArmorMaterials.GOLD,
		ArmorMaterials.NETHERITE
	};
	
	public BrainTreeBlock(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.COPPER_BLOCK).color(color).lightLevel(state -> state.getValue(ACTIVE) ? 15 : 0).randomTicks());
		this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> stateManager) {
		stateManager.add(ACTIVE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (!world.isClientSide() && random.nextInt(1024) == 0 && BiomeAPI.getFromBiome(world.getBiome(pos)) == EdenBiomes.BRAINSTORM) {
			int px = pos.getX() + MHelper.randRange(-16, 16, random);
			int pz = pos.getZ() + MHelper.randRange(-16, 16, random);
			int py = world.getHeight(Types.WORLD_SURFACE, px, pz);
			LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(world);
			bolt.setVisualOnly(true);
			bolt.teleportTo(px, py, pz);
			world.addFreshEntity(bolt);
		}
		
		if (state.getValue(ACTIVE)) {
			hitLighting(world, random, pos);
			world.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
		}
		else if (random.nextInt(4) == 0) {
			world.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
			Vec3i[] offsets = MHelper.getOffsets(random);
			MutableBlockPos p = new MutableBlockPos();
			for (Vec3i offset: offsets) {
				p.set(pos).move(offset);
				BlockState sideBlock = world.getBlockState(p);
				if (sideBlock.getBlock() instanceof BrainTreeBlock && sideBlock.getValue(ACTIVE)) {
					world.setBlockAndUpdate(p, sideBlock.setValue(ACTIVE, false));
					hitLighting(world, random, p);
				}
			}
		}
	}
	
	private void hitLighting(ServerLevel world, Random random, BlockPos pos) {
		List<LivingEntity> entities = world.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat(), null, new AABB(pos).inflate(16, 16, 16));
		if (!entities.isEmpty()) {
			LivingEntity entity = entities.get(random.nextInt(entities.size()));
			
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
				if (blockState.getMaterial().blocksMotion() && !(blockState.getBlock() instanceof BrainTreeBlock)) {
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
				
				if (entity.isInvulnerable() || (entity instanceof Player && ((Player) entity).isCreative())) {
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
					entity.hurt(DamageSource.LIGHTNING_BOLT, (1 - resistance) * 3F);
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
	public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
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
