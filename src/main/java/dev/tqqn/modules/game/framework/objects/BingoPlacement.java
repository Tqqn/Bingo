package dev.tqqn.modules.game.framework.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class BingoPlacement {

    private final int column;
    private final int row;

    public boolean is(int column, int row) {
        return this.column == column && this.row == row;
    }
}
