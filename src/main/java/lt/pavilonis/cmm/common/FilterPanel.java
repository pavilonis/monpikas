package lt.pavilonis.cmm.common;

import org.vaadin.viritin.layouts.MHorizontalLayout;

public abstract class FilterPanel<FILTER> extends MHorizontalLayout {
   public abstract FILTER getFilter();
}
