/*
* Copyright 2012-2018 webshoptv, Gamerboy59. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:
*
* 1. Redistributions of source code must retain the above copyright notice, this list of
* conditions and the following disclaimer.
*
* 2. Redistributions in binary form must reproduce the above copyright notice, this list
* of conditions and the following disclaimer in the documentation and/or other materials
* provided with the distribution.
* 
* 3. On any publishing of the files that contains this or part of this code have to be
* given credit to its original author (given above in Copyright row).
*
* THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
* FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
* ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
* ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
* The views and conclusions contained in the software and documentation are those of the
* authors and contributors and should not be interpreted as representing official policies,
* either expressed or implied, of anybody else.
*/

package com.Gamerboy59.bukkit.rocketboots;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public final class RBConfiguration {

    private final RocketBoots plugin;
    private final FileConfiguration config;

    public RBConfiguration(RocketBoots plugin) {
        this.plugin = plugin;
        final File dataFolder = plugin.getDataFolder();
        dataFolder.mkdir();
        final File configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            this.createDefaultConfigFile(configFile);
        }
        this.config = plugin.getConfig();
    }

    private void createDefaultConfigFile(File configFile) {
        try {
            configFile.createNewFile();
            final InputStream configIn = RocketBoots.class.getResourceAsStream("/config.default.yml");
            final FileOutputStream configOut = new FileOutputStream(configFile);
            final byte[] buffer = new byte[1024];
            int read;
            while ((read = configIn.read(buffer)) > 0) {
                configOut.write(buffer, 0, read);
            }
            configIn.close();
            configOut.close();
        } catch (final IOException ioe) {
            this.plugin.getLogger().log(Level.WARNING, "Couldn't create default config file, using default values", ioe);
            return;
        } catch (final NullPointerException npe) { // getResourceAsStream returns null under certain reload conditions (reloaded updated plugin)
            this.plugin.getLogger().log(Level.WARNING, "Couldn't create default config file (Bukkit bug?), using default values");
            configFile.delete();
        }
    }

    public boolean strikeRealLightning() {
        return this.config.getBoolean("boots.chainmail.realLightning", true);
    }

    public int numberLightningStrikes() {
        final int number = this.config.getInt("boots.chainmail.numLightningStrikes", 5);
        if (number < 0) {
            return 5;
        } else if (number > 20) {
            return 20;
        }
        return number;
    }

    public double leatherBootsSpeed() {
        int number = this.config.getInt("boots.leather.launchSpeed", 20);
        if (number < 10) {
            number = 10;
        } else if (number > 50) {
            number = 50;
        }
        return (double) number / (double) 10;
    }

    public double ironBootsVerticalSpeed() {
        int number = this.config.getInt("boots.iron.verticalSpeed", 10);
        if (number < 1) {
            number = 1;
        } else if (number > 50) {
            number = 50;
        }
        return (double) number / (double) 10;
    }

    public double diamondBootsVerticalSpeed() {
        int number = this.config.getInt("boots.diamond.verticalSpeed", 60);
        if (number < 10) {
            number = 10;
        } else if (number > 100) {
            number = 100;
        }
        return (double) number / (double) 100;
    }

    public double diamondBootsSpeedMultiplier() {
        int number = this.config.getInt("boots.diamond.speedMultiplier", 50);
        if (number < 10) {
            number = 10;
        } else if (number > 100) {
            number = 100;
        }
        return (double) number / (double) 100;
    }

    public boolean playersCanDisable() {
        return this.config.getBoolean("playersCanDisable", true);
    }

    public boolean playerEnabled(Player player) {
        return this.config.getBoolean("players." + player.getName() + ".enabled", true);
    }

    public void setPlayerEnabled(Player player, boolean enabled) {
        this.config.set("players." + player.getName() + ".enabled", enabled);
        this.plugin.saveConfig();
    }

    public boolean preventFlightKick() {
        return this.config.getBoolean("preventFlightKick", true);
    }
    
    public boolean enableFallDamage() {
        return this.config.getBoolean("AdvancedMod.enableFallDamage", false);
    }
    
    public int bootsDamage() {
        final int number = this.config.getInt("AdvancedMod.bootsDamage", 10);
        if (number < 0) {
            return 0;
        } else if (number > 99) {
            return 99;
        }
        return number;
    }
    
    public boolean enableItemLabels() {
        return this.config.getBoolean("AdvancedMod.enableItemLabels", true);
    }

}