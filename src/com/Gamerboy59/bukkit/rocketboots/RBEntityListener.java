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

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RBEntityListener implements Listener {

    private final RBConfiguration config;

    public RBEntityListener(RBConfiguration config) {
        this.config = config;
    }


    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof Player) {
            final Player player = (Player) entity;
            if ((event.getCause() == DamageCause.FALL) && this.config.playerEnabled(player)) {
                final Material playerBoots = Util.getPlayerBoots(player);
                final ItemStack equippedBoots = player.getEquipment().getBoots();
                if ((Material.GOLD_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player))) {
                    if (!this.config.enableFallDamage() || Permissions.bypassFallDamage(player)){
                	event.setCancelled(true);
                    }
                    if ((this.config.bootsDamage() != 0) || !Permissions.bypassBootsDamage(player)){
                        player.getEquipment().getBoots().setDurability((short) (equippedBoots.getDurability() + this.config.bootsDamage()));
                        player.sendMessage("RocketBoots durability: " + equippedBoots.getDurability());
                        setLore(equippedBoots);
                        if (equippedBoots.getDurability() >= 100) {
                        	player.getInventory().setBoots(null);
                        	player.sendMessage(ChatColor.RED + "[--> " + ChatColor.DARK_RED + ChatColor.BOLD + "X" + ChatColor.RESET + ChatColor.RED + " ]" + ChatColor.RESET + " RocketBoots destroyed");
                        }
                    }
                } else if (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player)) {
                    if (!this.config.enableFallDamage() || Permissions.bypassFallDamage(player)){
                	event.setCancelled(true);
                    }
                    if ((this.config.bootsDamage() != 0) || !Permissions.bypassBootsDamage(player)){
                        player.getEquipment().getBoots().setDurability((short) (equippedBoots.getDurability() + this.config.bootsDamage()));
                        player.sendMessage("RocketBoots durability: " + equippedBoots.getDurability());
                        setLore(equippedBoots);
                        if (equippedBoots.getDurability() >= 100) {
                        	player.getInventory().setBoots(null);
                        	player.sendMessage(ChatColor.RED + "[--> " + ChatColor.DARK_RED + ChatColor.BOLD + "X" + ChatColor.RESET + ChatColor.RED + " ]" + ChatColor.RESET + " RocketBoots destroyed");
                        }
                    }
                    final Location playerLocation = player.getLocation();
                    final int times = this.config.numberLightningStrikes();
                    final boolean useRealLightning = this.config.strikeRealLightning();
                    for (int i = 0; i < times; i++) {
                        if (useRealLightning) {
                            player.getWorld().strikeLightning(playerLocation);
                        } else {
                            player.getWorld().strikeLightningEffect(playerLocation);
                        }
                    }
                }
            }
        }
    }
    
    public void setLore(ItemStack equippedBoots) {
    	if(this.config.enableItemLabels()) {
    		ItemMeta equippedBootsMeta = equippedBoots.getItemMeta();
        	equippedBootsMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "RocketBoots");
            ArrayList<String> lore = new ArrayList<String>();
            if(equippedBoots.getDurability() < 40) {
            	lore.add(ChatColor.GRAY + "Durability: " + ChatColor.GREEN + equippedBoots.getDurability() + "%");
            }else if (equippedBoots.getDurability() < 70) {
            	lore.add(ChatColor.GRAY + "Durability: " + ChatColor.YELLOW + equippedBoots.getDurability() + "%");
            }else {
            	lore.add(ChatColor.GRAY + "Durability: " + ChatColor.RED + equippedBoots.getDurability() + "%");
            }
            equippedBootsMeta.setLore(lore);
            equippedBoots.setItemMeta(equippedBootsMeta);
    	}
    }
}