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

import org.bukkit.entity.Player;

public class Permissions {

    public static boolean canUseGoldBoots(Player player) {
        return player.hasPermission("rocketboots.boots.gold");
    }

    public static boolean canUseLeatherBoots(Player player) {
        return player.hasPermission("rocketboots.boots.leather");
    }

    public static boolean canUseChainmailBoots(Player player) {
        return player.hasPermission("rocketboots.boots.chainmail");
    }

    public static boolean canUseDiamondBoots(Player player) {
        return player.hasPermission("rocketboots.boots.diamond");
    }

    public static boolean canUseIronBoots(Player player) {
        return player.hasPermission("rocketboots.boots.iron");
    }

    public static boolean canUseFeather(Player player) {
        return player.hasPermission("rocketboots.feather");
    }

    public static boolean canLaunchPlayers(Player player) {
        return player.hasPermission("rocketboots.launchPlayers");
    }
    
    public static boolean bypassFallDamage(Player player) {
        return player.hasPermission("rocketboots.bypassFallDamage");
    }
    public static boolean bypassBootsDamage(Player player) {
        return player.hasPermission("rocketboots.bypassBootsDamage");
    }

}