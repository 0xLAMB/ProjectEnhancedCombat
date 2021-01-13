package _0xlamb.projectenhancedcombat;

import _0xlamb.projectenhancedcombat.client.LockOn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ProjectEnhancedCombat.MOD_ID)
public class ProjectEnhancedCombat {

    public static final String MOD_ID = "projectenhancedcombat";
    public static final String MOD_NAME = "Project Enhanced Combat";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private static LockOn lockOn = new LockOn();

    public ProjectEnhancedCombat() {
        // Register
        FMLJavaModLoadingContext.get().getModEventBus().addListener(lockOn::setup);
    }
}
