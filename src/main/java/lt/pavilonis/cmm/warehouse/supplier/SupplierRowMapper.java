package lt.pavilonis.cmm.warehouse.supplier;

import lt.pavilonis.cmm.common.util.SimpleRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierRowMapper extends SimpleRowMapper<Supplier> {
   @Override
   public Supplier mapRow(ResultSet rs, int i) throws SQLException {
      return new Supplier(rs.getLong("s.id"), rs.getString("s.code"), rs.getString("s.name"));
   }
}
