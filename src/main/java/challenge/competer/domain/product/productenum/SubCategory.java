package challenge.competer.domain.product.productenum;

import static challenge.competer.domain.product.productenum.MainCategory.COMPUTER_PARTS;
import static challenge.competer.domain.product.productenum.MainCategory.LAPTOP;

public enum SubCategory {

    SAMSUNG(LAPTOP),
    LG(LAPTOP),
    APPLE(LAPTOP),

    CPU(COMPUTER_PARTS),
    GRAPHIC_CARD(COMPUTER_PARTS),
    RAM(COMPUTER_PARTS),
    SSD(COMPUTER_PARTS);

    private final MainCategory parentCategory;

    SubCategory(MainCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public MainCategory getParentCategory() {
        return parentCategory;
    }

}