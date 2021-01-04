package _0xlamb.projectenhancedcombat;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Constants.PROJECTENHANCEDCOMBAT_ID)
public class ProjectEnhancedCombat
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private static KeyBinding KEY_LOCK_ON;

    public ProjectEnhancedCombat() {
        KEY_LOCK_ON = new KeyBinding(
                "key." + Constants.PROJECTENHANCEDCOMBAT_ID + ".lock_on",
                GLFW.GLFW_KEY_TAB,
                "key.categories." + Constants.PROJECTENHANCEDCOMBAT_ID);
        KEY_LOCK_ON.setKeyConflictContext(KeyConflictContext.IN_GAME);
        ClientRegistry.registerKeyBinding(KEY_LOCK_ON);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static Optional<Entity> raytrace(Minecraft mc) {
        final RayTraceResult result = mc.objectMouseOver;
        if (result instanceof EntityRayTraceResult) {
            final EntityRayTraceResult entityResult = (EntityRayTraceResult)result;
            return Optional.of(entityResult.getEntity());
        } else {
            return Optional.empty();
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

    private boolean isLockedOn = false;
    private boolean isLockOnHandled = false;
    private Optional<Entity> lockOnTarget = Optional.empty();

    @SubscribeEvent
    public void onRender(final TickEvent.RenderTickEvent e) {
        if (isLockedOn) {
            final Minecraft mc = Minecraft.getInstance();

            final Entity target = lockOnTarget.orElseThrow(NullPointerException::new);
            aim(mc.player, target, e.renderTickTime);
        }
    }

    @SubscribeEvent
    public void onRenderGui(final RenderGameOverlayEvent.Post e) {
        if (!KEY_LOCK_ON.isInvalid() && KEY_LOCK_ON.isKeyDown()) {
            if (!isLockOnHandled) {
                isLockOnHandled = true;

                LOGGER.info("LOCK_ON pressed");
                final Minecraft mc = Minecraft.getInstance();

                if (isLockedOn) {
                    LOGGER.info("Lock-off");
                    lockOnTarget = Optional.empty();
                    mc.mouseHelper.grabMouse();
                } else {
                    final Optional<Entity> target = raytrace(mc);

                    if (!target.isPresent()) {
                        LOGGER.info("No target to lock-on");
                        return;
                    }

                    LOGGER.info("Lock-on");
                    lockOnTarget = target;
                    // TODO: prevent opening menus during lock-on? or at least ungrab mouse again
                    mc.mouseHelper.ungrabMouse();
                }
                isLockedOn = !isLockedOn;
            }
        } else {
            isLockOnHandled = false;
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
