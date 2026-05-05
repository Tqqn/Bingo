package dev.tqqn.modules.menu.framework.objects;

import dev.tqqn.modules.menu.framework.exceptions.InvalidPageException;
import dev.tqqn.utils.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class AutoPagedMenu extends PagedMenu {

    protected final Map<Integer, MenuButton> pageButtons = new HashMap<>();
    @Setter @Getter private List<MenuButton> pageContents = new ArrayList<>();
    @Setter @Getter private List<Integer> contentSlots = new ArrayList<>();

    public AutoPagedMenu(String title, int rows, Player viewer) {
        super(title, rows, viewer);
    }

    public AutoPagedMenu(Component title, int rows, Player viewer) {
        super(title, rows, viewer);
    }

    @Override
    public void open() {
        open(0);
    }

    public void open(int page) {
        this.openPage(page);
    }

    @Override
    public void registerButton(MenuButton menuButton, int place) {
        pageButtons.put(place, menuButton);
    }

    @Override
    public void onOpenPage(int page) throws InvalidPageException {
        if (this.contentSlots.isEmpty()) {
            throw new InvalidPageException("You need to use multiple page slots. Use PagedInventory#setSlots()");
        } else {

            List<MenuButton> currentPageContents = this.getContents(page);

            for (int i = 0; i < currentPageContents.size(); ++i) {
                super.registerButton(currentPageContents.get(i), this.contentSlots.get(i));
            }

            this.pageButtons.forEach((slot, button) -> super.registerButton(button, slot));
        }
    }

    @Override
    public int getPageCount() {
        return (int)Math.ceil((double) pageContents.size() / (double)this.contentSlots.size());
    }

    public void setSlots(List<Integer> slots) {
        this.contentSlots = slots;
    }

    public List<MenuButton> getContents(int page) {
        return this.pageContents.subList(page * this.contentSlots.size(), Math.min((page + 1) * this.contentSlots.size(), this.pageContents.size()));
    }




}