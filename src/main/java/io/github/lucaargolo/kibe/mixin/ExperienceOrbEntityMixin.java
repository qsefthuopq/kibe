package io.github.lucaargolo.kibe.mixin;

import io.github.lucaargolo.kibe.blocks.vacuum.VacuumHopperEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
abstract public class ExperienceOrbEntityMixin extends Entity {

    @Shadow private int amount;

    public ExperienceOrbEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo info) {
        for(BlockEntity blockEntity : world.blockEntities) {
            if(blockEntity instanceof VacuumHopperEntity) {
                BlockPos pos = blockEntity.getPos();
                Vec3d vecPos = new Vec3d(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5);
                double distance = this.getPos().distanceTo(vecPos);
                if(distance < 8.0) {
                    if(distance < 1.0) {
                        ((VacuumHopperEntity) blockEntity).addLiquid(amount*10);
                        this.destroy();
                    }
                    Vec3d vel = this.getPos().reverseSubtract(vecPos).normalize().multiply(0.1);
                    this.addVelocity(vel.x, vel.y, vel.z);
                }
            }
        }
    }
}
