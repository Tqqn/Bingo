package dev.tqqn.modules.menu.framework.objects;

import dev.tqqn.modules.menu.framework.exceptions.InvalidPageException;
import dev.tqqn.utils.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public abstract class PagedMenu extends Menu {

    private ButtonPair backButton;
    private ButtonPair homeButton;
    private ButtonPair forwardButton;
    protected final Map<Integer, MenuButton> pageButtons = new HashMap<>();
    @Setter @Getter private List<MenuButton> pageContents = new ArrayList<>();
    @Setter @Getter private List<Integer> contentSlots = new ArrayList<>();

    @Getter @Setter private int page = 0;

    public PagedMenu(String title, int rows, Player viewer) {
        super(title, rows, viewer);
    }

    @Override
    public void open() {
        open(0);
    }

    public void open(int page) {
        this.openPage(page);
        super.open();
    }

    @Override
    public void registerButton(MenuButton menuButton, int place) {
        pageButtons.put(place, menuButton);
    }

    protected void openPage(int page) {
        if (this.contentSlots.isEmpty()) {
            throw new InvalidPageException("You need to use multiple page slots. Use PagedInventory#setSlots()");
        } else {
            this.page = page;
            getButtons().clear();
            getInventory().clear();

            List<MenuButton> currentPageContents = this.getContents(page);

            for(int i = 0; i < currentPageContents.size(); ++i) {
                super.registerButton(currentPageContents.get(i), this.contentSlots.get(i));
            }

            this.pageButtons.forEach((x$0, x$1) -> super.registerButton(x$1, x$0));

            if (this.homeButton != null) {
                super.registerButton(homeButton.getButton(), homeButton.getPlace());
            }

            if (this.backButton != null && page > 0) {
                super.registerButton(backButton.getButton(), backButton.getPlace());
            }

            if (this.forwardButton != null && page < this.getPageCount() - 1) {
                super.registerButton(forwardButton.getButton(), forwardButton.getPlace());
            }
        }
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
        }

    }

    public void setSlots(List<Integer> slots) {
        this.contentSlots = slots;
    }

    public List<MenuButton> getContents(int page) {
        System.out.println(page);
        System.out.println(contentSlots.size());
        System.out.println(pageContents.size());
        return this.pageContents.subList(page * this.contentSlots.size(), Math.min((page + 1) * this.contentSlots.size(), this.pageContents.size()));
    }

    public int getPageCount() {
        return (int)Math.ceil((double)this.pageContents.size() / (double)this.contentSlots.size());
    }

    @RequiredArgsConstructor
    @Getter
    private final class ButtonPair {

        private final int place;
        private final MenuButton button;
    }

    public enum PageButton {
        HOME,
        BACK,
        FORWARD;
    }
}