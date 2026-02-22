package org.theoslater.qol.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import org.theoslater.qol.ModConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            try {
                AutoConfig.getConfigHolder(ModConfig.class);
            } catch (RuntimeException e) {
                AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
            }
            return AutoConfig.getConfigScreen(ModConfig.class, parent).get();
        };
    }
}