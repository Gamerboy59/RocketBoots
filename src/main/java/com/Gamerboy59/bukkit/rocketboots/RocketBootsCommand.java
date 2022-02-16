/*
* Copyright 2012-2022 webshoptv, Gamerboy59. All rights reserved.
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

import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class RocketBootsCommand implements CommandExecutor {

    private final RBConfiguration config;
    private final Language lang;

    public RocketBootsCommand(RBConfiguration config, Language lang) {
        this.config = config;
        this.lang = lang;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if ((args.length > 0) && this.config.playersCanDisable()) {
                final boolean enabled = args[0].equalsIgnoreCase("yes") || args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("on");
                final boolean disabled = args[0].equalsIgnoreCase("no") || args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("off");
                this.config.setPlayerEnabled(player, enabled);
                if(enabled) {
                	player.sendMessage(lang.getString("RocketBootsNowEnabledMsg", player.getLocale()));
                } else if(disabled){
                	player.sendMessage(lang.getString("RocketBootsNowDisabledMsg", player.getLocale()));
                } else {
                	return false;
                }
            } else {
                if(this.config.playerEnabled(player)) {
                	player.sendMessage(lang.getString("RocketBootsEnabledMsg", player.getLocale()));
                } else {
                	player.sendMessage(lang.getString("RocketBootsDisabledMsg", player.getLocale()));
                }
            }
        } else {
        	Bukkit.getConsoleSender().sendMessage(lang.getString("Non_Console_Cmd", Locale.getDefault().toString()));
        }
        return true;
    }

}