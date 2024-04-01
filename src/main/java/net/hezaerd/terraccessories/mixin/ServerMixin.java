package net.hezaerd.terraccessories.mixin;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.hezaerd.terraccessories.workers.WorkerManager;
import net.minecraft.server.MinecraftServer;
@Mixin(MinecraftServer.class)
public class ServerMixin {
    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "HEAD"))
    private void startTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        WorkerManager.tick(true);
    }


    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At(value = "TAIL"))
    private void endTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        WorkerManager.tick(false);
    }

    @Inject(method = "shutdown()V", at = @At(value = "TAIL"))
    private void onShutdown(CallbackInfo ci) {
        WorkerManager.clear();
    }
}
