package dev.tqqn.modules.menu.framework.objects;

import dev.tqqn.BingoMain;
import dev.tqqn.utils.ChatUtils;
import dev.tqqn.utils.ItemBuilder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu implements InventoryHolder {

    private final Inventory inventory;
    @Getter private final Map<Integer, MenuButton> buttons;
    @Getter private final Player viewer;

    /**
     * Constructs a Menu with the specified title, number of rows, and viewer.
     *
     * @param title  The title of the menu.
     * @param rows   The number of rows in the menu.
     * @param viewer The player viewing the menu.
     * @throws IllegalArgumentException if the number of rows is invalid or the title length exceeds 32 characters.
     */
    public Menu(String title, int rows, Player viewer) {
        this(ChatUtils.format(title), rows, viewer);
    }

    public Menu(Component title, int rows, Player viewer) {
        if (rows > 6 || rows < 1 || PlainTextComponentSerializer.plainText().serialize(title).length() > 32) {
            throw new IllegalArgumentException("Invalid arguments passed to menu constructor.");
        }
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        this.buttons = new HashMap<>();
        this.viewer = viewer;
    }

    public abstract void reload();
    public abstract void onOpen();
    public abstract void onClose(Player viewer);

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * Opens the menu for the viewer.
     */
    public void open() {
        reload();
        onOpen();
        viewer.openInventory(inventory);
    }

    /**
     * Closes the menu.
     */
    protected void close() {
        handleClose();
    }

    /**
     * Handles the closing of the menu by running the close 2 ticks later.
     */
    public void handleClose() {
        Bukkit.getScheduler().runTaskLater(BingoMain.getInstance(), () -> viewer.closeInventory(), 1L);
        onClose(viewer);
    }

    /**
     * Handles a click event on the menu.
     *
     * @param event The InventoryClickEvent.
     */
    public void handleClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        event.setCancelled(event.getClickedInventory() != null && event.getClickedInventory().getHolder().equals(this) || event.isShiftClick());
        if (event.getClick() == ClickType.DOUBLE_CLICK) {
            buttons.values().stream().filter((element) -> element.getItemStack().isSimilar(event.getWhoClicked().getItemOnCursor())).findAny().ifPresent((element) -> {
                event.setCancelled(true);
            });
        }

        if (buttons.get(event.getRawSlot()) != null) {
            buttons.get(event.getRawSlot()).getClicker().accept(player);
            event.setCancelled(true);
        }
    }

    public void handleDrag(InventoryDragEvent event) {
        if (event.getRawSlots().stream().anyMatch((i) -> i < this.getInventory().getSize())) {
            event.setCancelled(true);
        }
    }

    /**
     * Registers a button in the menu.
     *
     * @param button The button to register.
     * @param slot   The slot in the menu.
     */
    protected void registerButton(MenuButton button, int slot) {
        if (button == null) return;
        buttons.put(slot, button);
        inventory.setItem(slot, button.getItemStack());
    }

    /**
     * Registers a close button in the menu.
     *
     * @param slot The slot for the close button.
     */
    public void registerCloseButton(int slot) {
        MenuButton closeButton = new MenuButton(ItemBuilder.getBuilder(Material.BARRIER).setDisplayName("<red>Close").hideAttributes().build())
                .setClicker(player -> close());
        registerButton(closeButton, slot);
    }

    public void registerPreviousMenuButton(int slot, Menu previous) {
        MenuButton previousButton = new MenuButton(ItemBuilder.getBuilder(Material.COMPASS).setDisplayName("<red>Back").hideAttributes().build())
                .setClicker(player -> previous.open());
        registerButton(previousButton, slot);
    }

    /**
     * Registers a filler item in the menu.
     *
     * @param fillerType The type of filler item.
     * @param itemStack  The ItemStack of the filler item.
     */
    public void registerFillerItem(FillerType fillerType, ItemStack itemStack) {
        switch (fillerType) {
            case BORDER -> {
                // Top side
                for (int i = 0; i < 9; i++) {
                    registerButton(new MenuButton(itemStack), i);
                }
                // Right side
                for (int i = 8; i < inventory.getSize(); i += 9) {
                    registerButton(new MenuButton(itemStack), i);
                }
                // Left side
                for (int i = 0; i < inventory.getSize(); i += 9) {
                    registerButton(new MenuButton(itemStack), i);
                }
                // Bottom side
                for (int i = inventory.getSize() -9; i < inventory.getSize(); i++) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
            case BOTTOM -> {
                for (int i = inventory.getSize() -9; i < inventory.getSize(); i++) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
            case FULL -> {
                for (int i = 0; i < inventory.getSize(); i++) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
            case LEFT -> {
                for (int i = 0; i < inventory.getSize(); i += 9) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
            case RIGHT -> {
                for (int i = 8; i < inventory.getSize(); i += 9) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
            case TOP -> {
                for (int i = 0; i < 9; i++) {
                    registerButton(new MenuButton(itemStack), i);
                }
            }
        }
    }

    /**
     * Enum representing the types of filler items.
     */
    public enum FillerType {
        BORDER,
        BOTTOM,
        FULL,
        LEFT,
        RIGHT,
        TOP;
    }
}

