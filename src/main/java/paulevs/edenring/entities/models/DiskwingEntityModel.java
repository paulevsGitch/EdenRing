package paulevs.edenring.entities.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import paulevs.edenring.entities.DiskwingEntity;

public class DiskwingEntityModel extends EntityModel<DiskwingEntity> {
	private final ModelPart model;
	private final ModelPart tailPivot1;
	private final ModelPart tailPivot2;
	private final ModelPart wingRight;
	private final ModelPart wingLeft;
	
	public DiskwingEntityModel(ModelPart root) {
		this.model = root.getChild("model");
		this.tailPivot1 = model.getChild("tail_pivot_1");
		this.tailPivot2 = tailPivot1.getChild("tail_pivot_2");
		this.wingRight = model.getChild("wing_right");
		this.wingLeft = model.getChild("wing_left");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		PartDefinition model = partdefinition.addOrReplaceChild("model", CubeListBuilder
			.create()
			.texOffs(0, 24)
			.addBox(-4.0F, -1.0F, -8.0F, 8.0F, 2.0F, 16.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, 22.0F, 0.0F)
		);
		
		model.addOrReplaceChild("wing_right", CubeListBuilder
			.create()
			.texOffs(-24, 0)
			.addBox(-12.0F, 0.0F, -6.0F, 14.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)),
			PartPose.offset(-4.0F, 0.0F, 0.0F)
		);
		
		model.addOrReplaceChild("wing_left", CubeListBuilder
			.create()
			.texOffs(-24, 0)
			.mirror()
			.addBox(-2.0F, 0.0F, -6.0F, 14.0F, 0.0F, 24.0F, new CubeDeformation(0.0F))
			.mirror(false),
			PartPose.offset(4.0F, 0.0F, 0.0F)
		);
		
		model.addOrReplaceChild("eye_3_r1", CubeListBuilder
			.create()
			.texOffs(0, 24)
			.mirror()
			.addBox(1.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.mirror(false),
			PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.8727F, -0.5236F, 0.0F)
		);
		
		model.addOrReplaceChild("eye_2_r1", CubeListBuilder
			.create()
			.texOffs(0, 28)
			.addBox(-3.0F, 1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
			PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, -0.8727F, 0.5236F, 0.0F)
		);
		
		model.addOrReplaceChild("eye_1_r1", CubeListBuilder
			.create()
			.texOffs(0, 24)
			.addBox(-3.0F, -3.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
			PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.8727F, 0.5236F, 0.0F)
		);
		
		model.addOrReplaceChild("eye_4_r1", CubeListBuilder
			.create()
			.texOffs(0, 28)
			.mirror().addBox(1.0F, 1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, -0.8727F, -0.5236F, 0.0F)
		);
		
		PartDefinition tail = model.addOrReplaceChild("tail_pivot_1", CubeListBuilder
			.create()
			.texOffs(0, 42)
			.addBox(-2.0F, -1.0F, 0.0F, 4.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, 0.0F, 8.0F)
		);
		
		tail.addOrReplaceChild("tail_pivot_2", CubeListBuilder
			.create()
			.texOffs(0, 52)
			.addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
			.texOffs(20, 0).addBox(-6.0F, 0.0F, 6.0F, 12.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)),
			PartPose.offset(0.0F, 0.0F, 8.0F)
		);
		
		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	
	@Override
	public void setupAnim(DiskwingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float angle = Mth.sin(animationProgress * 0.05F) * 0.2F;
		tailPivot1.xRot = angle;
		tailPivot2.xRot = angle;
		wingRight.zRot = angle * 0.2F;
		wingLeft.zRot = -wingRight.zRot;
	}
	
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.pushPose();
		//matrices.scale(scaleXZ, scaleY, scaleXZ);
		model.render(matrices, vertices, light, overlay);
		matrices.popPose();
	}
}
