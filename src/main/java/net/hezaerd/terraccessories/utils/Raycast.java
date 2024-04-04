package net.hezaerd.terraccessories.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class Raycast {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private Raycast() {}

    public static HitResult forwardFromPlayer(int range) {
        Entity player = client.cameraEntity;

        if (player == null) { return null; }

        Vec3d vector = player.getRotationVector();
        Vec3d rayStart = player.getCameraPosVec(client.getTickDelta());
        Vec3d rayEnd = rayStart.add(vector.multiply(range));

        return client.world.raycast(new RaycastContext(rayStart, rayEnd, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
    }

}
