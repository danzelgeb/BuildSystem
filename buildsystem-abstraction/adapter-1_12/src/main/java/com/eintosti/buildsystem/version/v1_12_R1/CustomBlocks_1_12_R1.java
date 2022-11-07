/*
 * Copyright (c) 2022, Thomas Meaney
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.eintosti.buildsystem.version.v1_12_R1;

import com.eintosti.buildsystem.version.customblocks.CustomBlock;
import com.eintosti.buildsystem.version.customblocks.CustomBlocks;
import com.eintosti.buildsystem.version.util.DirectionUtil;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.TrapDoor;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * @author einTosti
 */
public class CustomBlocks_1_12_R1 implements CustomBlocks {

    private final JavaPlugin plugin;

    private final int mcVersion;

    public CustomBlocks_1_12_R1(JavaPlugin plugin) {
        this.plugin = plugin;

        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        this.mcVersion = Integer.parseInt(version.replaceAll("\\D", ""));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setBlock(BlockPlaceEvent event, String key) {
        CustomBlock customBlock = CustomBlock.getCustomBlock(key);
        if (customBlock == null) {
            plugin.getLogger().warning("Could not find custom block with key: " + key);
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        Bukkit.getScheduler().runTask(plugin, () -> {
            switch (customBlock) {
                case FULL_OAK_BARCH:
                    block.setType(Material.LOG);
                    block.setData((byte) 12, true);
                    break;
                case FULL_SPRUCE_BARCH:
                    block.setType(Material.LOG);
                    block.setData((byte) 13, true);
                    break;
                case FULL_BIRCH_BARCH:
                    block.setType(Material.LOG);
                    block.setData((byte) 14, true);
                    break;
                case FULL_JUNGLE_BARCH:
                    block.setType(Material.LOG);
                    block.setData((byte) 15, true);
                    break;
                case FULL_ACACIA_BARCH:
                    block.setType(Material.LOG_2);
                    block.setData((byte) 12, true);
                    break;
                case FULL_DARK_OAK_BARCH:
                    block.setType(Material.LOG_2);
                    block.setData((byte) 13, true);
                    break;
                case RED_MUSHROOM:
                    block.setType(Material.HUGE_MUSHROOM_2);
                    break;
                case BROWN_MUSHROOM:
                    block.setType(Material.HUGE_MUSHROOM_1);
                    break;
                case MUSHROOM_STEM:
                    block.setType(Material.HUGE_MUSHROOM_1);
                    block.setData((byte) 10, true);
                    break;
                case FULL_MUSHROOM_STEM:
                    block.setType(Material.HUGE_MUSHROOM_1);
                    block.setData((byte) 15, true);
                    break;
                case MUSHROOM_BLOCK:
                    block.setType(Material.HUGE_MUSHROOM_1);
                    block.setData((byte) 0, true);
                    break;
                case SMOOTH_STONE:
                    block.setType(Material.DOUBLE_STEP);
                    block.setData((byte) 8, true);
                    break;
                case DOUBLE_STONE_SLAB:
                    block.setType(Material.DOUBLE_STEP);
                    block.setData((byte) 0, true);
                    break;
                case SMOOTH_SANDSTONE:
                    block.setType(Material.DOUBLE_STEP);
                    block.setData((byte) 9, true);
                    break;
                case SMOOTH_RED_SANDSTONE:
                    block.setTypeId(181, true);
                    block.setData((byte) 8, true);
                    break;
                case POWERED_REDSTONE_LAMP:
                    block.setType(Material.REDSTONE_LAMP_ON);
                    powerLamp(block);
                    break;
                case BURNING_FURNACE:
                    block.setType(Material.FURNACE);
                    powerFurnace(block);
                    rotateBlock(block, player, DirectionUtil.getBlockDirection(player, false));
                    break;
                case PISTON_HEAD:
                    block.setType(Material.PISTON_EXTENSION);
                    rotateBlock(block, player, DirectionUtil.getBlockDirection(player, true));
                    break;
                case COMMAND_BLOCK:
                    block.setType(Material.COMMAND);
                    break;
                case BARRIER:
                    block.setType(Material.BARRIER);
                    break;
                case INVISIBLE_ITEM_FRAME:
                    // Invalid server version
                    break;
                case MOB_SPAWNER:
                    block.setType(Material.MOB_SPAWNER);
                    break;
                case NETHER_PORTAL:
                    BlockFace direction = DirectionUtil.getBlockDirection(player, false);
                    int orientation = (direction == BlockFace.NORTH || direction == BlockFace.SOUTH) ? 0 : 2;
                    block.setType(Material.PORTAL);
                    block.setData((byte) orientation, false);
                    break;
                case END_PORTAL:
                    block.setType(Material.ENDER_PORTAL);
                    break;
                case DRAGON_EGG:
                    block.setType(Material.DRAGON_EGG);
                    break;
                default:
                    return;
            }

            event.setCancelled(true);
        });
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setPlant(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        Block adjacent = block.getRelative(event.getBlockFace());
        ItemStack itemStack = event.getItem();

        adjacent.setType(itemStack.getType(), false);
        adjacent.setData((byte) itemStack.getDurability());
    }

    @Override
    @SuppressWarnings("deprecation")
    public void modifySlab(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        Set<Material> slabs = Sets.newHashSet(Material.STEP, Material.WOOD_STEP, Material.STONE_SLAB2);
        if (mcVersion >= 190) {
            slabs.add(Material.PURPUR_SLAB);
        }

        Material material = block.getType();
        if (slabs.contains(material)) {
            return;
        }

        Material changedMaterial = null;
        if (mcVersion >= 190 && material == Material.PURPUR_DOUBLE_SLAB) {
            changedMaterial = Material.PURPUR_SLAB;
        }

        switch (material) {
            case DOUBLE_STEP:
                changedMaterial = Material.STEP;
                break;
            case WOOD_DOUBLE_STEP:
                changedMaterial = Material.WOOD_STEP;
                break;
            case DOUBLE_STONE_SLAB2:
                changedMaterial = Material.STONE_SLAB2;
                break;
            default:
                if (changedMaterial == null) {
                    return;
                }
                break;
        }

        if (block.getData() <= 7) {
            Player player = event.getPlayer();
            event.setCancelled(true);

            byte data = block.getData();
            if (DirectionUtil.isTop(player, block)) {
                block.setType(changedMaterial);
                block.setData(data);
            } else {
                block.setType(changedMaterial);
                block.setData((byte) (data + 8));
            }
        }
    }

    @Override
    public void toggleIronTrapdoor(PlayerInteractEvent event) {
        BlockState blockState = event.getClickedBlock().getState();
        TrapDoor trapDoor = (TrapDoor) blockState.getData();
        trapDoor.setOpen(!trapDoor.isOpen());
        blockState.update();
    }

    @Override
    public void toggleIronDoor(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        BlockState blockState = clickedBlock.getState();
        Door door = (Door) blockState.getData();

        if (door.isTopHalf()) {
            Block bottomBlock = clickedBlock.getRelative(BlockFace.DOWN);
            BlockState bottomBlockState = bottomBlock.getState();
            Door bottomDoor = (Door) bottomBlockState.getData();
            bottomDoor.setOpen(!bottomDoor.isOpen());
            bottomBlockState.update();
        } else {
            door.setOpen(!door.isOpen());
            blockState.update();
        }
    }

    @Override
    public void rotateBlock(Block block, Player player, BlockFace direction) {
        BlockState state = block.getState();
        MaterialData data = state.getData();
        if (data instanceof Directional) {
            ((Directional) data).setFacingDirection(direction);
            state.update(true);
        }
    }

    private void powerLamp(Block block) {
        Block redstoneBlock = block.getLocation().add(0, 1, 0).getBlock();
        Material originalMaterial = redstoneBlock.getType();
        BlockState originalState = redstoneBlock.getState();
        MaterialData originalMaterialData = originalState.getData();

        redstoneBlock.setType(Material.REDSTONE_BLOCK, true);
        redstoneBlock.setType(originalMaterial, false);
        originalState.setData(originalMaterialData);
        originalState.update(true, false);

        block.setMetadata("CustomRedstoneLamp", new FixedMetadataValue(plugin, true));
    }

    private void powerFurnace(Block block) {
        Furnace furnace = (Furnace) block.getState();
        furnace.setBurnTime(Short.MAX_VALUE);
        furnace.update();
    }
}