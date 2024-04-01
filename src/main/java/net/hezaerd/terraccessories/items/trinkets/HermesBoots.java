package net.hezaerd.terraccessories.items.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

import java.util.UUID;

public class HermesBoots extends TrinketItem {

    private final float timeToMaxSpeed = 3f;
    private double timeSprinting = 0f;

    public HermesBoots() {
        super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1));
    }

    private EntityAttributeModifier getSpeedBonus() {
        double speedMultiplier = MathHelper.lerp(timeSprinting / timeToMaxSpeed, 0, 0.4f);
        return new EntityAttributeModifier(
                UUID.fromString("f5d3b3d2-1b4b-4b7b-8f0d-1c1f2f3f4f5f"),
                "terraccessories:hermes_boots_movement_speed",
                speedMultiplier,
                EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        EntityAttributeInstance movementSpeed = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        EntityAttributeModifier speedBonus = getSpeedBonus();

        if(entity.isSprinting()) {
            timeSprinting = Math.min(timeSprinting + 1f / 20f, timeToMaxSpeed);

            if (timeSprinting == timeToMaxSpeed && Terraccessories.CONFIG.hermes_boots_sprint_particles())
                for (int i = 0; i < 3; i++) {
                    float randomXvel = Random.create().nextBetween(-1, 1) * 0.05f;
                    float randomZvel = Random.create().nextBetween(-1, 1) * 0.05f;

                    entity.getWorld().addParticle(ParticleTypes.CLOUD, entity.getX(), entity.getY() + 0.4, entity.getZ(), randomXvel, 0, randomZvel);
                }

            if (movementSpeed.hasModifier(speedBonus))
                movementSpeed.removeModifier(speedBonus);

            movementSpeed.addTemporaryModifier(speedBonus);
        } else {
            timeSprinting = 0;

            if (movementSpeed.hasModifier(speedBonus))
                movementSpeed.removeModifier(speedBonus);
        }

        super.tick(stack, slot, entity);
    }
}
