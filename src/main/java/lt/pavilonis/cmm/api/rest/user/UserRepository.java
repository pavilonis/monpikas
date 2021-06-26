package lt.pavilonis.cmm.api.rest.user;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.BaseEncoding;
import lt.pavilonis.cmm.common.util.QueryUtils;
import lt.pavilonis.cmm.school.user.UserFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class UserRepository {

   private static final Logger LOG = getLogger(UserRepository.class.getSimpleName());
   private static final String BLOCKS_FROM_WHERE = "" +
         "FROM tb_Users u " +
         "  JOIN tb_Cards c ON c.Cardcode = u.Cardcode " +
         "WHERE " +
         "  u.Cardcode IS NOT NULL " +
         "  AND c.ROMCode IS NOT NULL " +
         "  AND c.ROMCode <> '' " +
         "  AND (:name IS NULL OR u.FirstName LIKE :name OR u.LastName LIKE :name) " +
         "  AND (:role IS NULL OR u.Dummy4 LIKE :role) " +
         "  AND (:group IS NULL OR u.Dummy3 LIKE :group)" +
         "  AND (:withFirstLastNameOnly = '0' OR " +
         "     (u.FirstName IS NOT NULL AND u.FirstName <> '' AND u.LastName IS NOT NULL AND u.LastName <> '')" +
         "  )";

   @Autowired
   private NamedParameterJdbcTemplate jdbc;

   public User update(User user) {

      Map<String, Object> args = collectArgs(user);

      String picture = StringUtils.isNotBlank(user.getBase16photo()) && BaseEncoding.base16().canDecode(user.getBase16photo())
            ? "  Picture = CONVERT(VARBINARY(MAX), :base16photo, 2) "
            : "  Picture = NULL ";

      jdbc.update("" +
                  "UPDATE u " +
                  "SET " +
                  "  name = :name, " +
                  "  FirstName = :firstName, " +
                  "  LastName = :lastName, " +
                  "  Dummy2 = :birthDate, " +
                  "  Dummy3 = :group, " +
                  "  Dummy4 = :role," +
                  picture +
                  "FROM tb_Users u " +
                  "  JOIN tb_Cards c ON c.Cardcode = u.Cardcode " +
                  "WHERE c.ROMCode = :cardCode",
            args
      );
      return load(user.getCardCode(), false);
   }

   public List<User> load(UserFilter filter) {

      Map<String, Object> args = commonArgs(filter);
      args.put("argOffset", QueryUtils.argOffset(filter.getOffset()));
      args.put("argLimit", QueryUtils.argLimit(filter.getLimit()));

      return jdbc.query("" +
                  "SELECT " +
                  "  c.ROMCode AS cardCode, " +
                  "  u.FirstName AS firstName, " +
                  "  u.LastName AS lastName, " +
                  "  u.dummy2 AS birthDate, " +
                  "  u.dummy3 AS userGroup, " +
                  "  u.dummy4 AS userRole," +
                  "  NULL AS photo " + // Photos are only loaded for single user requests
                  BLOCKS_FROM_WHERE +
                  "ORDER BY u.FirstName, u.LastName ASC " +
                  "OFFSET :argOffset ROWS FETCH NEXT :argLimit ROWS ONLY",
            args,
            new UserMapper()
      );
   }

   protected Map<String, Object> commonArgs(UserFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("name", QueryUtils.likeArg(filter.getName()));
      args.put("role", filter.getRole());
      args.put("group", filter.getGroup());
      args.put("withFirstLastNameOnly", filter.isWithFirstLastNameOnly());
      return args;
   }


   public User load(String cardCode, boolean withPhoto) {
      List<User> result = jdbc.query("" +
                  "SELECT " +
                  "  c.ROMCode AS cardCode, " +
                  "  u.FirstName AS firstName, " +
                  "  u.LastName AS lastName, " +
                  "  u.dummy2 AS birthDate, " +
                  "  u.dummy3 AS userGroup, " +
                  "  u.dummy4 AS userRole, " +
                  "  CASE WHEN :withPhoto = 1 " +
                  "     THEN " +
                  "        u.Picture " +
                  "     ELSE " +
                  "        NULL " +
                  "  END AS photo " +

                  "FROM tb_Users u " +
                  "  JOIN tb_Cards c ON c.Cardcode = u.Cardcode " +

                  "WHERE u.Cardcode IS NOT NULL AND c.ROMCode = :cardCode ",

            ImmutableMap.of("cardCode", cardCode, "withPhoto", withPhoto),
            new UserMapper()
      );

      if (result.isEmpty()) {
         LOG.warn("User not found - returning NULL [cardCode={}]", cardCode);
         return null;
      } else {
         return result.get(0);
      }
   }

   private Map<String, Object> collectArgs(User user) {

      String photo = StringUtils.isNotBlank(user.getBase16photo()) && BaseEncoding.base16().canDecode(user.getBase16photo())
            ? user.getBase16photo()
            : null;

      Map<String, Object> args = new HashMap<>();
      args.put("cardCode", user.getCardCode());
      args.put("firstName", user.getFirstName());
      args.put("lastName", user.getLastName());
      args.put("name", Strings.nullToEmpty(user.getFirstName()) + " " + Strings.nullToEmpty(user.getLastName()));
      args.put("group", user.getGroup());
      args.put("role", user.getRole());
      args.put("birthDate", user.getBirthDate());
      args.put("base16photo", photo);
      args.put("now", LocalDateTime.now());
      return args;
   }

   public boolean exists(String cardCode) {
      return jdbc.queryForObject("" +
                  "SELECT " +
                  "  CASE " +
                  "     WHEN COUNT(u.Cardcode) > 0 " +
                  "     THEN 1 " +
                  "     ELSE 0 " +
                  "  END " +
                  "FROM tb_Users u " +
                  "  JOIN tb_Cards c ON c.Cardcode = u.Cardcode " +
                  "WHERE u.Cardcode IS NOT NULL " +
                  "  AND c.ROMCode = :cardCode",
            Collections.singletonMap("cardCode", cardCode),
            Boolean.class
      );
   }

   public int size(UserFilter filter) {
      return jdbc.queryForObject(
            "SELECT COUNT(u.Cardcode) " + BLOCKS_FROM_WHERE,
            commonArgs(filter),
            Integer.class
      );
   }

   public List<String> loadGroups() {
      return jdbc.queryForList("" +
                  "SELECT DISTINCT dummy3 " +
                  "FROM tb_Users " +
                  "WHERE dummy3 IS NOT NULL " +
                  "  AND dummy3 <> '' " +
                  "ORDER BY dummy3 ASC",
            Collections.emptyMap(),
            String.class
      );
   }

   public List<String> loadRoles() {
      return jdbc.queryForList("" +
                  "SELECT DISTINCT dummy4 " +
                  "FROM tb_Users " +
                  "WHERE dummy4 IS NOT NULL " +
                  "  AND dummy4 <> '' " +
                  "ORDER BY dummy4 ASC",
            Collections.emptyMap(),
            String.class
      );
   }
}
