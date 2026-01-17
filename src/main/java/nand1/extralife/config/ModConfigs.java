package nand1.extralife.config;
import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfigs {

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new Common(builder);
        COMMON_SPEC = builder.build();
    }

    public static class Common {
        public final ForgeConfigSpec.IntValue hardcoreLives;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("hardcore");
            hardcoreLives = builder
                    .comment("Amount of lives per player in hardcore mode")
                    .defineInRange("lives", 3, 1, 100);
            builder.pop();
        }
    }
}
