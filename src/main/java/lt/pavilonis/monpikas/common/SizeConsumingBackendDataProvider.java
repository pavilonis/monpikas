package lt.pavilonis.monpikas.common;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import java.util.function.Consumer;

public abstract class SizeConsumingBackendDataProvider<T, F> extends AbstractBackEndDataProvider<T, F> {

   private Consumer<Integer> sizeConsumer;

   public void setSizeConsumer(Consumer<Integer> sizeConsumer) {
      this.sizeConsumer = sizeConsumer;
   }

   @Override
   protected final int sizeInBackEnd(Query<T, F> query) {
      int size = sizeInBackEnd();
      sizeConsumer.accept(size);
      return size;
   }

   protected abstract int sizeInBackEnd();
}
