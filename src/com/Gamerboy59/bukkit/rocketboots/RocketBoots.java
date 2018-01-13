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

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

public class RocketBoots extends JavaPlugin {

    @Override
    public void onDisable() {
        this.getLogger().info("RocketBoots v" + this.getDescription().getVersion() + " disabled!");
    }

    @Override
    public void onEnable() {
        RBConfiguration config = new RBConfiguration(this);
        this.getServer().getPluginManager().registerEvents(new RBPlayerListener(config), this);
        this.getServer().getPluginManager().registerEvents(new RBEntityListener(config), this);
        this.getCommand("rocketboots").setExecutor(new RocketBootsCommand(config));
        this.getLogger().info("RocketBoots v" + this.getDescription().getVersion() + " enabled!");
        
        try {
            BukkitMetrics metrics = new BukkitMetrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

}