package dev.tqqn.modules.menu.framework.objects;

import dev.tqqn.modules.menu.framework.exceptions.InvalidPageException;
import dev.tqqn.utils.ItemBuilder;
import dev.tqqn.utils.Notify;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 03/05/2026
 */
public abstract class PagedMenu extends Menu {

    private ButtonPair backButton;
    private ButtonPair homeButton;
    private ButtonPair forwardButton;
    private ButtonPair closeButton;

    @Getter @Setter
    private int page = 0;

    public PagedMenu(Component title, int rows, Player viewer) {
        super(title, rows, viewer);
    }

    public PagedMenu(String title, int rows, Player viewer) {
        super(title, rows, viewer);
    }

    protected abstract void onOpenPage(int page);

    public void openPage(int page) {
        if (page > getPageCount()) return;
        getButtons().clear();
        getInventory().clear();

        try {
            onOpenPage(page);
        } catch (InvalidPageException e) {
            close();
            Notify.ERROR.chat(getViewer(), "Something weird happened opening this page. Closed Menu.");
            return;
        }

        setPage(page);

        if (this.homeButton != null && page > 0) {
            super.registerButton(homeButton.getButton(), homeButton.getPlace());
        }

        if (this.backButton != null && page > 0) {
            super.registerButton(backButton.getButton(), backButton.getPlace());
        }

        if (this.forwardButton != null && page < getPageCount()) {
            super.registerButton(forwardButton.getButton(), forwardButton.getPlace());
        }

        if (this.closeButton != null) {
            super.registerButton(closeButton.getButton(), closeButton.getPlace());
        }

        super.open();
    }

    public void addPageButton(PageButton pageButton, int slot) {
        switch (pageButton) {
            case HOME -> {
                MenuButton button = new MenuButton(ItemBuilder.getBuilder(Material.BOOK).setDisplayName("<red>Home").build());
                button.setClicker(player -> this.openPage(0));

                this.homeButton = new ButtonPair(slot, button);
            }
            case BACK -> {
                MenuButton button = new MenuButton(ItemBuilder.getBuilder(Material.RED_CANDLE).setDisplayName("<red>Back").build());
                button.setClicker(player -> this.openPage(this.page - 1));

                this.backButton = new ButtonPair(slot, button);
            }
            case FORWARD -> {
                MenuButton button = new MenuButton(ItemBuilder.getBuilder(Material.GREEN_CANDLE).setDisplayName("<red>Next").build());
                button.setClicker(player -> this.openPage(this.page + 1));

                this.forwardButton = new ButtonPair(slot, button);
            }
            case CLOSE -> this.closeButton = new ButtonPair(slot, getCloseButton());
        }

    }

    public abstract int getPageCount();

    @RequiredArgsConstructor
    @Getter
    public final class ButtonPair {

        private final int place;
        private final MenuButton button;
    }

    public enum PageButton {
        HOME,
        BACK,
        FORWARD,
        CLOSE;
    }
    
}
