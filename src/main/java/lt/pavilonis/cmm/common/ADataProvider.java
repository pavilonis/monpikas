package lt.pavilonis.cmm.common;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.Query;
import com.vaadin.shared.Registration;

import java.util.stream.Stream;

public class ADataProvider<T, F> implements DataProvider<T, F> {
   @Override
   public boolean isInMemory() {
      return false;
   }

   @Override
   public int size(Query<T, F> query) {
      return 0;
   }

   @Override
   public Stream<T> fetch(Query<T, F> query) {
      return null;
   }

   @Override
   public void refreshItem(T item) {

   }

   @Override
   public void refreshAll() {

   }

   @Override
   public Registration addDataProviderListener(DataProviderListener<T> listener) {
      return null;
   }
}
