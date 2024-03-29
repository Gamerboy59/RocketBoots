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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class RBPlayerListener implements Listener {

    private final RBConfiguration config;

    // list of players that can't fly until they next sneak
    // (for when player stops in mid-air, to stop them immediately flying off the block)
    private final List<Player> haltedPlayers = new ArrayList<Player>();

    public RBPlayerListener(RBConfiguration config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (player.isSneaking() && !this.haltedPlayers.contains(player)) {
            if (Util.getPlayerBoots(player) != null) {
                final Material playerBoots = Util.getPlayerBoots(player).getType();
                if ((Material.GOLDEN_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player))
                        || (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player))) {
                    if (this.config.playerEnabled(player)) {
                        final Location playerLocation = player.getLocation();
                        final Vector playerDirection = playerLocation.getDirection();
                        double speed = 1;
                        if (Material.GOLDEN_BOOTS.equals(playerBoots)) {
                            speed = this.config.goldenBootsSpeed();
                        } else if (Material.CHAINMAIL_BOOTS.equals(playerBoots)) {
                            speed = this.config.chainmailBootsSpeed();
                        }
                        playerDirection.multiply(speed);
                        player.setVelocity(playerDirection);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (this.haltedPlayers.contains(player)) {
            this.haltedPlayers.remove(player);
        }
        if (player.isSneaking() && this.config.playerEnabled(player)) {
            if (Util.getPlayerBoots(player) != null) {
                final Material playerBoots = Util.getPlayerBoots(player).getType();
                if (playerBoots == null)
                    return;
                if (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player)) {
                    final Vector playerDirection = player.getLocation().getDirection();
                    final double speedMultiplier = this.config.diamondBootsSpeedMultiplier();
                    final double verticalSpeed = this.config.diamondBootsVerticalSpeed();
                    playerDirection.multiply(speedMultiplier);
                    playerDirection.setY(verticalSpeed);
                    player.setVelocity(playerDirection);
                } else if (Material.IRON_BOOTS.equals(playerBoots) && Permissions.canUseIronBoots(player)) {
                    final List<Entity> nearbyEntities = player.getNearbyEntities(12, 5, 12);
                    final Location playerLocation = player.getLocation();
                    for (final Entity entity : nearbyEntities) {
                        if ((entity instanceof Player) && !Permissions.canLaunchPlayers(player)) {
                            continue;
                        } else if (!(entity instanceof LivingEntity)) {
                            continue;
                        }
                        final Location entityLocation = entity.getLocation();
                        // sk89q's Vector class makes this easy ;)
                        final Vector vectorAway = entityLocation.toVector().subtract(playerLocation.toVector());
                        final double verticalSpeed = this.config.ironBootsVerticalSpeed();
                        vectorAway.setY(verticalSpeed);
                        entity.setVelocity(vectorAway);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (Util.getPlayerBoots(player) != null) {
            final Material playerBoots = Util.getPlayerBoots(player).getType();
            if (Material.LEATHER_BOOTS.equals(playerBoots) && Permissions.canUseLeatherBoots(player) && this.config.playerEnabled(player)) {
                final Entity entity = event.getRightClicked();
                if (entity instanceof LivingEntity) {
                    if (!(entity instanceof Player) || Permissions.canLaunchPlayers(player)) {
                        final Vector entityVelocity = entity.getVelocity();

                        final double launchSpeed = this.config.leatherBootsSpeed();
                        entityVelocity.setY(launchSpeed);
                        entity.setVelocity(entityVelocity);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!this.config.preventFlightKick()) {
            return;
        }
        if (event.getReason().equals("Flying is not enabled on this server")) {
            final Player player = event.getPlayer();
            if (Util.getPlayerBoots(player) != null) {
                final Material playerBoots = Util.getPlayerBoots(player).getType();
                if ((Material.GOLDEN_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player)) || (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (Action.RIGHT_CLICK_AIR.equals(event.getAction())) {
            final Player player = event.getPlayer();
            if (Util.getPlayerBoots(player) != null) {
                final Material playerBoots = Util.getPlayerBoots(player).getType();
                if (playerBoots == null)
                    return;
                final ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (Material.FEATHER.equals(itemInHand.getType()) && Permissions.canUseFeather(player)) {
                    if ((Material.GOLDEN_BOOTS.equals(playerBoots) && Permissions.canUseGoldBoots(player)) || (Material.CHAINMAIL_BOOTS.equals(playerBoots) && Permissions.canUseChainmailBoots(player)) || (Material.DIAMOND_BOOTS.equals(playerBoots) && Permissions.canUseDiamondBoots(player))) {
                        final Location playerLocation = player.getLocation();
                        final Block blockBelow = playerLocation.getBlock().getRelative(BlockFace.DOWN);
                        if (player.isSneaking()) {
                            this.haltedPlayers.add(player); // players in this list can't fly until they toggle sneak
                        }
                        player.sendBlockChange(blockBelow.getLocation(), Material.GLASS.createBlockData());
                        player.setVelocity(new Vector(0, 0, 0));
                        player.teleport(playerLocation);
                    }
                }
            }
        }
    }
}