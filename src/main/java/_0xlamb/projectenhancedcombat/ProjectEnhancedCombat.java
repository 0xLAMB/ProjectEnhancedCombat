package _0xlamb.projectenhancedcombat;

import _0xlamb.projectenhancedcombat.client.LockOn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ProjectEnhancedCombat.MOD_ID)
public class ProjectEnhancedCombat {
    public static final String MOD_ID = "projectenhancedcombat";
    public static final String MOD_NAME = "Project Enhanced Combat";

    private static final LockOn LOCK_ON = new LockOn();

    public ProjectEnhancedCombat() {
        // Register
        FMLJavaModLoadingContext.get().getModEventBus().addListener(LOCK_ON::setup);
    }
}
