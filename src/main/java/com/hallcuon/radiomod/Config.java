package com.hallcuon.radiomod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // Конфігурація для радіо-моду
    private static final ForgeConfigSpec.BooleanValue ENABLE_RADIO = BUILDER
            .comment("Чи увімкнено радіо-функціонал")
            .define("enableRadio", true);

    private static final ForgeConfigSpec.IntValue MAX_VOLUME = BUILDER
            .comment("Максимальна гучність радіо (0-100)")
            .defineInRange("maxVolume", 80, 0, 100);

    public static final ForgeConfigSpec.ConfigValue<String> DEFAULT_STATION = BUILDER
            .comment("URL станції за замовчуванням")
            .define("defaultStation", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

    // Список дозволених URL
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOWED_STATIONS = BUILDER
            .comment("Список дозволених радіо-станцій")
            .defineList("allowedStations",
                    List.of(
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            "https://www.youtube.com/watch?v=9bZkp7q19f0"
                    ),
                    Config::validateUrl);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enableRadio;
    public static int maxVolume;
    public static String defaultStation;
    public static Set<String> allowedStations;

    // Валідація URL
    private static boolean validateUrl(final Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }
        String url = (String) obj;
        return url.startsWith("https://") && (url.contains("youtube.com") || url.contains("youtu.be"));
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        enableRadio = ENABLE_RADIO.get();
        maxVolume = MAX_VOLUME.get();
        defaultStation = DEFAULT_STATION.get();
        allowedStations = Set.copyOf(ALLOWED_STATIONS.get());
    }

    // Допоміжний метод для перевірки дозволених станцій
    public static boolean isStationAllowed(String url) {
        return allowedStations.contains(url);
    }
}