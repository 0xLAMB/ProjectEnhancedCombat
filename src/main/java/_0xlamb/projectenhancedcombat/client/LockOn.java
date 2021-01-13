package _0xlamb.projectenhancedcombat.client;

import _0xlamb.projectenhancedcombat.ProjectEnhancedCombat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = ProjectEnhancedCombat.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LockOn {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private final KeyBinding KEY_LOCK_ON;

    private boolean isKeyLockOnHandled = false;
    private Entity lockOnTarget = null;

    public LockOn() {
        KEY_LOCK_ON = new KeyBinding(
                "key." + ProjectEnhancedCombat.MOD_ID + ".lock_on",
                GLFW.GLFW_KEY_TAB,
                "key.categories." + ProjectEnhancedCombat.MOD_ID);
        KEY_LOCK_ON.setKeyConflictContext(KeyConflictContext.IN_GAME);
    }

    public void setup(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(KEY_LOCK_ON);

        MinecraftForge.EVENT_BUS.addListener(this::onRender);
    }

    private void onRender(final TickEvent.RenderTickEvent event) {
        // trigger once on key down
        if (!KEY_LOCK_ON.isInvalid() && KEY_LOCK_ON.isKeyDown()) {
            if (!isKeyLockOnHandled) {
                isKeyLockOnHandled = true;
                handleKeyLockOn();
            }
        } else {
            // key is released
            isKeyLockOnHandled = false;
        }

        if (lockOnTarget != null) {
            final Minecraft mc = Minecraft.getInstance();

            if (mc.player != null) {
                aim(mc.player, lockOnTarget, event.renderTickTime);
            } else {
                // exited from game
                lockOnTarget = null;
            }
        }
    }

    private void handleKeyLockOn() {
        LOGGER.info("LOCK_ON pressed");
        final Minecraft mc = Minecraft.getInstance();

        if (lockOnTarget == null) {
            lockOnTarget = raytrace(mc);

            if (lockOnTarget != null) {
                LOGGER.info("Lock-on");
                mc.mouseHelper.ungrabMouse();
            } else {
                LOGGER.info("No target to lock-on");
            }
        } else {
            LOGGER.info("Lock-off");
            lockOnTarget = null;
            mc.mouseHelper.grabMouse();
        }
    }

    private static Entity raytrace(Minecraft mc) {
        final RayTraceResult result = mc.objectMouseOver;
        if (result instanceof EntityRayTraceResult) {
            final EntityRayTraceResult entityResult = (EntityRayTraceResult)result;
            return entityResult.getEntity();
        } else {
            return null;
        }
    }

    private static void aim(Entity subject, Entity target, double renderTickTime) {
        Vector3d targetPos = target.getPositionVec();
        Vector3d directionVec = targetPos.subtract(subject.getPositionVec()).normalize();
        double angle = Math.atan2(-directionVec.x, directionVec.z) * 180 / Math.PI;

        float adjustedPrevYaw = subject.prevRotationYaw;
        if (Math.abs(angle - adjustedPrevYaw) > 180) {
            if (adjustedPrevYaw > angle) {
                angle += 360;
            } else if (adjustedPrevYaw < angle) {
                angle -= 360;
            }
        }

        double newDelta = MathHelper.lerp(renderTickTime, adjustedPrevYaw, angle);
        if (newDelta > 180) {
            newDelta -= 360;
        }
        if (newDelta < -180) {
            newDelta += 360;
        }
        subject.rotationYaw = (float)newDelta;
    }
}
